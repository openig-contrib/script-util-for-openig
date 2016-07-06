import static org.forgerock.http.protocol.Status.FORBIDDEN

// This script manage the 403 - FORBIDDEN response when the policy decision denies the request:
next.handle(context, request)
        .then {if (it.status == FORBIDDEN) {
                   it.entity = """
                <!DOCTYPE html>
                <html>
                    <head>
                        <title>Policy FORBIDDEN access the protected resource!</title>
                        <link rel='icon' type='image/png' href='https://forgerock.org/ico/favicon-196x196.png' />
                    </head>
                    <body>
                        <p><b>${attributes.user}</b>, your access to this resource has been limited.</p>
                        <br/>
                        <p><h3>TIP:</h3> This resource is available only on:
                        ${attributes.myPolicyDecision.advices.VALID_DAYS_OF_THE_WEEK}</p>
                    </body>
                </html>
                """ as String
                }
                    return it
                }
