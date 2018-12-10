OPENIG-933 Handle oauth2/openid token expiration in OpenIG
======
----------

**Use a Resource server to validate the access token returned by the OAuth2ClientFilter (and refresh it if expired)**

----------

**STEP-1**: - Open and **EDIT the groovy script configuration** to configure your OpenAM. <br>
              (The properties to modify are in the top of the file.)<br>
               Then launch it with: `$ groovy openig-933-openam-configuration.groovy`'

This script configures the OAuth2 OpenID connect, OAuth2 policy, an openID's agent 'ForgeShop' and
a user `gabby/secret12`.
<br> It also creates a 'openig.properties' in your `user.home` directory used by the json configuration files.
  
**STEP-2**: Backup your home folder '.openig'. <br>
            Copy the provided folder '.openig' to replace it.

**STEP-3**: Launch OpenIG and check on the url '<openig-url>/openid' 
(example: `http://openig.example.com:8082/openid`)


**Behind the scenes:**

The `OAuth2ClientFilter` generates an access token then go through the `RelayFilter` which places the access token in a
`WWW-Authenticate header`. 
The `ResourceServer` filter checks if the access token is valid.
When calling for the first time your protected application, you should access it.
Then, wait more than 3 seconds for the access token to expire(according to the agent configuration) then hit F5.
At this point, the `OAuth2ClientFilter` receives a 401, forcing him to refresh the token and it tries to access again to 
the protected resource.

**In a nutshell: Access and refresh tokens**

Access tokens have limited lifetimes and affect how long you can access to the OAuth2 protected resource.
If your application needs access to a protected resource after the access token expires,
then you can refresh the token to obtain a new access token. Usually, best practice is that the refresh token lifetime 
should be much larger than your access token lifetime(but should be adapted according to your requirements).

----------

* [Gateway Guide](http://openig.forgerock.org/doc/bootstrap/gateway-guide/index.html#chap-oauth2-client)
* [Reference](https://forgerock.org/openig/doc/bootstrap/reference/index.html#OAuth2ClientFilter)
* [Reference](https://forgerock.org/openig/doc/bootstrap/reference/index.html#OAuth2ResourceServerFilter)
* [OPENIG-933](https://bugster.forgerock.org/jira/browse/OPENIG-933)

----------

**Configuration:** AM-6.5.0 | IG-6.5.0
