package both.common.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class XmlUtil {
	private static String defaultEncoding = "GBK";

	public static Document getDocument(String filePath, String fileName)
			throws Exception {
		return getDocument(filePath + File.separator + fileName);
	}

	public static Document getDocument(String fileName) throws Exception {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(fileName);
			return getDocument(fis);
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if(fis != null)
					fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static Document getDocument(InputStream is) throws Exception {
		SAXReader reader = new SAXReader();

		Document document = reader.read(is);
		return document;

	}

	public static Document getDocument(InputStream is, String encoding)
			throws Exception {
		SAXReader reader = new SAXReader();

		reader.setEncoding(encoding);
		Document document = reader.read(is);
		return document;

	}

	public static Element getElementByName(Element e, String name) {
		Iterator<Element> iterator = e.elementIterator(name);
		if (iterator.hasNext()) {
			return (Element) iterator.next();
		} else {
			return null;
		}
	}

	public static List<Element> getElementsByName(Element e, String name) {
		return e.elements(name);
	}

	public static Element getElementByNameAndAttribute(Element e, String name,
			String attribute, String value) {
		for (Iterator<Element> iterator = e.elementIterator(name); iterator.hasNext();) {
			Element element = (Element) iterator.next();
			if (value.equals(element.attribute(attribute).getValue())) {
				return element;
			}
		}

		return null;
	}

	public static String getElementValueByName(Element e, String name) {
		Iterator<Element> iterator = e.elementIterator(name);
		if (iterator.hasNext()) {
			return ((Element) iterator.next()).getText();
		} else {
			return "";
		}
	}

	public static String getElementAttributeByName(Element e, String name,
			String attribute) {
		Iterator<Element> iterator = e.elementIterator(name);
		if (iterator.hasNext()) {
			return ((Element) iterator.next()).attribute(attribute).getValue();
		} else {
			return "";
		}
	}

	public static String[] getElementValuesByName(Element e, String name) {
		Iterator<Element> iterator = e.elementIterator(name);
		StringBuffer buffer = new StringBuffer();
		while (iterator.hasNext()) {
			if ("".equals(buffer.toString())) {
				buffer.append(((Element) iterator.next()).getText());
			} else {
				buffer.append(((Element) iterator.next()).getText() + ",");
			}
		}
		return buffer.toString().split(",");
	}

	public static Document createDocument() {
		Document document = DocumentHelper.createDocument();
		return document;
	}

	public static Element createRootElement(Document document, String rootName) {
		if (document != null) {
			return document.addElement(rootName);
		} else {
			return null;
		}
	}

	public static Element createElement(Element pElement, String elementName,
			String elmentText) {
		if (pElement != null) {
			Element nElement = pElement.addElement(elementName);
			if (elmentText != null) {
				nElement.addText(elmentText);
			}
			return nElement;
		} else {
			return null;
		}
	}

	public static void saveSocketXml(Document document, OutputStream os) {
		saveSocketXml(document.getRootElement(), os);
	}

	public static void saveSocketXml(Element e, OutputStream os) {

		DataOutputStream dos = new DataOutputStream(os);
		try {
			int len = e.asXML().getBytes("GBK").length;
			dos.writeInt(len);
			dos.write(e.asXML().getBytes("GBK"));
			dos.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public static Document parseDocumentText(String xmlStr)
			throws DocumentException {
		Document doc = DocumentHelper.parseText(xmlStr);
		return doc;
	}

	public static Element parseElementText(String xmlStr)
			throws DocumentException {
		Document doc = DocumentHelper
				.parseText("<?xml version=\"1.0\" encoding=\"GBK\"?>\n"
						+ xmlStr);
		return doc.getRootElement();
	}

	public static byte[] readByte(DataInputStream is, int len)
			throws IOException {
		byte[] b = new byte[len];
		is.readFully(b);
		return b;
	}

	public static void saveXml(Document document, OutputStream os,
			String encoding) {

		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding(encoding);
		XMLWriter output = null;
		try {
			output = new XMLWriter(os, format);
			output.write(document);
			output.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void saveXml(Document document, OutputStream os) {

		saveXml(document, os, defaultEncoding);

	}

	public static void saveXml(Element element, OutputStream os) {
		Document document = DocumentHelper.createDocument();
		document.add(element);
		saveXml(document, os, defaultEncoding);
	}

}
