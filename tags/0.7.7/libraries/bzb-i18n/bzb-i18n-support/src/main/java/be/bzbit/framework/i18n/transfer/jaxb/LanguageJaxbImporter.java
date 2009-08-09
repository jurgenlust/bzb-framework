package be.bzbit.framework.i18n.transfer.jaxb;

import java.io.Serializable;

import be.bzbit.framework.domain.transfer.jaxb.GenericJaxbImporter;
import be.bzbit.framework.i18n.model.Language;
import be.bzbit.framework.i18n.transfer.LanguageImporter;

public class LanguageJaxbImporter extends GenericJaxbImporter<Language, Long>
		implements LanguageImporter, Serializable {

	private static final long serialVersionUID = -6061555084796155703L;

}
