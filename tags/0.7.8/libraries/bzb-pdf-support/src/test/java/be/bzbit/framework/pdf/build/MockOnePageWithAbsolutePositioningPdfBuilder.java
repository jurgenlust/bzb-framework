package be.bzbit.framework.pdf.build;

import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import be.bzbit.framework.pdf.render.PdfFilePrinter;

import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfContentByte;

public class MockOnePageWithAbsolutePositioningPdfBuilder extends
		AbstractOnePageWithAbsolutePositioningPdfBuilder {

	@Override
	protected void generatePdf(PdfContentByte contentByte) throws Exception {
		drawText(contentByte, "Hallo", 23, 214, 100, 6, Font.NORMAL, Phrase.ALIGN_LEFT);
	}

	@Override
	protected OutputStream getPdfOutputStream() {
		try {
			return new FileOutputStream(getPdfFile());
		} catch (FileNotFoundException e) {
			return null;
		}
	}
	
	public File getPdfFile() {
		return new File("test.pdf");
	}
	
	public void print() throws IOException, PrinterException {
		PdfFilePrinter printer = new PdfFilePrinter(getPdfFile());
		printer.print();
	}

}
