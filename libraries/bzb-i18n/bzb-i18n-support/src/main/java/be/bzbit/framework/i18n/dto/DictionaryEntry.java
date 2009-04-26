package be.bzbit.framework.i18n.dto;

import java.io.Serializable;

import be.bzbit.framework.i18n.model.Message;

public class DictionaryEntry implements Serializable {
	private static final long serialVersionUID = -8063935160602486420L;
	private String messageCode;
	private String defaultMessage;
	private String translatedMessage;
	
	public DictionaryEntry(Message defaultMessage) {
		super();
		this.messageCode = defaultMessage.getCode();
		this.defaultMessage = defaultMessage.getMessage();
	}
	
	public String getMessageCode() {
		return messageCode;
	}
	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}
	public String getDefaultMessage() {
		return defaultMessage;
	}
	public void setDefaultMessage(String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}
	public String getTranslatedMessage() {
		return translatedMessage;
	}
	public void setTranslatedMessage(String translatedMessage) {
		this.translatedMessage = translatedMessage;
	}
	public void setTranslatedMessage(Message translatedMessage) {
		this.translatedMessage = translatedMessage.getMessage();
	}
}
