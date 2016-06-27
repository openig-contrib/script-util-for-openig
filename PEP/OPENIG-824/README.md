OPENIG-824 Use the "Policy Enforcement Filter" and retrieve back given "attributes" with the policy decision
======
----------

**This example shows how to configure OPENIG and the policy enforcement filter to retrieve the attributes returned with the policy decision **

----------

**STEP-1**: Open and EDIT the groovy script configuration to configure your OpenAM. Then launch it with: <br>
        '`$ groovy openig-824-openam-configuration.groovy'`
        

 - In this sample, the OPENAM policy about the URL: 
    `http://localhost:8082/pep-policy-attributes`, allows access to all registered users and returns within its response the given
    and pre-configured policy settings: the **subject's "cn" and "dn" profile attributes**.
    You can retrieve these attribute when using **OPENIG**: in `${attributes.policy.attributes.dn}` or `${attributes.myPolicyDecision.attributes.cn}` 
  
**STEP-2**: Backup your home folder '.openig'
        Copy the provided folder '.openig' to replace it.
        
 - Files in `<openig>/config/*`                                  NEEDS to be modified according to your configuration.
 - Files in `<openig>/scripts/*`                                 NEEDS to be modified according to your configuration.                

**STEP-3**: Launch OpenIG and check on the url '<openig-url>/pep-policy-attributes' (example: http://localhost:8082/pep-policy-attributes)
        The "Policy Enforcement Filter" gives you access to the resource where the given attributes are returned.
        You can change the 'condition' of the route to check that the policy denies access to other resources.
        
----------        
* [Documentation](https://forgerock.org/openig/doc/bootstrap/gateway-guide/index.html#chap-pep)
* [Reference Guide](http://openig.forgerock.org/doc/bootstrap/reference/index.html#PolicyEnforcementFilter)
* [OpenAM Developer's Guide](http://openam.forgerock.org/doc/bootstrap/dev-guide/index.html#rest-api-authz-policy-decisions)
* [OpenIG-824](https://bugster.forgerock.org/jira/browse/OPENIG-824)

----------

*Configuration : OPENAM-13 | OPENIG-5*
