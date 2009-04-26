package be.bzbit.framework.mojo.tomcat;

import org.apache.catalina.Context;
import org.apache.catalina.Engine;
import org.apache.catalina.Host;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.loader.WebappLoader;
import org.apache.catalina.realm.MemoryRealm;
import org.apache.catalina.startup.Embedded;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import org.dom4j.Document;
import org.dom4j.DocumentException;

import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.net.URL;


/**
 * This mojo runs an embedded Tomcat server, using the webapp target
 * directory as webapp Source Dir
 *
 * @author Jurgen Lust
 * @author $LastChangedBy$
 *
 * @version $LastChangedRevision$
 *
 * @goal run
 * @requiresDependencyResolution runtime
 * @phase integration-test
 */
public class EmbeddedTomcatMojo
        extends AbstractMojo {
    //~ Instance fields --------------------------------------------------------

    /**
     * the HTTP port
     *
     * @parameter expression="${tomcat.http.port}"
     */
    private Integer httpPort;

    /**
     * the baseDir for the Embedded Tomcat
     *
     * @parameter expression="${project.build.directory}"
     */
    private String baseDir;

    /**
     * the source directory containing the webapp
     *
     * @parameter expression="${project.build.finalName}"
     */
    private String webappSrcDir;

    /**
     * the work directory for the Embedded Tomcat
     *
     * @parameter expression="${project.build.directory}/tomcat"
     */
    private String workDir;

    //~ Methods ----------------------------------------------------------------

    /**
     *
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute()
            throws MojoExecutionException, MojoFailureException {
        try {
            initWorkDir();
            getLog().info("Initializing Embedded Tomcat");

            Embedded server = new Embedded();
            server.setCatalinaHome(getWorkDir());

            MemoryRealm memoryRealm = new MemoryRealm();
            server.setRealm(memoryRealm);

            Engine engine = server.createEngine();
            engine.setName("localEngine");
            engine.setDefaultHost("localHost");

            Host localHost = server.createHost("localHost", getBaseDir());
            engine.addChild(localHost);

            Context rootContext = server.createContext("", getWebappSrcDir());
            localHost.addChild(rootContext);

            //The classloader for this context should include the classpath
            //of this plugin, which contains all tomcat jars.
            WebappLoader loader =
                new WebappLoader(this.getClass().getClassLoader());

            rootContext.setLoader(loader);
            rootContext.setReloadable(true);
            server.addEngine(engine);

            Connector httpConnector =
                server.createConnector(
                    (java.net.InetAddress) null, getPort(), false
                );
            server.addConnector(httpConnector);
            server.start();
            //now just wait for requests
            waitInfinitely();
        } catch (final LifecycleException e) {
            throw new MojoExecutionException("Could not start tomcat", e);
        } catch (final DocumentException e) {
            throw new MojoExecutionException(
                "Could not create config files", e
            );
        } catch (final IOException e) {
            throw new MojoExecutionException(
                "Could not create config files", e
            );
        }
    }

    /**
     * Create the Tomcat conf directory, and copy the tomcat-users.xml
     * web.xml, and log4j.xml files into it.
     *
     * @throws DocumentException
     * @throws IOException
     */
    private void initWorkDir()
            throws DocumentException, IOException {
        getLog().info("Setting up Tomcat work directory: " + getWorkDir());

        File workDirFile = new File(getWorkDir());
        File confDir = new File(workDirFile, "conf");
        confDir.mkdirs();
        writeConfigFile(confDir, "tomcat-users.xml");
        writeConfigFile(confDir, "web.xml");
        writeConfigFile(confDir, "log4j.xml");
    }

    /**
     * Copy the specified config xml file from the classpath to the
     * specified config directory
     *
     * @param confDir the config directory
     * @param filename the name of the xml file
     *
     * @throws DocumentException
     * @throws IOException
     */
    private void writeConfigFile(
        final File confDir,
        final String filename
    )
            throws DocumentException, IOException {
        getLog().info("Creating " + filename);

        URL sourceUrl = this.getClass().getClassLoader().getResource(filename);
        SAXReader xmlReader = new SAXReader(false);
        xmlReader.setIncludeExternalDTDDeclarations(false);
        xmlReader.setValidation(false);

        Document xml = xmlReader.read(sourceUrl);
        FileOutputStream out =
            new FileOutputStream(new File(confDir, filename));
        XMLWriter writer = new XMLWriter(out);
        writer.write(xml);
        writer.flush();
    }

    /**
     * getter for workDir
     *
     * @return Returns the workDir.
     */
    public String getWorkDir() {
        return workDir;
    }

    /**
     * setter for workDir
     *
     * @param workDir The workDir to set.
     */
    public void setWorkDir(final String workDir) {
        this.workDir = workDir;
    }

    /**
     * getter for the baseDir
     *
     * @return Returns the baseDir.
     */
    public String getBaseDir() {
        return baseDir;
    }

    private int getPort() {
        return (httpPort == null) ? 8080 : httpPort.intValue();
    }

    /**
     * set for the baseDir
     *
     * @param webappsDir The baseDir to set.
     */
    public void setBaseDir(final String webappsDir) {
        this.baseDir = webappsDir;
    }

    /**
     * getter for the webappSrcDir
     *
     * @return Returns the webappSrcDir.
     */
    public String getWebappSrcDir() {
        return webappSrcDir;
    }

    /**
     * setter for the webappSrcDir
     *
     * @param webappSrcDir The webappSrcDir to set.
     */
    public void setWebappSrcDir(final String webappSrcDir) {
        this.webappSrcDir = webappSrcDir;
    }

    /**
     * Keep this mojo going
     */
    private void waitInfinitely() {
        Object lock = new Object();

        synchronized (lock) {
            try {
                lock.wait();
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}