package com.aecon.utils.pdf;

import com.aecon.logic.BasePage;
import com.hp.lft.report.*;
import com.hp.lft.verifications.Verify;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PdfTemplate extends BasePage {

    HashMap<String, Rectangle> headersCoordinates = new HashMap<>();
    public String file;
    String[] headers = {"Information", "Bill-To-Party", "Ship-To-Party", "Comments", "Contract Summary",
            "Billings:", "Cumulative Billing:", "Previously Billed:"};
    public ArrayList<PDFLinePosition> linePositions;

    public PdfTemplate(String file, HashMap<String, String> parametersFile) {
        super();
        this.file = file;
        setHeadersCoordinates();
        linePositions = new ArrayList<>();
        mapParametersToHeaders(parametersFile);
    }

    private HashMap<String, Rectangle> setHeadersCoordinates() {
        PdfReader reader = null;
        try {
            reader = new PdfReader(file);
            PDFRenderImages listener = new PDFRenderImages(file);
            PdfReaderContentParser parser = new PdfReaderContentParser(reader);
            for (int i = 0; i < headers.length; i++) {
                listener.setParameter(headers[i]);
                for (int j = 1; j <= reader.getNumberOfPages(); j++) {
                    parser.processContent(j, listener);
                }
                headersCoordinates.put(headers[i], listener.rectangle);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return headersCoordinates;
    }

    public Rectangle getParameterCoordinates(String parameter) {
        PdfReader reader = null;
        Rectangle rectangle = null;
        try {
            reader = new PdfReader(file);
            PDFRenderImages listener = new PDFRenderImages(file);
            PdfReaderContentParser parser = new PdfReaderContentParser(reader);
            listener.setParameter(parameter);
            for (int j = 1; j <= reader.getNumberOfPages(); j++) {
                parser.processContent(j, listener);
                rectangle = listener.rectangle;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rectangle;
    }

    public void isColumnPlacementMatch(PdfTemplate secondTemplate) throws ReportException {
        VerificationData verificationData = new VerificationData();
        verificationData.name = "Columns Placement Verification for: " + secondTemplate.file;
        verificationData.verificationParameters.add(new VerificationParameter("Template document headers coordinates", this.headersCoordinates.toString()));
        verificationData.verificationParameters.add(new VerificationParameter("Tested document headers coordinates", secondTemplate.headersCoordinates.toString()));
        if (this.headersCoordinates.keySet().equals(secondTemplate.headersCoordinates.keySet())
                && areEqualKeyValues(this.headersCoordinates, secondTemplate.headersCoordinates)) {
            Reporter.reportVerification(Status.Passed, verificationData);
        } else {
            Reporter.reportVerification(Status.Failed, verificationData);
        }
    }

    private boolean areEqualKeyValues(Map<String, Rectangle> first, Map<String, Rectangle> second) {
        if (first.size() != second.size()) {
            return false;
        }
        int errorCounter = 0;
        for (Map.Entry<String, Rectangle> entry1 : first.entrySet()) {
            String key = entry1.getKey();
            Rectangle value1 = entry1.getValue();
            Rectangle value2 = second.get(key);
            if (value1.getTop() != value2.getTop()
                    && value1.getBottom() != value2.getBottom()
                    && value1.getLeft() != value2.getLeft()
                    && value1.getRight() != value2.getRight()) {
                errorCounter++;
            }
        }
        return errorCounter == 0;
    }

    public void fieldsValidation(HashMap<String, String> expected, String filePath) throws ReportException, IOException {
        startStep(expected.values().toArray(new String[0]),"Fields validation for "+filePath);
        expected.forEach((k, v) -> {
            try {
                Verify.areEqual(v, PDFParser.getValueByFieldName(filePath, k),
                        "Validating " + k + " for: " + filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        endStep();
    }

    public void mapParametersToHeaders(HashMap<String, String> parameters) {
        boolean isAbove = false;
        boolean isBelow = false;
        boolean isRight = false;
        boolean isLeft = false;
        boolean isBelongs = false;
        Set<String> keys = parameters.keySet();
        String[] parametersArray = keys.toArray(new String[keys.size()]);
        Set<String> headerskeys = headersCoordinates.keySet();
        String[] headersArray = headerskeys.toArray(new String[headerskeys.size()]);

        for (int i = 0; i < parametersArray.length; i++) {
            PDFLinePosition linePosition = new PDFLinePosition(parametersArray[i]);
            HashMap<String, HashMap<String, Boolean>> tempMap = new HashMap<>();
            for (int j = 0; j < headersArray.length; j++) {
                HashMap<String, Boolean> booleanMap = new HashMap<>();
                Rectangle lineRec = getParameterCoordinates(parametersArray[i]);
                if (lineRec == null) {
                    Verify.isTrue(lineRec != null, "Validating that Line: " + parametersArray[i] + " is present in the pdf");
                }
                assert lineRec != null;
                Rectangle headerRec = headersCoordinates.get(headersArray[j]);
                if (lineRec.getTop() > headerRec.getTop()) {
                    isAbove = true;
                } else {
                    isAbove = false;
                }
                if (lineRec.getBottom() < headerRec.getBottom()) {
                    isBelow = true;
                } else {
                    isBelow = false;
                }
                if (lineRec.getRight() > headerRec.getRight()) {
                    isRight = true;
                } else {
                    isRight = false;
                }
                if (lineRec.getLeft() < headerRec.getLeft()) {
                    isLeft = true;
                } else {
                    isLeft = false;
                }
                if (lineRec.getLeft() == headerRec.getLeft() && lineRec.getBottom() < headerRec.getBottom()) {
                    isBelongs = true;
                } else {
                    isBelongs = false;
                }
                booleanMap.put("isAbove", isAbove);
                booleanMap.put("isBellow", isBelow);
                booleanMap.put("isRight", isRight);
                booleanMap.put("isLeft", isLeft);
                booleanMap.put("belongsToColumn", isBelongs);
                tempMap.put(headersArray[j], booleanMap);
                linePosition.setPositions(tempMap);
            }
            this.linePositions.add(linePosition);
        }
    }

    @Override
    public BasePage ensurePageLoaded() throws InterruptedException, IOException {
        return null;
    }
}
