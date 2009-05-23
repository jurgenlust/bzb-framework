package be.bzbit.framework.i18n.test;

import javax.persistence.PersistenceException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import be.bzbit.framework.i18n.model.Message;
import be.bzbit.framework.i18n.repository.MessageRepository;


@RunWith(SpringJUnit4ClassRunner.class)
//specifies the Spring configuration to load for this test fixture
@ContextConfiguration(locations =  {"classpath:context/test/test-persistence.xml"}
)
public class MessageTest extends AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	private MessageRepository repository;

	public MessageRepository getRepository() {
		return repository;
	}

	public void setRepository(MessageRepository repository) {
		this.repository = repository;
	}

	@Test(expected=PersistenceException.class)
	public void testEmptyMessage() {
		Message message = new Message();
		getRepository().makePersistent(message);
	}
	
	@Test
	public void testDefaultMessage() {
		Message message = new Message();
		message.setCode("test.message");
		message.setMessage("This is a test message");
		getRepository().makePersistent(message);
	}
	
	@Test
	public void testNotUniqueMessage() {
		Message message1 = new Message();
		message1.setLanguage("en");
		message1.setCountry("US");
		message1.setCode("test.message");
		message1.setMessage("This is the first test message");
		getRepository().save(message1);
		Message message2 = new Message();
		message2.setLanguage("en");
		message2.setCountry("US");
		message2.setCode("test.message");
		message2.setMessage("This is the second test message");
		getRepository().save(message2);
	}
	
	@Test
	public void testUniqueMessage() {
		Message message1 = new Message();
		message1.setLanguage("nl");
		message1.setCountry("BE");
		message1.setCode("test.message");
		message1.setMessage("This is the first test message");
		getRepository().save(message1);
		Message message2 = new Message();
		message2.setLanguage("nl");
		message2.setCode("test.message");
		message2.setMessage("This is the second test message");
		getRepository().save(message2);
	}

}
