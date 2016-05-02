import org.forgerock.http.io.ByteArrayBranchingStream

// -----------------------------------------------------------------------------------------------------
// Simple protected resource
// -----------------------------------------------------------------------------------------------------
response = new Response()
response.status = Status.OK
response.entity = new ByteArrayBranchingStream(("""<html>
                                                <head><title id='head'>Policy allows you to access the protected resource!</title>
                                                <link rel='shortcuticon'href='http: //forgerock.com/favicon.ico'>
                                                <link rel='icon' type='image/png' href='https://forgerock.org/ico/favicon-196x196.png' />
                                                </head>
                                                <p> Access granted for <b>${attributes.user}</b>
                                                <p> Extra attributes in context: <b>${attributes.myPolicyDecision}</b></p>""").getBytes())
// last line MUST be exchange or return
// -----------------------------------------------------------------------------------------------------
return response

