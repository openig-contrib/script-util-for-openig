TokenTransformationFilter — transform a OAuth2/OpenID Connect ID token issued by OpenAM to SAML assertions
======
----------

**This example will show you how to use the Token Transformation Filter: OpenID Connect ID token to SAML assertions.**

----------

**STEP-1**: - Open and **EDIT the groovy script configuration** to configure your OpenAM. <br>
              (The properties to modify are in the top of the file.)<br>
               Then launch it with: '`$ groovy STS.groovy`'
            
            
This script configures OpenAM as described in the documentation:<br>
        - creates OAuth2 policy, agent, user `george costanza`, Bearer authentication module and configures the REST STS.
        - creates 'openig.properties' in your `user.home` directory.
  
**STEP-2**: **Backup your home folder '.openig'.** <br>
            Copy the provided folder '.openig' to replace it.<br>
            OpenIG configuration files read the `openig.properties` file. By default, you do not need to modify those files.

**STEP-3**: **Launch OpenIG and check on the url '<openig-url>/id_token'**
            id: george
            password: costanza
(example: `http://localhost:8082/id_token`)


**In a nutshell: SAML2.0 and OAuth2**

- SAML 2.0 is an XML based framework that is used for describing and exchanging security information. 
It can be used for Single Sign On (SSO), Identity Management and Federation.
- OAuth2 is a standard for authorization.

In one hand, we have enterprises with existing/ legacy SAML 2.0 based Single Sign-On (SSO) and on the other, 
we have a world ruled by mobile and social applications(OAuth2/OpenID)<br>
**OpenIG** bridges OAuth 2.0 and SAML frameworks with the Token Transformation Filter and transforms OAuth2/OpenID 
Connect ID token to SAML assertions.


----------
* [Gateway Guide](http://openig.forgerock.org/doc/bootstrap/gateway-guide/index.html#chap-ttf)
* [Reference](https://forgerock.org/openig/doc/bootstrap/reference/index.html#TokenTransformationFilter)

----------

*Configuration : OPENAM-13 | OPENIG-5*
