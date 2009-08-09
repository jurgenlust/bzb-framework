/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.bzbit.framework.pdf.build;

import java.io.OutputStream;

import be.bzbit.framework.pdf.exception.PdfBuilderException;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;

/**
 *
 * @author jlust
 */
public abstract class AbstractPdfBuilder {

    public void build() {
        try {
            Document document = new Document(getPageSize());
            PdfWriter writer = PdfWriter.getInstance(document, getPdfOutputStream());
            document.open();
            generatePdf(document, writer);
            document.close();
        } catch (Exception ex) {
            throw new PdfBuilderException(ex);
        }
    }
    
    protected abstract void generatePdf(Document document, PdfWriter writer) throws Exception;

    protected abstract OutputStream getPdfOutputStream();
    
    protected Rectangle getPageSize() {
    	return PageSize.A4;
    }
}
