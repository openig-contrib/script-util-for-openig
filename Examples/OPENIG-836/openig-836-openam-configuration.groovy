@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7.1' )
import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.GET
import static groovyx.net.http.Method.POST
import org.apache.commons.codec.binary.Base64

import groovyx.net.http.*

// -----------------------------------------------------------------------------------------------------
// This script is use to configure your OPENAM according to OPENIG-836
// https://bugster.forgerock.org/jira/browse/OPENIG-836
// # tested with OpenAM 13.0.0 Build 5d4589530d (2016-January-14 21:15)
// # vrom 2016
// -----------------------------------------------------------------------------------------------------
// CONFIGURATION (Update it if necessary)
// -----------------------------------------------------------------------------------------------------

def user = "amadmin"
def userpass = "secret12"
def openamurl = "http://localhost:8090/openam" // URL must NOT end with a slash
def resourceToProtect = "http://localhost:8082/pep-advices"

// EXAMPLE CONFIGURATION 
// -----------------------------------------------------------------------------------------------------
def applicationName = "pep-advices-application"
def policyName = "pep-advices-policy"
def description = "An example for OpenIG-836 - policy enforcement filter advices/environment"

// -----------------------------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------------------------
def SSOToken;
// Request to get an SSOToken
def http = new HTTPBuilder("${openamurl}/json/authenticate")
http.request(POST,JSON) { req ->
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

println()

// Creates the application|policy set
http = new HTTPBuilder("${openamurl}/json/applications/?_action=create")
http.request(POST, JSON) { req ->
    headers.'iPlanetDirectoryPro' = SSOToken
    headers.'Content-Type' = 'application/json'
    requestContentType = ContentType.JSON
    body = """{
                "name": "${applicationName}",
                "resourceTypeUuids": [
                    "76656a38-5f8e-401b-83aa-4ccb74ce88d2"
                ],
                "actions": {
                    "GET": true,
                    "POST": true,
                    "PUT": true,
                    "DELETE": true,
                    "HEAD": true,
                    "OPTIONS": true,
                    "PATCH": true
                },
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
                "applicationType": "iPlanetAMWebAgentService",
                "description": "${description}",
                "resourceComparator": "com.sun.identity.entitlement.URLResourceName",
                "subjects": [
                    "AND",
                    "OR",
                    "NOT",
                    "AuthenticatedUsers",
                    "Identity",
                    "JwtClaim"
                ],
                "entitlementCombiner": "DenyOverride",
                "saveIndex": null,
                "searchIndex": null,
                "attributeNames": []
            }"""

    response.success = { resp, json ->
        println(json)
    }

    response.failure = { resp -> println "(DEBUG)Create application: ${resp.entity.content.text}" }
}

println()
def scriptId

// Checks if the script already exists
http = new HTTPBuilder("${openamurl}/json/scripts?_queryFilter=name%20eq%20%22${applicationName}-script%22")
http.request(GET, JSON) { req ->
    headers.'iPlanetDirectoryPro' = SSOToken
    headers.'Content-Type' = 'application/json'

    response.success = { resp, json ->
        if(json.result.size() > 0) {
            scriptId = json.result['_id'].get(0)
            println("Script '${applicationName}-script' already exists. Using _id='${scriptId}'")            
            println()
        }
    }

    response.failure = { resp -> println "(DEBUG)Unable to retrieve the script: ${resp.entity.content.text}" }
}

if(scriptId == null) {
    // Creates the script
    def encodedScript = """
    if (environment) {
        var dayOfWeek = environment.get("DAY_OF_WEEK");
        if (dayOfWeek != null && !dayOfWeek.isEmpty()) {
            var today = dayOfWeek.iterator().next();
            if (today === "Saturday" || today === "Sunday") {
                advice.put("VALID_DAYS_OF_THE_WEEK", ["Mon, Tue, Wed, Thu, Fri"]);
                authorized = false;
            } else {
                authorized = true;
            }
        } else {
            logger.error("No Day of week specified in the evaluation request environment parameters.");
            authorized = false;
        }
    } else {
      logger.error("No environment parameters specified in the evaluation request.");
      authorized = false;
    }
    """.getBytes().encodeBase64().toString()

    http = new HTTPBuilder("${openamurl}/json/scripts/?_action=create")
    http.request(POST, JSON) { req ->
        headers.'iPlanetDirectoryPro' = SSOToken
        headers.'Content-Type' = 'application/json'
        requestContentType = ContentType.JSON
        body = """{
                    "name": "${applicationName}-script",
                    "script": "$encodedScript",
                    "language": "JAVASCRIPT",
                    "context": "POLICY_CONDITION",
                    "description": "A script for ${description}"
                  }"""

        response.success = { resp, json ->
            println(json)
            scriptId = json._id;
        }

        response.failure = { resp -> println "(DEBUG)Unable to create the script: ${resp.entity.content.text}" }
    }

    println()
}
//// Creates the policy
http = new HTTPBuilder("${openamurl}/json/policies?_action=create")
http.request(POST, JSON) { req ->
    headers.'iPlanetDirectoryPro' = SSOToken
    headers.'Content-Type' = 'application/json'
    requestContentType = ContentType.JSON
    body = """{
                "name": "${policyName}",
                "active": true,
                "description": "A policy for ${description}",
                "applicationName": "${applicationName}",
                "resourceTypeUuid": "76656a38-5f8e-401b-83aa-4ccb74ce88d2",
                "resources": [
                    "${resourceToProtect}"
                ],
                "actionValues": {
                    "GET": true,
                    "POST": false
                },
                "subject": {
                    "type": "AuthenticatedUsers"
                },
                "condition": {
                    "type": "Script",
                    "scriptId": "${scriptId}"
                }
            }"""

    response.success = { resp, json ->
        println(json)
    }

    response.failure = { resp -> println "(DEBUG)Create policy: ${resp.entity.content.text}" }
}

