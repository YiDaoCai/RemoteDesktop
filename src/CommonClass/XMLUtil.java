package CommonClass;
//获取xml里的ip
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLUtil {
	
	public static boolean exist() {
		File f = new File("config.xml");
		return f.exists();
	}
	
	public static void createXML(String ip) {
		String out = "<?xml version = \"1.0\"?>\r\n<root>\r\n	<server>" + ip + "</server>\r\n</root>";
		String filename = "config.xml";
		File f = new File(filename);
		if(!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(f);
			fos.write(out.getBytes());
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}
	
	public static String getBean(String configName) {
		try {
			//
			DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dFactory.newDocumentBuilder();
			Document doc = builder.parse(new File("config.xml"));
			
			//
			NodeList nl = doc.getElementsByTagName(configName);
			Node classNode = nl.item(0).getFirstChild();
			return classNode.getNodeValue();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
}
