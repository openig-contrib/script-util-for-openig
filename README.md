Forgerock Identity Gateway amazing route samples!
======

This repository contains short and simple routes for [**Forgerock Identity Gateway(IG)**](https://backstage.forgerock.com/downloads/OpenIG#browse)

An install of [**Forgerock Access Management(AM)**](https://backstage.forgerock.com/downloads/OpenAM/Access%20Management/5.1.1/AM%20Zip/zip#list) is also required to perform authentication/authorization examples. 

What does IG do? What can I do with IG?
----------------------------- 

* [AWS: Deploy OpenIG with Amazon Web Services](https://github.com/openig-contrib/script-util-for-openig/tree/master/AWS-config-examples)_(scripts and utilities)_
<br><br>
* [OAuth2: NASCAR page - Authenticate through AM, Google, MSN or LinkedIn](https://github.com/openig-contrib/script-util-for-openig/tree/master/OAuth2/OPENIG-712)
* [OAuth2: Discovery](https://github.com/openig-contrib/script-util-for-openig/tree/master/OAuth2/OPENIG-712)
* [OAuth2/OpenID: Authenticate through OpenAM, check the token and get access to your protected application!](https://github.com/openig-contrib/script-util-for-openig/tree/master/OAuth2/OPENIG-933)
* [OAuth2/OpenID: Authenticate using private_key_jwt](https://github.com/openig-contrib/script-util-for-openig/tree/master/OAuth2/private_key_jwt)
<br><br>
* [Policy Enforcement Filter(PEF): how to retrieve attributes in a policy decision](https://github.com/openig-contrib/script-util-for-openig/tree/master/PEP/OPENIG-824)
* [Policy Enforcement Filter(PEF): how to set environment and retrieve advices in a policy decision](https://github.com/openig-contrib/script-util-for-openig/tree/master/PEP/OPENIG-836)
* [Policy Enforcement Filter(PEF): configure OpenAM as a PDP](https://github.com/openig-contrib/script-util-for-openig/tree/master/PEP/Setting_Up_OpenAM_As_A_PDP.groovy) _(groovy script only)_
<br><br>
* [UMA 1.0](https://github.com/openig-contrib/script-util-for-openig/tree/master/UMA) _(groovy script only)_
* [UMA 2.0](https://github.com/openig-contrib/script-util-for-openig/tree/master/UMA) _(groovy script only)_
<br><br>
* [JwtBuilderFilter(6.1): Passdown AM user information from IG into a header](https://github.com/openig-contrib/script-util-for-openig/tree/master/Others/JwtBuilderFilter)
* [User Profile: Retrieve user information from AM](https://github.com/openig-contrib/script-util-for-openig/tree/master/Others/UserProfile)
<br><br>
* [STS: transform OAuth2/OpenID to SAML assertions!](https://github.com/openig-contrib/script-util-for-openig/tree/master/STS/TokenTransformationFilter)

> **Note: that IG is now launched in PRODUCTION MODE by default**<br>
Don't forget to change it to DEVELOPMENT to access Studio or for editing routes.
<br>**Tip:** You can run IG with java -jar start.jar -Dig.run.mode=development

How to configure OpenAM??? I just want to try...
-----------------------------

**Don't panic!**

There are Groovy scripts provided for each example to help you to configure your AM quickly. 
<br>

Official links
-----------------------------
* [Get Forgerock Identity Gateway(IG)!](https://www.forgerock.com/platform/identity-gateway/)
* [Get Forgerock Access Management(AM)!](https://www.forgerock.com/platform/access-management/)
* [IG JIRA](https://bugster.forgerock.org/jira/browse/OPENIG)
* [Forgerock](https://www.forgerock.com/platform/identity-gateway/)


Disclaimer
=============

This project is not supported by ForgeRock AS.

----------
