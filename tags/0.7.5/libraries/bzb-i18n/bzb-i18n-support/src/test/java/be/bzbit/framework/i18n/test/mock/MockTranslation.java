package be.bzbit.framework.i18n.test.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import be.bzbit.framework.i18n.dto.Translation;
import be.bzbit.framework.i18n.model.Message;

public class MockTranslation extends Translation {
	private static final long serialVersionUID = 6982758515295985760L;
	public static final String CODE = "testCode1";
	public static final String DUTCH_MESSAGE = "Dit is de Nederlandse vertaling van test boodschap 1";
	public static final String DEFAULT_MESSAGE = "This is the default translation of test message 1";
	public static final String ENGLISH_MESSAGE = "This is the English translation of test message 1";
	public static final String FRENCH_MESSAGE = "Ceci est la traduction Française du premier message de test";

	public MockTranslation() {
		this(null);
	}
	
	public MockTranslation(Message message) {
		super();
		Message defaultMessage = new Message(CODE);
		defaultMessage.setMessage(DEFAULT_MESSAGE);
		Message dutchMessage = new Message(CODE);
		dutchMessage.setCountry("BE");
		dutchMessage.setLanguage("nl");
		dutchMessage.setMessage(DUTCH_MESSAGE); 
		Message englishMessage = new Message(CODE);
		englishMessage.setCountry(Locale.ENGLISH.getCountry());
		englishMessage.setLanguage(Locale.ENGLISH.getLanguage());
		englishMessage.setMessage(ENGLISH_MESSAGE); 
		List<Message> messages = new ArrayList<Message>();
		messages.add(defaultMessage);
		messages.add(dutchMessage);
		messages.add(englishMessage);
		if (message != null) {
			messages.add(message);
		}
		List<Locale> locales = new ArrayList<Locale>();
		locales.add(Locale.ENGLISH);
		locales.add(Locale.FRENCH);
		locales.add(Locale.GERMAN);
		locales.add(new Locale("nl", "BE"));
		fill(CODE, locales, messages);
	}
}
