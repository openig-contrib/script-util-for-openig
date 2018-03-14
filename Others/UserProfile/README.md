User Profile: Retrieve user information from AM
======
----------

**This example requires a running AM (Access Management) > 5.0 And IG > 5.0 **

----------
**STEP-1**: Backup your home folder '.openig'<br>
            Copy the provided folder '.openig' to replace it.                        

**STEP-2**: Launch OpenIG and check on the url `<openig-url>/userProfile` (example: http://openig.example.com:8082/userProfile)<br>
            After Login to AM (demo + changeit), the groovy script will display on screen the user information:
            <br>
 ![User profile display](https://raw.githubusercontent.com/openig-contrib/script-util-for-openig/master/media/userProfile.png)
       
----------        
* [OpenIG-2253](https://bugster.forgerock.org/jira/browse/OPENIG-2253)
* [OpenIG-2481](https://bugster.forgerock.org/jira/browse/OPENIG-2481)
----------

**Tested with:** AM-6.0.0 | IG 6.0.0
