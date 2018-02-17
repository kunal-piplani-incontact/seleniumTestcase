package com.nice.incontact.dataLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nice.incontact.util.SeleniumLogger;

public class XmlParser {

    private static final SeleniumLogger log = new SeleniumLogger(XmlParser.class);
    private String incontactNode = null;
    Document currentDocument = null;  

    public XmlParser(String clusterName) {
        incontactNode = "CL_" + clusterName;
        log.debug("XmlParser", "Incontact Node   :    " + incontactNode);
    }

    /**
     * @param args
     *            the command line arguments
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map parseXML(String filePath, String xpath) {
        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory sourceFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder sourceBuilder = sourceFactory.newDocumentBuilder();
            Document sourceDocument = sourceBuilder.parse(xmlFile);
            
            sourceDocument = getDocumentWithValidNode(sourceDocument);
            currentDocument = sourceDocument;
            
            sourceDocument.getDocumentElement().normalize();
            log.debug("parseXML", "Root element :" + sourceDocument.getNodeName());

            NodeList resultNodeList = sourceDocument.getElementsByTagName(xpath);
            HashMap result = new HashMap();
            XmlParser.MyNodeList tempNodeList = new XmlParser.MyNodeList();

            String emptyNodeName = null, emptyNodeValue = null;

            for (int index = 0; index < resultNodeList.getLength(); index++) {
                Node tempNode = resultNodeList.item(index);
                if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                    tempNodeList.addNode(tempNode);
                }
                emptyNodeName = tempNode.getNodeName();
                emptyNodeValue = tempNode.getNodeValue();
            }

            if (tempNodeList.getLength() == 0 && emptyNodeName != null && emptyNodeValue != null) {
                result.put(emptyNodeName, emptyNodeValue);
                return result;
            }

            this.parseXMLNode(tempNodeList, result);
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    private Document getDocumentWithValidNode(Document document) throws Exception {
        NodeList validNodeList = document.getElementsByTagName(incontactNode);
        if (validNodeList != null && validNodeList.getLength() > 0 ) {
            Node rootMidNode = validNodeList.item(0);
            NodeList childNodeList = rootMidNode.getParentNode().getChildNodes();
            for (int i = 0; i < childNodeList.getLength(); i++) {
                Node childNode = childNodeList.item(i);
                String childNodeName = childNode.getNodeName();
                if (!childNodeName.equalsIgnoreCase(incontactNode) && !childNodeName.startsWith("#")) {
                    childNode.getParentNode().removeChild(childNode);
                }
            }
        } else {
            // TODO: If we need to throw the error in case of invalid CL node in XML, uncomment the below line with exception.
            //throw new Exception("Failed to retrieve the node ["+incontactNode+"] from the XML.");
            log.warn("getDocumentWithValidNode", "Failed to retrieve the node ["+incontactNode+"] from the XML. Using the existing node from XML.");
        }
        return document;
    }

    @SuppressWarnings({ "unchecked", "static-access", "rawtypes" })
    private void parseXMLNode(NodeList nList, HashMap result) {
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE && nNode.hasChildNodes() && nNode.getFirstChild() != null
                    && (nNode.getFirstChild().getNextSibling() != null || nNode.getFirstChild().hasChildNodes())) {
                NodeList childNodes = nNode.getChildNodes();
                XmlParser.MyNodeList tempNodeList = new XmlParser.MyNodeList();
                for (int index = 0; index < childNodes.getLength(); index++) {
                    Node tempNode = childNodes.item(index);
                    if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                        tempNodeList.addNode(tempNode);
                    }
                }
                HashMap counterHashMap = new HashMap();
                HashMap dataHashMap = new HashMap();
                if (result.containsKey(nNode.getNodeName()) && ((HashMap) result.get(nNode.getNodeName())).containsKey(0)) {
                    Map mapExisting = (Map) result.get(nNode.getNodeName());
                    Integer index = 0;
                    if (mapExisting.containsKey(0)) {
                        while (true) {
                            if (mapExisting.containsKey(index)) {
                                counterHashMap.put(index, mapExisting.get(index));
                                index++;
                            } else {
                                break;
                            }
                        }
                    } else {
                        result.put(nNode.getNodeName(), counterHashMap);
                        counterHashMap.put("0", mapExisting);
                        index = 1;
                    }
                    result.put(nNode.getNodeName(), counterHashMap);
                    counterHashMap.put(index, dataHashMap);
                } else if (result.containsKey(nNode.getNodeName())) {
                    counterHashMap.put(0, result.get(nNode.getNodeName()));
                    result.put(nNode.getNodeName(), counterHashMap);
                    counterHashMap.put(1, dataHashMap);
                } else {
                    result.put(nNode.getNodeName(), dataHashMap);
                }
                if (nNode.getAttributes().getLength() > 0) {
                    Map attributeMap = new HashMap();
                    for (int attributeCounter = 0; attributeCounter < nNode.getAttributes().getLength(); attributeCounter++) {
                        attributeMap.put(nNode.getAttributes().item(attributeCounter).getNodeName(),
                                nNode.getAttributes().item(attributeCounter).getNodeValue());
                    }
                    dataHashMap.put("__attributes", attributeMap);
                }
                this.parseXMLNode(tempNodeList, dataHashMap);
            } else if (nNode.getNodeType() == Node.ELEMENT_NODE && nNode.hasChildNodes() && nNode.getFirstChild() != null
                    && nNode.getFirstChild().getNextSibling() == null) {
                this.putValue(result, nNode);
            } else if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                this.putValue(result, nNode);
            }
        }
    }

    private Object getNodeValueFromXpathValue(String nodeValueXpath) {
        Object result = null;
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();
        try {
            XPathExpression expr = xpath.compile(nodeValueXpath + "/text()");
            result = expr.evaluate(currentDocument, XPathConstants.STRING);
            log.debug("getNodeValueFromXpathValue", "Evaluated expression   :   " + result);
            if (result == null) {
                result = "";
            }
        } catch (XPathExpressionException e) {
            log.warn("getNodeValueFromXpathValue", e.getMessage());
            log.warn("getNodeValueFromXpathValue", "Error occurred evaluating the Xpath. Setting the value as blank.");
            result = "";
        }
        return result;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void putValue(HashMap result, Node nNode) {
        HashMap attributeMap = new HashMap();
        Object nodeValue = null;
        if (nNode.getFirstChild() != null) {
            nodeValue = nNode.getFirstChild().getNodeValue();
            if (nodeValue != null) {
                nodeValue = nodeValue.toString().trim();
                if (nodeValue.toString().length() >= 2 && nodeValue.toString().substring(0, 2).equals("//")) {
                    String nodeValueXpath = nodeValue.toString();
                    log.debug("putValue", "Node value : " + nodeValueXpath);
                    if (nodeValueXpath.contains("\\")) {
                        nodeValueXpath = nodeValueXpath.replaceAll("\\\\", "/");
                        log.debug("putValue", "Found \\ in the node value. Replacing it with /.");
                    }
                    nodeValue = (Object) getNodeValueFromXpathValue(nodeValueXpath);
                }
            }
        } else {
            nodeValue = "";
        }
        HashMap nodeMap = new HashMap();
        nodeMap.put("value", nodeValue);
        Object putNode = nodeValue;
        if (nNode.getAttributes().getLength() > 0) {
            for (int attributeCounter = 0; attributeCounter < nNode.getAttributes().getLength(); attributeCounter++) {
                attributeMap.put(nNode.getAttributes().item(attributeCounter).getNodeName(), nNode.getAttributes().item(attributeCounter)
                        .getNodeValue());
            }
            nodeMap.put("__attributes", attributeMap);
            putNode = nodeMap;
        }
        HashMap counterHashMap = new HashMap();
        HashMap dataHashMap = new HashMap();
        if (result.containsKey(nNode.getNodeName()) && result.get(nNode.getNodeName()) instanceof HashMap
                && ((HashMap) result.get(nNode.getNodeName())).containsKey(0)) {
            Map mapExisting = (Map) result.get(nNode.getNodeName());
            Integer index = 0;
            if (mapExisting.containsKey(0)) {
                while (true) {
                    if (mapExisting.containsKey(index)) {
                        counterHashMap.put(index, mapExisting.get(index));
                        index++;
                    } else {
                        break;
                    }
                }
            } else {
                index = 1;
            }
            counterHashMap.put(index, putNode);
            result.put(nNode.getNodeName(), counterHashMap);
        } else if (result.containsKey(nNode.getNodeName())) {
            Object existingObject = result.get(nNode.getNodeName());
            result.put(nNode.getNodeName(), dataHashMap);
            dataHashMap.put(0, existingObject);
            dataHashMap.put(1, putNode);
        } else {
            result.put(nNode.getNodeName(), putNode);
        }
    }

    static class MyNodeList implements NodeList {
        List<Node> nodes = new ArrayList<Node>();
        int length = 0;

        public MyNodeList() {
        }

        public void addNode(Node node) {
            nodes.add(node);
            length++;
        }

        @Override
        public Node item(int index) {
            try {
                return nodes.get(index);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        public int getLength() {
            return length;
        }
    }

}
