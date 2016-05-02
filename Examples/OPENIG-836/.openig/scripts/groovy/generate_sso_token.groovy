import groovy.json.JsonSlurper

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
// We use a curl command to retrieve the SSO token:
// -----------------------------------------------------------------------------------------------------
if(!session.containsKey("ssoTokenSubject")) {
    println "Generating a new ssoToken for the subject>>>>"
    def curl = ["curl", "-k", "-X", "POST", \
                              "-H", "X-OpenAM-Username: ${attributes.user}", \
                              "-H", "X-OpenAM-Password: ${attributes.userpass}", \
                              "-H", "Content-Type: application/json", \
                              "-d", "{}", "${attributes.openamurl}/json/authenticate"].execute()

    def jsonResponseContainingSSOToken = new JsonSlurper().parseText(curl.text);
    session.put("ssoTokenSubject", jsonResponseContainingSSOToken.tokenId);
}
println "SCRIPT(debug)>> tokenID >> " + session.get("ssoTokenSubject");

return next.handle(context, request)
