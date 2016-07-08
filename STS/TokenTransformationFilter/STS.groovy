import static groovyx.net.http.ContentType.JSON
@Grab(group = 'org.codehaus.groovy.modules.http-builder', module = 'http-builder', version = '0.7.1')
import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.POST

import groovyx.net.http.*

// -----------------------------------------------------------------------------------------------------
// This script is use to configure your OPENAM for STS
// # tested with OpenAM 13.0.0 Build 5d4589530d (2016-January-14 21:15)
// # vrom - 07 01 2016
// -----------------------------------------------------------------------------------------------------
// CONFIGURATION (Update it if necessary)
// -----------------------------------------------------------------------------------------------------

final String openigBase = "http://localhost:8082"       // URL must NOT end with a slash
final String openamUrl = "http://localhost:8090/openam" // URL must NOT end with a slash
final String user = 'amadmin'
final String userPass = "secret12"
// Following properties are for the doc example - if you modify them, you MUST modify the 'id_token' route.
final String agentName = "oidc_client_for_sts"
final String agentPassword = "password"
final String redirectionUri = "${openigBase}/id_token/callback"
final String idTokenSigningAlgorithm = "HS256" // must be HMAC - not RS256!
final String tokenEndPointAuthMethod = "client_secret_basic"
final String cryptoContextType = "client_secret"
final String idTokenIssuer = "${openamUrl}/oauth2"
final String saml_issuer_name = "OpenAM"
final String oidc_issuer = "oidc"
// -----------------------------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------------------------
// Create a properties file according to your configuration.
// This file will be used in your route to access the different values.
final Properties props = new Properties()
final String pathPropsFile = System.getProperty("user.home");
final File propsFile = new File(pathPropsFile + "/openig.properties")
props.setProperty("openigBase", openigBase)
props.setProperty("openamUrl", openamUrl)
props.store(propsFile.newWriter(), "Properties file generated for STS-OPENIG example")
println()
println "(DEBUG)Created properties file in >>${pathPropsFile}.<<"
println()

def SSOToken;
def http;
// Request to get an SSOToken
http = new HTTPBuilder("${openamUrl}/json/authenticate")
http.request(POST, JSON) { req ->
    headers.'X-OpenAM-Username' = user
    headers.'X-OpenAM-Password' = userPass
    headers.'Content-Type' = 'application/json'
    requestContentType = ContentType.JSON
    body = ''

    response.success = { resp, json ->
        println(json)
        SSOToken = json.tokenId;
    }

    response.failure = { resp ->
        println()
        println "(DEBUG)Unable to create SSO token: ${resp.entity.content.text}"
    }
}

// Create a user george costanza
http = new HTTPBuilder("${openamUrl}/json/users/?_action=create")
http.request(POST, JSON) { req ->
    headers.'iPlanetDirectoryPro' = SSOToken
    headers.'Content-Type' = 'application/json'
    requestContentType = ContentType.JSON
    body = """{
                "username": "george",
                "cn": "george costanza",
                "userpassword": "costanza",
                "mail": ["costanza"]
              }"""

    response.success = { resp, json ->
        println()
        println(json)
    }

    response.failure = { resp ->
        println()
        println """(DEBUG)Create user 'george costanza': ${resp.entity.content.text}"""
    }
}

def oauth2ResourceType
// Creates OAuth2 resource type
http = new HTTPBuilder("${openamUrl}/json/resourcetypes/?_action=create")
http.request(POST, JSON) { req ->
    headers.'iPlanetDirectoryPro' = SSOToken
    headers.'Content-Type' = 'application/json'
    requestContentType = ContentType.JSON
    body = """{
                "name": "OAuth2",
                "description": "",
                "patterns": ["*://*:*/*/authorize?*"],
                "actions": {
                    "POST": true,
                    "GET": true
                }
            }"""

    response.success = { resp, json ->
        println()
        oauth2ResourceType = json.uuid;
        println(json)
    }

    response.failure = { resp ->
        println()
        println "(DEBUG)(DEBUG)Create resource type OAuth2: ${resp.entity.content.text}"
    }
}

