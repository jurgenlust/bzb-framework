echo loading missing artifacts in local repository
start mvn install:install-file -DgroupId=net.sourceforge.jsfcomp -DartifactId=chartcreator -Dversion=1.2.0 -Dpackaging=jar -DgeneratePom=true -Dfile=chartcreator-1.2.0.jar
start mvn install:install-file -DgroupId=net.java.dev.pdfrenderer -DartifactId=pdfrenderer -Dversion=20080921 -Dpackaging=jar -DgeneratePom=true -Dfile=PDFRenderer.jar
start mvn install:install-file -DgroupId=net.java.dev.weblets -DartifactId=weblets-api -Dversion=1.1 -Dpackaging=jar -DgeneratePom=true -Dfile=weblets-api-1.1.jar
start mvn install:install-file -DgroupId=net.java.dev.weblets -DartifactId=weblets-impl -Dversion=1.1 -Dpackaging=jar -DgeneratePom=true -Dfile=weblets-impl-1.1.jar
