OPENIG-712 Improve 'supportedDomains' experience in OAuth2-OIDC
======
----------

**These examples show how to configure OpenIG to use OpenID Connect discovery and dynamic client registration with 'supportedDomains'.**

----------

**STEP-1**: Open and EDIT the groovy script configuration to configure your OpenAM. Then launch it with:
        '`$ groovy openig-712-openam-configuration.groovy`'
        This script configures the OAuth2 OpenID connect, OAuth2 policy ,an openID's agent 'ForgeShop' and
        a user "gabby/secret12"
  
**STEP-2**: Backup your home folder '.openig'. <br>
            Copy the provided folder '.openig' to replace it.
        
 - File `<openig>/config/config.json`                          NEEDS to be modified according to your configuration.
 - File `<openig>/config/routes/issuers-router.json`           NEEDS to be modified according to your configuration.
 - File `<openig>/config/routes/issuers-router/*`              NEEDS to be modified according to your configuration.
 - File `<openig>/scripts/*`                                   NEEDS to be modified according to your configuration.

**STEP-3**: Launch OpenIG and check on the url '<openig-url>/openid'
(example: `http://localhost:8082/openid`)

**Four route examples available** in the issuers/routers folder which should cover the different OIDC configurations:
- dynamic registration only
- supportedDomains with NASCAR page
- use a single client registration
- use a single client registration(version 4.0)
<br>
-> Try them separately by changing the extension into .json. 
<br>
**Only one .json at the time!**
  
This examples use supportedDomains* `"supportedDomains": ["irock.*", "iforge.*"]` for OpenAM.
That means that if you use the input box and type: `jackson@irock.com` or `bjensen@iforge.fr` you would be redirected to your OpenAM provider.(see issuers-router.json)

_note:_ 

*Nascar pages use multiple providers like Google, LinkedIn and Msn but you have to configure the file `<openig>/config/routes/issuers-router.json` with your clients id to be able to use them.*

----------
* [Gateway Guide](http://openig.forgerock.org/doc/bootstrap/gateway-guide/index.html#chap-oauth2-client)
* [Reference](http://openig.forgerock.org/doc/bootstrap/reference/index.html#Issuer)*
* [OPENIG-712](https://bugster.forgerock.org/jira/browse/OPENIG-712)

----------

*Configuration : OPENAM-13 | OPENIG-5*
