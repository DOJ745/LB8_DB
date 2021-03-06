package TaskThings;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

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

            // Group element
            Element group = doc.createElement("Group");
            rootElement.appendChild(group);
            group.setAttribute("name", "study");

            // Task elements
            Element task = doc.createElement("task");
            group.appendChild(task);
            task.setAttribute("id", "test");

            // Info elements
            Element info = doc.createElement("info");
            info.appendChild(doc.createTextNode("12345"));
            task.appendChild(info);

            // Date elements
            Element date = doc.createElement("date");
            date.appendChild(doc.createTextNode("14-9-2020"));
            task.appendChild(date);

            // Write the content into xml file
            File xmlFile = new File(filePath, fileName);
            saveChanges(doc, xmlFile);
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

        public static void addCategory(File readedXML, String newCategory)
                throws ParserConfigurationException, IOException, SAXException, TransformerException {
            DocumentBuilderFactory docBuildFact = DocumentBuilderFactory.newInstance();
            DocumentBuilder xmlBuilder = docBuildFact.newDocumentBuilder();
            Document doc = xmlBuilder.parse(readedXML);

            Element group = doc.createElement("Group");
            group.setAttribute("name", newCategory);
            doc.getDocumentElement().appendChild(group);
            saveChanges(doc, readedXML);
        }

        public static void updateCategory(File readedXML, String oldCategory, String newCategory)
                throws ParserConfigurationException, IOException, SAXException, TransformerException {
            DocumentBuilderFactory docBuildFact = DocumentBuilderFactory.newInstance();
            DocumentBuilder xmlBuilder = docBuildFact.newDocumentBuilder();
            Document doc = xmlBuilder.parse(readedXML);
            NodeList groups = doc.getElementsByTagName("Group");

            for(int i = 0; i < groups.getLength(); i++){
                Element group = (Element) groups.item(i);
                if(oldCategory.equals(group.getAttribute("name"))){
                    group.setAttribute("name", newCategory);
                }
            }
            saveChanges(doc, readedXML);
        }

        public static void deleteCategory(File readedXML, String Category)
                throws ParserConfigurationException, IOException, SAXException, TransformerException {
            DocumentBuilderFactory docBuildFact = DocumentBuilderFactory.newInstance();
            DocumentBuilder xmlBuilder = docBuildFact.newDocumentBuilder();
            Document doc = xmlBuilder.parse(readedXML);
            NodeList groups = doc.getElementsByTagName("Group");

            for(int i = 0; i < groups.getLength(); i++){
                Element group = (Element) groups.item(i);
                if(group.getAttribute("name").equals(Category)){
                    group.getParentNode().removeChild(group);
                }
            }
            saveChanges(doc, readedXML);
        }

        public static ArrayList<Task> getDateTasks(File readedXML, String currentDate)
                throws ParserConfigurationException, IOException, SAXException {
            DocumentBuilderFactory docBuildFact = DocumentBuilderFactory.newInstance();
            DocumentBuilder xmlBuilder = docBuildFact.newDocumentBuilder();
            Document doc = xmlBuilder.parse(readedXML);
            NodeList tasksNode = doc.getElementsByTagName("task");
            ArrayList<Task> taskList = new ArrayList<>();

            for(int i = 0; i < tasksNode.getLength(); i++){
                Task findedTask = new Task();
                String taskInfo;
                String taskDate;
                Element task = (Element) tasksNode.item(i);
                String taskName = task.getAttribute("id");

                Node infoNode = task.getFirstChild();
                Node dateNode = task.getLastChild();
                taskInfo = infoNode.getTextContent();
                taskDate = dateNode.getTextContent();

                findedTask.setDate(taskDate);
                findedTask.setInfo(taskInfo);
                findedTask.setName(taskName);
                if(taskDate.equals(currentDate)){ taskList.add(findedTask); }
            }

            return taskList;
        }

        public static void createXSLT(File filePath, String date) throws IOException {
            String rawXSLT = "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\"\n" +
                    "xmlns:msxsl=\"urn:schemas-microsoft-com:xslt\" exclude-result-prefixes=\"msxsl\">\n" +
                    "\n" +
                    "<xsl:output method=\"xml\" encoding=\"utf-16\"/>\n" +
                    "\n" +
                    "<xsl:template match=\"Tasks\">\n" +
                    "\n" +
                    "<xsl:for-each select=\"Group\">\n" +
                    "   Категория: <xsl:value-of select=\"@name\"/><xsl:text/>\n" +
                    "\n" +
                    "<xsl:for-each select=\"task\">\n" +
                    "<xsl:if test=\"date = '" + date + "'\">\n" +
                    "Название - <xsl:value-of select=\"@id\"/><xsl:text/>\n" +
                    "Info - <xsl:value-of select=\"info\"/><xsl:text/>\n" +
                    "Дата - <xsl:value-of select=\"date\"/><xsl:text/>\n" +
                    "</xsl:if>\n" +
                    "</xsl:for-each>\n" +
                    "\n" +
                    "</xsl:for-each>\n" +
                    "\n" +
                    "</xsl:template>\n" +
                    "\n" +
                    "</xsl:stylesheet>";
            File xsltFile = new File(filePath, "form.xslt");
            xsltFile.createNewFile();
            try(FileWriter writer = new FileWriter(xsltFile, false))
            {
                writer.write(rawXSLT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static void resultXSLT(String XMLFIle, String XSLTFile, File filePath)
                throws IOException, TransformerException {
            File txtFile = new File(filePath, "result.txt");
            txtFile.createNewFile();
            TransformerFactory factory = TransformerFactory.newInstance();
            Source xsltFile = new StreamSource(new File(filePath, XSLTFile));
            Source xmlFile = new StreamSource(new File(filePath, XMLFIle));
            OutputStream txtResult = new FileOutputStream(txtFile);

            Transformer transformer = factory.newTransformer(xsltFile);
            transformer.transform(xmlFile, new StreamResult(txtResult));
        }

        public static ArrayList<Task> findByXPath(File readedXML, String category)
                throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
            DocumentBuilderFactory docBuildFact = DocumentBuilderFactory.newInstance();
            DocumentBuilder xmlBuilder = docBuildFact.newDocumentBuilder();
            Document doc = xmlBuilder.parse(readedXML);

            XPathFactory xpf = XPathFactory.newInstance();
            XPath xp = xpf.newXPath();

            XPathExpression xpe = xp.compile("/Tasks/Group[@name='" + category + "']/task");
            NodeList nodes = (NodeList) xpe.evaluate(doc, XPathConstants.NODESET);
            ArrayList<Task> taskList = new ArrayList<>();

            for(int i = 0; i < nodes.getLength(); i++){
                Task findedTask = new Task();
                String taskInfo;
                String taskDate;
                Element task = (Element) nodes.item(i);
                String taskName = task.getAttribute("id");

                Node infoNode = task.getFirstChild();
                Node dateNode = task.getLastChild();
                taskInfo = infoNode.getTextContent();
                taskDate = dateNode.getTextContent();

                findedTask.setCategory(category);
                findedTask.setDate(taskDate);
                findedTask.setInfo(taskInfo);
                findedTask.setName(taskName);

                taskList.add(findedTask);
            }
            return taskList;
        }

        public static void addTask(File readedXML, Task newTask)
                throws ParserConfigurationException, IOException, SAXException, TransformerException {

            DocumentBuilderFactory docBuildFact = DocumentBuilderFactory.newInstance();
            DocumentBuilder xmlBuilder = docBuildFact.newDocumentBuilder();
            Document doc = xmlBuilder.parse(readedXML);

            Element task = doc.createElement("task");
            task.setAttribute("id", String.valueOf(newTask.getId()));
            Element info = doc.createElement("info");
            info.appendChild(doc.createTextNode(newTask.getInfo()));
            task.appendChild(info);
            Element date = doc.createElement("date");
            date.appendChild(doc.createTextNode(newTask.getDate()));
            task.appendChild(date);

            NodeList groups = doc.getElementsByTagName("Group");
            for(int i = 0; i < groups.getLength(); i++){
                Element groupElem = (Element) groups.item(i);
                if(newTask.getCategory().equals(groupElem.getAttribute("name"))){
                    groupElem.appendChild(task);
                }
            }

            saveChanges(doc, readedXML);
            Log.e("log_file", "Task added successfully!");
            readXMLRaw(readedXML);
        }

        public static void changeTask(File readedXML, String oldId,
                                      String newInfo, String newCategory, String newId)
                throws ParserConfigurationException, IOException, SAXException, TransformerException {
            DocumentBuilderFactory docBuildFact = DocumentBuilderFactory.newInstance();
            DocumentBuilder xmlBuilder = docBuildFact.newDocumentBuilder();
            Document doc = xmlBuilder.parse(readedXML);

            Element task = doc.getElementById(oldId);

            if(newCategory != null)
            {
                task.setAttribute("category", newCategory);
            }
            Node taskInfo = task.getFirstChild();

            if(newInfo != null){ taskInfo.setTextContent(newInfo); }
            if(newId != null){ task.setAttribute("id", newId); }

            saveChanges(doc, readedXML);
            Log.e("log_file", "Task changed successfully!");
        }

        public static void deleteTask(File readedXML, String id) throws ParserConfigurationException, IOException,
                SAXException, TransformerException {
            DocumentBuilderFactory docBuildFact = DocumentBuilderFactory.newInstance();
            DocumentBuilder xmlBuilder = docBuildFact.newDocumentBuilder();
            Document doc = xmlBuilder.parse(readedXML);

            Element task = doc.getElementById(id);
            String checkId = task.getAttribute("id");
                if(id.equals(checkId)){
                    task.getParentNode().removeChild(task);
                    saveChanges(doc, readedXML);
                    Log.e("log_file", "Task deleted successfully!");
               }
        }

        private static void saveChanges(Document doc, File XML) throws TransformerException {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(XML);
            transformer.transform(source, result);
        }
    }
}
