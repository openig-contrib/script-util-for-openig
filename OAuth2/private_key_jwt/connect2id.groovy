@Grab(group = 'org.codehaus.groovy.modules.http-builder', module = 'http-builder', version = '0.7.1')
@Grab(group = 'org.mitre', module = 'json-web-key-generator', version = '0.2')
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jose.jwk.KeyUse
import groovyx.net.http.*
import org.mitre.jose.jwk.*

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.POST

// -----------------------------------------------------------------------------------------------------
// This script is use to configure your OpenIG according to your Connect2Id configuration
// # vrom 2017
// -----------------------------------------------------------------------------------------------------
// CONFIGURATION (Update it if necessary)
// -----------------------------------------------------------------------------------------------------

final String openigBase = "http://openig.example.com:8082"  // URL must NOT end with a slash - no localhost URI
final String connect2IdUrl = "http://127.0.0.1:8080"        // URL must NOT end with a slash - no localhost URI
final String redirectionUri = "${openigBase}/openid/callback"
final String pathPropsFile = System.getProperty("user.home");
final String openigRouteLocation = "${pathPropsFile}/.openig/config/routes";
// -----------------------------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------------------------
def http
def clientId
def publicKey
def keyPair
// Generates first a KeyPair:
JWK jwk = RSAKeyMaker.make(512, KeyUse.SIGNATURE, JWSAlgorithm.parse("RS256"), "sig");
publicKey =  jwk.toPublicJWK()
println ""
println "---- key/pair RS256 - 512 generated ----"

keyPair = jwk.toJSONObject().toJSONString()
println "OK...key/pair RS256 - 512 generated successfully"
println ""
keyPair = jwk.toJSONObject().toJSONString()

// Creates the client on Connect2id
println "---- Creating client connect2id ----"
http = new HTTPBuilder("${connect2IdUrl}/c2id/clients")
http.request(POST, JSON) { req ->
    headers.'Content-Type' = 'application/json'
    requestContentType = ContentType.JSON
    body = """{
	  "redirect_uris"                   : [ "${redirectionUri}" ],
	  "token_endpoint_auth_method"      : "private_key_jwt",
	  "token_endpoint_auth_signing_alg" : "RS256",
	  "jwks"                            : {
		  "keys": [
			${publicKey}
		  ]
		}
	}"""

    response.success = { resp, json ->
        println(json)
        clientId = json.client_id;
        println "OK...Created client: >>${clientId}<<"
    }

    response.failure = { resp ->
        println()
        println "(DEBUG)Unable to create client: ${resp.entity}"
    }
}

// Create a properties file according to your configuration.
// This file will be used in your route to access the different values.
println "---- Creating file properties ----"
final Properties props = new Properties()
final File propsFile = new File(pathPropsFile + "/openig.properties")
props.setProperty("openigBase", openigBase)
props.setProperty("connect2IdUrl", connect2IdUrl)
props.setProperty("clientId", clientId)
props.setProperty("redirectionUri", redirectionUri)
props.setProperty("openigRouteLocation", openigRouteLocation)
props.store(propsFile.newWriter(), "Properties file generated for private_key_jwt")
println()
println "OK...Created properties file in >>${pathPropsFile}.<<"
println()
println "Note: The private key must be c/p to the route file<<"


