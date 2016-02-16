@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7.1' )
import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.POST
import org.apache.commons.codec.binary.Base64

import groovyx.net.http.*

// -----------------------------------------------------------------------------------------------------
// This script is use to configure your OPENAM - As described in Gateway Guide 6.3
// # tested with OpenAM 13.0.0 Build 5d4589530d (2016-January-14 21:15)
// # vrom 2016
// -----------------------------------------------------------------------------------------------------
// CONFIGURATION (Update it if necessary)
// -----------------------------------------------------------------------------------------------------

def user = "amadmin"
def userpass = "secret12"
def openamurl = "http://localhost:8090/openam" // URL must NOT end with a slash
def resourceToProtect = "http://www.example.com:*/*" // Your OpenIG URL resource

// EXAMPLE CONFIGURATION
// -----------------------------------------------------------------------------------------------------
def applicationName = "iPlanetAMWebAgentService"
def policyName = "Policy for OpenIG as PEP"
def description = "See Gateway Guide 6.3. Setting Up OpenAM As a PDP"

// -----------------------------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------------------------
def SSOToken;
// Request to get an SSOToken
def http = new HTTPBuilder("${openamurl}/json/authenticate")
http.request(POST, JSON) { req ->
    headers.'X-OpenAM-Username' = user
    headers.'X-OpenAM-Password' = userpass
    headers.'Content-Type' = 'application/json'
    requestContentType = ContentType.JSON
    body = ''

    response.success = { resp, json ->
        println(json)
        SSOToken = json.tokenId;
    }

    response.failure = { resp -> println "(DEBUG)Unable to create token: ${resp.entity.content.text}" }
}

// Create a user
http = new HTTPBuilder("${openamurl}/json/users/?_action=create")
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
http = new HTTPBuilder("${openamurl}/json/groups?_action=create")
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
http = new HTTPBuilder("${openamurl}/json/applications/?_action=create")
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
http = new HTTPBuilder("${openamurl}/json/policies?_action=create")
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