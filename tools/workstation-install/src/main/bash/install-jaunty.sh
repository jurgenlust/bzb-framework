#!/bin/bash

###################################################
# Install script for BZB Framework Workstations
#
# This script prepares an Ubuntu Linux 9.04 workstation for developing
# web applications using the BZB Framework
#
# Jurgen Lust, April 19, 2009
# Jurgen.Lust@gmail.com
#
###################################################



# Some global variables
userName=$LOGNAME
userHome=$HOME
javaHome="/usr/lib/jvm/java-6-sun"
optDir="/opt"
eclipseDownloadUrl="http://eclipse.mirror.kangaroot.net/technology/epp/downloads/release/ganymede/SR2/eclipse-jee-ganymede-SR2-linux-gtk.tar.gz"
eclipseDownloadFile="/tmp/eclipse.tar.gz"
eclipseHome="/opt/eclipse"
javaMajorVersion=6
bzbProfile="$userHome/.bzbProfile"
profile="$userHome/.profile"


#check if current user is not root
if [ $userName = "root" ]
then
    echo "This script should not be run as root!"
    exit 1
fi

#update package repository
sudo apt-get update

#install latest versions of everything
sudo apt-get upgrade -y

#install some required packages
echo "Installing required packages"
sudo apt-get install "sun-java6-jdk" -y
sudo apt-get install "sun-java6-fonts" -y
sudo apt-get install "sun-java6-plugin" -y
sudo apt-get install curl -y
sudo apt-get install ant -y
sudo apt-get install subversion -y
sudo apt-get install nautilus-open-terminal -y
sudo apt-get install texlive-full -y
sudo apt-get install lat -y
sudo apt-get install mysql-server -y
sudo apt-get install mysql-admin -y
sudo apt-get install netbeans -y
sudo apt-get install maven2 -y

#install optional stuff
sudo apt-get install cups-pdf -y
sudo apt-get install firefox-webdeveloper -y
sudo apt-get install mozilla-livehttpheaders -y
sudo apt-get install flashplugin-nonfree -y

#install eid stuff
sudo apt-get install libccid -y
sudo apt-get install beidgui -y
sudo apt-get install beid-tools -y
sudo apt-get install libbeid2 -y
sudo apt-get install libbeid2-dev -y
sudo apt-get install libbeidlibopensc2 -y
sudo apt-get install libbeidlibopensc2-dev -y
sudo apt-get install pcscd -y

#Choose JDK
echo "Choose the SUN JDK here"
sudo update-alternatives --config java

#install eclipse
echo "Installing Eclipse"
curl -o $eclipseDownloadFile $eclipseDownloadUrl
cd $optDir
sudo tar -xvzf $eclipseDownloadFile

#create desktop shortcuts for Eclipse
echo '[Desktop Entry]' > /tmp/eclipse.desktop
echo 'Encoding=UTF-8' >> /tmp/eclipse.desktop
echo 'Name=Eclipse' >> /tmp/eclipse.desktop
echo 'Type=Application' >> /tmp/eclipse.desktop
echo 'StartupNotify=true' >> /tmp/eclipse.desktop
echo 'Terminal=false' >> /tmp/eclipse.desktop
echo 'Comment=Eclipse Europa' >> /tmp/eclipse.desktop
echo "Exec=$eclipseHome/eclipse" >> /tmp/eclipse.desktop
echo "TryExec=$eclipseHome/eclipse" >> /tmp/eclipse.desktop
echo "Icon=$eclipseHome/icon.xpm" >> /tmp/eclipse.desktop
echo 'Categories=GTK;GNOME;Application;Development;' >> /tmp/eclipse.desktop

sudo mv /tmp/eclipse.desktop /usr/share/applications/eclipse.desktop
sudo chown root:root /usr/share/applications/eclipse.desktop

#configure maven
mkdir -p "$userHome/.m2/repository"


#setting up environment
echo "Setting up environment for user $userName"
echo "NLS_LANG=Dutch_Belgium.AL32UTF8" > $bzbProfile
echo "NLS_SORT=Dutch" >> $bzbProfile
echo "NLS_LENGTH_SEMANTICS=CHAR" >> $bzbProfile
echo "JAVA_HOME=$javaHome" >> $bzbProfile
echo "PATH=$javaHome/bin:$eclipseHome:\$PATH" >> $bzbProfile
echo "export NLS_LANG NLS_SORT NLS_LENGTH_SEMANTICS" >> $bzbProfile
echo "export JAVA_HOME PATH" >> $bzbProfile

echo "" >> $profile
echo "#Append settings for BZB Framework" >> $profile
echo ". $bzbProfile" >> $profile

source $bzbProfile

mkdir -p ~/Development/Java
mkdir -p ~/Development/workspaces
mkdir -p ~/conf

#permissions fix. If we don't do this, X session immediately quits
sudo chmod -R a+w /tmp

#done
echo "BZB Framework workstation install finished"
