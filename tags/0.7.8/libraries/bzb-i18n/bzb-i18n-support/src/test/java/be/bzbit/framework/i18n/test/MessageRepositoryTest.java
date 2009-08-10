package be.bzbit.framework.i18n.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import be.bzbit.framework.i18n.model.Message;
import be.bzbit.framework.i18n.repository.MessageRepository;
import be.bzbit.framework.i18n.test.mock.MockMessage;

@RunWith(SpringJUnit4ClassRunner.class)
//specifies the Spring configuration to load for this test fixture
@ContextConfiguration(locations =  {"classpath:context/test/test-persistence.xml"}
)
public class MessageRepositoryTest extends
		AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	private MessageRepository messageRepository;

	public void setMessageRepository(MessageRepository messageRepository) {
		this.messageRepository = messageRepository;
	}

	public MessageRepository getMessageRepository() {
		return messageRepository;
	}

	@Before
	public void setUp() {
		executeSqlScript("classpath:insert_messages.sql", false);
	}
	
	@Test
	@Transactional
	public void testSaveOrDeleteEmptyMessage() {
		assertEquals(1, countRowsInTable("I18N_MESSAGE"));
		Message message = new MockMessage();
		message.setMessage("");
		getMessageRepository().saveOrDelete(message);
		Message savedMessage = getMessageRepository().findById(message.getId());
		assertNull(savedMessage);
	}
	
}
