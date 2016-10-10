OPENIG' configuration examples
======

This repository contains short and simple samples on how to configure **OpenIG**
to use **Policy Enforcement Filter**(PEP/PEF), **OAuth2** **with OpenAM(13+)** or **TokenTransformationFilter**(TTF).

In all these examples, **OPENAM** acts as a policy decision point(for the PEF) or as an an OpenID Provider/AS(for OAuth2) or REST STS instance(for TTF).

How to configure OpenAM?
-----------------------------

**Don't panic!**

Groovy scripts are provided to help you to configure your OpenAM-13+ quickly. This means you only have to install an OPENAM,
and run the given groovy script to have the OPENIG example ready!
<br>

What am I looking for?
----------------------------- 
* [Policy Enforcement Filter(PEF): how to retrieve attributes and advices](https://github.com/openig-contrib/script-util-for-openig/tree/master/PEP/OPENIG-824)
* [Policy Enforcement Filter(PEF): environment and subject claims](https://github.com/openig-contrib/script-util-for-openig/tree/master/PEP/OPENIG-836)
* [Policy Enforcement Filter(PEF): configure OpenAM as a PDP](https://github.com/openig-contrib/script-util-for-openig/tree/master/PEP/Setting_Up_OpenAM_As_A_PDP.groovy) _(groovy script only)_
<br><br>
* [OAuth2: nascar page, dynamic client registration and more!](https://github.com/openig-contrib/script-util-for-openig/tree/master/OAuth2/OPENIG-712) 
* [OAuth2/OpenID: authenticate through OpenAM, check the token and get access to your protected application!](https://github.com/openig-contrib/script-util-for-openig/tree/master/OAuth2/OPENIG-933)
<br><br>
* [STS: transform OAuth2/OpenID to SAML assertions!](https://github.com/openig-contrib/script-util-for-openig/tree/master/STS/TokenTransformationFilter)
<br><br>
* [AWS: Deploy OpenIG](https://github.com/openig-contrib/script-util-for-openig/tree/master/AWS-config-examples)

OpenIG Official links
-----------------------------
* [Get OpenIG!](https://github.com/ForgeRock/openig)  
* [OpenIG documentation](https://forgerock.org/documentation/openig/)
* [OpenIG JIRA](https://bugster.forgerock.org/jira/browse/OPENIG)
* [Forgerock](https://www.forgerock.com/platform/identity-gateway/)


Disclaimer
=============

This project is not supported by ForgeRock AS.

----------

*Configuration : OPENAM-13 | OPENIG-5*
