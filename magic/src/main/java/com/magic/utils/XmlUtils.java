package com.magic.utils;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringBufferInputStream;

/**
 * Created by xm on 2018/2/5.
 */
public class XmlUtils {

    //Load and parse XML file into DOM
    public static Document parse(String xmlContent) {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        Document document = null;
        try {
            //DOM parser instance
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            //parse an XML file into a DOM tree
            document = builder.parse(new StringBufferInputStream(xmlContent));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return document;
    }

    public static Document create() {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        Document document = null;
        try {
            //DOM parser instance
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            //parse an XML file into a DOM tree
            document = builder.newDocument();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return document;
    }

    public static String toXml(Document doc) {
       return doc.toString();
    }
}
