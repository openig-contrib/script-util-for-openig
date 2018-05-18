@Grab(group = 'org.codehaus.groovy.modules.http-builder', module = 'http-builder', version = '0.7.1')
import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.POST

import groovyx.net.http.*

// -----------------------------------------------------------------------------------------------------
// This script is used to create a user in AM-6.0.0
// -----------------------------------------------------------------------------------------------------
// CONFIGURATION (Update it if necessary)
// -----------------------------------------------------------------------------------------------------
final String openamUrl = "http://openam.example.com:8090/openam" // URL must NOT end with a slash
final String user = 'amadmin'
final String userpass = "secret12"
// -----------------------------------------------------------------------------------------------------
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
        println "(DEBUG)Unable to create token: ${resp.entity.content.text}" }
}

// Create a user
http = new HTTPBuilder("${openamUrl}/json/users/?_action=create")
http.request(POST, JSON) { req ->
    headers.'iPlanetDirectoryPro' = SSOToken
    headers.'Content-Type' = 'application/json'
    headers.'Accept-Api-Version' = 'resource=2.1'
    requestContentType = ContentType.JSON
    body = """{
                "username": "gabby",
                "userpassword": "password",
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
