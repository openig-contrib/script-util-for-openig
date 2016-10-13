// -----------------------------------------------------------------------------------------------------
// Simple protected resource - It could be anything else.
// -----------------------------------------------------------------------------------------------------
response = new Response(Status.OK)
response.entity = """
<!DOCTYPE html>
<html>
    <head><title id='head'>Policy allows you to access the protected resource!</title>
        <link rel='icon' type='image/png' href='https://forgerock.org/ico/favicon-196x196.png' />
    </head>
    <body>
        <p> Access granted for <b>${attributes.user}</b>
        <p> Extra attributes in context: <b>${attributes.myPolicyDecision}</b></p>
    </body>
</html>""" as String
// -----------------------------------------------------------------------------------------------------
return response

