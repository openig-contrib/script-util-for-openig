import static groovyx.net.http.ContentType.JSON
@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7.1' )
import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.POST

import groovyx.net.http.*

// -----------------------------------------------------------------------------------------------------
// This script is used to configure your OPENAM - As described in Gateway Guide 6.3
// # tested with OpenAM 13.0.0 Build 5d4589530d (2016-January-14 21:15)
// # vrom 07/07/2016
// -----------------------------------------------------------------------------------------------------
// CONFIGURATION (Update it if necessary)
// -----------------------------------------------------------------------------------------------------
final String user = "amadmin"
final String userPassword = "secret12"
final String openamUrl = "http://localhost:8090/openam" // URL must NOT end with a slash
final String resourceToProtect = "http://localhost:8081/pep"

// EXAMPLE CONFIGURATION
// -----------------------------------------------------------------------------------------------------
final String applicationName = "iPlanetAMWebAgentService"
final String policyName = "Policy for OpenIG as PEP"
final String description = "See Gateway Guide 6.3. Setting Up OpenAM As a PDP"
// -----------------------------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------------------------
def SSOToken
def http
// Request to get an SSOToken
http = new HTTPBuilder("${openamUrl}/json/authenticate")
http.request(POST, JSON) { req ->
    headers.'X-OpenAM-Username' = user
    headers.'X-OpenAM-Password' = userPassword
    headers.'Content-Type' = 'application/json'
    requestContentType = ContentType.JSON
    body = ''

    response.success = { resp, json ->
        println(json)
        SSOToken = json.tokenId
    }

    response.failure = { resp -> println "(DEBUG)Unable to create token: ${resp.entity.content.text}" }
}

// Create a user
http = new HTTPBuilder("${openamUrl}/json/users/?_action=create")
http.request(POST, JSON) { req ->
    headers.'iPlanetDirectoryPro' = SSOToken
    headers.'Content-Type' = 'application/json'
    requestContentType = ContentType.JSON
    body = """{
                "username" : "policyAdmin",
                "userpassword": "password",
                "mail": "policyAdmin@example.com"
           }"""

    response.success = { resp, json ->
        println()
        println(json)
    }

    response.failure = { resp ->
        println()
        println "(DEBUG)Unable to create user: ${resp.entity.content.text}" }
}

// Create a group
http = new HTTPBuilder("${openamUrl}/json/groups?_action=create")
http.request(POST, JSON) { req ->
    headers.'iPlanetDirectoryPro' = SSOToken
    headers.'Content-Type' = 'application/json'
    requestContentType = ContentType.JSON
    body = """{
                "username":"policyAdmins",
                "uniquemember":["uid=policyAdmin,ou=people,dc=openam,dc=forgerock,dc=org"]
           }"""

    response.success = { resp, json ->
        println()
        println(json)
    }

    response.failure = { resp ->
        println()
        println "(DEBUG)Unable to create group: ${resp.entity.content.text}" }
}

// Creates the application|policy set
http = new HTTPBuilder("${openamUrl}/json/applications/?_action=create")
http.request(POST, JSON) { req ->
    headers.'iPlanetDirectoryPro' = SSOToken
    headers.'Content-Type' = 'application/json'
    requestContentType = ContentType.JSON
    body = """{
                "name": "${applicationName}",
                "realm": "/",
                "conditions": [
                    "AND",
                    "OR",
                    "NOT",
                    "AMIdentityMembership",
                    "AuthLevel",
                    "AuthScheme",
                    "AuthenticateToRealm",
                    "AuthenticateToService",
                    "IPv4",
                    "IPv6",
                    "LDAPFilter",
                    "LEAuthLevel",
                    "OAuth2Scope",
                    "ResourceEnvIP",
                    "Session",
                    "SessionProperty",
                    "SimpleTime",
                    "Script"
                ],
                "subjects": [
                    "AND",
                    "OR",
                    "NOT",
                    "AuthenticatedUsers",
                    "Identity",
                    "JwtClaim"
                ],
                "applicationType": "iPlanetAMWebAgentService",
                "description": "${description}",
                "resourceComparator": "com.sun.identity.entitlement.URLResourceName",
                "entitlementCombiner": "DenyOverride",
                "saveIndex": null,
                "searchIndex": null,
                "resourceTypeUuids": [
                    "76656a38-5f8e-401b-83aa-4ccb74ce88d2"
                ]
            }"""

    response.success = { resp, json ->
        println()
        println(json)
    }

    response.failure = { resp ->
        println()
        println "(DEBUG)Create application: ${resp.entity.content.text}" }
}

// Creates the policy
http = new HTTPBuilder("${openamUrl}/json/policies?_action=create")
http.request(POST, JSON) { req ->
    headers.'iPlanetDirectoryPro' = SSOToken
    headers.'Content-Type' = 'application/json'
    requestContentType = ContentType.JSON
    body = """{
                "name": "${policyName}",
                "active": true,
                "description": "${description}",
                "applicationName": "${applicationName}",
                "actionValues": {
                    "POST": false,
                    "GET": true,
                    "PUT": false,
                    "DELETE": false
                },
                "resources": [
                    "${resourceToProtect}"
                ],
                "subject": {
                    "type": "AuthenticatedUsers"
                },
                "resourceTypeUuid": "76656a38-5f8e-401b-83aa-4ccb74ce88d2"
            }"""

    response.success = { resp, json ->
        println()
        println(json)
    }

    response.failure = { resp ->
        println()
        println "(DEBUG)Create policy: ${resp.entity.content.text}" }
}
