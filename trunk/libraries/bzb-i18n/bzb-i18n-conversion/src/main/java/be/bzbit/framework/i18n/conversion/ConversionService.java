package be.bzbit.framework.i18n.conversion;

import java.io.InputStream;
import java.io.OutputStream;

public interface ConversionService {
	void exportTranslations(OutputStream outputStream);
	void importTranslations(InputStream inputStream);
}
