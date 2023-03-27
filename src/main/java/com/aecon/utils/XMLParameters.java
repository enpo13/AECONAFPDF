package com.aecon.utils;

import com.aecon.logic.overrided.DataProviders;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.hp.lft.verifications.Verify;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeMap;

public class XMLParameters {

    public static Document init() {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document document = null;
        try {
            document = documentBuilder.parse(Consts.PARAMETER_FILE_XML);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return document;
    }

    public static void save(Document document) {
        DOMSource source = new DOMSource(document);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        StreamResult result = new StreamResult(Consts.PARAMETER_FILE_XML);
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public static void writeParameter(String[] parameterNames, String[] parameterValues) {
        Document document = init();
        Element root = document.getDocumentElement();
        TreeMap<String, String> map = new TreeMap<>();
        Verify.areEqual(parameterNames.length, parameterValues.length, "Validated that number of names and number of parameters match");
        Iterator namesI = Arrays.stream(parameterNames).iterator();
        Iterator valuesI = Arrays.stream(parameterValues).iterator();
        while (namesI.hasNext() && valuesI.hasNext()) {
            String updatedName;
            updatedName = namesI.next().toString().replaceAll("IP_", "");
            map.put(updatedName, (String) valuesI.next());
        }
        map.forEach((i, j) -> {
            if (document.getElementsByTagName(i).getLength() == 0) {
                Element element = document.createElement(i);
                element.appendChild(document.createTextNode(j));
                root.appendChild(element);
            } else if (!document.getElementsByTagName(i).item(0).getTextContent().equals(j)) {
                document.getElementsByTagName(i).item(0).setTextContent(j);
            }
        });
        if (root.getFirstChild().getTextContent().length() == 0)
            root.removeChild(root.getFirstChild());
        save(document);
    }

    public static TreeMap<String, Object> readParameter(String[] parametersNames) {
        TreeMap<String, Object> map = new TreeMap<>();
        Document document = init();
        Element root = document.getDocumentElement();
        Arrays.stream(parametersNames).forEachOrdered(i -> map.put(i, document.getElementsByTagName(i).item(0).getTextContent()));
        return map;
    }

    public static void main(String[] args) throws JsonProcessingException, FileNotFoundException {
        String[] a = DataProviders.getInputParametersNames();
        System.out.println();
    }

}
