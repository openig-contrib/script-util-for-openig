import org.forgerock.http.io.ByteArrayBranchingStream

// -----------------------------------------------------------------------------------------------------
// Displays the nascar page
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
        }
        .logo {
            float:left;
            margin-top: 50px;
        }
        #double a {
            margin-bottom: 30px;
        }
        ul {
            width:760px;
            margin-bottom:20px;
            overflow:hidden;
        }
        li {
            float:left;
            display: block;
        }
        #double li  { width:50%; }
        </style>
    </head>
    <body>
        <div class="topbottom"></div>
        <div>
            <img class="logo" src='https://raw.githubusercontent.com/openig-contrib/script-util-for-openig/master/Examples/media/logoForgeShop.png'/>
            <section class="middle">
                <br/>
                <p>Log to <b>ForgeShop</b> using your favorite your provider: </p>
                <ul id='double'>
                    <li>
                        <a href='/openid/login?registration=openamPortal&goto=${contexts.router.originalUri}'>
                            <img src='https://raw.githubusercontent.com/openig-contrib/script-util-for-openig/master/Examples/media/FR_AM_resized.png'/>
                        </a>
                    </li>
                    <li>
                        <a href='/openid/login?registration=googlePortal&goto=${contexts.router.originalUri}'>
                            <img src='https://raw.githubusercontent.com/openig-contrib/script-util-for-openig/master/Examples/media/google_resized.png'/>
                        </a>
                    </li>
                    <li>
                        <a href='/openid/login?registration=msnPortal&goto=${contexts.router.originalUri}'>
                            <img src='https://raw.githubusercontent.com/openig-contrib/script-util-for-openig/master/Examples/media/msn_resized.jpg'/>
                        </a>
                    </li>
                    <li>
                        <a href='/openid/login?registration=linkedinPortal&goto=${contexts.router.originalUri}'>
                            <img src='https://raw.githubusercontent.com/openig-contrib/script-util-for-openig/master/Examples/media/linkedin_resized.png'/>
                        </a>
                    </li>
                </ul>
                <p>Or enter your host or email address :
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
