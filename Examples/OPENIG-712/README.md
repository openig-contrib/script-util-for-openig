OPENIG-712 Improve 'supportedDomains' experience in OAuth2-OIDC use
======
----------

**This example shows how to configure OpenIG to use OpenID Connect discovery and dynamic client registration with 'supportedDomains'.**

----------

**STEP-1**: Open and EDIT the groovy script configuration to configure your OpenAM. Then launch it with:
        '`$ groovy openig-712-openam-configuration.groovy`'
        This script configures the OAuth2 OpenID connect, OAuth2 policy and an openID's agent ''ForgeShop'
  
**STEP-2**: Backup your home folder '.openig'. <br>
            Copy the provided folder '.openig' to replace it.
        
 - File `<openig>/config/config.json`                                  NEEDS to be modified according to your configuration.
 - File `<openig>/config/routes/issuers-router.json`                   NEEDS to be modified according to your configuration.
 - File `<openig>/config/routes/issuers-router/712-oauth2-supported-domains-nascar.json`                   NEEDS to be modified according to your configuration.   
 - File `<openig>/scripts/*`                                           NEEDS to be modified according to your configuration.                

**STEP-3**: Launch OpenIG and check on the url '<openig-url>/openid' 
(example: `http://localhost:8082/openid`)

In this sample, a nascar page is configured:
 - use static registration to access your favorite provider with one click.
 - use the input box to use discovery and/or dynamic client registration.
  
This example use supportedDomains* `"supportedDomains": ["irock.*", "iforge.*"]` for OpenAM.
That means that if you use the input box and type: `jackson@irock.com` or `bjensen@iforge.fr` you would be redirected to your OpenAM provider.(see issuers-router.json)

_note:_ 

*This example sets multiples providers like Google, linkedin and msn but you have to configure the file `<openig>/config/routes/issuers-router.json` with your clients id._*
----------
* [Gateway Guide](http://openig.forgerock.org/doc/bootstrap/gateway-guide/index.html#chap-oauth2-client)
* [Reference](http://openig.forgerock.org/doc/bootstrap/reference/index.html#Issuer)*
* [OPENIG-712](https://bugster.forgerock.org/jira/browse/OPENIG-712)

----------

*Configuration : OPENAM-13 | OPENIG-5*