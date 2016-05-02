import org.forgerock.http.io.ByteArrayBranchingStream
// -----------------------------------------------------------------------------------------------------
// This is a simple page which autorized user could see displaying the attributes dn and cn
// -----------------------------------------------------------------------------------------------------
response = new Response()
response.status = Status.OK
response.entity = new ByteArrayBranchingStream(("""<html>
                                                <head><title id='OIDC-DCR'>Policy allows you!</title>
                                                <link rel='shortcuticon'href='http: //forgerock.com/favicon.ico'>
                                                <link rel='icon' type='image/png' href='https://forgerock.org/ico/favicon-196x196.png' />
                                                </head>
                                                <style>
                                                    ul {
                                                        text-align: left;
                                                        padding-left: 30%;
                                                    }
                                                </style>
                                                <center>
                                                <h1>Access granted <b>'${attributes.user}'</b></h1>
                                                <p>If you are able to see this page, that means that the policy filter authorized you.</p>
                                                <p>Attributes and advices should be accessible with \${attributes.myPolicyDecision.attributes} | \${attributes.myPolicyDecision.advices}</p>
                                                <p style="text-decoration: underline;">In this sample, <b>'dn'</b> and <b>'cn'</b> attributes are accessible:</p>
                                                    <ul>
                                                        <li>\${attributes.myPolicyDecision.attributes.dn} = ${attributes.myPolicyDecision.attributes.dn}</li>
                                                        <li>\${attributes.myPolicyDecision.attributes.cn} = ${attributes.myPolicyDecision.attributes.cn}</li>
                                                    </ul>
                                                </center>""").getBytes())
// last line MUST be exchange or return
// -----------------------------------------------------------------------------------------------------
return response

