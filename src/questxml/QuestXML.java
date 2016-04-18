package questxml;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class QuestXML
{
	/**
	 * Class for QuestXML.
	 */
	
	public static String obtainText(final Element e) 
	{
		final StringBuilder sb = new StringBuilder();
		obtainText(e, sb, false);
		return sb.toString();
	}

	public static List<Element> obtainElementsList(final Element parent)
	{
		final LinkedList<Element> list = new LinkedList<Element>();
		Node current = parent.getFirstChild();
		while (current != null) {
			if (current.getNodeType() == Node.ELEMENT_NODE)
		      list.add((Element) current);
			
			current = current.getNextSibling();
		}
		return list;
	} // obtainElementsList

	
   public static void obtainText(final Element e,
		                         final StringBuilder sb,
			                     final boolean ok)
   {
		Node current = e.getFirstChild();

		while (current != null)
		{
			switch (current.getNodeType())
			{
			  case Node.TEXT_NODE:
				sb.append(current.getNodeValue());
				break;

			  case Node.ELEMENT_NODE:
				if (ok)
				  obtainText((Element) current, sb, ok);
								
				break;
			}
			current = current.getNextSibling();
		} // while
	} // obtainText


	public static Document docParse(final InputStream is) throws SAXException,
			                                                     IOException 
	{
	  return docParse(new InputSource(is));
	} // docParse


	public static Document docParse(final InputSource isource) throws SAXException,
			                                                          IOException 
	{
		try
		{
			final DocumentBuilder b = DocumentBuilderFactory.newInstance().newDocumentBuilder();

			return b.parse(isource);
		} 
		catch (final ParserConfigurationException ex)
		{
			throw new IllegalArgumentException(
					"An error of type 'DOM parser configuration' " + ex.getMessage());
		}
	} // docParse



	public static boolean check(String c)
	{
		if ((c == null) || c.trim().equals(""))
		  return true;
		
		String value  = c.trim();
		if (value.charAt(0) == '!') 
	      return System.getProperty(value.substring(1)) == null;

		return System.getProperty(value) != null;
	}
} // 