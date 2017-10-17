OPENIG-824 Use the "Policy Enforcement Filter" and retrieve back given "attributes" with the policy decision
======
----------

**This example shows how to configure OPENIG and the policy enforcement filter to retrieve the attributes returned with the policy decision**

----------

**STEP-1**: - Open and **EDIT the groovy script configuration** to configure your OpenAM. <br>
              (The properties to modify are in the top of the file.)<br>
               Then launch it with:`$ groovy openig-824-openam-configuration.groovy`
<br>        
This script configures the policy set and policy to protect the resource: `/pep-policy-attributes`
<br> It also creates a 'openig.properties' in your `user.home` directory used by OpenIG json configuration files.
<br>
 - In this sample, the OPENAM policy about the URL: 
    `http://openig.example.com:8082/pep-policy-attributes`, allows access to all registered users and returns within its response the given
    and pre-configured policy settings: the **subject's "cn" and "dn" profile attributes**.

    You can retrieve these attribute when using **OPENIG**: in `${contexts.policyDecision.attributes.dn}` or `${contexts.policyDecision.attributes.cn}`
  
**STEP-2**: Backup your home folder '.openig'<br>
            Copy the provided folder '.openig' to replace it.                        

**STEP-3**: Launch OpenIG and check on the url `<openig-url>/pep-policy-attributes` (example: http://openig.example.com:8082/pep-policy-attributes)<br>
            The **Policy Enforcement Filter** gives you access to the resource where the given attributes are returned.<br>
            <br>
            You can change the condition of the route to verify that the policy denies access to other resources.
        
----------        
* [Documentation](https://forgerock.org/openig/doc/bootstrap/gateway-guide/index.html#chap-pep)
* [Reference Guide](http://openig.forgerock.org/doc/bootstrap/reference/index.html#PolicyEnforcementFilter)
* [OpenAM Developer's Guide](http://openam.forgerock.org/doc/bootstrap/dev-guide/index.html#rest-api-authz-policy-decisions)
* [OpenIG-824](https://bugster.forgerock.org/jira/browse/OPENIG-824)
* [OpenIG-1202](https://bugster.forgerock.org/jira/browse/OPENIG-1202)
* [OpenIG-1433](https://bugster.forgerock.org/jira/browse/OPENIG-1433)

----------

**Configuration : AM-5.5.0 | IG 5.5.0
