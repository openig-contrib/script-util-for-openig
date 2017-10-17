OPENIG-712 Improve 'supportedDomains' experience in OAuth2-OIDC
======
----------

**These routes show how to configure OpenIG to use OpenID Connect discovery and dynamic client registration with 'supportedDomains'.**

----------

**STEP-1**: Open and EDIT the groovy script configuration to configure your OpenAM. Then launch it with:
        '`$ groovy openig-712-openam-configuration.groovy`'
        This script configures the OAuth2 OpenID connect, OAuth2 policy ,an openID's agent 'ForgeShop' and
        a user "gabby/secret12"
        <br> It also creates a 'openig.properties' in your `user.home` directory used by the json configuration files.
  
**STEP-2**: Backup your home folder '.openig'. <br>
            Copy the provided folder '.openig' to replace it.

**STEP-3**: Launch OpenIG and check on the url '<openig-url>/openid' <br>
            example: `http://openig.example.com:8082/openid`

**Four route examples available** in the issuers/routers folder which should cover the different OIDC configurations:<br>
- dynamic registration only
- supportedDomains with NASCAR page:
- use a single client registration
- use a single client registration(version 4.0)
<br>
-> Try them separately by changing the extension into .json. 
<br>
**Only one .json at the time!**
  
This examples use supportedDomains* `"supportedDomains": ["irock.*", "iforge.*"]` for OpenAM.
That means that if you use the input box and type: `jackson@irock.com` or `bjensen@iforge.fr` you would be redirected to your OpenAM provider.(see issuers-router.json)


![ForgeShop Nascar Page](https://raw.githubusercontent.com/openig-contrib/script-util-for-openig/master/media/forgeshop_nascarpage.png)
_note:_ 

*Nascar pages use multiple providers like Google, LinkedIn and Msn but you have to configure the file `<openig>/config/routes/issuers-router.json` with your clients id to be able to use them.*

----------
* [Gateway Guide](http://openig.forgerock.org/doc/bootstrap/gateway-guide/index.html#chap-oauth2-client)
* [Reference](http://openig.forgerock.org/doc/bootstrap/reference/index.html#Issuer)*
* [OPENIG-712](https://bugster.forgerock.org/jira/browse/OPENIG-712)

----------

**Configuration:** AM-5.5.0 | IG-5.5.0
