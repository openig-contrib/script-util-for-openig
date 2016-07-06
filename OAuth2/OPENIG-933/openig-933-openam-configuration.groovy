import static groovyx.net.http.ContentType.JSON
@Grab(group = 'org.codehaus.groovy.modules.http-builder', module = 'http-builder', version = '0.7.1')
import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.POST

import groovyx.net.http.*

// -----------------------------------------------------------------------------------------------------
// This script is use to configure your OPENAM according to OPENIG-933
// https://bugster.forgerock.org/jira/browse/OPENIG-933
// # tested with OpenAM 13.0.0 Build 5d4589530d (2016-January-14 21:15)
// # vrom 2016
// -----------------------------------------------------------------------------------------------------
// CONFIGURATION (Update it if necessary)
// -----------------------------------------------------------------------------------------------------

final String openigBase = "http://localhost:8082/"
final String user = "amadmin"
final String userPass = "secret12"
final String openamUrl = "http://localhost:8090/openam" // URL must NOT end with a slash
final String agentName = "ForgeShop"
final String agentPassword = "password"
final String redirectionUri = "http://localhost:8082/openid/callback"
// -----------------------------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------------------------
// Create a properties file according to your configuration.
// This file will be used in your route to access the different values.
final Properties props = new Properties()
final String pathPropsFile = System.getProperty("user.home");
final File propsFile = new File(pathPropsFile + "/openig.properties")
props.setProperty("openigBase", openigBase)
props.setProperty("openamUrl", openamUrl)
props.setProperty("agentName", agentName)
props.setProperty("agentPassword", agentPassword)
props.setProperty("redirectionUri", redirectionUri)
props.store(propsFile.newWriter(), "Properties file generated for OPENIG-933")
println()
println "(DEBUG)Created properties file in >>${pathPropsFile}.<<"
println()
// -----------------------------------------------------------------------------------------------------
def SSOToken
def http
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
        println "(DEBUG)Unable to create token: ${resp.entity.content.text}"
    }
}

// Create a user
http = new HTTPBuilder("${openamUrl}/json/users/?_action=create")
http.request(POST, JSON) { req ->
    headers.'iPlanetDirectoryPro' = SSOToken
    headers.'Content-Type' = 'application/json'
    requestContentType = ContentType.JSON
    body = """{
                "username": "gabby",
                "userpassword": "secret12",
                "mail": "gabby@wisteria.com",
                "postalAddress": "4349, Wisteria Lane, San Francisco, CA 94105",
                "telephoneNumber": "(831) 799-9999"
              }"""

    response.success = { resp, json ->
        println()
        println(json)
    }

    response.failure = { resp ->
        println()
        println """(DEBUG)Create user: ${resp.entity.content.text}"""
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
                "accessTokenLifetime": 10,
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
        println "(DEBUG)Create application: ${resp.entity.content.text}" }
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
                "resourceTypeUuids": ["a4a432f3-d55b-40af-8771-fbfc51517f1f"],
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
        println "(DEBUG)Create application: ${resp.entity.content.text}"
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
                "resourceTypeUuid": "a4a432f3-d55b-40af-8771-fbfc51517f1f"
            }"""

    response.success = { resp, json ->
        println()
        println(json)
    }

    response.failure = { resp ->
        println()
        println """(DEBUG)Create policy: ${resp.entity.content.text}"""
    }
}

// Create the OpenID's agent.
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
                "com.forgerock.openam.oauth2provider.scopes": ["[0]=openid", "[1]=profile", "[2]=address", "[3]=phone"],
                "com.forgerock.openam.oauth2provider.responseTypes": ["[6]=code token id_token", "[0]=code", "[4]=token id_token", "[2]=id_token", "[3]=code token", "[1]=token", "[5]=code id_token"],
                "com.forgerock.openam.oauth2provider.authorizationCodeLifeTime": ["0"],
                "com.forgerock.openam.oauth2provider.description": ["[0]="],
                "com.forgerock.openam.oauth2provider.accessTokenLifeTime": ["3"],
                "com.forgerock.openam.oauth2provider.defaultMaxAgeEnabled": ["false"],
                "com.forgerock.openam.oauth2provider.subjectType": ["Public"],
                "agentgroup": [],
                "com.forgerock.openam.oauth2provider.postLogoutRedirectURI": ["[0]="],
                "com.forgerock.openam.oauth2provider.refreshTokenLifeTime": ["3600"],
                "com.forgerock.openam.oauth2provider.defaultScopes": ["[0]="],
                "com.forgerock.openam.oauth2provider.name": ["[0]=${agentName}"],
                "AgentType": ["OAuth2Client"],
                "com.forgerock.openam.oauth2provider.redirectionURIs": ["[0]=${redirectionUri}"],
                "com.forgerock.openam.oauth2provider.idTokenSignedResponseAlg": ["RS256"],
                "com.forgerock.openam.oauth2provider.clientName": ["[0]=${agentName}"],
                "com.forgerock.openam.oauth2provider.tokenEndPointAuthMethod": ["client_secret_basic"],
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
        println "(DEBUG)Create agent: ${resp.entity.content.text}"
    }
}
