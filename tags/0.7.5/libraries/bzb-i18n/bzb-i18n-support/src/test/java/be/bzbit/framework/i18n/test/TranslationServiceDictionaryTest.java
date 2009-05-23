package be.bzbit.framework.i18n.test;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import be.bzbit.framework.i18n.dto.DictionaryEntry;
import be.bzbit.framework.i18n.dto.DictionaryLookup;
import be.bzbit.framework.i18n.dto.DictionaryPage;
import be.bzbit.framework.i18n.model.Message;
import be.bzbit.framework.i18n.repository.MessageRepository;
import be.bzbit.framework.i18n.service.TranslationService;

@RunWith(SpringJUnit4ClassRunner.class)
//specifies the Spring configuration to load for this test fixture
@ContextConfiguration(locations =  {"classpath:context/test/test-services.xml"}
)
public class TranslationServiceDictionaryTest extends
AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	private TranslationService translationService;
	@Autowired
	private MessageRepository messageRepository;
	
	@Before
	public void createMockMessages() {
		for (int i = 10; i < 30; i++) {
			Message defaultMessage = new Message("message" + i);
			defaultMessage.setMessage("Message " + i + " - default translation");
			getMessageRepository().save(defaultMessage);
		}
		for (int i = 10; i < 30; i+=2) {
			Message dutchMessage = new Message("message" + i);
			dutchMessage.setLocale(new Locale("nl"));
			dutchMessage.setMessage("Message " + i + " - dutch translation");
			getMessageRepository().save(dutchMessage);
		}
		for (int i = 10; i < 30; i+=4) {
			Message frenchMessage = new Message("message" + i);
			frenchMessage.setLocale(new Locale("fr"));
			frenchMessage.setMessage("Message " + i + " - french translation");
			getMessageRepository().save(frenchMessage);
		}
	}
	
	@Test
	public void testLoadDictionaryPage() {
		DictionaryPage page = loadPage("nl", 0);
		assertEquals(20, page.getTotalNumberOfResults());
		assertEquals(5, page.getEntries().length);
		for (int i = 0; i < page.getEntries().length; i++) {
			int messageNumber = i + 10;
			DictionaryEntry entry = page.getEntries()[i];
			System.out.println(entry.getMessageCode() + ": " + entry.getTranslatedMessage());
			assertEquals("message" + messageNumber, entry.getMessageCode());
			assertEquals("Message " + messageNumber + " - default translation", entry.getDefaultMessage());
			if (messageNumber % 2 == 0) {
				assertEquals("Message " + messageNumber + " - dutch translation", entry.getTranslatedMessage());
			} else {
				assertNull(entry.getTranslatedMessage());
			}
		}
	}
	
	@Test
	public void testSaveDictionaryPage() {
		DictionaryPage page = loadPage("nl", 0);
		for (int i = 0; i < page.getEntries().length; i++) {
			int messageNumber = i + 10;
			DictionaryEntry entry = page.getEntries()[i];
			if (messageNumber % 2 == 0) {
				entry.setTranslatedMessage("Message " + messageNumber + " has a new dutch translation");
			} else {
				entry.setTranslatedMessage("Message " + messageNumber + " was translated in dutch for the first time");
			}
		}
		getTranslationService().saveDictionaryPage(page);
		for (int i = 10; i < 14; i++) {
			Message message = getMessageRepository().getMessage("nl", null, "message" + i);
			if (i % 2 == 0) {
				assertEquals("Message " + i + " has a new dutch translation", message.getMessage());
			} else {
				assertEquals("Message " + i + " was translated in dutch for the first time", message.getMessage());
			}
		}
		for (int i = 15; i < 30; i++) {
			Message message = getMessageRepository().getMessage("nl", null, "message" + i);
			if (i % 2 == 0) {
				assertEquals("Message " + i + " - dutch translation", message.getMessage());
			} else {
				assertNull(message);
			}
		}
	}
	
	private DictionaryPage loadPage(String language, int page) {
		DictionaryLookup lookup = new DictionaryLookup();
		lookup.setLocale(new Locale(language));
		lookup.setCurrentPage(page);
		lookup.setPageSize(5);
		return getTranslationService().getDictionaryPage(lookup);
	}

	public void setTranslationService(TranslationService translationService) {
		this.translationService = translationService;
	}

	public TranslationService getTranslationService() {
		return translationService;
	}

	public void setMessageRepository(MessageRepository messageRepository) {
		this.messageRepository = messageRepository;
	}

	public MessageRepository getMessageRepository() {
		return messageRepository;
	}
	
}
