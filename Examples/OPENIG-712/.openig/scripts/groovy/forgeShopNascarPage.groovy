import org.forgerock.http.io.ByteArrayBranchingStream
import org.forgerock.http.protocol.Response
import org.forgerock.http.protocol.Status

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
          margin-left: 25%;
          margin-top: 25px;
        }
        img {
          padding: 10px;
        }
        #double a {
          margin-bottom: 30px;
        }
        i {
          font-size: 9px;
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
        <h1>Welcome to ForgeShop...</h1>
        <section>
            <br/>
            <p>Login to ForgeShop using your favorite your provider: </p>
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
    </body>
</html>""").getBytes())

return response
