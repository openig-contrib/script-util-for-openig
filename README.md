Forgerock Identity Gateway route examples!
======

This repository contains short and simple routes ready to deploy for [**Forgerock Identity Gateway (IG)**](https://backstage.forgerock.com/downloads/OpenIG#browse)

An install of [**Forgerock Access Management (AM)**](https://backstage.forgerock.com/downloads/) is required to perform authentication/authorization examples. 

What does IG do? What can I do with IG?
----------------------------- 

* [AWS: Deploy old IG war with Amazon Web Services](https://github.com/openig-contrib/script-util-for-openig/tree/master/AWS-config-examples)_(scripts and utilities)_
* [Docker Secrets and IG Secrets](https://github.com/openig-contrib/script-util-for-openig/tree/master/docker)
<br><br>
* [OAuth2/OpenID: Authenticate using private_key_jwt](https://github.com/openig-contrib/script-util-for-openig/tree/master/OAuth2/private_key_jwt)
<br><br>
* [Create a simple redirect route](https://github.com/openig-contrib/script-util-for-openig/tree/master/Others/Redirect)

> **Note: that IG is now launched in PRODUCTION MODE by default**<br>
Don't forget to change it to DEVELOPMENT to access Studio or for editing routes.
<br>**Tip:** You can run IG with java -jar start.jar -Dig.run.mode=development

How to configure Forgerock Access Management (AM)??? I just want to try...
-----------------------------

**Don't panic!**

There are Groovy scripts provided for each example to help you to configure your AM quickly. 
Each folder contains Groovy scripts which are configuring AM via REST: configure OAuth2Provider, Policies, Identities and so on...
<br>

Official links
-----------------------------
* [Get Forgerock Identity Gateway IG](https://www.forgerock.com/platform/identity-gateway/)
* [Get Forgerock Access Management AM](https://www.forgerock.com/platform/access-management/)
* [IG JIRA](https://bugster.forgerock.org/jira/browse/OPENIG)
* [Forgerock official website](https://www.forgerock.com/platform/identity-gateway/)


Disclaimer
=============

This project is not supported by ForgeRock AS.

----------
