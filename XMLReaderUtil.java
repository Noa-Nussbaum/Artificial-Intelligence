import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLReaderUtil {

    public static String[] nodeListToStringArray(NodeList nodeList) {
        String[] result = new String[nodeList.getLength()]; // Create an array of strings with the same length as the
                                                            // NodeList
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i); // Get the node at the current index
            result[i] = node.getTextContent(); // Store the text content of the node in the array
        }
        return result; // Return the array of strings
    }

    // Read from the XML and create the net
    public static BayesianNetwork XMLReader(String fileName) {

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
                    ArrayList<Double> probabilities = new ArrayList<>();
                    String[] parts = table.split(" ");

                    for (String m : parts) {
                        probabilities.add(Double.parseDouble(m));
                    }

                    // Add all relevant nodes to the factor
                    ArrayList<AlgNode> nodes = new ArrayList<AlgNode>();
                    for (int j = 0; j < currentNode.getParents().size(); j++) {
                        nodes.add(currentNode.getParents().get(j));
                    }

                    nodes.add(currentNode);

                    // Initialize factor
                    NodeFactor factor = new NodeFactor(probabilities, nodes);
                    currentNode.setFactor(factor);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return network;
    }

}