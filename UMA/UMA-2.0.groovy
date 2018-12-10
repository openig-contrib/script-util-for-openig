import static groovyx.net.http.ContentType.JSON
@Grab(group = 'org.codehaus.groovy.modules.http-builder', module = 'http-builder', version = '0.7.1')
import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.*

import groovyx.net.http.*

// -----------------------------------------------------------------------------------------------------
// Use this script to configure OPENAM for the UMA example IG the Gateway Guide
// # tested with AM-5.5.0
// -----------------------------------------------------------------------------------------------------
// CONFIGURATION (Update it if necessary)
// -----------------------------------------------------------------------------------------------------
final String openamUrl = "http://openam.example.com:8090/openam" // URL must NOT end with a slash
final String amadmin = 'amadmin'
final String amadminPassword = "secret12"
// Following properties are for the doc example - DO NOT CHANGE!
final String agentShare = "OpenIG"
final String agentSharePassword = "password"
final String agentAccess = "UmaClient"
final String agentAccessPassword = "password"
// -----------------------------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------------------------
def SSOToken
def http
// Request to get an SSOToken
http = new HTTPBuilder("${openamUrl}/json/authenticate")
http.request(POST, JSON) { req ->
    headers.'X-OpenAM-Username' = amadmin
    headers.'X-OpenAM-Password' = amadminPassword
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
        println "(DEBUG)Unable to create SSO token: ${resp.entity.content.text}"
    }
}

// Create users
http = new HTTPBuilder("${openamUrl}/json/users/?_action=create")
createUser(http, SSOToken, "Alice")
createUser(http, SSOToken, "Bob")

