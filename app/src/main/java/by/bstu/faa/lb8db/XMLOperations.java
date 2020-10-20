package by.bstu.faa.lb8db;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class XMLOperations {
    public static class Operations{
        public static File createXMLFile(File filePath, String fileName) throws
                ParserConfigurationException, TransformerException {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // Root elements - Tasks
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Tasks");
            doc.appendChild(rootElement);

            // Task elements
            Element task = doc.createElement("task");
            rootElement.appendChild(task);
            task.setAttribute("category", "study");
            task.setAttribute("id", "1");

            // Info elements
            Element info = doc.createElement("info");
            info.appendChild(doc.createTextNode("12345"));
            task.appendChild(info);

            // Date elements
            Element date = doc.createElement("date");
            date.appendChild(doc.createTextNode("14-9-2020"));
            task.appendChild(date);

            // Write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            File xmlFile = new File(filePath, fileName);
            StreamResult result = new StreamResult(xmlFile);

            transformer.transform(source, result);
            Log.e("log_file", "File saved!");
            return xmlFile;
        }

        public static void readXMLRaw(File readedXML){
            DocumentBuilderFactory docBuildFact = DocumentBuilderFactory.newInstance();
            try{
                DocumentBuilder xmlBuilder = docBuildFact.newDocumentBuilder();
                FileReader fr = new FileReader(readedXML);
                char[] buffer = new char[(int)readedXML.length()];
                fr.read(buffer);
                String xmlInfo = new String(buffer);
                Log.e("log_xml", xmlInfo);
            } catch (ParserConfigurationException | IOException e) {
                e.printStackTrace();
            }
        }

        public static void addTask(File readedXML, Task newTask)
                throws ParserConfigurationException, IOException, SAXException, TransformerException {
            DocumentBuilderFactory docBuildFact = DocumentBuilderFactory.newInstance();
            DocumentBuilder xmlBuilder = docBuildFact.newDocumentBuilder();
            Document doc = xmlBuilder.parse(readedXML);

            Element rootElement = doc.getDocumentElement();
            Element task = doc.createElement("task");
            rootElement.appendChild(task);
            task.setAttribute("category", newTask.getCategory());
            task.setAttribute("id", String.valueOf(newTask.getId()));

            Element info = doc.createElement("info");
            info.appendChild(doc.createTextNode(newTask.getInfo()));
            task.appendChild(info);

            Element date = doc.createElement("date");
            date.appendChild(doc.createTextNode(newTask.getDate()));
            task.appendChild(date);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(readedXML);
            transformer.transform(source, result);
            Log.e("log_file", "Task added successfully!");

            readXMLRaw(readedXML);
        }

        public static void changeTask(File readedXML, String newInfo,
                                      String newDate, String newCategory, String id)
                throws ParserConfigurationException, IOException, SAXException, TransformerException {
            DocumentBuilderFactory docBuildFact = DocumentBuilderFactory.newInstance();
            DocumentBuilder xmlBuilder = docBuildFact.newDocumentBuilder();
            Document doc = xmlBuilder.parse(readedXML);

            Element task = doc.getElementById(id);

            if(newCategory != null){ task.setAttribute("category", newCategory); }
            Node taskInfo = task.getFirstChild();
            Node taskDate = task.getLastChild();

            if(newInfo != null){ taskInfo.setTextContent(newInfo); }
            if(newDate != null){ taskDate.setTextContent(newDate);}

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(readedXML);
            transformer.transform(source, result);
            Log.e("log_file", "Task changed successfully!");

            readXMLRaw(readedXML);
        }
    }
}
