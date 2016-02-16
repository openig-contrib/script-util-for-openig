
----------------------------------------------------------------------------------------------------------------------------------------
OPENIG-824 Complete Policy Enforcement Filter with "attributes" and "advices"

This is a very basic example to see how to retrieve attributes returned by a policy decision.
----------------------------------------------------------------------------------------------------------------------------------------

STEP-1: Open and EDIT the groovy script configuration to configure your OpenAM. Then launch it with:
        '$ groovy openig-824-pep-attributes.groovy'

        In this sample, the policy that applies to 'http://localhost:8082/pep-policy-attributes' causes that the value of the subject's "cn" and "dn" 
        profile attributes to be returned.
  
STEP-2: Backup your home folder '.openig'
        Copy the provided folder '.openig' to replace it.
        Do modifications if needed in scripts and json configuration files.

STEP-3: Launch OpenIG and check on the url '<openig-url>/pep-policy-attributes' (example: http://localhost:8082/pep-policy-attributes)
        The PEP filter should work and gives you access to the resource where the given attributes are returned.
        You can change the 'condition' of the route to check that the policy denied access to other resource.

