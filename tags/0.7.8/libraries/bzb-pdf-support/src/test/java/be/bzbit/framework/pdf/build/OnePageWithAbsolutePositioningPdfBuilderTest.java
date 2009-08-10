package be.bzbit.framework.pdf.build;

import java.awt.print.PrinterException;
import java.io.IOException;

import org.junit.Test;

public class OnePageWithAbsolutePositioningPdfBuilderTest {

	@Test
	public void testBuild() throws IOException, PrinterException {
		MockOnePageWithAbsolutePositioningPdfBuilder builder = new MockOnePageWithAbsolutePositioningPdfBuilder();
		builder.build();
		builder.print();
	}

}
