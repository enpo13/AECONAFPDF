package com.aecon.utils.pdf;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.Matrix;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.IOException;

public class PDFParser {

    private static PDFRenderImages extractImage(String file) throws IOException {
        PdfReader reader = new PdfReader(file);
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        PDFRenderImages listener = new PDFRenderImages(file);
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            parser.processContent(i, listener);
        }
        return listener;
    }

    public static void getTable(String file, String parameter) throws IOException {
        PdfReader reader = new PdfReader(file);
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        PDFRenderImages listener = new PDFRenderImages(file);
        listener.setParameter(parameter);
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            parser.processContent(i, listener);
        }
    }

    public static Matrix getLogoCoordinates(String file) throws IOException {
        return extractImage(file).matrix;
    }

    public static String getValueByFieldName(String file, String parameter) throws IOException {
        PdfReader reader = new PdfReader(file);
        String resultIntermediate = "";
        String result = "";
        String page = PdfTextExtractor.getTextFromPage(reader, 1);
        String[] lines = page.split("\n");
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        PDFRenderImages listener = new PDFRenderImages(file);
        parser.processContent(1, listener);
        for (String line : lines)
            if (line.contains(parameter)) {
                listener.setParameter(line);
                resultIntermediate = line.replaceAll(parameter, "").replaceFirst(" ", "");
                result = resultIntermediate.replaceAll("[:+]*", "");
            }
        return result;
    }

    public static boolean isTextPresent(String file, String text) throws IOException {
        PdfReader reader = new PdfReader(file);
        String page = PdfTextExtractor.getTextFromPage(reader, 1);
        return page.contains(text);

    }
}
