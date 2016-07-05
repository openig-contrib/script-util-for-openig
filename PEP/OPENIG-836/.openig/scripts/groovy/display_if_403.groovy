import static org.forgerock.http.protocol.Status.FORBIDDEN

import org.forgerock.http.io.ByteArrayBranchingStream

// This script manage the 403 - FORBIDDEN response when the policy decision denies the request:
next.handle(context, request)
        .then {if (it.status == FORBIDDEN) {
                   it.status = Status.OK
                   it.entity = new ByteArrayBranchingStream((
                    """<html>
					  <head><title id='head'>Policy FORBIDDEN access the protected resource!</title>
					    <link rel='shortcuticon'href='http: //forgerock.com/favicon.ico'>
					    <link rel='icon' type='image/png' href='https://forgerock.org/ico/favicon-196x196.png' />
					  </head>
					  <p><b>${attributes.user}</b>, your access to this ressource has been limited.</p>
					  <br/>
					  <p><h3>TIP:</h3> This ressource is available only on:
					  ${attributes.myPolicyDecision.advices.VALID_DAYS_OF_THE_WEEK}</p>
				   """).getBytes())
}
    return it
}
