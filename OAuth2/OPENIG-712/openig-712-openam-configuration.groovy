import groovyx.net.http.*

import static groovyx.net.http.ContentType.JSON
@Grab(group = 'org.codehaus.groovy.modules.http-builder', module = 'http-builder', version = '0.7.1')
import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.POST
import static groovyx.net.http.Method.PUT

// -----------------------------------------------------------------------------------------------------
// This script is used to configure your OPENAM according to OPENIG-712
// https://bugster.forgerock.org/jira/browse/OPENIG-712
// # tested with AM-6.5.0
// -----------------------------------------------------------------------------------------------------
// CONFIGURATION (Update it if necessary)
// -----------------------------------------------------------------------------------------------------

final String openigBase = "http://openig.example.com:8084"       // URL must NOT end with a slash
final String openamUrl = "http://openam.example.com:8088/openam" // URL must NOT end with a slash
final String user = 'amadmin'
final String userpass = "password"
final String agentName = "ForgeShop"
final String agentPassword = "password"
final String redirectionUri = "${openigBase}/openid/callback"
// -----------------------------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------------------------
// Create a properties file according to your configuration.
// This file will be used in your route to access the different values.
final Properties props = new Properties()
final String pathPropsFile = System.getProperty("user.home")
final File propsFile = new File(pathPropsFile + "/openig.properties")
props.setProperty("openigBase", openigBase)
props.setProperty("openamUrl", openamUrl)
props.setProperty("agentName", agentName)
props.setProperty("agentPassword", agentPassword)
props.setProperty("redirectionUri", redirectionUri)
props.store(propsFile.newWriter(), "Properties file generated for OPENIG-712")
println()
println "(DEBUG) Created properties file in >>${pathPropsFile}.<<"
println()

// -----------------------------------------------------------------------------------------------------
def SSOToken
def http
// Request to get an SSOToken
http = new HTTPBuilder("${openamUrl}/json/authenticate")
http.request(POST, JSON) { req ->
    headers.'X-OpenAM-Username' = user
    headers.'X-OpenAM-Password' = userpass
    headers.'Accept-Api-Version' = 'resource=2.1'
    headers.'Content-Type' = 'application/json'
    requestContentType = ContentType.JSON
    body = ''

    response.success = { resp, json ->
        println(json)
        SSOToken = json.tokenId
    }

    response.failure = { resp ->
        println()
        println "(DEBUG)Unable to create token: ${resp.entity.content.text}"
    }
}

