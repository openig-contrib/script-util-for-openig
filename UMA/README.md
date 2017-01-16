UMA — Supporting UMA Resource Servers
======
----------

**Configure automatically your OpenAM for IG documentation test case.**

----------
**STEP-1**: Launch your OpenAM.

**STEP-2**: Open `UMA.groovy` and check that the parameters in the first section, CONFIGURATION, are correct for
your installation. If they are not, change them and save the script.
- Launch the script with the command `$ groovy UMA.groovy`.

The script does the following:
- create the users Alice and Bob.
- create the OAuth2/UMA policy if needed.
- configure your OAuth 2.0/UMA client agents: OpenIG and UmaClient.

**STEP-3**: Test the configuration by following the documentation.

----------
* [Gateway Guide](https://backstage.forgerock.com/docs/openig/5.0/gateway-guide#chap-uma)
----------

*Configuration : OPENAM-13.5 | OPENIG-5*
