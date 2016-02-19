OPENIG-836 Add policy enforcement filter support for 'environment', 'subject/claims'
======
----------

**In a really simple way, this example shows how to set environment attributes and retrieve advices from the policy decision.**

----------

**STEP-1**: Open and EDIT the groovy script configuration to configure your OpenAM. Then launch it with:
        '`$ groovy openig-836-openam-configuration.groovy`'
        Verify that your OpenAM-13 gets the modifications. (Check application/policy)
        

 - In this sample, the policy that applies to '`http://localhost:8082/pep-advices`'
  
**STEP-2**: Backup your home folder '.openig'
        Copy the provided folder '.openig' to replace it.
        
 - Files in `<openig>/config/*`                                  NEEDS to be modified according to your configuration.
 - Files in `<openig>/scripts/*`                                 NEEDS to be modified according to your configuration.                

**STEP-3**: Launch OpenIG and check on the url '<openig-url>/pep-advices' 
(example: `http://localhost:8082/pep-advices`)
You should <u>NOT access to the resource</u> but you can display the returned <b>advices</b>.

If you change the route `66-policy_enforcement_advices.json`, by choosing a week day in the environment field (instead of saturday), that will give you access to the resource (note that <u>advices will not appear</u> in this case).   
                
----------
* [Reference Guide](http://openig.forgerock.org/doc/bootstrap/reference/index.html#PolicyEnforcementFilter)
* [OpenAM Developer's Guide](http://openam.forgerock.org/doc/bootstrap/dev-guide/index.html#rest-api-authz-policy-decisions)

----------

*Configuration : OPENAM-13 | OPENIG-5*