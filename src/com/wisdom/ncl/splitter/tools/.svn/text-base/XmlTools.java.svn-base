package com.wisdom.ncl.splitter.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * @Description xml文件操作的工具类
 * @author yuhao
 * 
 */
public class XmlTools {
	/**
	 * 创建文档 并赋值根节点元素名称
	 * 
	 * @param rootElementName
	 * @return Document
	 */
	public Document createDocument(String rootElementName) {
		Document document = DocumentHelper.createDocument();
		document.addElement(rootElementName);
		return document;
	}

	/**
	 * 根据文件名获取document
	 * 
	 * @param file
	 * @return Document
	 */
	public Document getDocument(File file) {
		Document document = null;
		if (file != null && !"".equals(file)) {
			if (file.getName().endsWith(".xml")) {
				SAXReader saxReader = new SAXReader();
				try {
					document = saxReader.read(file);
				} catch (DocumentException e) {
					System.out.println("对不起" + file.getName() + "不正确.");
				}
			} else {
				System.out.println("对不起" + file.getName() + "不是一个xml文件。");
			}
		}
		return document;
	}

	/**
	 * 根据字符串转化成document
	 * 
	 * @param srcXml
	 * @return Document
	 */
	public Document getDocumentByString(String srcXml) {
		Document document = null;
		if (srcXml != null && !"".equals(srcXml)) {
			try {
				document = DocumentHelper.parseText(srcXml);
			} catch (DocumentException e) {
				System.out
						.println("XmlTools类的getDocumentByString()方法的参数srcXml："
								+ srcXml + " 的格式不正确");
				// e.printStackTrace();
			}
		}
		return document;
	}

	/**
	 * 根据文件路径 得到Document 其中filePath是从工程根路径开始的相对路径 如：com/ow/xml/school.xml
	 * 
	 * @param filePath
	 * @return Document
	 */
	public Document getDocumentByPath(String filePath) {
		if (filePath != null && !"".equals(filePath)) {
			SAXReader saxReader = new SAXReader();
			// web工程加载相对路径
			InputStream in = ClassLoader.getSystemResourceAsStream(filePath);
			Document document = null;
			try {
				document = saxReader.read(in);
			} catch (DocumentException e) {
				System.out.println("getDocumentByPath() 方法的参数filePath的路径:"
						+ filePath + "不正确。");
			}
			return document;
		} else {
			return null;
		}
	}

	/**
	 * 得到根元素
	 * 
	 * @param document
	 * @return Element
	 */
	public Element getRootElement(Document document) {
		if (document == null) {
			return null;
		}
		return document.getRootElement();
	}

	/**
	 * 得到某元素的所有子元素
	 * 
	 * @param element
	 * @return List
	 */
	public List getAllElements(Element element) {
		if (element == null) {
			return null;
		}
		return element.elements();
	}

