OPENIG-1412 Use OIDC with private_key_jwt
======

**STEP-1**: Install and configure [connect2id server](https://connect2id.com). <br>
            Don't forget to modify your connect2id-server-6.3/tomcat/webapps/c2id/WEB-INF to:<br>
                 Allow dynamic client registration: **op.reg.allowOpenRegistration = true**<br>
                 Allow non TLS redirections: **op.reg.rejectNonTLSRedirectionURIs = false**<br>
<br>
**STEP-2**: Open and EDIT the groovy script configuration to configure your OpenIG with your Connect2Id. Then launch it with:<br>
        '`$ groovy connect2id.groovy`'
        <br> It also creates a 'openig.properties' in your `user.home` directory used by the json configuration files.
  
**STEP-2**: Backup your home folder '.openig'. <br>
            Copy the provided folder '.openig' to replace it.

**STEP-3**: Launch OpenIG and check on the url '<openig-url>/openid' <br>
            example: `http://openig.example.com:8082/openid`
  
![ForgeShop logged in using private_key_jwt](https://raw.githubusercontent.com/openig-contrib/script-util-for-openig/master/media/logged_in_private_key_jwt.png)
 


----------
* [OPENIG-1412](https://bugster.forgerock.org/jira/browse/OPENIG-1412)

----------

*Configuration : OPENAM-13* | OPENIG-5*


Disclaimer
=============

This project is not supported by ForgeRock AS.
