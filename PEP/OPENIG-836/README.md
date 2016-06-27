OPENIG-836 Configure "Policy Enforcement Filter" to support 'environment' get back the 'advices' attributes
======
----------

**In a really simple way, this example shows how to set environment attributes and retrieve advices from the policy decision.**

----------

**STEP-1**: Open and EDIT the groovy script configuration to configure your OpenAM. Then launch it with:
        '`$ groovy openig-836-openam-configuration.groovy`'
        
Verify that your OpenAM-13 gets the modifications. (Check application/policy)
        

 - In this sample, the policy that applies to the URI: '`http://localhost:8082/pep-advices`'
  
**STEP-2**: - **Backup your home folder '.openig'**
            - Copy the provided folder '.openig' to replace it.
        
 - Files in `<openig>/config/*`                                  NEEDS to be modified according to your configuration.
 - Files in `<openig>/scripts/*`                                 NEEDS to be modified according to your configuration.                

**STEP-3**: Launch OpenIG and check on the url '<openig-url>/pep-advices' 
(example: `http://localhost:8082/pep-advices`)
You should **NOT access to the resource** but you can <u>see the returned <b>advices</b>.<u>

If you change the route `66-policy_enforcement_advices.json`, by choosing a week day in the environment field (instead of saturday), 
that will give you access to the resource (note that <u>advices will not appear</u> in this case).   
                
----------       
* [Documentation](https://forgerock.org/openig/doc/bootstrap/gateway-guide/index.html#chap-pep)
* [Reference Guide](http://openig.forgerock.org/doc/bootstrap/reference/index.html#PolicyEnforcementFilter)
* [OpenAM Developer's Guide](http://openam.forgerock.org/doc/bootstrap/dev-guide/index.html#rest-api-authz-policy-decisions)
* [OpenIG-836](https://bugster.forgerock.org/jira/browse/OPENIG-836)

----------

*Configuration : OPENAM-13 | OPENIG-5*
