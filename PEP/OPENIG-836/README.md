OPENIG-836 Configure "Policy Enforcement Filter" to support 'environment' get back the 'advices' attributes
======
----------

**In a really simple way, this example shows how to set environment attributes and retrieve advices from the policy decision.**

----------

**STEP-1**: Open and EDIT the groovy script configuration to configure your OpenAM. Then launch it with:
        '`$ groovy openig-836-openam-configuration.groovy`'
<br> The script creates the policy set, the policy and the following script in AM:
```
if (environment) {
        var dayOfWeek = environment.get("DAY_OF_WEEK");
        if (dayOfWeek != null && !dayOfWeek.isEmpty()) {
            var today = dayOfWeek.iterator().next();
            if (today === "Saturday" || today === "Sunday") {
                advice.put("VALID_DAYS_OF_THE_WEEK", ["Mon, Tue, Wed, Thu, Fri"]);
                authorized = false;
            } else {
                authorized = true;
            }
        } else {
            logger.error("No Day of week specified in the evaluation request environment parameters.");
            authorized = false;
        }
    } else {
      logger.error("No environment parameters specified in the evaluation request.");
      authorized = false;
    }   
```
<br>
Verify that your OpenAM-13 gets the modifications. (Check application/policy)
<br>        

 - In this sample, the policy that applies to the URI: '`http://localhost:8082/pep-advices`'
  
**STEP-2**: - **Backup your home folder '.openig'**<br>
            - Copy the provided folder '.openig' to replace it.                    

**STEP-3**: Launch OpenIG and check on the url '<openig-url>/pep-advices'<br>
example: `http://localhost:8082/pep-advices`
<br><br>
You should **NOT access to the resource** but you can <u>see the returned <b>advices</b></u>.
<br>
**Script and advices**
The given 'javascript' script makes OpenAM policy to react on the environment attributes declared in your PolicyEnforcementFilter.<br>
By default, in this example, you declared the `"DAY_OF_WEEK"` to be `Saturday`in `66-policy_enforcement_advices.json` <br>
If you call the URI  `/pep-advices`:
--You are not allowed to see the protected resource, but you will see the advices.
<br><br>
But if you change the route `66-policy_enforcement_advices.json`, by choosing a week day in the environment field, let's say`Monday`, 
and if you call again the URI  `/pep-advices`, then you will have access to the resource!<br>
(note that <u>advices will not appear</u> in this case).  
                
----------       
* [Documentation](https://forgerock.org/openig/doc/bootstrap/gateway-guide/index.html#chap-pep)
* [Reference Guide](http://openig.forgerock.org/doc/bootstrap/reference/index.html#PolicyEnforcementFilter)
* [OpenAM Developer's Guide](http://openam.forgerock.org/doc/bootstrap/dev-guide/index.html#rest-api-authz-policy-decisions)
* [OpenIG-836](https://bugster.forgerock.org/jira/browse/OPENIG-836)

----------

*Configuration : OPENAM-13 | OPENIG-5*
