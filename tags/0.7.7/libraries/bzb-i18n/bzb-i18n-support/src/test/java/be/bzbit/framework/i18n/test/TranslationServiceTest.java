package be.bzbit.framework.i18n.test;

import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import be.bzbit.framework.i18n.dto.Translation;
import be.bzbit.framework.i18n.model.Message;
import be.bzbit.framework.i18n.repository.MessageRepository;
import be.bzbit.framework.i18n.service.TranslationService;
import be.bzbit.framework.i18n.test.mock.MockTranslation;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
//specifies the Spring configuration to load for this test fixture
@ContextConfiguration(locations =  {"classpath:context/test/test-services.xml"}
)
public class TranslationServiceTest extends
		AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	private TranslationService translationService;
	@Autowired
	private MessageRepository messageRepository;

	@Test
	public void testNewTranslation() {
		Translation translation = new MockTranslation();
		getTranslationService().saveTranslation(translation);
		int rows = countRowsInTable("I18N_MESSAGE");
		assertEquals(3, rows);
	}
	
	@Test
	public void testTranslationWithUnchangedExistingMessage() {
		Message message = new Message(MockTranslation.CODE);
		message.setCountry(Locale.FRENCH.getCountry());
		message.setLanguage(Locale.FRENCH.getLanguage());
		message.setMessage(MockTranslation.FRENCH_MESSAGE);
		Message savedMessage = getMessageRepository().save(message);
		assertNotNull(savedMessage.getId());
		assertEquals(1, countRowsInTable("I18N_MESSAGE"));
		Translation translation = new MockTranslation(savedMessage);
		getTranslationService().saveTranslation(translation);
		assertEquals(4, countRowsInTable("I18N_MESSAGE"));
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
