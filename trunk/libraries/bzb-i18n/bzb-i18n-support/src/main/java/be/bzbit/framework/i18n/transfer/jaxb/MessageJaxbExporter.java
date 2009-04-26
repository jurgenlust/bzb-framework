package be.bzbit.framework.i18n.transfer.jaxb;

import java.io.Serializable;

import be.bzbit.framework.domain.transfer.jaxb.GenericJaxbExporter;
import be.bzbit.framework.i18n.model.Message;
import be.bzbit.framework.i18n.transfer.MessageExporter;

public class MessageJaxbExporter extends GenericJaxbExporter<Message, Long>
		implements MessageExporter, Serializable {

	private static final long serialVersionUID = -833566829504255322L;

}
