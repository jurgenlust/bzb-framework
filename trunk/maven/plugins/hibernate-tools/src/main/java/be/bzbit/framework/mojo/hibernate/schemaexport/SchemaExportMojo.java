package be.bzbit.framework.mojo.hibernate.schemaexport;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.tool.hbm2ddl.SchemaExport;

/**
 * Exporteer een DDL file voor de gespecifieerde databanken
 * 
 * @author Jurgen Lust
 * @author $LastChangedBy: jlust $
 * 
 * @version $LastChangedRevision: 253 $
 * 
 * @requiresDependencyResolution
 * @goal export
 * @phase install
 */
public class SchemaExportMojo extends AbstractMojo {
	// ~ Instance fields
	// --------------------------------------------------------

	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	/**
	 * @parameter expression="true"
	 */
	private String comments;

	/**
	 * @parameter expression="default"
	 */
	private String persistenceUnitName;

	/**
	 * @parameter expression=";"
	 */
	private String delimiter;

	/**
	 * @parameter expression="org.hibernate.dialect.MySQL5Dialect"
	 */
	private String dialects;

	/**
	 * @parameter expression="true"
	 */
	private String format;

	// ~ Methods
	// ----------------------------------------------------------------

	/**
	 * @see org.apache.maven.plugin.AbstractMojo#execute()
	 */
	@SuppressWarnings("unchecked")
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("Generating database DDL scripts");
		updateClassLoader();

		Ejb3Configuration configuration = new Ejb3Configuration();

		Map properties = new HashMap();
		properties.put("hibernate.format_sql", format);
		properties.put("hibernate.use_sql_comments", comments);
		configuration = configuration.configure(persistenceUnitName, properties);

		if (configuration == null) {
			getLog().error("Unable to export schema");
			return;
		}

		for (Iterator iterator = configuration.getClassMappings(); iterator
				.hasNext();) {
			PersistentClass pc = (PersistentClass) iterator.next();
			getLog().info("Found persistent class: " + pc.getClassName());
		}

		for (final String dialect : getDialects()) {
			try {
				export(configuration, dialect);
			} catch (final Exception e) {
				getLog().error("Error exporting DDL for dialect " + dialect, e);
			}
		}
	}

	private void export(final Ejb3Configuration configuration,
			final String dialect) {
		if ((configuration == null) || (dialect == null)) {
			return;
		}

		getLog().info("Generating DDL for " + getDatabaseName(dialect));
		configuration.setProperty("hibernate.dialect", dialect);

		SchemaExport export = new SchemaExport(configuration
				.getHibernateConfiguration());
		export.setDelimiter(delimiter);
		export.setFormat(Boolean.valueOf(format));
		export.setHaltOnError(true);
		// just drop
		export.setOutputFile(getOutputFile(dialect, false, true)
				.getAbsolutePath());
		export.execute(false, false, true, false);
		// just create
		export.setOutputFile(getOutputFile(dialect, true, false)
				.getAbsolutePath());
		export.execute(false, false, false, true);
		// drop and create
		export.setOutputFile(getOutputFile(dialect, true, true)
				.getAbsolutePath());
		export.execute(false, false, false, false);
	}

	private File getOutputFile(final String dialect, final boolean create,
			final boolean drop) {
		if (dialect == null) {
			return null;
		}

		String dbname = getDatabaseName(dialect).toLowerCase();

		File ddlDir = new File(project.getBuild().getDirectory(), "ddl");
		File dbDir = new File(ddlDir, dbname);
		dbDir.mkdirs();

		StringBuilder filename = new StringBuilder();
		filename.append(project.getArtifactId());
		filename.append("-");

		if (drop) {
			filename.append("drop");
		}

		if (drop && create) {
			filename.append("_");
		}

		if (create) {
			filename.append("create");
		}

		filename.append(".sql");

		File targetFile = new File(dbDir, filename.toString());

		return targetFile;
	}

	private List<String> getDialects() {
		ArrayList<String> dialectList = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(dialects, ",");

		while (tokenizer.hasMoreTokens()) {
			dialectList.add(tokenizer.nextToken());
		}

		return dialectList;
	}

	private String getDatabaseName(final String dialect) {
		if (dialect == null) {
			return null;
		}

		String dbname = dialect.replaceAll("org.hibernate.dialect.", "");
		dbname = dbname.replaceAll("Dialect", "");

		return dbname;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	private void updateClassLoader() throws MojoExecutionException {
		List classpathFiles;

		try {
			classpathFiles = project.getCompileClasspathElements();

			URL[] urls = new URL[classpathFiles.size() + 1];

			getLog().debug(
					"number of classpath files: " + classpathFiles.size());

			for (int i = 0; i < classpathFiles.size(); ++i) {
				getLog().debug((String) classpathFiles.get(i));
				urls[i] = new File((String) classpathFiles.get(i)).toURL();
			}

			urls[classpathFiles.size()] = new File(project.getBuild()
					.getOutputDirectory()).toURL();

			URLClassLoader ucl = new URLClassLoader(urls, Thread
					.currentThread().getContextClassLoader());
			Thread.currentThread().setContextClassLoader(ucl);
		} catch (final Exception e) {
			getLog().error(e);
			throw new MojoExecutionException(e.getMessage());
		}
	}
}
