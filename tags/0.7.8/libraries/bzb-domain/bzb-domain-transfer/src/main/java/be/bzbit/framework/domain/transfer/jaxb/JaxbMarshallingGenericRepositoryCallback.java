package be.bzbit.framework.domain.transfer.jaxb;

import java.io.Serializable;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLStreamWriter;

import be.bzbit.framework.domain.repository.GenericRepositoryCallback;
import be.bzbit.framework.domain.transfer.ExportException;

public class JaxbMarshallingGenericRepositoryCallback<T> implements
		GenericRepositoryCallback<T>, Serializable {
	
	private static final long serialVersionUID = -1654351431923153283L;
	private final Marshaller marshaller;
	private final XMLStreamWriter writer;

	public JaxbMarshallingGenericRepositoryCallback(Marshaller marshaller,
			XMLStreamWriter writer) {
		super();
		this.marshaller = marshaller;
		this.writer = writer;
	}

	@Override
	public void process(T entity) {
		try {
			marshaller.marshal(entity, writer);
		} catch (JAXBException e) {
			throw new ExportException(e);
		}
	}

}