// Configure OpenAM for oauth2-oidc
http = new HTTPBuilder("${openamUrl}/json/realm-config/services/oauth-oidc")
http.request(PUT, JSON) { req ->
    headers.'iPlanetDirectoryPro' = SSOToken
    headers.'Accept-Api-Version' = 'resource=2.1'
    headers.'Content-Type' = 'application/json'
    requestContentType = ContentType.JSON
    body = """
            {
              "coreOAuth2Config": {
                "refreshTokenLifetime": 604800,
                "accessTokenLifetime": 3600,
                "codeLifetime": 120,
                "issueRefreshTokenOnRefreshedToken": false,
                "issueRefreshToken": false,
                "statelessTokensEnabled": false
              },
              "advancedOAuth2Config": {
                "customLoginUrlTemplate": null,
                "supportedScopes": [
                  "email|Your email address",
                  "openid|",
                  "address|Your postal address",
                  "phone|Your telephone number(s)",
                  "profile|Your personal information",
                  "uma_authorization|Provide access to data shared with you",
                  "uma_protection|Control access to your data"
                ],
                "codeVerifierEnforced": false,
                "tokenSigningHmacSharedSecret": null,
                "tokenSigningAlgorithm": "HS256",
                "authenticationAttributes": [
                  "uid"
                ],
                "defaultScopes": [
                  "address",
                  "phone",
                  "openid",
                  "uma_protection",
                  "profile",
                  "uma_authorization",
                  "email"
                ],
                "scopeImplementationClass": "org.forgerock.openam.oauth2.OpenAMScopeValidator",
                "modifiedTimestampAttribute": null,
                "responseTypeClasses": [
                  "code|org.forgerock.oauth2.core.AuthorizationCodeResponseTypeHandler",
                  "id_token|org.forgerock.openidconnect.IdTokenResponseTypeHandler",
                  "device_code|org.forgerock.oauth2.core.TokenResponseTypeHandler",
                  "token|org.forgerock.oauth2.core.TokenResponseTypeHandler"
                ],
                "tokenSigningECDSAKeyAlias": [
                  "ES512|es512test",
                  "ES384|es384test",
                  "ES256|es256test"
                ],
                "hashSalt": null,
                "moduleMessageEnabledInPasswordGrant": false,
                "tokenCompressionEnabled": null,
                "createdTimestampAttribute": null,
                "displayNameAttribute": "cn",
                "keypairName": "test",
                "supportedSubjectTypes": [
                  "public"
                ]
              },
              "coreOIDCConfig": {
                "supportedIDTokenEncryptionMethods": [
                  "A256GCM",
                  "A192GCM",
                  "A128GCM",
                  "A128CBC-HS256",
                  "A192CBC-HS384",
                  "A256CBC-HS512"
                ],
                "jwtTokenLifetime": 3600,
                "supportedClaims": [
                  "phone_number|Phone number",
                  "family_name|Family name",
                  "given_name|Given name",
                  "locale|Locale",
                  "email|Email address",
                  "profile|Your personal information",
                  "zoneinfo|Time zone",
                  "address|Postal address",
                  "name|Full name"
                ],
                "supportedIDTokenEncryptionAlgorithms": [
                  "RSA-OAEP",
                  "RSA-OAEP-256",
                  "A128KW",
                  "A256KW",
                  "RSA1_5",
                  "dir",
                  "A192KW"
                ],
                "supportedIDTokenSigningAlgorithms": [
                  "ES384",
                  "HS256",
                  "HS512",
                  "ES256",
                  "RS256",
                  "HS384",
                  "ES512"
                ],
                "oidcClaimsScript": "36863ffb-40ec-48b9-94b1-9a99f71cc3b5",
                "tokenEncryptionSigningKeyAlias": [
                  "RSA1_5|test",
                  "RSA-OAEP|test",
                  "RSA-OAEP-256|test"
                ]
              },
              "advancedOIDCConfig": {
                "storeOpsTokens": true,
                "defaultACR": null,
                "supportedRequestParameterEncryptionEnc": [
                  "A256GCM",
                  "A192GCM",
                  "A128GCM",
                  "A128CBC-HS256",
                  "A192CBC-HS384",
                  "A256CBC-HS512"
                ],
                "claimsParameterSupported": false,
                "amrMappings": {},
                "requireRequestUriRegistration": false,
                "alwaysAddClaimsToToken": false,
                "supportedRequestParameterEncryptionAlgorithms": [
                  "RSA-OAEP",
                  "RSA-OAEP-256",
                  "A128KW",
                  "RSA1_5",
                  "A256KW",
                  "dir",
                  "A192KW"
                ],
                "authorisedOpenIdConnectSSOClients": [],
                "idTokenInfoClientAuthenticationEnabled": true,
                "supportedRequestParameterSigningAlgorithms": [
                  "ES384",
                  "HS256",
                  "HS512",
                  "ES256",
                  "RS256",
                  "HS384",
                  "ES512"
                ],
                "loaMapping": {},
                "jkwsURI": null
              },
              "clientDynamicRegistrationConfig": {
                "dynamicClientRegistrationSoftwareStatementRequired": false,
                "requiredSoftwareStatementAttestedAttributes": [
                  "redirect_uris"
                ],
                "generateRegistrationAccessTokens": true,
                "allowDynamicRegistration": false
              },
              "consent": {
                "enableRemoteConsent": false,
                "supportedRcsRequestSigningAlgorithms": [
                  "ES384",
                  "HS256",
                  "HS512",
                  "ES256",
                  "RS256",
                  "HS384",
                  "ES512"
                ],
                "supportedRcsResponseSigningAlgorithms": [
                  "ES384",
                  "HS256",
                  "HS512",
                  "ES256",
                  "RS256",
                  "HS384",
                  "ES512"
                ],
                "clientsCanSkipConsent": false,
                "supportedRcsRequestEncryptionAlgorithms": [
                  "RSA-OAEP",
                  "RSA-OAEP-256",
                  "A128KW",
                  "RSA1_5",
                  "A256KW",
                  "dir",
                  "A192KW"
                ],
                "supportedRcsResponseEncryptionMethods": [
                  "A256GCM",
                  "A192GCM",
                  "A128GCM",
                  "A128CBC-HS256",
                  "A192CBC-HS384",
                  "A256CBC-HS512"
                ],
                "supportedRcsRequestEncryptionMethods": [
                  "A256GCM",
                  "A192GCM",
                  "A128GCM",
                  "A128CBC-HS256",
                  "A192CBC-HS384",
                  "A256CBC-HS512"
                ],
                "remoteConsentServiceId": null,
                "savedConsentAttribute": null,
                "supportedRcsResponseEncryptionAlgorithms": [
                  "RSA-OAEP",
                  "RSA-OAEP-256",
                  "A128KW",
                  "A256KW",
                  "RSA1_5",
                  "dir",
                  "A192KW"
                ]
              },
              "deviceCodeConfig": {
                "devicePollInterval": 5,
                "completionUrl": null,
                "deviceCodeLifetime": 300,
                "verificationUrl": null
              },
              "_type": {
                "_id": "oauth-oidc",
                "name": "OAuth2 Provider",
                "collection": false
              }
            }
           """

    response.success = { resp, json ->
        println()
        println(json)
    }

    response.failure = { resp ->
        println()
        println "(DEBUG)Configure OpenAM for oauth2-oidc: ${resp.entity.content.text}" }
}

