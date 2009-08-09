package be.bzbit.framework.i18n.test.mock;

import be.bzbit.framework.i18n.model.Message;

public class MockMessage extends Message {
	private static final long serialVersionUID = -9072854969979528543L;

	public MockMessage() {
		super();
		setId(new Long(1));
		setCountry("BE");
		setLanguage("nl");
		setMessage("Dit is testboodschap 1");
		setCode("testCode1");
	}
}
