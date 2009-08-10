/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.bzbit.framework.mojo.prettyprinter.java;

import de.hunsicker.io.FileFormat;

import de.hunsicker.jalopy.Jalopy;

import org.apache.maven.model.Developer;
import org.apache.maven.model.Organization;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import org.apache.tools.ant.DirectoryScanner;

import org.dom4j.Document;
import org.dom4j.DocumentException;

import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.net.URL;

import java.util.Iterator;


/**
 * Formatteer de Java source code van een project met behulp van Jalopy
 *
 * @author Jurgen Lust
 * @author $LastChangedBy$
 *
 * @version $LastChangedRevision$
 *
 * @goal format
 * @phase process-sources
 */
public class JavaPrettyPrinterMojo
        extends AbstractMojo {
    //~ Instance fields --------------------------------------------------------

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    //~ Methods ----------------------------------------------------------------

    /**
     * @see org.apache.maven.plugin.AbstractMojo#execute()
     */
    public void execute()
            throws MojoExecutionException, MojoFailureException {
        try {
            File conventionFile = prepareConventionFile();
            Jalopy.setConvention(conventionFile);

            Jalopy jalopy = new Jalopy();
            jalopy.setFileFormat(FileFormat.UNIX);
            jalopy.setEncoding("UTF8");
            format(jalopy, project.getBuild().getSourceDirectory());
            format(jalopy, project.getBuild().getTestSourceDirectory());
        } catch (final DocumentException e) {
            throw new MojoFailureException("Could not prepare convention file");
        } catch (final IOException e) {
            throw new MojoFailureException("Could not write convention file");
        }
    }

    private void format(
        final Jalopy jalopy,
        final String baseDir
    )
            throws IOException {
        File baseDirFile = new File(baseDir);

        if (!baseDirFile.exists() || !baseDirFile.isDirectory()) {
            return;
        }

        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(baseDir);

        String[] includes = { "**/*.java" };
        scanner.setIncludes(includes);
        scanner.scan();

        String[] files = scanner.getIncludedFiles();

        for (final String file : files) {
            File sourceFile = new File(baseDir, file);
            getLog().info("formatting " + file);
            jalopy.setInput(sourceFile);
            jalopy.setOutput(sourceFile);
            jalopy.format();

            if (jalopy.getState() == Jalopy.State.ERROR) {
                getLog().warn(file + " could not be formatted!");
            }
        }
    }

    private File prepareConventionFile()
            throws DocumentException, IOException {
        File conventionFile = new File(initWorkDir(), "bzb-convention.xml");
        URL sourceUrl =
            this.getClass().getClassLoader().getResource("bzb-convention.xml");
        SAXReader xmlReader = new SAXReader(false);

        Document xml = xmlReader.read(sourceUrl);
        String xmlText = xml.asXML();
        xmlText = xmlText.replaceAll("@project.name@", getName());
        xmlText = xmlText.replaceAll("@project.description@", getDescription());

        xmlText = xmlText.replaceAll("@project.developers@", getDevelopers());
        xmlText = xmlText.replaceAll(
                "@project.inceptionYear@", getInceptionYear()
            );
        xmlText = xmlText.replaceAll(
                "@project.organisation.name@", getOrganisationName()
            );

        FileWriter writer = new FileWriter(conventionFile);
        writer.write(xmlText);
        writer.flush();

        return conventionFile;
    }

    private String getInceptionYear() {
        return (project.getInceptionYear() == null) ? ""
                                                    : project.getInceptionYear();
    }

    private String getOrganisationName() {
        Organization organisation = project.getOrganization();

        if ((organisation == null) || (organisation.getName() == null)) {
            return "";
        }

        return organisation.getName();
    }

    @SuppressWarnings("unchecked")
	private String getDevelopers() {
        StringBuilder developers = new StringBuilder();

        for (Iterator dit = project.getDevelopers().iterator(); dit.hasNext();) {
            Developer developer = (Developer) dit.next();
            developers.append(developer.getName());

            if (dit.hasNext()) {
                developers.append(", ");
            }
        }

        return developers.toString();
    }

    private String getName() {
        return (project.getName() == null) ? "" : project.getName();
    }

    private String getDescription() {
        return (project.getDescription() == null) ? "" : project.getDescription();
    }

    private File initWorkDir() {
        File workDirFile =
            new File(project.getBuild().getDirectory(), "jalopy");
        workDirFile.mkdirs();

        return workDirFile;
    }
}