// Configure OpenAM for UMA
http = new HTTPBuilder("${openamUrl}/json/realm-config/services/uma")
http.request(PUT, JSON) { req ->
    headers.'iPlanetDirectoryPro' = SSOToken
    headers.'Accept-Api-Version' = 'resource=2.1'
    headers.'Content-Type' = 'application/json'
    requestContentType = ContentType.JSON
    body = """{
              "deletePoliciesOnDeleteRS": true,
              "resharingMode": "IMPLICIT",
              "permissionTicketLifetime": 120,
              "emailRequestingPartyOnPendingRequestApproval": true,
              "userProfileLocaleAttribute": "inetOrgPerson",
              "grantRptConditions": [
                "REQUEST_PARTIAL",
                "REQUEST_NONE",
                "TICKET_PARTIAL"
              ],
              "pendingRequestsEnabled": true,
              "deleteResourceSetsOnDeleteRS": true,
              "emailResourceOwnerOnPendingRequestCreation": true,
              "_type": {
                "_id": "uma",
                "name": "UMA Provider",
                "collection": false
              }
            }"""

    response.success = { resp, json ->
        println()
        println(json)
    }

    response.failure = { resp ->
        println()
        println "(DEBUG)Configure OpenAM for UMA: ${resp.entity.content.text}" }
}

http = new HTTPBuilder("${openamUrl}/json/agents/?_action=create")
createAgent(http, SSOToken, agentShare, agentSharePassword, "uma_protection")
createAgent(http, SSOToken, agentAccess, agentAccessPassword, "openid")

// -----------------------------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------------------------
private void createUser(HTTPBuilder http, String SSOToken, String name) {
    http.request(POST, JSON) { req ->
        headers.'iPlanetDirectoryPro' = SSOToken
        headers.'Accept-Api-Version' = 'resource=2.1'
        headers.'Content-Type' = 'application/json'
        requestContentType = ContentType.JSON
        body = """{
                "username": "${name.toLowerCase()}",
                "cn": "${name} User",
                "givenName": "${name}",
                "sn": "User",
                "userpassword": "password"
               }"""

        response.success = { resp, json ->
            println()
            println(json)
        }

        response.failure = { resp ->
            println()
            println """(DEBUG)Create user '${name}': ${resp.entity.content.text}"""
        }
    }
}

private String getResourceTypeUuid(String openamUrl, String SSOToken, String resourceName) {
    http = new HTTPBuilder("${openamUrl}/json/resourcetypes/?_queryFilter=name+eq+%22${resourceName}%22&_prettyPrint=true")
    http.request(GET, JSON) { req ->
        headers.'iPlanetDirectoryPro' = SSOToken
        headers.'Accept-Api-Version' = 'resource=2.1'
        headers.'Content-Type' = 'application/json'
        requestContentType = ContentType.JSON

        response.success = { resp, json ->
            return json.result[0].uuid
        }

        response.failure = { resp ->
            println()
            println """(DEBUG)getResourceTypeUuid '${resourceName}': ${resp.entity.content.text}"""
        }
    }
}

private void createAgent(HTTPBuilder http, String SSOToken, String agentName, String agentPassword, String scope) {
    http.request(POST, JSON) { req ->
        headers.'iPlanetDirectoryPro' = SSOToken
        headers.'Accept-Api-Version' = 'resource=2.1'
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
                "com.forgerock.openam.oauth2provider.scopes": ["[0]=${scope}"],
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
                "com.forgerock.openam.oauth2provider.redirectionURIs": ["[0]="],
                "com.forgerock.openam.oauth2provider.idTokenSignedResponseAlg": ["HS256"],
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
            println "(DEBUG)Create agent '${agentName}: ${resp.entity.content.text}"
        }
    }
}


