import static groovyx.net.http.ContentType.JSON
@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7.1' )
import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.POST

import groovyx.net.http.*

// -----------------------------------------------------------------------------------------------------
// This script is used to configure your OPENAM - Creating an application and a policy
// According to OPENIG-824. It will create application and policy
// # tested with OpenAM 13.0.0 Build 5d4589530d (2016-January-14 21:15)
// # vrom 2016
// -----------------------------------------------------------------------------------------------------
// CONFIGURATION (Update it if necessary)
// -----------------------------------------------------------------------------------------------------

final String openigBase = "http://localhost:8082"       // URL must NOT end with a slash
final String openamUrl = "http://localhost:8090/openam" // URL must NOT end with a slash
final String user = "amadmin"                 // If you modify these fields, modify your route file accordingly
final String userpass = "secret12"
final String resourceToProtect = "${openigBase}/pep-policy-attributes"

// EXAMPLE CONFIGURATION
// -----------------------------------------------------------------------------------------------------

final String applicationName = "pep-attributes-application"
final String policyName = "pep-attributes-policy"
final String description = "An example for OpenIG-824 - policy enforcement filter attributes"
// -----------------------------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------------------------
// Create a properties file according to your configuration.
// This file will be used in your route to access the different values.
final Properties props = new Properties()
final String pathPropsFile = System.getProperty("user.home")
final File propsFile = new File(pathPropsFile + "/openig.properties")
props.setProperty("openigBase", openigBase)
props.setProperty("openamUrl", openamUrl)
props.setProperty("applicationName", applicationName)
props.store(propsFile.newWriter(), "Properties file generated for OPENIG-824")
println()
println "(DEBUG)Created properties file in >>${pathPropsFile}.<<"
println()
// -----------------------------------------------------------------------------------------------------
def SSOToken
def http
// Request to get an SSOToken
http = new HTTPBuilder("${openamUrl}/json/authenticate")
http.request(POST,JSON) { req ->
    headers.'X-OpenAM-Username' = user
    headers.'X-OpenAM-Password' = userpass
    headers.'Content-Type' = 'application/json'
    requestContentType = ContentType.JSON
    body = ''

    response.success = { resp, json ->
        println(json)
        SSOToken = json.tokenId
    }

    response.failure = { resp -> println "(DEBUG)Unable to create token: ${resp.entity.content.text}" }
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
                    "POST": true,
                    "GET": true,
                    "PUT": false,
                    "DELETE": true
                },
                "resources": [
                    "${resourceToProtect}"
                ],
                "subject": {
                    "type": "AuthenticatedUsers"
                },
                "resourceAttributes": [{
                    "type": "User",
                    "propertyName": "dn"
                }, {
                    "type": "User",
                    "propertyName": "cn"
                }],
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

