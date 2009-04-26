package be.bzbit.framework.i18n.transfer.jaxb;

import java.io.Serializable;

import be.bzbit.framework.domain.transfer.jaxb.GenericJaxbImporter;
import be.bzbit.framework.i18n.model.Message;
import be.bzbit.framework.i18n.transfer.MessageImporter;

public class MessageJaxbImporter extends GenericJaxbImporter<Message, Long>
		implements MessageImporter, Serializable {

	private static final long serialVersionUID = -8148483561695201740L;

}