// Configure Openam for oauth2-oidc
http = new HTTPBuilder("${openamUrl}/json/realm-config/services/oauth-oidc/?_action=create")
http.request(POST, JSON) { req ->
    headers.'iPlanetDirectoryPro' = SSOToken
    headers.'Content-Type' = 'application/json'
    requestContentType = ContentType.JSON
    body = """{
                "loaMapping": {},
                "oidcClaimsScript": "36863ffb-40ec-48b9-94b1-9a99f71cc3b5",
                "jkwsURI": "",
                "supportedClaims": ["name|Full name",
                                    "zoneinfo|Time zone",
                                    "email|Email address",
                                    "address|Postal address",
                                    "locale|Locale",
                                    "given_name|Given name",
                                    "phone_number|Phone number",
                                    "profile|Your personal information",
                                    "family_name|Family name"],
                "codeVerifierEnforced": false,
                "hashSalt": "changeme",
                "customLoginUrlTemplate": [],
                "savedConsentAttribute": "myconsent",
                "responseTypeClasses": ["id_token|org.forgerock.restlet.ext.oauth2.flow.responseTypes.IDTokenResponseType",
                                        "token|org.forgerock.restlet.ext.oauth2.flow.responseTypes.TokenResponseType",
                                        "code|org.forgerock.restlet.ext.oauth2.flow.responseTypes.CodeResponseType"],
                "issueRefreshTokenOnRefreshedToken": true,
                "claimsParameterSupported": false,
                "modifiedTimestampAttribute": "modifyTimestamp",
                "generateRegistrationAccessTokens": false,
                "refreshTokenLifetime": 60,
                "allowDynamicRegistration": true,
                "displayNameAttribute": "cn",
                "amrMappings": {},
                "authenticationAttributes": ["uid"],
                "defaultScopes": ["phone|Your phone number(s)", "address|Your postal address", "email|Your personal email", "openid|", "profile|Your personal information"],
                "supportedScopes": ["phone|Your phone number(s)", "address|Your postal address", "email|Your personal email", "openid|", "profile|Your personal information"],
                "createdTimestampAttribute": "createTimestamp",
                "keypairName": "test",
                "supportedIDTokenSigningAlgorithms": ["HS256", "HS512", "RS256", "HS384"],
                "codeLifetime": 60,
                "accessTokenLifetime": 5,
                "issueRefreshToken": true,
                "alwaysAddClaimsToToken": false,
                "supportedSubjectTypes": ["public"],
                "defaultACR": [],
                "jwtTokenLifetime": 600,
                "scopeImplementationClass": "org.forgerock.openam.oauth2.OpenAMScopeValidator"
            }"""

    response.success = { resp, json ->
        println()
        println(json)
    }

    response.failure = { resp ->
        println()
        println "(DEBUG)Create service 'oauth-oidc': ${resp.entity.content.text}"
    }
}

// Creates the application|policy set
http = new HTTPBuilder("${openamUrl}/json/applications/?_action=create")
http.request(POST, JSON) { req ->
    headers.'iPlanetDirectoryPro' = SSOToken
    headers.'Content-Type' = 'application/json'
    requestContentType = ContentType.JSON
    body = """{
                "creationDate": 1458595099104,
                "lastModifiedDate": 1458595099104,
                "conditions": [],
                "createdBy": "id=dsameuser,ou=user,dc=openam,dc=forgerock,dc=org",
                "lastModifiedBy": "id=dsameuser,ou=user,dc=openam,dc=forgerock,dc=org",
                "resourceTypeUuids": ["${oauth2ResourceType}"],
                "resourceComparator": null,
                "applicationType": "iPlanetAMWebAgentService",
                "subjects": [],
                "entitlementCombiner": "DenyOverride",
                "saveIndex": null,
                "searchIndex": null,
                "attributeNames": [],
                "editable": true,
                "description": null,
                "name": "OAuth2"
            }"""

    response.success = { resp, json ->
        println()
        println(json)
    }

    response.failure = { resp ->
        println()
        println "(DEBUG)Create application 'OAuth2': ${resp.entity.content.text}"
    }
}

// Create the OAUTH2 policy
http = new HTTPBuilder("${openamUrl}/json/policies?_action=create")
http.request(POST, JSON) { req ->
    headers.'iPlanetDirectoryPro' = SSOToken
    headers.'Content-Type' = 'application/json'
    requestContentType = ContentType.JSON
    body = """{
                "name": "OAuth2ProviderPolicy",
                "active": true,
                "description": "",
                "applicationName": "OAuth2",
                "actionValues": {
                    "POST": true,
                    "GET": true
                },
                "resources": [
                    "${openamUrl}/oauth2/authorize?*"
                ],
                "subject": {
                    "type": "AuthenticatedUsers"
                },
                "resourceTypeUuid": "${oauth2ResourceType}"
            }"""

    response.success = { resp, json ->
        println()
        println(json)
    }

    response.failure = { resp ->
        println()
        println """(DEBUG)Create policy 'OAuth2ProviderPolicy': ${resp.entity.content.text}"""
    }
}

