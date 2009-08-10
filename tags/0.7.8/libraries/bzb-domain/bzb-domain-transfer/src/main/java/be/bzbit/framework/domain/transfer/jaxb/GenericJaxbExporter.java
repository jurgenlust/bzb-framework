package be.bzbit.framework.domain.transfer.jaxb;

import java.io.OutputStream;
import java.io.Serializable;
import java.io.Writer;
import java.lang.reflect.ParameterizedType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import be.bzbit.framework.domain.repository.GenericRepository;
import be.bzbit.framework.domain.transfer.ExportException;
import be.bzbit.framework.domain.transfer.GenericExporter;

public class GenericJaxbExporter<T, ID extends Serializable> implements
		GenericExporter<T, ID>, Serializable {
	private static final long serialVersionUID = -8147623303548751905L;

	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger
			.getLogger(GenericJaxbExporter.class);

	private boolean generateDocument = true;
	private boolean generateRoot = true;
	private GenericRepository<T, ID> repository;
	private final Class<T> rootClass;
	private final Class<ID> idClass;
	private String rootElementName = "export";
	private String encoding = "UTF-8";

	
	@SuppressWarnings("unchecked")
	public GenericJaxbExporter() {
		super();
		this.rootClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
		this.idClass = (Class<ID>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[1];
	}
	
	public GenericJaxbExporter(Class<T> rootClass, Class<ID> idClass) {
		super();
		this.rootClass = rootClass;
		this.idClass = idClass;
	}

	@Override
	public void export(OutputStream outputStream) {
		export(outputStream, null);
	}

	@Override
	public void export(Writer writer) {
		export(writer, null);
	}

	@Override
	public void export(XMLStreamWriter streamWriter) {
		export(streamWriter, null);
	}

	public GenericRepository<T, ID> getRepository() {
		return repository;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public Class<ID> getIdClass() {
		return idClass;
	}
	
	public Class<T> getRootClass() {
		return rootClass;
	}

	public String getRootElementName() {
		return rootElementName;
	}

	public boolean isGenerateDocument() {
		return generateDocument;
	}

	public boolean isGenerateRoot() {
		return generateRoot;
	}

	public void setGenerateDocument(boolean generateDocument) {
		this.generateDocument = generateDocument;
	}

	public void setGenerateRoot(boolean generateRoot) {
		this.generateRoot = generateRoot;
	}

	public void setRepository(GenericRepository<T, ID> repository) {
		this.repository = repository;
	}

	public void setRootElementName(String rootElement) {
		this.rootElementName = rootElement;
	}

	@Override
	public void export(OutputStream outputStream, String namedQuery,
			Object... parameters) {
		try {
			XMLOutputFactory factory = XMLOutputFactory.newInstance();
			XMLStreamWriter writer = factory
					.createXMLStreamWriter(outputStream, getEncoding());
			export(writer, namedQuery, parameters);
			writer.flush();
			writer.close();
		} catch (XMLStreamException e) {
			log.error("Cannot export", e);
			throw new ExportException(e);
		}
	}

	@Override
	public void export(Writer writer, String namedQuery, Object... parameters) {
		try {
			XMLOutputFactory factory = XMLOutputFactory.newInstance();
			XMLStreamWriter streamWriter = factory
					.createXMLStreamWriter(writer);
			export(streamWriter, namedQuery, parameters);
			streamWriter.flush();
			streamWriter.close();
		} catch (XMLStreamException e) {
			log.error("Cannot export", e);
			throw new ExportException(e);
		}
	}

	@Override
	public void export(XMLStreamWriter streamWriter, String namedQuery,
			Object... parameters) {
		try {
			JAXBContext context = JAXBContext.newInstance(rootClass);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
					Boolean.TRUE);
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, getEncoding());
			if (isGenerateDocument()) {
				streamWriter.writeStartDocument(getEncoding(), "1.0");
			}
			if (isGenerateRoot()) {
				streamWriter.writeStartElement(getRootElementName());
			}
			
			JaxbMarshallingGenericRepositoryCallback<T> callback = new JaxbMarshallingGenericRepositoryCallback<T>(marshaller, streamWriter);
			if (StringUtils.isBlank(namedQuery)) {
				getRepository().iterateAll(callback);
			} else {
				getRepository().iterateByNamedQuery(callback, namedQuery, parameters);
			}
			
			if (isGenerateRoot()) {
				streamWriter.writeEndElement();
			}
			if (isGenerateDocument()) {
				streamWriter.writeEndDocument();
			}
		} catch (Exception e) {
			log.error("Cannot export", e);
			throw new ExportException(e);
		}
	}
}
