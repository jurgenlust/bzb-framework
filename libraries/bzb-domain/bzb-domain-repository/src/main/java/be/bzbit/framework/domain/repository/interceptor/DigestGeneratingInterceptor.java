package be.bzbit.framework.domain.repository.interceptor;

import java.security.Key;
import java.security.KeyStore;
import java.util.Collection;
import java.util.SortedMap;
import java.util.Map.Entry;

import javax.crypto.Mac;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.bzbit.framework.domain.model.Digestable;

/**
 * Interceptor that generates a message digest for a number of properties on the
 * entity. Inspired by http://www.hibernate.org/164.html
 * 
 * @author jlust
 * @author $LastChangedBy$
 * 
 * @version $LastChangedRevision$
 */
public class DigestGeneratingInterceptor<T> extends AbstractInterceptor<T> {
	private static final Log log = LogFactory
			.getLog(DigestGeneratingInterceptor.class);

	private KeyStore keystore;
	private String keystorePassword;
	private String keyAlias;
	private boolean debug = false;
	private boolean checkOnLoad = true;
	private boolean exceptionOnLoad = false;

	public DigestGeneratingInterceptor(Class<T> persistentClass) {
		super(persistentClass);
	}

	/**
	 * After loading a Digestable entity, make sure the message digest is
	 * correct.
	 * 
	 * @see be.bzbit.framework.domain.repository.interceptor.AbstractInterceptor#postLoad(java.util.Collection)
	 */
	@Override
	public void postLoad(Collection<T> entities) {
		if (!checkOnLoad) {
			return;
		}
		debug("Checking digest for collection of entities of type %s",
				getPersistentClass().getName());
		for (T entity : entities) {
			checkDigest(entity);
		}
	}

	public boolean isExceptionOnLoad() {
		return exceptionOnLoad;
	}

	public void setExceptionOnLoad(boolean exceptionOnLoad) {
		this.exceptionOnLoad = exceptionOnLoad;
	}

	private void checkDigest(T entity) {
		if (entity instanceof Digestable) {
			debug("Entity is Digestable. Checking digest...");
			Digestable digestable = (Digestable) entity;
			debug("Generating digest with input: %s", digestable
					.getDigestInput());
			String digest = generateDigest(entity);
			debug("Generated digest %s", digest);
			debug("Entity has digest %s", ((Digestable) entity).getDigest());
			if (digest.equals(digestable.getDigest())) {
				digestable.setDigestValid(true);
			} else {
				debug("Generated digest does not match digest in entity!");
				if (exceptionOnLoad) {
					throw new InvalidDigestException();
				} else {
					digestable.setDigestValid(false);
				}
			}
		} else {
			debug("Entity is not Digestable. Ignoring...");
			return;
		}
	}

	/**
	 * After loading a Digestable entity, make sure the message digest is
	 * correct.
	 * 
	 * @see be.bzbit.framework.domain.repository.interceptor.AbstractInterceptor#postLoad(java.lang.Object)
	 */
	@Override
	public void postLoad(T entity) {
		if (!checkOnLoad) {
			return;
		}
		debug("Checking digest for entity of type %s", getPersistentClass()
				.getName());
		checkDigest(entity);
	}

	/**
	 * Before saving a Digestable entity, generate a message digest for it
	 * 
	 * @see be.bzbit.framework.domain.repository.interceptor.AbstractInterceptor#preSave(java.lang.Object)
	 */
	@Override
	public void preSave(T entity) {
		if (entity instanceof Digestable) {
			debug("Generating digest for entity of type %s",
					getPersistentClass().getName());
			Digestable digestable = (Digestable) entity;
			digestable.setDigest(generateDigest(entity));
		}
	}

	private String generateDigest(T entity) {
		try {
			if (entity instanceof Digestable) {
				debug("Entity is Digestable. Generating digest...");
				Digestable digestable = (Digestable) entity;
				debug("Generating digest with input: %s", digestable
						.getDigestInput());
				Key key = getKeystore().getKey(keyAlias,
						keystorePassword.toCharArray());
				Mac mac = Mac.getInstance(key.getAlgorithm());
				mac.init(key);
				final String digest = new String(Base64.encodeBase64(mac
						.doFinal(getDigestInput(digestable))));
				debug("Generated digest %s", digest);
				return digest;
			} else {
				debug("Entity is not Digestable. Ignoring...");
				return null;
			}

		} catch (Exception e) {
			log.error(e);
			throw new DigestFailureException(e);
		}
	}
	
	private byte[] getDigestInput(Digestable digestable) {
		SortedMap<String,String> inputMap = digestable.getDigestInput();
		StringBuilder inputBuilder = new StringBuilder();
		for (Entry<String,String> entry : inputMap.entrySet()) {
			inputBuilder.append("[");
			inputBuilder.append(entry.getKey());
			inputBuilder.append("=");
			inputBuilder.append(entry.getValue());
			inputBuilder.append("]");
		}
		return inputBuilder.toString().getBytes();
	}

	public String getKeystorePassword() {
		return keystorePassword;
	}

	public void setKeystorePassword(String keystorePassword) {
		this.keystorePassword = keystorePassword;
	}

	public String getKeyAlias() {
		return keyAlias;
	}

	public void setKeyAlias(String keyAlias) {
		this.keyAlias = keyAlias;
	}

	public KeyStore getKeystore() {
		return keystore;
	}

	public void setKeystore(KeyStore keystore) {
		this.keystore = keystore;
	}

	protected void debug(String message, Object... parameters) {
		if (!debug)
			return;
		log.debug(String.format(message, parameters));
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public boolean isDebug() {
		return debug;
	}

	public boolean isCheckOnLoad() {
		return checkOnLoad;
	}

	public void setCheckOnLoad(boolean checkOnLoad) {
		this.checkOnLoad = checkOnLoad;
	}

}
