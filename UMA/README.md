IG provides for building a User-Managed Access (UMA 1.0) resource server.
======
**STEP-1**: Launch your AM.

**STEP-2**: 
- For OpenAM < **14 or AM 5.0**, use `UMA-1.0.groovy` and check that the parameters in the first section, 
CONFIGURATION, are correct for your installation. If they are not, change them and save the script.

- For AM >= **5.5**, open `UMA-2.0.groovy` and change it according ot your configuration.

- Launch the script with the command `$ groovy UMA-1.0.groovy` or `$ groovy UMA-2.0.groovy`.

The script does the following:
- create the users Alice and Bob.
- create the OAuth2/UMA policy if needed.
- configure your OAuth 2.0/UMA client agents: OpenIG and UmaClient.

**STEP-3**: Test the configuration by following the documentation.

----------
* [IG Gateway Guide for UMA](https://backstage.forgerock.com/docs/openig/5.0/gateway-guide#chap-uma)
----------

**Configuration:** OPENAM-13.5/5.5 | OPENIG-5/5.5
