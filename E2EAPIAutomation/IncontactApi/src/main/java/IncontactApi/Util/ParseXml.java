package IncontactApi.Util;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ParseXml {

	/**
	 * Method to get count of nodes.
	 * 
	 * @param filePath
	 *            -> Path of the xml
	 * @param nodeName
	 *            -> name of the node
	 * @return number of nodes
	 */
	public int getNodeCount(String filePath, String nodeName) {
		int nodeCount = 0;
		try {
			File xmlFile = new File(filePath);

			DocumentBuilderFactory sourceFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder sourceBuilder = sourceFactory.newDocumentBuilder();

			Document sourceDocument = sourceBuilder.parse(xmlFile);

			sourceDocument.getDocumentElement().normalize();
			System.out.println("The root element is : " + sourceDocument.getDocumentElement().getNodeName());

			NodeList nodeList = sourceDocument.getElementsByTagName(nodeName);

			System.out.println("------------------------------------------------");
			nodeCount = nodeList.getLength();
		} catch (Exception e) {
			e.printStackTrace();
			return nodeCount;
		}
		return nodeCount;
	}

	/**
	 * Method to get node name or node value from xml.
	 * 
	 * @param filePath
	 *            -> path of the xml
	 * @param path
	 *            -> path of the node in xml
	 * @param isNode
	 *            -> true for node name and false for node value
	 * @return String -> Node name or Node value based on boolean isNode
	 */

	public String getNodeValue(String filePath, String path) {
		Node node = null;
		String result = "";

		try {
			File fXmlFile = new File(filePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();
			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile(path);

			NodeList n1 = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < n1.getLength(); i++) {
				node = n1.item(i);
				result = node.getTextContent();
				System.out.println("node value is : " + result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}

		return result;
	}

	public boolean nodeExists(String filePath, String path) {
		boolean exists = false;
		try {
			File fXmlFile = new File(filePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();
			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile(path);

			NodeList n1 = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			if (n1.getLength() > 0) {
				exists = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return exists;
		}

		return exists;
	}

	/**
	 * 
	 * @param filePath @param path @return
	 */
	public boolean checkNodeValueNull(String filePath, String path) {
		Boolean value = false;
		if (getNodeValue(filePath, path).equals(null)) {
			value = true;
		} else {
			value = false;
		}
		return value;
	}
	/*
	 * public static void main(String args[]){ ParseXml parse = new ParseXml();
	 * String filePath =
	 * "C:\\Users\\cagrawal\\Documents\\SmartBear\\Framework\\ResponseFiles\\testResponse.xml";
	 * System.out.println("Count of nodes is " + parse.getNodeCount(filePath,
	 * "ID")); String nodeValue=parse.getNodeValue(filePath,
	 * "/Response/e[2]/ID"); System.out.println(nodeValue); boolean
	 * result=parse.nodeExists(filePath, "Response/e/IDBook");
	 * System.out.println(result); //Assert.assertTrue(result); }
	 */
}