// Create the OpenID's agent:oidc_client_for_sts
http = new HTTPBuilder("${openamUrl}/json/agents/?_action=create")
http.request(POST, JSON) { req ->
    headers.'iPlanetDirectoryPro' = SSOToken
    headers.'Content-Type' = 'application/json'
    requestContentType = ContentType.JSON
    body = """{
                "username": "${agentName}",
                "userpassword": "${agentPassword}",
                "realm": "/",
                "com.forgerock.openam.oauth2provider.clientType": ["Confidential"],
                "com.forgerock.openam.oauth2provider.accessToken": [],
                "com.forgerock.openam.oauth2provider.claims": ["[0]="],
                "com.forgerock.openam.oauth2provider.sectorIdentifierURI": [],
                "com.forgerock.openam.oauth2provider.jwtTokenLifeTime": ["0"],
                "com.forgerock.openam.oauth2provider.contacts": ["[0]="],
                "com.forgerock.openam.oauth2provider.clientSessionURI": [],
                "com.forgerock.openam.oauth2provider.scopes": ["[0]=openid", "[1]=profile", "[2]=email"],
                "com.forgerock.openam.oauth2provider.responseTypes": ["[6]=code token id_token", "[0]=code", "[4]=token id_token", "[2]=id_token", "[3]=code token", "[1]=token", "[5]=code id_token"],
                "com.forgerock.openam.oauth2provider.authorizationCodeLifeTime": ["0"],
                "com.forgerock.openam.oauth2provider.description": ["[0]="],
                "com.forgerock.openam.oauth2provider.accessTokenLifeTime": ["0"],
                "com.forgerock.openam.oauth2provider.defaultMaxAgeEnabled": ["false"],
                "com.forgerock.openam.oauth2provider.subjectType": ["Public"],
                "agentgroup": [],
                "com.forgerock.openam.oauth2provider.postLogoutRedirectURI": ["[0]="],
                "com.forgerock.openam.oauth2provider.refreshTokenLifeTime": ["0"],
                "com.forgerock.openam.oauth2provider.defaultScopes": ["[0]="],
                "com.forgerock.openam.oauth2provider.name": ["[0]=${agentName}"],
                "AgentType": ["OAuth2Client"],
                "com.forgerock.openam.oauth2provider.redirectionURIs": ["[0]=${redirectionUri}"],
                "com.forgerock.openam.oauth2provider.idTokenSignedResponseAlg": ["${idTokenSigningAlgorithm}"],
                "com.forgerock.openam.oauth2provider.clientName": ["[0]="],
                "com.forgerock.openam.oauth2provider.tokenEndPointAuthMethod": ["${tokenEndPointAuthMethod}"],
                "universalid": ["id=OpenIG,ou=agent,dc=openam,dc=forgerock,dc=org"],
                "com.forgerock.openam.oauth2provider.defaultMaxAge": ["600"],
                "sunIdentityServerDeviceStatus": ["Active"],
                "com.forgerock.openam.oauth2provider.publicKeyLocation": ["x509"],
                "com.forgerock.openam.oauth2provider.jwksURI": [],
                "com.forgerock.openam.oauth2provider.clientJwtPublicKey": []
            }"""

    response.success = { resp, json ->
        println()
        println(json)
    }

    response.failure = { resp ->
        println()
        println "(DEBUG)Create agent '${agentName}: ${resp.entity.content.text}"
    }
}

// Create a Bearer authentication module
http = new HTTPBuilder("${openamUrl}/json/realm-config/authentication/modules/openidconnect?_action=create")
http.request(POST, JSON) { req ->
    headers.'iPlanetDirectoryPro' = SSOToken
    headers.'Content-Type' = 'application/json'
    requestContentType = ContentType.JSON
    body = """{
                "cryptoContextValue": "${agentPassword}",
                "jwtToLdapAttributeMappings": ["sub=uid", "email=mail"],
                "principalMapperClass": "org.forgerock.openam.authentication.modules.oidc.JwtAttributeMapper",
                "acceptedAuthorizedParties": ["${agentName}"],
                "idTokenHeaderName": "oidc_id_token",
                "accountProviderClass": "org.forgerock.openam.authentication.modules.common.mapping.DefaultAccountProvider",
                "idTokenIssuer": "${idTokenIssuer}",
                "cryptoContextType": "${cryptoContextType}",
                "audienceName": "${agentName}",
                "_id": "${oidc_issuer}"
		    }"""

    response.success = { resp, json ->
        println()
        println(json)
    }

    response.failure = { resp ->
        println()
        println "(DEBUG)Create Bearer authentication module '${oidc_issuer}: ${resp.entity.content.text}"
    }
}

