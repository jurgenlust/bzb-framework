package be.bzbit.framework.i18n.test;

import be.bzbit.framework.i18n.dto.Translation;
import be.bzbit.framework.i18n.model.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

import static org.junit.Assert.*;


public class TranslationTest {
	private Message dutchMessage;
	private Message defaultMessage;
	private String messageCode;
	private List<Message> messages;
	private List<Locale> locales;
	private Translation translation;
	
	public TranslationTest() {
		messageCode = "testCode1";
		defaultMessage = new Message(messageCode);
		defaultMessage.setMessage("This is the default translation of test message 1");
		dutchMessage = new Message(messageCode);
		dutchMessage.setCountry("BE");
		dutchMessage.setLanguage("nl");
		dutchMessage.setMessage("Dit is test boodschap 1"); 
		messages = new ArrayList<Message>();
		locales = new ArrayList<Locale>();
		locales.add(Locale.ENGLISH);
		locales.add(Locale.FRENCH);
		locales.add(Locale.GERMAN);
		locales.add(new Locale("nl", "BE"));
		translation = new Translation();
	}
	
	@Test
	public void testTranslationWithDefaultAndDutch() {
		messages.add(defaultMessage);
		messages.add(dutchMessage);
		translation.fill(messageCode, locales, messages);
		
		assertNotNull(translation.getDefaultMessage());
		assertNull(translation.getDefaultMessage().getLanguage());
		assertNull(translation.getDefaultMessage().getCountry());
		assertEquals("This is the default translation of test message 1", translation.getDefaultMessage().getMessage());
		assertNotNull(translation.getMessageCode());
		assertEquals(messageCode, translation.getMessageCode());
		assertNotNull(translation.getMessages());
		assertEquals(4, translation.getMessages().length);
		assertEquals(messageCode, translation.getMessages()[3].getCode());
		assertEquals("nl", translation.getMessages()[3].getLanguage());
		assertEquals("Dit is test boodschap 1", translation.getMessages()[3].getMessage());
		assertEquals(messageCode, translation.getMessages()[0].getCode());
		assertNull(translation.getMessages()[0].getMessage());
		assertEquals("en", translation.getMessages()[0].getLanguage());
		assertNull(translation.getMessages()[0].getCountry());
	}
	
	@Test
	public void testTranslationWithoutDefaultAndWithDutch() {
		messages.add(dutchMessage);
		translation.fill(messageCode, locales, messages);
		
		assertNotNull(translation.getDefaultMessage());
		assertNull(translation.getDefaultMessage().getLanguage());
		assertNull(translation.getDefaultMessage().getCountry());
		assertNull(translation.getDefaultMessage().getMessage());
		assertNotNull(translation.getMessageCode());
		assertEquals(messageCode, translation.getMessageCode());
		assertNotNull(translation.getMessages());
		assertEquals(4, translation.getMessages().length);
		assertEquals(messageCode, translation.getMessages()[3].getCode());
		assertEquals("Dit is test boodschap 1", translation.getMessages()[3].getMessage());
		assertEquals(messageCode, translation.getMessages()[0].getCode());
		assertNull(translation.getMessages()[0].getMessage());
		assertEquals("en", translation.getMessages()[0].getLanguage());
		assertNull(translation.getMessages()[0].getCountry());
	}
	
	@Test
	public void testEmptyTranslation() {
		translation.fill(messageCode, locales, messages);

		assertNotNull(translation.getDefaultMessage());
		assertNull(translation.getDefaultMessage().getLanguage());
		assertNull(translation.getDefaultMessage().getCountry());
		assertNull(translation.getDefaultMessage().getMessage());
		assertNotNull(translation.getMessageCode());
		assertEquals(messageCode, translation.getMessageCode());
		assertNotNull(translation.getMessages());
		assertEquals(4, translation.getMessages().length);
		assertEquals(messageCode, translation.getMessages()[3].getCode());
		assertNull(translation.getMessages()[3].getMessage());
		assertEquals(messageCode, translation.getMessages()[0].getCode());
		assertNull(translation.getMessages()[0].getMessage());
		assertEquals("en", translation.getMessages()[0].getLanguage());
		assertNull(translation.getMessages()[0].getCountry());
	}
}
