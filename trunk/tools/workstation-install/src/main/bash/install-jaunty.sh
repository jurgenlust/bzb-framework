#!/bin/bash

###################################################
# Install script for BZB Framework Workstations
#
# This script prepares an Ubuntu Linux 9.04 workstation for developing
# web applications using the BZB Framework
#
# Jurgen Lust, June 28, 2009
# Jurgen.Lust@gmail.com
#
###################################################



# Some global variables
userName=$LOGNAME
userHome=$HOME
javaHome="/usr/lib/jvm/java-6-sun"
optDir="/opt"
eclipseDownloadUrl="http://eclipse.a3-system.be/technology/epp/downloads/release/galileo/R/eclipse-jee-galileo-linux-gtk-x86_64.tar.gz"
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

#install a database
#uncomment this if you want MySQL
#sudo apt-get install mysql-server -y
#sudo apt-get install mysql-admin -y
#we use PostgreSQL by default
sudo apt-get install postgresql -y
sudo apt-get install pgadmin3 -y

#We still use the Sun JDK
sudo apt-get install "sun-java6-jdk" -y
sudo apt-get install "sun-java6-fonts" -y
sudo apt-get install "sun-java6-plugin" -y

#CURL is required for downloading some stuff later on
sudo apt-get install curl -y

#Ant is in every Java developer's toolbox
sudo apt-get install ant -y

#So is Maven 2
sudo apt-get install maven2 -y

#So is Subversion
sudo apt-get install subversion -y

#So is Tomcat 6
sudo apt-get install tomcat6

#Very handy nautilus plugin
sudo apt-get install nautilus-open-terminal -y

#If you don't like NetBeans, comment this
sudo apt-get install netbeans -y

#Nice tool for editing CSS files
sudo apt-get install cssed -y

#Apache is always good to have
sudo apt-get install apache2 -y
sudo apt-get install libapache2-mod-jk -y

#Do we want remote access to this machine?
sudo apt-get install openssh-server -y
sudo apt-get install samba -y


#If you don't want PHP, comment this
sudo apt-get install libapache2-mod-php5 -y
sudo apt-get install php5 -y
sudo apt-get install php5-gd -y
sudo apt-get install php5-pgsql -y
sudo apt-get install php5-mysql -y

#some nice browser plugins
sudo apt-get install firefox-webdeveloper -y
sudo apt-get install mozilla-livehttpheaders -y
sudo apt-get install flashplugin-nonfree -y

#password manager: if you maintain a number of websites, this comes in very handy
sudo apt-get install revelation -y

#LDAP administration tool
sudo apt-get install lat -y

#Yes, I still like LaTeX
sudo apt-get install texlive-full -y

#install eid stuff - only useful in Belgium really
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
echo 'Comment=Eclipse Galileo' >> /tmp/eclipse.desktop
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
echo "JAVA_HOME=$javaHome" >> $bzbProfile
echo "PATH=$javaHome/bin:$eclipseHome:\$PATH" >> $bzbProfile
echo "export JAVA_HOME PATH" >> $bzbProfile

echo "" >> $profile
echo "#Append settings for BZB Framework" >> $profile
echo ". $bzbProfile" >> $profile

source $bzbProfile

#permissions fix. If we don't do this, X session immediately quits
sudo chmod -R a+w /tmp

#done
echo "BZB Framework workstation install finished"
