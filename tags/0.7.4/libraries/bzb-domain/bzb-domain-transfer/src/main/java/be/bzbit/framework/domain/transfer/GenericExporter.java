package be.bzbit.framework.domain.transfer;

import java.io.OutputStream;
import java.io.Serializable;
import java.io.Writer;

import javax.xml.stream.XMLStreamWriter;

/**
 * An exporter helps provides a way to export a repository
 *
 * @author Jurgen Lust
 * @author $LastChangedBy$
 *
 * @version $LastChangedRevision$
 */
public interface GenericExporter<T, ID extends Serializable> {
	void export(OutputStream outputStream, String namedQuery, Object... parameters);
	void export(OutputStream outputStream);
	void export(Writer writer);
	void export(Writer writer, String namedQuery, Object... parameters);
	void export(XMLStreamWriter streamWriter);
	void export(XMLStreamWriter streamWriter, String namedQuery, Object... parameters);
}
