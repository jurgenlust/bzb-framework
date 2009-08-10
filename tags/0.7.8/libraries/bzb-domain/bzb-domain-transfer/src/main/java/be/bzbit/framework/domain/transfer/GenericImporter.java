package be.bzbit.framework.domain.transfer;

import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;

public interface GenericImporter<T, ID extends Serializable> {
	void importEntities(InputStream inputStream);
	void importEntities(Reader reader);
}