// Create an Instance of STS REST
http = new HTTPBuilder("${openamUrl}/sts-publish/rest?_action=create")
http.request(POST, JSON) { req ->
    headers.'iPlanetDirectoryPro' = SSOToken
    headers.'Content-Type' = 'application/json'
    requestContentType = ContentType.JSON
    body = """{
				"invocation_context": "invocation_context_client_sdk",
				"instance_state": {
					"saml2-config": {
						"issuer-name": "${saml_issuer_name}",
						"saml2-name-id-format": "urn:oasis:names:tc:SAML:2.0:nameid-format:transient",
						"saml2-token-lifetime-seconds": "600",
						"saml2-custom-conditions-provider-class-name": null,
						"saml2-custom-subject-provider-class-name": null,
						"saml2-custom-attribute-statements-provider-class-name": null,
						"saml2-custom-attribute-mapper-class-name": null,
						"saml2-custom-authn-context-mapper-class-name": null,
						"saml2-custom-authentication-statements-provider-class-name": null,
						"saml2-custom-authz-decision-statements-provider-class-name": null,
						"saml2-sign-assertion": "false",
						"saml2-encrypt-assertion": "false",
						"saml2-encrypt-attributes": "false",
						"saml2-encrypt-nameid": "false",
						"saml2-encryption-algorithm": "http://www.w3.org/2001/04/xmlenc#aes128-cbc",
						"saml2-encryption-algorithm-strength": "128",
						"saml2-attribute-map": {
							"password": "mail",
							"userName": "uid"
						},
						"saml2-keystore-filename": null,
						"saml2-keystore-password": null,
						"saml2-sp-acs-url": null,
						"saml2-sp-entity-id": "openig_sp",
						"saml2-signature-key-alias": null,
						"saml2-signature-key-password": null,
						"saml2-encryption-key-alias": null
					},
					"deployment-config": {
						"deployment-url-element": "openig",
						"deployment-realm": "/",
						"deployment-auth-target-mappings": {
							"USERNAME": {
								"mapping-auth-index-type": "service",
								"mapping-auth-index-value": "ldapService"
							},
							"X509": {
								"mapping-auth-index-type": "module",
								"mapping-auth-index-value": "cert_module",
								"mapping-context": {
									"x509_token_auth_target_header_key": "client_cert"
								}
							},
							"OPENIDCONNECT": {
								"mapping-auth-index-type": "module",
								"mapping-auth-index-value": "oidc",
								"mapping-context": {
									"oidc_id_token_auth_target_header_key": "oidc_id_token"
								}
							}
						},
						"deployment-offloaded-two-way-tls-header-key": null,
						"deployment-tls-offload-engine-hosts": []
					},
					"persist-issued-tokens-in-cts": "false",
					"supported-token-transforms": [{
						"inputTokenType": "OPENAM",
						"outputTokenType": "SAML2",
						"invalidateInterimOpenAMSession": false
					}, {
						"inputTokenType": "OPENAM",
						"outputTokenType": "OPENIDCONNECT",
						"invalidateInterimOpenAMSession": false
					}, {
						"inputTokenType": "OPENIDCONNECT",
						"outputTokenType": "OPENIDCONNECT",
						"invalidateInterimOpenAMSession": true
					}, {
						"inputTokenType": "USERNAME",
						"outputTokenType": "SAML2",
						"invalidateInterimOpenAMSession": true
					}, {
						"inputTokenType": "OPENIDCONNECT",
						"outputTokenType": "SAML2",
						"invalidateInterimOpenAMSession": true
					}, {
						"inputTokenType": "X509",
						"outputTokenType": "OPENIDCONNECT",
						"invalidateInterimOpenAMSession": true
					}, {
						"inputTokenType": "USERNAME",
						"outputTokenType": "OPENIDCONNECT",
						"invalidateInterimOpenAMSession": true
					}, {
						"inputTokenType": "X509",
						"outputTokenType": "SAML2",
						"invalidateInterimOpenAMSession": true
					}],
					"oidc-id-token-config": {
						"oidc-issuer": "${oidc_issuer}",
						"oidc-public-key-reference-type": "NONE",
						"oidc-token-lifetime-seconds": "600",
						"oidc-authorized-party": null,
						"oidc-audience": ["${agentName}"],
						"oidc-signature-algorithm": "${idTokenSigningAlgorithm}",
						"oidc-claim-map": {},
						"oidc-custom-claim-mapper-class": null,
						"oidc-custom-authn-context-mapper-class": null,
						"oidc-custom-authn-method-references-mapper-class": null,
						"oidc-keystore-location": null,
						"oidc-keystore-password": null,
						"oidc-client-secret": "${agentPassword}",
						"oidc-signature-key-alias": null,
						"oidc-signature-key-password": null
					}
				}
			}"""

    response.success = { resp, json ->
        println()
        println(json)
    }

    response.failure = { resp ->
        println()
        println "(DEBUG)Create an Instance of STS REST: ${resp.entity.content.text}"
    }
}

