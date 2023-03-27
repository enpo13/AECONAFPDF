package com.aecon.utils.pdf;


import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.Matrix;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

public class PDFRenderImages implements RenderListener {
    final String name;
    int counter = 100000;
    Matrix matrix;
    String parameter;
    Rectangle rectangle;

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public PDFRenderImages(String name) {
        this.name = name;
    }

    public void beginTextBlock() {
    }

    public void renderText(TextRenderInfo renderInfo) {
        String text = renderInfo.getText();
        if (text.equals(parameter)) {
            this.rectangle = new Rectangle(renderInfo.getBaseline().getBoundingRectange().getBounds());
        }
    }

    public void endTextBlock() {
    }

    public void renderImage(ImageRenderInfo renderInfo) {
        this.matrix = renderInfo.getImageCTM();
    }


}
