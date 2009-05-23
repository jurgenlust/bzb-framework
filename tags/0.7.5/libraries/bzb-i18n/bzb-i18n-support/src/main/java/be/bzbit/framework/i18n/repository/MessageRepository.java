package be.bzbit.framework.i18n.repository;

import java.util.List;
import java.util.Locale;
import java.util.Properties;

import be.bzbit.framework.domain.repository.DomainObjectRepository;
import be.bzbit.framework.domain.repository.Sort;
import be.bzbit.framework.i18n.model.Message;

public interface MessageRepository extends DomainObjectRepository<Message> {
	Properties loadMessages(Locale locale);
	List<Message> getMessages(String language, String country);
	List<Message> getMessages(String language, String country, int firstResult, int maxResults, Sort sort);
	List<Message> getMessages(String language, String country, List<String> codes, Sort sort);
	int countMessages(String language, String country);
	List<Message> getMessages(String messageCode);
	void saveOrDelete(Message message);
    void deleteMessages(String messageCode);
    void deleteMessages(String language, String country);
    Message getMessage(String language, String country, String code);
}