	/**
	 * 得到某Element下面某确定名称的元素的集合
	 * 
	 * @param element
	 * @param elementName
	 * @return List
	 */
	public List getAllElementsByName(Element element, String elementName) {
		if (element != null && elementName != null && !"".equals(elementName)) {
			// 判断元素是否含有此元素节点
			if (isContainElementByName(element, elementName)) {
				return element.elements(elementName);
			} else {
				System.out.println(element.getName() + "不含有" + elementName);
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * 得到某Element下面某个确定名称的元素
	 * 
	 * @param element
	 * @param elementName
	 * @return 第一个符合标准的元素节点
	 */
	public Element getElementByName(Element element, String elementName) {
		if (element != null && elementName != null && !"".equals(elementName)) {
			// 判断元素是否含有此元素节点
			if (isContainElementByName(element, elementName)) {
				return element.element(elementName);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * 获取某element的所有属性
	 * 
	 * @param element
	 * @return
	 */
	public List getAttributes(Element element) {
		if (element == null) {
			return null;
		}
		return element.attributes();
	}

	/**
	 * 获取某属性的值
	 * 
	 * @param attributeName
	 * @param element
	 * @return
	 */
	public String getAttributeValue(String attributeName, Element element) {
		if (element != null && attributeName != null
				&& !"".equals(attributeName)) {
			if (isElementAttribute(element, attributeName)) {
				return element.attributeValue(attributeName);
			} else {
				System.out.println("元素" + element.getName() + "不含有属性"
						+ attributeName);
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * 得到某元素的值
	 * 
	 * @param element
	 * @return
	 */
	public String getText(Element element) {
		if (element == null) {
			return null;
		}
		return element.getTextTrim();
	}

	/**
	 * 
	 * 函数名: getText 功能描述: 查询某元素下面的所有元素的子元素的值 修改历史:
	 * 
	 * @date 2011-10-27 下午03:23:07
	 * @author yuhao
	 * @version 1.0
	 * @description 输入、输出参数:
	 * @param element
	 *            父元素
	 * @param elementName
	 * @return 要查询元素的值
	 */
	public String getText(Element parentElement, String elementName) {
		String result = "";
		Element element = getElementByName(parentElement, elementName);
		if (element != null) {
			result = getText(element);
		}
		return result;
	}

	/**
	 * xpath 快速查找某个文档的所有节点
	 * 
	 * @param file
	 * @param pathValue
	 * @return
	 */
	public List getElementByXPath(Document document, String pathValue) {
		if (document != null && pathValue != null && !"".equals(pathValue)) {
			return document.selectNodes(pathValue);
		} else {
			return null;
		}
	}

	/**
	 * 添加节点元素的属性
	 * 
	 * @param attrName
	 * @param element
	 * @return
	 */
	public Element addElementAttribute(Element element, String attrName,
			String attrValue) {
		if (attrName != null && !"".equals(attrName) && element != null) {
			element.addAttribute(attrName, attrValue);
		}
		return element;
	}

	/**
	 * 设置某元素的属性的值
	 * 
	 * @param element
	 * @param attrName
	 * @param attrValue
	 * @return
	 */
	public Element setAttributeValue(Element element, String attrName,
			String attrValue) {
		if (element != null && attrName != null && !"".equals(attrName)) {
			if (isElementAttribute(element, attrName)) {
				element.setAttributeValue(attrName, attrValue);
			} else {
				System.out.println("元素" + element.getName() + "不含有" + attrName
						+ "属性");
			}
		}
		return element;
	}

	/**
	 * 添加节点元素的文本
	 * 
	 * @param element
	 * @param text
	 * @return
	 */
	public Element addElementText(Element element, String text) {
		Element resultElement = element;
		if (element != null && text != null && !"".equals(text)) {
			resultElement = element.addText(text);
		}
		return resultElement;
	}

	/**
	 * 元素节点添加元素节点
	 * 
	 * @param element
	 * @param elementName
	 * @return
	 */
	public Element addElementByName(Element element, String elementName) {
		Element subElement = null;
		if (element != null && elementName != null && !"".equals(elementName)) {
			subElement = element.addElement(elementName);
		}
		return subElement;
	}

	/**
	 * document 转化成String
	 * 
	 * @param document
	 * @return
	 */
	public String changeDocumentToString(Document document) {
		String result = "";
		if (document != null) {
			result = document.asXML();
		}
		return result;
	}

	/**
	 * element 转化成String
	 * 
	 * @param element
	 * @return
	 */
	public String changeElementToString(Element element) {
		String result = "";
		if (element != null) {
			result = element.asXML();
		}
		return result;
	}

	/**
	 * 删除元素节点下的文本
	 * 
	 * @param element
	 * @return
	 */
	public Element deleteElementText(Element element) {
		if (element != null) {
			element.setText("");
		}
		return element;
	}

	/**
	 * 删除某元素节点的属性
	 * 
	 * @param element
	 * @param attributeName
	 * @return
	 */
	public Element deleteElementAttribute(Element element, String attributeName) {
		if (element != null && attributeName != null
				&& !"".equals(attributeName)) {
			if (isElementAttribute(element, attributeName)) {
				Attribute attribute = element.attribute(attributeName);
				element.remove(attribute);
			} else {
				System.out.println(attributeName + "属性不是" + element.getName()
						+ "的属性");
			}
		}
		return element;
	}

	/**
	 * 删除某元素节点的属性值
	 * 
	 * @param element
	 * @param attributeName
	 * @return
	 */
	public Element deleteElementAttributeValue(Element element,
			String attributeName) {
		if (element != null && attributeName != null
				&& !"".equals(attributeName)) {
			if (isElementAttribute(element, attributeName)) {
				element.setAttributeValue(attributeName, "");
			} else {
				System.out.println(attributeName + "属性不是" + element.getName()
						+ "的属性");
			}
		}
		return element;
	}

	/**
	 * 删除 某元素节点下的元素节点
	 * 
	 * @param parentElement
	 * @param element
	 * @return
	 */
	public Element deleteElement(Element parentElement, Element element) {
		if (parentElement != null && element != null) {
			if (isContainElement(parentElement, element)) {
				parentElement.remove(element);
			} else {
				System.out.println(parentElement.getName() + "下不含有"
						+ element.getName() + "元素节点.");
			}
		}
		return parentElement;
	}

	/**
	 * 判断 元素 是否具有此属性
	 * 
	 * @param element
	 * @param attributeName
	 * @return
	 */
	public boolean isElementAttribute(Element element, String attributeName) {
		boolean flag = false;
		Attribute attribute = element.attribute(attributeName);
		if (attribute != null) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 判断某元素下是否含有此元素
	 * 
	 * @param parentElement
	 * @param element
	 * @return
	 */
	public boolean isContainElement(Element parentElement, Element element) {
		boolean flag = false;
		Element myElement = parentElement.element(element.getName());
		if (myElement != null) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 某元素是否含有指定名称的元素
	 * 
	 * @param parentElement
	 * @param elementName
	 * @return
	 */
	public boolean isContainElementByName(Element parentElement,
			String elementName) {
		boolean flag = false;
		Element element = parentElement.element(elementName);
		if (element != null) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 将文档写成xml文件
	 * 
	 * @param document
	 * @param writePath
	 *            文件写到的路径
	 * @return
	 */
	public boolean writeXml(Document document, String writePath) {
		boolean flag = false;
		XMLWriter writer = null;
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		try {
			writer = new XMLWriter(new FileWriter(writePath), format);
			writer.write(document);
			writer.close();
			flag = true;
		} catch (Exception e) {
			System.out
					.println("XmlTools类的writeXml(Document document, String writePath)方法：写文件路径错误.");
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 将文档写成xml文件
	 * 
	 * @param document
	 * @param writePath
	 * @param encodingType
	 *            编码格式设置 如含有中文 需要设置编码格式
	 * @return
	 */
	public boolean writeXml(Document document, String writePath,
			String encodingType) {
		boolean flag = false;
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding(encodingType); // 指定XML编码
		XMLWriter writer = null;
		try {
			writer = new XMLWriter(new FileWriter(writePath), format);
			writer.write(document);
			writer.close();
		} catch (Exception e) {
			System.out
					.println("XmlTools类的writeXml(Document document, String writePath,String encodingType)方法：写文件路径错误.");
			e.printStackTrace();
		}
		return flag;
	}

}
