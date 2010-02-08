package be.bzbit.framework.i18n.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import be.bzbit.framework.i18n.model.Message;

public class Translation implements Serializable {
	private static final long serialVersionUID = -7997728180541660067L;

	private String messageCode;

	private Message defaultMessage;
	private Message[] messages;

	public Translation() {
		super();
	}

	public Translation(String messageCode) {
		super();
		this.messageCode = messageCode;
	}

	public Translation(String messageCode, List<Locale> locales, List<Message> messages) {
		super();
		fill(messageCode, locales, messages);
	}
	

	public String getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	public Message[] getMessages() {
		return messages;
	}

	public void setMessages(Message[] messages) {
		this.messages = messages;
	}

	public void fill(String messageCode, List<Locale> locales, List<Message> messages) {
		this.messageCode = messageCode;
		this.messages = new Message[locales.size()];
		Map<String, Message> messageMap = new HashMap<String, Message>();
		for (Message message : messages) {
			String localeCode = message.getLocaleCode();
			//ignore messages with a different code
			if (!message.getCode().equals(messageCode)) continue;
			//language and country are null, so this becomes the default
			if (localeCode == null) this.defaultMessage = message;
			else messageMap.put(message.getLocaleCode(), message);
		}
		//make sure we have a defaultMessage
		if (this.defaultMessage == null) {
			this.defaultMessage = new Message();
		}
		//we want a message for every available locale.
		//When none was supplied, create a new one
		int i = 0; 
		for (Locale locale : locales) {
			Message message = messageMap.get(locale.toString());
			if (message == null) message = new Message();
			message.setLanguage(locale.getLanguage());
			message.setCountry(locale.getCountry());
			message.setCode(this.messageCode);
			this.messages[i++] = message;
		}
	}

	public void setDefaultMessage(Message defaultMessage) {
		this.defaultMessage = defaultMessage;
	}

	public Message getDefaultMessage() {
		return defaultMessage;
	}
}
