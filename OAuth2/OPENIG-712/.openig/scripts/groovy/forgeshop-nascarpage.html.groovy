// -----------------------------------------------------------------------------------------------------
// Displays the NASCAR page
// -----------------------------------------------------------------------------------------------------
response = new Response(Status.OK)
response.entity = """
<!DOCTYPE html>
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
        <link rel='icon' type='image/png' href='https://forgerock.org/ico/favicon-196x196.png' />
        <title>Welcome to ForgeShop</title>
    </head>
    <body>
        <div class="topbottom"></div>
        <div>
            <img alt='ForgeShop' class="logo" src='https://raw.githubusercontent.com/openig-contrib/script-util-for-openig/master/media/logoForgeShop.png'/>
            <section class="middle">
                <br/>
                <p>Log to <b>ForgeShop</b> using your favorite your provider: </p>
                <ul id='double'>
                    <li>
                        <a href='/openid/login?registration=openamPortal&goto=${contexts.router.originalUri}'>
                            <img alt='log in with OpenAM' src='https://raw.githubusercontent.com/openig-contrib/script-util-for-openig/master/media/FR_AM_resized.png'/>
                        </a>
                    </li>
                    <li>
                        <a href='/openid/login?registration=googlePortal&goto=${contexts.router.originalUri}'>
                            <img alt='log in with Google' src='https://raw.githubusercontent.com/openig-contrib/script-util-for-openig/master/media/google_resized.png'/>
                        </a>
                    </li>
                    <li>
                        <a href='/openid/login?registration=msnPortal&goto=${contexts.router.originalUri}'>
                            <img alt='log in with MSN' src='https://raw.githubusercontent.com/openig-contrib/script-util-for-openig/master/media/msn_resized.jpg'/>
                        </a>
                    </li>
                    <li>
                        <a href='/openid/login?registration=linkedinPortal&goto=${contexts.router.originalUri}'>
                            <img alt='log in with LinkedIN' src='https://raw.githubusercontent.com/openig-contrib/script-util-for-openig/master/media/linkedin_resized.png'/>
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
</html>""" as String
return response
