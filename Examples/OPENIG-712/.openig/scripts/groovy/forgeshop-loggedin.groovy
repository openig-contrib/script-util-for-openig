import org.forgerock.http.protocol.Response
import org.forgerock.http.protocol.Status
import groovy.json.JsonOutput
// -----------------------------------------------------------------------------------------------------
// Could be replaced by anything else.
// -----------------------------------------------------------------------------------------------------
response = new Response()
response.status = Status.OK
response.entity.string = """
<html>
    <head>
        <style>
        body {
          background-color: white;
          margin-left: 25%;
          margin-top: 25px;
        }
        </style>
    </head>
    <body>
        <h1>You are logged in, ${attributes.openid.user_info.name}</h1>
        <section>
            <br/>
            <p>The ForgeShop command will be send to ${attributes.openid.user_info.address!=null?attributes.openid.user_info.address.formatted:"No address!"}</p>
            <p>We can contact you at: ${attributes.openid.user_info.phone_number!=null?attributes.openid.user_info.phone_number:"No phone number"}</p>
            <button type="button">Modify my command</button><button type="button">Order</button>
        </section>
    </body>
</html>"""

return response
