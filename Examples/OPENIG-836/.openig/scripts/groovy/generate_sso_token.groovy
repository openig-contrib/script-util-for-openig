@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7.1' )

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.POST

import org.forgerock.http.protocol.Response;
import org.forgerock.http.protocol.Status

import groovyx.net.http.*

def session = contexts.session.session
// -----------------------------------------------------------------------------------------------------
// >>>> UPDATE these fields according to your configuration <<<< 
// -----------------------------------------------------------------------------------------------------
attributes.openamurl = "http://localhost:8090/openam" // URL must NOT end with a slash

// Sets the user who WANT TO ACCESS to the resource
attributes.user = "amadmin"
attributes.userpass = "secret12"

// Set the user ALLOWED TO ACCESS to policy decisions. (used in the filter)
attributes.authorizedPolicyUser = "amadmin"
attributes.authorizedPolicyUserPassword = "secret12"

// -----------------------------------------------------------------------------------------------------
// Request to get an SSOToken
// -----------------------------------------------------------------------------------------------------
if(!session.containsKey("ssoTokenSubject")) {
    def http = new HTTPBuilder("${attributes.openamurl}/json/authenticate")
    http.request(POST,JSON) { req ->
        headers.'X-OpenAM-Username' = attributes.user
        headers.'X-OpenAM-Password' = attributes.userpass
        headers.'Content-Type' = 'application/json'
        requestContentType = ContentType.JSON
        body = ''

        response.success = { resp, json ->
            println(json)
            session.put("ssoTokenSubject", json.tokenId);
        }

        response.failure = { resp -> println "(DEBUG)Unable to create token: ${resp.entity.content.text}" }
    }
}

return next.handle(context, request)
