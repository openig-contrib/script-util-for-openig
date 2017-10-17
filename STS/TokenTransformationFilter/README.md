TokenTransformationFilter — transform an OAuth2/OpenID Connect ID token issued by OpenAM into SAML assertions
======
----------

**This example shows you how to use the Token Transformation Filter to convert an OpenID Connect ID token into SAML assertions.**

----------
**STEP-1**: Open `STS.groovy` and check that the parameters in the first section, CONFIGURATION, are correct for                         
your installation. If they are not, change them and save the script.

**STEP-2**: With OpenAM running, launch the script with the command `$ groovy STS.groovy`.
            The script does the following:
- Configures OpenAM as described in the user documentation, creating the following items in OpenAM: OAuth2 policy, an agent, the user `george costanza`, a Bearer authentication module, and an instance of REST STS. 
- Creates the directory `openig.properties` in your `user.home` directory. The provided OpenIG routes read the `openig.properties` file. By default, you do not need to modify these files.

**STEP-3**: - **Backup your home folder '.openig'**<br>
            - Copy the provided folder '.openig' to replace it. 

**STEP-4**: Test the configuration
- Restart OpenIG.
- Access the route, for example `http://openig.example.com:8082/id_token` => The OpenAM login page is displayed.
- Log in as `username: george`, `password: costanza`. The OpenID Connect permissions page is displayed.
- Allow OpenIG to access your personal information. The open_id token and SAML assertions are displayed. 

![OAuth2/OpenID to SAML](https://raw.githubusercontent.com/openig-contrib/script-util-for-openig/master/media/STS_openid_to_saml.png)

**SAML2.0 and OAuth2 In a Nutshell:**

- SAML 2.0 is an XML based framework that is used to describe and exchange security information. It can be used for Single     Sign On (SSO), Identity Management, and Federation.
- OAuth2 is a standard for authorization.

On one hand we have enterprises with existing/ legacy SAML 2.0 based Single Sign-On (SSO), and on the other we have a world ruled by mobile and social applications(OAuth2/OpenID).
OpenIG bridges OAuth 2.0 and SAML frameworks with the Token Transformation Filter, which and transforms OAuth2/OpenID Connect ID tokens into SAML assertions.


----------
* [Gateway Guide](http://openig.forgerock.org/doc/bootstrap/gateway-guide/index.html#chap-ttf)
* [Reference](https://forgerock.org/openig/doc/bootstrap/reference/index.html#TokenTransformationFilter)

----------

**Configuration : AM-5.5.0 | IG 5.5.0
