Deploy OpenIG with AWS EC2
==========================
----------

**AWSome OpenIG!**

----------

This folder contains resources to help you install OpenIG on EC2 AMI for test purposes.

Prerequesites:
- https://aws.amazon.com/ec2/run-command/
- http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/execute-remote-commands.html
- http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/remote-commands-prereq.html


The script `install_openig.sh` has been created to download latest `OpenIG SNAPSHOT` and install it on the `8080` of the machine where the script has been launched.
Ths will be useful to create an image for example.

In details, the script will:
- Download latest OpenIG
- Download tomcat7
- Create OpenIG configuration files(with routes)
- Set tomcat as a service. (OpenIG will be accessible when you start the EC2 instance).

Even if I tested it, I strongly suggest you to have a look before launching it. It may require some adjustments.


------------------------------------------------
* [OPENIG-1160](https://bugster.forgerock.org/jira/browse/OPENIG-1160)
* [Amazon EC2](https://aws.amazon.com/ec2/)

----------

*Configuration : OPENAM-13 | OPENIG-5*
