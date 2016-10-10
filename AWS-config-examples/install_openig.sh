#!/bin/bash
# This script is used to deploy OpenIG and configure it
# simply with a route example and healthcheck ready.
# The script must be executed with sudo:
# $sudo install_openig.sh
# vrom 2016
#
OPENIG_FILE=OpenIG-5.0.0-SNAPSHOT.war
TOMCAT_DIRECTORY=/usr/share/tomcat7
OPENIG_DIRECTORY=$TOMCAT_DIRECTORY/.openig

# Requires apache tomcat
yum install -y tomcat7 tomcat7-webapps tomcat7-docs-webapp tomcat7-admin-webapps
if [ $? == 1 ]; then
    echo "Unable to install tomcat - installation aborted."
else
# Get Nightly OpenIG war file
    if [ -f "$OPENIG_FILE" ];
    then
       echo "File $OPENIG_FILE exists."
    else
        wget https://download.forgerock.org/downloads/openig/nightly/$OPENIG_FILE
        if [[ $? -ne 0 ]]; then
           echo "wget failed - unable to get OpenIG file from remote server."
           exit 1;
        fi
    fi
# Rename it and copying it to tomcat directory
    cp -vf OpenIG-5.0.0-SNAPSHOT.war ROOT.war
    service tomcat7 stop
    rm -rf /var/lib/tomcat7/webapps/ROOT/
    cp -v ROOT.war /var/lib/tomcat7/webapps/

# Create OpenIG configuration files
    mkdir -v $OPENIG_DIRECTORY
    mkdir -vp $OPENIG_DIRECTORY/config/routes/
    mkdir -vp $OPENIG_DIRECTORY/scripts/groovy/

# Get the configuration files from OpenIG github repo
# > Hello world example
    if [ ! -f "hello-world.json" ];
    then
        wget https://raw.githubusercontent.com/openig-contrib/script-util-for-openig/master/AWS-config-examples/hello-world.json
    fi
    cp -vf hello-world.json $OPENIG_DIRECTORY/config/routes/

    if [ ! -f "hello-world.groovy" ];
    then
        wget https://raw.githubusercontent.com/openig-contrib/script-util-for-openig/master/AWS-config-examples/hello-world.groovy
    fi
    cp -vf hello-world.groovy $OPENIG_DIRECTORY/scripts/groovy
# > HealthCheck example
    if [ ! -f "healthcheck.json" ];
    then
        wget https://raw.githubusercontent.com/openig-contrib/script-util-for-openig/master/AWS-config-examples/healthcheck.json
    fi
    cp -vf healthcheck.json $OPENIG_DIRECTORY/config/routes/

# Setting tomcat as a service
    chkconfig --level 345 tomcat7 on
    sudo service tomcat7 start
    echo ""
    echo "Done. You can access Openig on port <ec2 machine>:8080/hello-world"
    echo ""
fi
