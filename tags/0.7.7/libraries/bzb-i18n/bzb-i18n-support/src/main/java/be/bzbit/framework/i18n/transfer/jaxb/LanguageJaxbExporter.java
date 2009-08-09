package be.bzbit.framework.i18n.transfer.jaxb;

import java.io.Serializable;

import be.bzbit.framework.domain.transfer.jaxb.GenericJaxbExporter;
import be.bzbit.framework.i18n.model.Language;
import be.bzbit.framework.i18n.transfer.LanguageExporter;

public class LanguageJaxbExporter extends GenericJaxbExporter<Language, Long> implements LanguageExporter, Serializable {

	private static final long serialVersionUID = -7053013716592387847L;

}
