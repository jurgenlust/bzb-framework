package be.bzbit.framework.i18n.test;

import javax.persistence.PersistenceException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import be.bzbit.framework.i18n.model.Language;
import be.bzbit.framework.i18n.repository.LanguageRepository;

@RunWith(SpringJUnit4ClassRunner.class)
//specifies the Spring configuration to load for this test fixture
@ContextConfiguration(locations =  {"classpath:context/test/test-persistence.xml"}
)
public class LanguageTest extends AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	private LanguageRepository repository;

	public LanguageRepository getRepository() {
		return repository;
	}

	public void setRepository(LanguageRepository repository) {
		this.repository = repository;
	}

	@Test(expected=PersistenceException.class)
	public void testNullLanguage() {
		Language language = new Language();
		getRepository().makePersistent(language);
	}
	
	@Test
	public void testEnglish() {
		Language language = new Language();
		language.setLanguageCode("en");
		getRepository().makePersistent(language);
	}

	@Test
	public void testUSEnglish() {
		Language language = new Language();
		language.setLanguageCode("en");
		language.setCountryCode("US");
		getRepository().makePersistent(language);
	}
	
	@Test(expected=PersistenceException.class)
	public void testDuplicateLanguage() {
		Language language1 = new Language();
		language1.setLanguageCode("nl");
		language1.setCountryCode("BE");
		getRepository().makePersistent(language1);
		Language language2 = new Language();
		language2.setLanguageCode("nl");
		language2.setCountryCode("BE");
		getRepository().makePersistent(language2);
	}
	
	@Test
	public void testNotDuplicateLanguage() {
		Language language1 = new Language();
		language1.setLanguageCode("en");
		language1.setCountryCode("US");
		getRepository().makePersistent(language1);
		Language language2 = new Language();
		language2.setLanguageCode("en");
		language2.setCountryCode("GB");
		getRepository().makePersistent(language2);
	}
}
