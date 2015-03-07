package com.crypto.helpers;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.*;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;

/**
 * Invokes the method of SOAP service.
 */
public final class SoapHelper {
    private final static String NAMESPACE_FIELD = "targetNamespace";
    private final static String DEFINITIONS_FIELD = "wsdl:definitions";
    private final static String REQUEST_PREFIX = "cry";
    private final static String RESPONSE_PREFIX = "cs";
    private final static String WSDL_SUFFIX = "?wsdl";
    private final static String RESPONSE_SUFFIX = "Response";

    private final static String INVALID_FORMAT_ERROR = "Invalid format of wsdl file is used";

    public static String invokeMethod(String url, String methodName, String data) throws Exception {
        String namespace = getNamespace(url);

        SOAPConnectionFactory factory = SOAPConnectionFactory.newInstance();
        SOAPConnection connection = factory.createConnection();

        try {
            SOAPMessage response = connection.call(createRequestMessage(namespace, methodName, data), url);
            return getResponseData(response.getSOAPBody(), namespace, methodName);
        } finally {
            connection.close();
        }
    }

    private static String getNamespace(String address) throws Exception {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document;
        try(InputStream stream = new URL(address + WSDL_SUFFIX).openStream()) {
            document = builder.parse(stream);
        }

        document.getDocumentElement().normalize();

        Node definitionsNode = getDefinitionsNode(document);
        if (definitionsNode == null) {
            throw new Exception(INVALID_FORMAT_ERROR);
        }

        Node namespaceNode = definitionsNode.getAttributes().getNamedItem(NAMESPACE_FIELD);
        if (namespaceNode == null || namespaceNode.getNodeValue() == null) {
            throw new Exception(INVALID_FORMAT_ERROR);
        }

        return namespaceNode.getNodeValue();
    }

    private static Node getDefinitionsNode(Document document) {
        NodeList list = document.getChildNodes();
        for (int i = 0; i < list.getLength(); ++i) {
            org.w3c.dom.Node node = list.item(i);
            if (node.getNodeName().equals(DEFINITIONS_FIELD)) {
                return node;
            }
        }

        return null;
    }

    private static SOAPMessage createRequestMessage(String namespace, String methodName, String data) throws Exception {
        SOAPMessage request = MessageFactory.newInstance().createMessage();
        SOAPBody body = request.getSOAPBody();

        SOAPBodyElement bodyElement = body.addBodyElement(new QName(namespace, methodName, REQUEST_PREFIX));
        bodyElement.addTextNode(data);

        request.saveChanges();
        request.writeTo(System.out);
        return request;
    }

    private static String getResponseData(SOAPBody body, String namespace, String methodName) throws Exception {
        Iterator iterator = body.getChildElements(new QName(namespace, methodName + RESPONSE_SUFFIX, RESPONSE_PREFIX));

        while (iterator.hasNext()) {
            SOAPBodyElement responseElement = (SOAPBodyElement) iterator.next();
            return responseElement.getTextContent();
        }

        throw new Exception(INVALID_FORMAT_ERROR);
    }
}