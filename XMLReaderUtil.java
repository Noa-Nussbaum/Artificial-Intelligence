import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class XMLReaderUtil {


        // Read from the XML and create the net
    public static BayesianNetwork XMLReader(String fileName){

        BayesianNetwork network = new BayesianNetwork();

        try {
            File xmlFile = new File(fileName);
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            org.w3c.dom.Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();

            // Process each VARIABLE to create nodes
            org.w3c.dom.NodeList variableList = document.getElementsByTagName("VARIABLE");
            for (int i = 0; i < variableList.getLength(); i++) {
                org.w3c.dom.Node variableNode = variableList.item(i);
                if (variableNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                    org.w3c.dom.Element variableElement = (org.w3c.dom.Element) variableNode;
                    String nodeName = variableElement.getElementsByTagName("NAME").item(0).getTextContent();
                    org.w3c.dom.NodeList outcomes = variableElement.getElementsByTagName("OUTCOME");
                    ArrayList<String> values = new ArrayList<>();

                    for (int j = 0; j < outcomes.getLength(); j++) {
                        values.add(outcomes.item(j).getTextContent());
                    }
                    AlgNode newNode = new AlgNode(nodeName, values);
                    network.setNode(nodeName, newNode);
                }
            }

            // Process each DEFINITION to update nodes
            org.w3c.dom.NodeList definitionList = document.getElementsByTagName("DEFINITION");
            for (int i = 0; i < definitionList.getLength(); i++) {
                org.w3c.dom.Node definitionNode = definitionList.item(i);
                if (definitionNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                    org.w3c.dom.Element definitionElement = (org.w3c.dom.Element) definitionNode;
                    String nodeName = definitionElement.getElementsByTagName("FOR").item(0).getTextContent();
                    AlgNode currentNode = (AlgNode) network.getNodesList().get(nodeName);
                    org.w3c.dom.NodeList givenList = definitionElement.getElementsByTagName("GIVEN");

                    for (int j = 0; j < givenList.getLength(); j++) {
                        String parentName = givenList.item(j).getTextContent();
                        AlgNode parentNode = (AlgNode) network.getNodesList().get(parentName);
                        currentNode.setParent(parentNode);
                        parentNode.setChild(currentNode);
                    }

                    String table = definitionElement.getElementsByTagName("TABLE").item(0).getTextContent();
                    // Further processing for the factor can be done here
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return network;
    }
    

}
