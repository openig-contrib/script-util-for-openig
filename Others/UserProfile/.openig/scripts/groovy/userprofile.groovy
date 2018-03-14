// -----------------------------------------------------------------------------------------------------
// Protected resource
// -----------------------------------------------------------------------------------------------------
response = new Response(Status.OK)
response.entity = """
<!DOCTYPE html>
<html>
    <head>
        <style>
        body {
            background-color: white;
            float:left;
            top: 0;
            left: 0;
            width: 100%;
        }
        .topbottom {
            position: relative;
            width:100%;
            height: 4em;
            margin-top:6px;
            float:left;
            background-color: rgba(31,34,35,.85);
            top: 0;
            left: 0;
        }
        .middle {
            padding-top: 5px;
            padding-bottom: 5px;
            margin-left: 25%;
            margin-top: 25px;
        }
        .left {
            clear: left;
            margin: 6px;
        }
        .left img {
            padding-top: 20px;
        }
        .left img, form {
            float: left;
        }
        </style>
        <link rel='icon' type='image/png' href='https://forgerock.org/ico/favicon-196x196.png' />
        <meta charset='utf-8'/>
        <title>Welcome to ForgeShop</title>
    </head>
    <body>
    <script>
    function NIY() {
        alert("Not implemented!");
    }
    </script>
    <div class="topbottom"></div>
        <div class ="left">
            <img alt='ForgeShop' src='https://raw.githubusercontent.com/openig-contrib/script-util-for-openig/master/media/logoForgeShop.png'/>
            <section class="middle">
                <h1>User Profile Information</h1>
				<ul>
					<li><b>'\${contexts.userProfile.commonName}'</b>:<code>${contexts.userProfile.commonName}</code></li>
					<li><b>'\${contexts.userProfile.distinguishedName}'</b>:<code>${contexts.userProfile.distinguishedName}</code></li>
					<li><b>'\${contexts.userProfile.realm}'</b>:<code>${contexts.userProfile.realm}</code></li>
					<li><b>'\${contexts.userProfile.username}'</b>:<code>${contexts.userProfile.username}</code></li>
					<li><b>'\${contexts.userProfile.asJsonValue()}'</b>:<code>${contexts.userProfile.asJsonValue()}</code></li>
					<li><b>'\${contexts.userProfile.rawInfo}'</b>:<code>${contexts.userProfile.rawInfo}</code></li>
					<li><b>'\${contexts.userProfile.rawInfo.username}'</b>:<code>${contexts.userProfile.rawInfo.username}</code></li>
					<li><b>'\${contexts.userProfile.rawInfo.employeeNumber}'</b>:<code>${contexts.userProfile.rawInfo.employeeNumber == null?"null":contexts.userProfile.rawInfo.employeeNumber[0]}</code></li>
				</ul>
            </section>
        </div>
        <div class="topbottom"></div>
    </body>
</html>""" as String

return response
