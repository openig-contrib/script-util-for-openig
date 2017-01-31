import static groovyx.net.http.ContentType.JSON
@Grab(group = 'org.codehaus.groovy.modules.http-builder', module = 'http-builder', version = '0.7.1')
import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.POST
import static groovyx.net.http.Method.GET

import groovyx.net.http.*

// -----------------------------------------------------------------------------------------------------
// This script is use to configure your OPENAM for UMA
// # tested with OpenAM 13.5.0 Build 550cfe7d60 (2016-July-13 08:43)
// # vrom - 16 01 2017
// -----------------------------------------------------------------------------------------------------
// CONFIGURATION (Update it if necessary)
// -----------------------------------------------------------------------------------------------------
final String openamUrl = "http://localhost:8090/openam" // URL must NOT end with a slash
final String amadmin = 'amadmin'
final String adadminpassword = "secret12"
// Following properties are for the doc example - DO NOT CHANGE!
final String agentShare = "OpenIG"
final String agentSharePassword = "password"
final String agentAccess = "UmaClient"
final String agentAccessPassword = "password"
// -----------------------------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------------------------
def SSOToken;
def http;
// Request to get an SSOToken
http = new HTTPBuilder("${openamUrl}/json/authenticate")
http.request(POST, JSON) { req ->
    headers.'X-OpenAM-Username' = amadmin
    headers.'X-OpenAM-Password' = adadminpassword
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

// Create users
http = new HTTPBuilder("${openamUrl}/json/users/?_action=create")
createUser(http, SSOToken, "Alice")
createUser(http, SSOToken, "Bob")

// Configure OpenAM for oauth2-oidc
http = new HTTPBuilder("${openamUrl}/json/realm-config/services/oauth-oidc/?_action=create&_prettyPrint=true")
http.request(POST, JSON) { req ->
    headers.'iPlanetDirectoryPro' = SSOToken
    headers.'Content-Type' = 'application/json'
    requestContentType = ContentType.JSON
    body = '{}'

    response.success = { resp, json ->
        println()
        println(json)
    }

    response.failure = { resp ->
        println()
        println "(DEBUG)Configure OpenAM for oauth2-oidc: ${resp.entity.content.text}" }
}

// Configure OpenAM for UMA
http = new HTTPBuilder("${openamUrl}/json/realm-config/services/uma/?_action=create&_prettyPrint=true")
http.request(POST, JSON) { req ->
    headers.'iPlanetDirectoryPro' = SSOToken
    headers.'Content-Type' = 'application/json'
    requestContentType = ContentType.JSON
    body = """{
                "requireTrustElevation": false
            }"""

    response.success = { resp, json ->
        println()
        println(json)
    }

    response.failure = { resp ->
        println()
        println "(DEBUG)Configure OpenAM for UMA: ${resp.entity.content.text}" }
}

def oauth2ResourceTypeUuid
// Creates OAuth2 resource type
http = new HTTPBuilder("${openamUrl}/json/resourcetypes/?_action=create")
http.request(POST, JSON) { req ->
    headers.'iPlanetDirectoryPro' = SSOToken
    headers.'Content-Type' = 'application/json'
    requestContentType = ContentType.JSON
    body = """{
                "name": "OAuth2",
                "description": "",
                "patterns": ["*:/:**///*authorize?*"],
                "actions": {
                    "POST": true,
                    "GET": true
                }
            }"""

    response.success = { resp, json ->
        println()
        oauth2ResourceTypeUuid = json.uuid
        println(json)
    }

    response.failure = { resp ->
        println()
        println "(DEBUG)Create resource type OAuth2: ${resp.entity.content.text}"
        // Test if the oauth2 resource already exists
        if (resp.status == 409) {
            // Retrieve UUID
            oauth2ResourceTypeUuid = getResourceTypeUuid(openamUrl, SSOToken, "OAuth2")
        }
    }
}

// Creates the application|policy set
http = new HTTPBuilder("${openamUrl}/json/applications/?_action=create")
http.request(POST, JSON) { req ->
    headers.'iPlanetDirectoryPro' = SSOToken
    headers.'Content-Type' = 'application/json'
    requestContentType = ContentType.JSON
    body = """{
            "saveIndex": null,
            "searchIndex": null,
            "resourceComparator": null,
            "applicationType": "iPlanetAMWebAgentService",
            "entitlementCombiner": "DenyOverride",
            "subjects": ["Policy", "NOT", "OR", "JwtClaim", "AuthenticatedUsers", "AND", "Identity", "NONE"],
            "resourceTypeUuids": ["${oauth2ResourceTypeUuid}"],
            "attributeNames": [],
            "creationDate": 1484560313223,
            "lastModifiedDate": 1484560313223,
            "lastModifiedBy": "id=dsameuser,ou=user,dc=openam,dc=forgerock,dc=org",
            "createdBy": "id=dsameuser,ou=user,dc=openam,dc=forgerock,dc=org",
            "editable": true,
            "conditions": ["LEAuthLevel", "Policy", "Script", "AuthenticateToService", "SimpleTime", "AMIdentityMembership", "OR", "IPv6", "IPv4", "SessionProperty", "AuthScheme", "AuthLevel", "NOT", "AuthenticateToRealm", "AND", "ResourceEnvIP", "LDAPFilter", "OAuth2Scope", "Session"],
            "description": null,
            "displayName": null,
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
            "resources": ["${openamUrl}/oauth2/authorize?*"],
            "subject": {
            "type": "AuthenticatedUsers"
            },
            "resourceTypeUuid": "${oauth2ResourceTypeUuid}"
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
createAgent(http, SSOToken, agentShare, agentSharePassword, "uma_protection")
createAgent(http, SSOToken, agentAccess, agentAccessPassword, "uma_authorization")

// -----------------------------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------------------------
private void createUser(HTTPBuilder http, String SSOToken, String name) {
    http.request(POST, JSON) { req ->
        headers.'iPlanetDirectoryPro' = SSOToken
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


