JwtBuilderFilter: Passdown AM user information from IG into a header
======
----------

**This example requires running IG Sample Application**

----------
**STEP-1**: Backup your home folder '.openig'<br>
            Copy the provided folder '.openig' to replace it.                        

**STEP-2**: Launch the given groovy script to configure your AM: it adds a new user `gabby`-`password`.<br>
            `groovy create_user_gabby.groovy`

**STEP-3**: Launch OpenIG and go to the url `<openig-url>/home/user` (example: http://openig.example.com:8082/home/user)<br>
            <br>
            After login to AM with `gabby` account, you will be redirected to the sample application where
            you will see the new added **header x-openig-user** containing the JWT: 
            `x-openig-user: eyJ0eXAiOiJKV1QiLCJhbGciOiJub25lIn0.eyJuYW1lIjoiZ2FiYnkiLCJhZGRyZXNzIjoiNDM0OSwgV2lzdGVyaWEgTGFuZSwgU2FuIEZyYW5jaXNjbywgQ0EgOTQxMDUiLCJwaG9uZSI6Iig4MzEpIDc5OS05OTk5IiwiZW1haWwiOiJnYWJieUB3aXN0ZXJpYS5jb20ifQ.`
            <br>
            Decrypt this JWT and we retrieve the claims/template set previously in the route:
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
 <br> <br>
 **NOTE** Signature is available for IG 6.1 and signature then encryption is available in IG 6.5. Follow the doc for further
 details.
----------        
* [Official documentation about the JwtBuilderFilter](https://ea.forgerock.com/docs/ig/reference/index.html#JwtBuilderFilter)
* [Get the sample application](https://ea.forgerock.com/nightlies/openig/IG-sample-application-6.0.0-SNAPSHOT.jar)
----------        
* [OpenIG-2667](https://bugster.forgerock.org/jira/browse/OPENIG-2667)
* [OpenIG-2669](https://bugster.forgerock.org/jira/browse/OPENIG-2669)
----------

**Tested with:** AM-6.0.0 | IG 6.1.0
