JwtBuilderFilter: Passdown AM user information from IG into a header
======
----------

**This example requires a running AM (Access Management) > 5.0 And IG > 5.0**

**This example requires IG sample application**

----------
**STEP-1**: Backup your home folder '.openig'<br>
            Copy the provided folder '.openig' to replace it.                        

**STEP-2**: Launch the given groovy script to configure your AM: it add a new user `gabby`/`password`.

**STEP-3**: Launch OpenIG and go to the url `<openig-url>/home/user` (example: http://openig.example.com:8082/home/user)<br>
            <br>
            After login to AM with `gabby` account, you will be redirected to the sample application where
            you will see the new added **header x-openig-user** containing the JWT: 
            `x-openig-user: eyJ0eXAiOiJKV1QiLCJhbGciOiJub25lIn0.eyJuYW1lIjoiZ2FiYnkiLCJhZGRyZXNzIjoiNDM0OSwgV2lzdGVyaWEgTGFuZSwgU2FuIEZyYW5jaXNjbywgQ0EgOTQxMDUiLCJwaG9uZSI6Iig4MzEpIDc5OS05OTk5IiwiZW1haWwiOiJnYWJieUB3aXN0ZXJpYS5jb20ifQ.`
            <br>
            This JWT contains the data set in the JSON route:
            ```
            
            HEADER:ALGORITHM & TOKEN TYPE
            {
              "typ": "JWT",
              "alg": "none"
            }
            PAYLOAD:DATA
            {
              "name": "gabby",
              "address": "4349, Wisteria Lane, San Francisco, CA 94105",
              "phone": "(831) 799-9999",
              "email": "gabby@wisteria.com"
            }
            ```
 ![JwtBuilderFilter example](https://raw.githubusercontent.com/openig-contrib/script-util-for-openig/master/media/jwtBuilderFilter.png)
 <br>
 **WARNING** The JWT is not signed or encrypted and should not contain sensitive data.
----------        
* [OpenIG-2667](https://bugster.forgerock.org/jira/browse/OPENIG-2667)
* [Get the sample application](https://ea.forgerock.com/nightlies/openig/IG-sample-application-6.0.0-SNAPSHOT.jar)
----------

**Tested with:** AM-6.0.0 | IG 6.0.0
