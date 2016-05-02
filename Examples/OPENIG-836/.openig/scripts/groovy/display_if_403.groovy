import static org.forgerock.http.protocol.Status.FORBIDDEN

import org.forgerock.http.io.ByteArrayBranchingStream


// This script displays in the console the advices if they are present.
next.handle(context, request)
    .thenOnResult {
                  if (it.status == FORBIDDEN) {
                       println("(DEBUG) Script >>>> " + attributes.myPolicyDecision)
                       it.status = Status.OK
                       it.entity = new ByteArrayBranchingStream((
                           """<html>
                              <head><title id='head'>Policy FORBIDDEN access the protected resource!</title>
                              <link rel='shortcuticon'href='http: //forgerock.com/favicon.ico'>
                              <link rel='icon' type='image/png' href='https://forgerock.org/ico/favicon-196x196.png' />
                              </head>
                              <p> FORBIDDEN access to the resource for <b>${attributes.user}</b>
                              <p> Extra attributes in context: <b>${attributes.myPolicyDecision}</b></p>
                           """).getBytes())
                        return it
                    } else {
                      return it
                    }
                  }
