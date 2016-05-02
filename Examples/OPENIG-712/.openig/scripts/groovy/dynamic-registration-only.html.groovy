import org.forgerock.http.io.ByteArrayBranchingStream

// -----------------------------------------------------------------------------------------------------
// Displays the html page
// -----------------------------------------------------------------------------------------------------
response = new Response()
response.status = Status.OK
response.entity = new ByteArrayBranchingStream(("""
<html>
    <head>
        <style>
        body {
            background-color: white;
        }
        .topbottom {
            width:100%;
            height: 4em;
            margin-top:6px;
            background-color: rgba(31,34,35,.85);
        }
        .middle {
            margin-left: 25%;
            margin-bottom: 50px;
        }
        .logo {
            float:left;
        }
        </style>
    </head>
    <body>
        <div class="topbottom"></div>
        <div>
            <img class="logo" src='https://raw.githubusercontent.com/openig-contrib/script-util-for-openig/master/Examples/media/logoForgeShop.png'/>
            <section class="middle">
                <br/>
                <p>Login to ForgeShop using your email address :
                <form action='/openid/login?'>
                    <input type='text' name='discovery' value='http://server.register.com:8090/jackson' size='35'>
                    <br/>
                    <input type='hidden' name='goto' value='${contexts.router.originalUri}'>
                </form>
            </section>
        </div>
        <div class="topbottom"></div>
    </body>
</html>""").getBytes())

return response
