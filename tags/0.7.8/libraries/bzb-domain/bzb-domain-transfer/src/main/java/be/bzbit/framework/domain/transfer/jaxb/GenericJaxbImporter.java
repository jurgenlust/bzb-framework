package be.bzbit.framework.domain.transfer.jaxb;

import org.apache.log4j.Logger;
import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.EventFilter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import be.bzbit.framework.domain.repository.GenericRepository;
import be.bzbit.framework.domain.transfer.GenericImporter;
import be.bzbit.framework.domain.transfer.ImportException;

public class GenericJaxbImporter<T, ID extends Serializable> implements
		GenericImporter<T, ID>, Serializable {
	private static final long serialVersionUID = -6230660981766235293L;
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(GenericJaxbImporter.class);

	public GenericRepository<T, ID> getRepository() {
		return repository;
	}

	public void setRepository(GenericRepository<T, ID> repository) {
		this.repository = repository;
	}

	public String getRootElementName() {
		return rootElementName;
	}

	public void setRootElementName(String rootElementName) {
		this.rootElementName = rootElementName;
	}

	public Class<T> getRootClass() {
		return rootClass;
	}

	public Class<ID> getIdClass() {
		return idClass;
	}

	private GenericRepository<T, ID> repository;
	private final Class<T> rootClass;
	private final Class<ID> idClass;
	private String rootElementName = "export";
	private int flushInterval = 10;
	private String encoding = "UTF-8";

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public int getFlushInterval() {
		return flushInterval;
	}

	public void setFlushInterval(int flushInterval) {
		if (flushInterval > 0) {
			this.flushInterval = flushInterval;
		}
	}

	@SuppressWarnings("unchecked")
	public GenericJaxbImporter() {
		super();
		this.rootClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
		this.idClass = (Class<ID>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[1];
	}
	
	public GenericJaxbImporter(Class<T> rootClass, Class<ID> idClass) {
		super();
		this.rootClass = rootClass;
		this.idClass = idClass;
	}

	
	@Override
	public void importEntities(InputStream inputStream) {
		try {
		// Parse the data, filtering out the start elements
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLEventReader eventReader = inputFactory.createXMLEventReader(inputStream, getEncoding());
        importEntities(inputFactory, eventReader);
		} catch (XMLStreamException e) {
			log.error("Cannot import", e);
			throw new ImportException(e);
		}
    }

	@Override
	public void importEntities(Reader reader) {
		try {
			// Parse the data, filtering out the start elements
	        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
	        XMLEventReader eventReader = inputFactory.createXMLEventReader(reader);
	        importEntities(inputFactory, eventReader);
			} catch (XMLStreamException e) {
				log.error("Cannot import", e);
				throw new ImportException(e);
			}
	}

	@SuppressWarnings("unchecked")
	protected void importEntities(XMLInputFactory inputFactory, XMLEventReader eventReader) {
		try {
        EventFilter filter = new EventFilter() {
            public boolean accept(XMLEvent event) {
                return event.isStartElement();
            }
        };
        XMLEventReader filteredEventReader = inputFactory.createFilteredReader(eventReader, filter);
        JAXBContext context = JAXBContext.newInstance(rootClass);
        Unmarshaller um = context.createUnmarshaller();
        StartElement startElement = (StartElement) filteredEventReader.nextEvent();
        if (!startElement.getName().getLocalPart().equals(getRootElementName())) {
        	throw new ImportException("Invalid root element: expected " + getRootElementName() + " but got " + startElement.getName().getLocalPart());
        }
        int entityCounter = 0;
        while (filteredEventReader.peek() != null) {
             Object object = um.unmarshal(eventReader);
             if (rootClass.isInstance(object)) {
            	 T entity = (T)object;
            	 importEntity(entity);
             }
             if (entityCounter % getFlushInterval() == 0) {
            	 getRepository().flush();
             }
        }

		} catch (Exception e) {
			log.error("Cannot import", e);
			throw new ImportException(e);
		}
	}
	
	protected void importEntity(T entity) {
		log.debug(entity);
		getRepository().save(processEntityBeforeSaving(entity));
	}
	
	protected T processEntityBeforeSaving(T entity) {
		return entity;
	}

}
