/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.bzbit.framework.pdf.build;

import be.bzbit.framework.pdf.build.AbstractPdfBuilder;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.IOException;

/**
 *
 * @author jlust
 */
public abstract class AbstractOnePageWithAbsolutePositioningPdfBuilder extends AbstractPdfBuilder {
    private static final float MM = 72f / 25.4f;
    private static BaseFont baseFont = null;
    private float verticalAdjustment = 0f;
    private float horizontalAdjustment = 0f;

    public float getHorizontalAdjustment() {
        return horizontalAdjustment;
    }

    public void setHorizontalAdjustment(float horizontalAdjustment) {
        this.horizontalAdjustment = horizontalAdjustment;
    }

    public float getVerticalAdjustment() {
        return verticalAdjustment;
    }

    public void setVerticalAdjustment(float verticalAdjustment) {
        this.verticalAdjustment = verticalAdjustment;
    }

    protected Font getFont(final int fontStyle)
            throws DocumentException, IOException {
        return new Font(getBaseFont(), 11, fontStyle, Color.BLACK);
    }

    protected BaseFont getBaseFont()
            throws DocumentException, IOException {
        if (baseFont == null) {
            baseFont = BaseFont.createFont(
                    BaseFont.COURIER, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
        }

        return baseFont;
    }
    
    protected void drawText(
            final PdfContentByte cb,
            final String text,
            final float theX,
            final float theY,
            final float width,
            final float height
    		) throws DocumentException, IOException {
    	drawText(cb, text, theX, theY, width, height, Font.NORMAL, Phrase.ALIGN_LEFT);
    }

    protected void drawText(
            final PdfContentByte cb,
            final String text,
            final float theX,
            final float theY,
            final float width,
            final float height,
            final int fontStyle,
            final int alignment)
            throws DocumentException, IOException {
        ColumnText columnText = new ColumnText(cb);
        Phrase phrase = new Phrase(text, getFont(fontStyle));
        float x = theX + getHorizontalAdjustment();
        float y = theY + getVerticalAdjustment();

        columnText.setSimpleColumn(
                x * MM, y * MM, (x * MM) + (width * MM), (y * MM) + (height * MM),
                11, alignment);
        columnText.addText(phrase);
        columnText.go();
    }

    protected Rectangle drawImage(
            final PdfContentByte cb,
            final Image image,
            final float theX,
            final float theY,
            final float width,
            final float height)
            throws IOException, DocumentException {

        image.scaleToFit(width * MM, height * MM);
        
        float scaledWidth = image.getScaledWidth();
        float scaledHeight = image.getScaledHeight();

        float x = (theX + horizontalAdjustment) * MM;
        float y = (theY + verticalAdjustment) * MM;
        Rectangle rectangle =
                new Rectangle(x, y, x + scaledWidth, y + scaledHeight);
        cb.addImage(image, scaledWidth, 0, 0, scaledHeight, x, y);

        return rectangle;
    }

    @Override
    protected void generatePdf(Document document, PdfWriter writer) throws Exception {
        PdfContentByte cb = writer.getDirectContent();
        generatePdf(cb);
    }
    
    protected abstract void generatePdf(PdfContentByte contentByte) throws Exception; 


}