// Create a user
http = new HTTPBuilder("${openamUrl}/json/realms/root/users/gabby")
http.request(PUT, JSON) { req ->
    headers.'iPlanetDirectoryPro' = SSOToken
    headers.'Content-Type' = 'application/json'
    headers.'Accept-Api-Version' = 'protocol=2.0,resource=4.1'
    requestContentType = ContentType.JSON
    body = """{
        "realm": "/",
        "username": "gabby",
        "mail": "gabby@example.org",
        "postalAddress": "14349, Wisteria Lane, San Francisco, CA 94105",
        "inetUserStatus": ["Active"],
        "userPassword": "Ch4ng31t",
        "telephoneNumber": ["831 799-9999"],
        "employeeNumber": ["123"]
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

// Configure OpenAM for oauth2-oidc
http = new HTTPBuilder("${openamUrl}/json/realm-config/services/oauth-oidc")
http.request(PUT, JSON) { req ->
    headers.'iPlanetDirectoryPro' = SSOToken
    headers.'Content-Type' = 'application/json'
    requestContentType = ContentType.JSON
    body = """{
      "advancedOIDCConfig": {
        "supportedRequestParameterEncryptionEnc": [
          "A256GCM",
          "A192GCM",
          "A128GCM",
          "A128CBC-HS256",
          "A192CBC-HS384",
          "A256CBC-HS512"
        ],
        "authorisedOpenIdConnectSSOClients": [],
        "supportedUserInfoEncryptionAlgorithms": [
          "ECDH-ES+A256KW",
          "ECDH-ES+A192KW",
          "RSA-OAEP",
          "ECDH-ES+A128KW",
          "RSA-OAEP-256",
          "A128KW",
          "A256KW",
          "ECDH-ES",
          "dir",
          "A192KW"
        ],
        "supportedAuthorizationResponseEncryptionEnc": [
          "A256GCM",
          "A192GCM",
          "A128GCM",
          "A128CBC-HS256",
          "A192CBC-HS384",
          "A256CBC-HS512"
        ],
        "supportedTokenIntrospectionResponseEncryptionAlgorithms": [
          "ECDH-ES+A256KW",
          "ECDH-ES+A192KW",
          "RSA-OAEP",
          "ECDH-ES+A128KW",
          "RSA-OAEP-256",
          "A128KW",
          "A256KW",
          "ECDH-ES",
          "dir",
          "A192KW"
        ],
        "useForceAuthnForPromptLogin": false,
        "alwaysAddClaimsToToken": false,
        "supportedTokenIntrospectionResponseSigningAlgorithms": [
          "PS384",
          "RS384",
          "EdDSA",
          "ES384",
          "HS256",
          "HS512",
          "ES256",
          "RS256",
          "HS384",
          "ES512",
          "PS256",
          "PS512",
          "RS512"
        ],
        "supportedTokenEndpointAuthenticationSigningAlgorithms": [
          "PS384",
          "ES384",
          "RS384",
          "HS256",
          "HS512",
          "ES256",
          "RS256",
          "HS384",
          "ES512",
          "PS256",
          "PS512",
          "RS512"
        ],
        "supportedRequestParameterSigningAlgorithms": [
          "PS384",
          "ES384",
          "RS384",
          "HS256",
          "HS512",
          "ES256",
          "RS256",
          "HS384",
          "ES512",
          "PS256",
          "PS512",
          "RS512"
        ],
        "includeAllKtyAlgCombinationsInJwksUri": false,
        "amrMappings": {},
        "loaMapping": {},
        "authorisedIdmDelegationClients": [],
        "idTokenInfoClientAuthenticationEnabled": true,
        "storeOpsTokens": true,
        "supportedUserInfoSigningAlgorithms": [
          "ES384",
          "HS256",
          "HS512",
          "ES256",
          "RS256",
          "HS384",
          "ES512"
        ],
        "supportedAuthorizationResponseSigningAlgorithms": [
          "PS384",
          "RS384",
          "EdDSA",
          "ES384",
          "HS256",
          "HS512",
          "ES256",
          "RS256",
          "HS384",
          "ES512",
          "PS256",
          "PS512",
          "RS512"
        ],
        "supportedUserInfoEncryptionEnc": [
          "A256GCM",
          "A192GCM",
          "A128GCM",
          "A128CBC-HS256",
          "A192CBC-HS384",
          "A256CBC-HS512"
        ],
        "claimsParameterSupported": false,
        "supportedTokenIntrospectionResponseEncryptionEnc": [
          "A256GCM",
          "A192GCM",
          "A128GCM",
          "A128CBC-HS256",
          "A192CBC-HS384",
          "A256CBC-HS512"
        ],
        "supportedAuthorizationResponseEncryptionAlgorithms": [
          "ECDH-ES+A256KW",
          "ECDH-ES+A192KW",
          "RSA-OAEP",
          "ECDH-ES+A128KW",
          "RSA-OAEP-256",
          "A128KW",
          "A256KW",
          "ECDH-ES",
          "dir",
          "A192KW"
        ],
        "supportedRequestParameterEncryptionAlgorithms": [
          "ECDH-ES+A256KW",
          "ECDH-ES+A192KW",
          "ECDH-ES+A128KW",
          "RSA-OAEP",
          "RSA-OAEP-256",
          "A128KW",
          "A256KW",
          "ECDH-ES",
          "dir",
          "A192KW"
        ],
        "defaultACR": []
      },
      "advancedOAuth2Config": {
        "tokenCompressionEnabled": false,
        "tokenEncryptionEnabled": false,
        "requirePushedAuthorizationRequests": false,
        "tlsCertificateBoundAccessTokensEnabled": true,
        "defaultScopes": [],
        "moduleMessageEnabledInPasswordGrant": false,
        "supportedSubjectTypes": [
          "public",
          "pairwise"
        ],
        "refreshTokenGracePeriod": 0,
        "tlsClientCertificateHeaderFormat": "URLENCODED_PEM",
        "hashSalt": "changeme",
        "macaroonTokenFormat": "V2",
        "maxAgeOfRequestObjectNbfClaim": 0,
        "tlsCertificateRevocationCheckingEnabled": false,
        "nbfClaimRequiredInRequestObject": false,
        "requestObjectProcessing": "OIDC",
        "maxDifferenceBetweenRequestObjectNbfAndExp": 0,
        "responseTypeClasses": [
          "code|org.forgerock.oauth2.core.AuthorizationCodeResponseTypeHandler",
          "id_token|org.forgerock.openidconnect.IdTokenResponseTypeHandler",
          "token|org.forgerock.oauth2.core.TokenResponseTypeHandler"
        ],
        "expClaimRequiredInRequestObject": false,
        "tokenValidatorClasses": [
          "urn:ietf:params:oauth:token-type:id_token|org.forgerock.oauth2.core.tokenexchange.idtoken.OidcIdTokenValidator",
          "urn:ietf:params:oauth:token-type:access_token|org.forgerock.oauth2.core.tokenexchange.accesstoken.OAuth2AccessTokenValidator"
        ],
        "tokenSigningAlgorithm": "HS256",
        "codeVerifierEnforced": "false",
        "displayNameAttribute": "cn",
        "tokenExchangeClasses": [
          "urn:ietf:params:oauth:token-type:access_token=>urn:ietf:params:oauth:token-type:access_token|org.forgerock.oauth2.core.tokenexchange.accesstoken.AccessTokenToAccessTokenExchanger",
          "urn:ietf:params:oauth:token-type:id_token=>urn:ietf:params:oauth:token-type:id_token|org.forgerock.oauth2.core.tokenexchange.idtoken.IdTokenToIdTokenExchanger",
          "urn:ietf:params:oauth:token-type:access_token=>urn:ietf:params:oauth:token-type:id_token|org.forgerock.oauth2.core.tokenexchange.accesstoken.AccessTokenToIdTokenExchanger",
          "urn:ietf:params:oauth:token-type:id_token=>urn:ietf:params:oauth:token-type:access_token|org.forgerock.oauth2.core.tokenexchange.idtoken.IdTokenToAccessTokenExchanger"
        ],
        "parRequestUriLifetime": 90,
        "allowedAudienceValues": [],
        "persistentClaims": [],
        "supportedScopes": [],
        "authenticationAttributes": [
          "uid"
        ],
        "grantTypes": [
          "implicit",
          "urn:ietf:params:oauth:grant-type:saml2-bearer",
          "refresh_token",
          "password",
          "client_credentials",
          "urn:ietf:params:oauth:grant-type:device_code",
          "authorization_code",
          "urn:openid:params:grant-type:ciba",
          "urn:ietf:params:oauth:grant-type:uma-ticket",
          "urn:ietf:params:oauth:grant-type:token-exchange",
          "urn:ietf:params:oauth:grant-type:jwt-bearer"
        ]
      },
      "clientDynamicRegistrationConfig": {
        "dynamicClientRegistrationScope": "dynamic_client_registration",
        "allowDynamicRegistration": false,
        "requiredSoftwareStatementAttestedAttributes": [
          "redirect_uris"
        ],
        "dynamicClientRegistrationSoftwareStatementRequired": false,
        "generateRegistrationAccessTokens": true
      },
      "coreOIDCConfig": {
        "overrideableOIDCClaims": [],
        "oidcDiscoveryEndpointEnabled": false,
        "supportedIDTokenEncryptionMethods": [
          "A256GCM",
          "A192GCM",
          "A128GCM",
          "A128CBC-HS256",
          "A192CBC-HS384",
          "A256CBC-HS512"
        ],
        "supportedClaims": [],
        "supportedIDTokenSigningAlgorithms": [
          "PS384",
          "ES384",
          "RS384",
          "HS256",
          "HS512",
          "ES256",
          "RS256",
          "HS384",
          "ES512",
          "PS256",
          "PS512",
          "RS512"
        ],
        "supportedIDTokenEncryptionAlgorithms": [
          "ECDH-ES+A256KW",
          "ECDH-ES+A192KW",
          "RSA-OAEP",
          "ECDH-ES+A128KW",
          "RSA-OAEP-256",
          "A128KW",
          "A256KW",
          "ECDH-ES",
          "dir",
          "A192KW"
        ],
        "jwtTokenLifetime": 3600
      },
      "coreOAuth2Config": {
        "refreshTokenLifetime": 604800,
        "scopesPolicySet": "oauth2Scopes",
        "accessTokenMayActScript": "[Empty]",
        "accessTokenLifetime": 3600,
        "macaroonTokensEnabled": false,
        "codeLifetime": 120,
        "statelessTokensEnabled": false,
        "usePolicyEngineForScope": false,
        "issueRefreshToken": true,
        "oidcMayActScript": "[Empty]",
        "issueRefreshTokenOnRefreshedToken": true
      },
      "consent": {
        "supportedRcsRequestSigningAlgorithms": [
          "PS384",
          "ES384",
          "RS384",
          "HS256",
          "HS512",
          "ES256",
          "RS256",
          "HS384",
          "ES512",
          "PS256",
          "PS512",
          "RS512"
        ],
        "supportedRcsResponseEncryptionAlgorithms": [
          "ECDH-ES+A256KW",
          "ECDH-ES+A192KW",
          "ECDH-ES+A128KW",
          "RSA-OAEP",
          "RSA-OAEP-256",
          "A128KW",
          "A256KW",
          "ECDH-ES",
          "dir",
          "A192KW"
        ],
        "supportedRcsRequestEncryptionMethods": [
          "A256GCM",
          "A192GCM",
          "A128GCM",
          "A128CBC-HS256",
          "A192CBC-HS384",
          "A256CBC-HS512"
        ],
        "enableRemoteConsent": false,
        "supportedRcsRequestEncryptionAlgorithms": [
          "ECDH-ES+A256KW",
          "ECDH-ES+A192KW",
          "RSA-OAEP",
          "ECDH-ES+A128KW",
          "RSA-OAEP-256",
          "A128KW",
          "A256KW",
          "ECDH-ES",
          "dir",
          "A192KW"
        ],
        "clientsCanSkipConsent": false,
        "supportedRcsResponseSigningAlgorithms": [
          "PS384",
          "ES384",
          "RS384",
          "HS256",
          "HS512",
          "ES256",
          "RS256",
          "HS384",
          "ES512",
          "PS256",
          "PS512",
          "RS512"
        ],
        "supportedRcsResponseEncryptionMethods": [
          "A256GCM",
          "A192GCM",
          "A128GCM",
          "A128CBC-HS256",
          "A192CBC-HS384",
          "A256CBC-HS512"
        ]
      },
      "deviceCodeConfig": {
        "deviceUserCodeLength": 8,
        "deviceCodeLifetime": 300,
        "deviceUserCodeCharacterSet": "234567ACDEFGHJKLMNPQRSTWXYZabcdefhijkmnopqrstwxyz",
        "devicePollInterval": 5
      },
      "pluginsConfig": {
        "evaluateScopeClass": "org.forgerock.oauth2.core.plugins.registry.DefaultScopeEvaluator",
        "validateScopeScript": "25e6c06d-cf70-473b-bd28-26931edc476b",
        "accessTokenEnricherClass": "org.forgerock.oauth2.core.plugins.registry.DefaultAccessTokenEnricher",
        "oidcClaimsPluginType": "SCRIPTED",
        "authorizeEndpointDataProviderClass": "org.forgerock.oauth2.core.plugins.registry.DefaultEndpointDataProvider",
        "authorizeEndpointDataProviderPluginType": "JAVA",
        "userCodeGeneratorClass": "org.forgerock.oauth2.core.plugins.registry.DefaultUserCodeGenerator",
        "evaluateScopeScript": "da56fe60-8b38-4c46-a405-d6b306d4b336",
        "evaluateScopePluginType": "JAVA",
        "authorizeEndpointDataProviderScript": "3f93ef6e-e54a-4393-aba1-f322656db28a",
        "accessTokenModificationScript": "d22f9a0c-426a-4466-b95e-d0f125b0d5fa",
        "validateScopePluginType": "JAVA",
        "accessTokenModificationPluginType": "SCRIPTED",
        "validateScopeClass": "org.forgerock.oauth2.core.plugins.registry.DefaultScopeValidator",
        "oidcClaimsScript": "36863ffb-40ec-48b9-94b1-9a99f71cc3b5"
      },
      "cibaConfig": {
        "cibaMinimumPollingInterval": 2,
        "supportedCibaSigningAlgorithms": [
          "ES256",
          "PS256"
        ],
        "cibaAuthReqIdLifetime": 600
      }
    }"""

    response.success = { resp, json ->
        println()
        println(json)
    }

    response.failure = { resp ->
        println()
        println "(DEBUG)Configure AM for oauth2-oidc: ${resp.entity.content.text}"
    }
}

def oauth2ResourceType
// Creates OAuth2 resource type
http = new HTTPBuilder("${openamUrl}/json/resourcetypes/?_action=create")
http.request(POST, JSON) { req ->
    headers.'iPlanetDirectoryPro' = SSOToken
    headers.'Content-Type' = 'application/json'
    headers.'Accept-Api-Version' = 'resource=1.0'
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
        oauth2ResourceType = json.uuid
        println(json)
    }

    response.failure = { resp ->
        println()
        println "(DEBUG)Create resource type OAuth2: ${resp.entity.content.text}"
    }
}

// Creates the application|policy set
http = new HTTPBuilder("${openamUrl}/json/applications/?_action=create")
http.request(POST, JSON) { req ->
    headers.'iPlanetDirectoryPro' = SSOToken
    headers.'Content-Type' = 'application/json'
    headers.'Accept-Api-Version' = 'resource=2.1'
    requestContentType = ContentType.JSON
    body = """{
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
        println "(DEBUG)Create OAuth2 application: ${resp.entity.content.text}"
    }
}

// Create the OAUTH2 policy
http = new HTTPBuilder("${openamUrl}/json/policies?_action=create")
http.request(POST, JSON) { req ->
    headers.'iPlanetDirectoryPro' = SSOToken
    headers.'Content-Type' = 'application/json'
    headers.'Accept-Api-Version' = 'resource=2.1'
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
        println """(DEBUG)Create OAuth2 policy: ${resp.entity.content.text}"""
    }
}

// Create the OpenID's agent.
http = new HTTPBuilder("${openamUrl}/json/agents/?_action=create")
http.request(POST, JSON) { req ->
    headers.'iPlanetDirectoryPro' = SSOToken
    headers.'Content-Type' = 'application/json'
    headers.'Accept-Api-Version' = 'resource=2.1'
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
                "com.forgerock.openam.oauth2provider.idTokenSignedResponseAlg": ["RS256"],
                "com.forgerock.openam.oauth2provider.clientName": ["[0]="],
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

println(" -----------------------------------------------------------------------------------------------------")
println("----     DO NOT FORGET to export your system env for IG                            -------------------")
println(" -----------------------------------------------------------------------------------------------------")
println "such as: export OIDC.SECRET.ID='cGFzc3dvcmQ='"
