package com.aecon;

import com.aecon.logic.overrided.CustomVerify;
import com.aecon.utils.ExcelParser;
import com.aecon.utils.pdf.PDFParser;
import com.aecon.utils.pdf.PdfTemplate;
import com.hp.lft.report.ReportException;
import com.hp.lft.verifications.Verify;
import org.testng.annotations.*;
import unittesting.UnitTestClassBase;

import java.io.IOException;
import java.lang.reflect.Method;

public class FDC_PDF_Test extends UnitTestClassBase {

    public String pdfForValidation = "C:/Users/v-poltoram/Documents/pdf/samples/1000_A22235G_0090944914_20220531_080529_EN.pdf";
    public String pdfIdeal = "Z:/UFT/QA/Inbound/Staging/SD Orders/Printed documents/CRQ72629/rez/TC01.01/1000_A22235G_0090938916_20220531_080604_EN.pdf";
    public String pdfFolder = "C:/Users/v-poltoram/Documents/pdf/samples";
    public String pdfExcelParameters = "Z:/UFT/QA/Inbound/Staging/Automation Framework/Excel/parametersPDF.xlsx";

    PdfTemplate pdfValidationTemplate, pdfIdealTemplate;

    @BeforeClass
    public void beforeClass() throws Exception {
        pdfIdealTemplate = new PdfTemplate(pdfIdeal, ExcelParser.getMapFromExcel(pdfExcelParameters));
        pdfValidationTemplate = new PdfTemplate(pdfForValidation, ExcelParser.getMapFromExcel(pdfExcelParameters));
    }

    @AfterClass
    public void afterClass() throws Exception {

    }

    @BeforeMethod
    public void beforeMethod(Method method) throws Exception {
    }

    @AfterMethod
    public void afterMethod() throws Exception {

    }
    @Test
    public void pdfSingleDocumentTest() throws IOException, ReportException {
        //logo
//        Verify.isTrue(PDFParser.getLogoCoordinates(pdfIdeal)
//                .equals(PDFParser.getLogoCoordinates(pdfForValidation)), "Verifying Logo placement for: " + pdfForValidation);
        //columns placement
        //pdfIdealTemplate.isColumnPlacementMatch(pdfValidationTemplate);
        //data validation

        pdfValidationTemplate.fieldsValidation(ExcelParser.getMapFromExcel(pdfExcelParameters), pdfForValidation);

        //fields placement
//        pdfIdealTemplate.mapParametersToHeaders(ExcelParser.getMapFromExcel(pdfExcelParameters));
//        pdfValidationTemplate.mapParametersToHeaders(ExcelParser.getMapFromExcel(pdfExcelParameters));
//        CustomVerify.verifyPDFColumnsHeadersBased(pdfIdealTemplate, pdfValidationTemplate);
    }


}
