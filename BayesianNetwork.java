import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;

public class BayesianNetwork {

    // Hashtable with each node name and node
    private final Hashtable<String, AlgNode> nodesList;
    
    // List of names for retrieval
    private final ArrayList<String> nodesNames;

    // Getters
    public Hashtable<String, AlgNode> getNodesList() {
        return this.nodesList;
    }

    public ArrayList<String> getNodeNames() {
        return this.nodesNames;
    }

    public AlgNode getNode(String name) {
        return (AlgNode) this.nodesList.get(name);
    }

    // Constructor
    public BayesianNetwork() {
        this.nodesList = new Hashtable<String, AlgNode>();
        this.nodesNames = new ArrayList<String>();
    }

    // Node setter
    public void setNode(String name, AlgNode node) {
        this.nodesList.put(name, node);
        this.nodesNames.add(name);
    }

    // Print function
    public void Print() {
        nodesList.forEach((name, node) -> {
            System.out.println(node.toString());
        });
    }

    /**
     * Implements the Bayes Ball algorithm to determine the independence of two
     * nodes given evidence.
     * This method recursively checks if there is a valid path from the source to
     * the destination node,
     * considering the provided evidence that may block certain paths. The function
     * handles direct checks
     * and also recurses through children and parents as needed based on the
     * direction of the traversal.
     * 
     * @param network  The Bayesian network instance containing all nodes.
     * @param src      The name of the source node from which independence is being
     *                 tested.
     * @param destin   The name of the destination node to which independence is
     *                 being tested.
     * @param evidence A list of nodes that are observed (evidence nodes).
     * @param prev     The previously visited node name, used to determine the
     *                 direction of traversal.
     * @param passed   A list of nodes already visited in this path, used to prevent
     *                 cycles.
     * @return true if the nodes are independent given the evidence, false
     *         otherwise.
     */
    public boolean BayesBall(BayesianNetwork network, String src, String destin, ArrayList<String> evidence,
            String prev, ArrayList<AlgNode> visited) {

        // Retrieve source, destination, and previous nodes
        AlgNode source = network.getNode(src);
        // Node we are trying to get to
        AlgNode destination = network.getNode(destin);
        // The last node we visited
        AlgNode previous = network.getNode(prev);

        // If the source is the destination or the nodes don't exist
        if (source == null || destination == null)
            return true;
        // If the source is the destination
        if (source.getName().equals(destination.getName()))
            return false;

        // If the source is in the evidence
        if (evidence.contains(source.getName())) {
            // If it came from a child
            if (source.getChildren().contains(previous)) {
                return true;
            }

            // If it came from a parent
            else {
                for (int i = 0; i < source.getParents().size(); i++) {
                    if (!visited.contains(source.getParents().get(i))) {
                        visited.add(source);
                        // Recursively check
                        if (!BayesBall(network, source.getParents().get(i).getName(), destination.getName(), evidence,
                                source.getName(), visited))
                            return false;
                    }
                }
                return true;
            }
        }

        // If the source is not in the evidence
        else {
            // If it came from a child
            if (source.getChildren().contains(previous) || previous == (null)) {
                for (int i = 0; i < source.getParents().size(); i++) {
                    if (!visited.contains(source.getParents().get(i))) {
                        visited.add(source);
                        // Recursively check

                        if (!BayesBall(network, source.getParents().get(i).getName(), destination.getName(), evidence,
                                source.getName(), visited))
                            return false;
                    }
                }
                for (int i = 0; i < source.getChildren().size(); i++) {
                    if (!visited.contains(source.getChildren().get(i))) {
                        visited.add(source);
                        // Recursively check

                        if (!BayesBall(network, source.getChildren().get(i).getName(), destination.getName(), evidence,
                                source.getName(), visited))
                            return false;
                    }
                }
                return true;
            }

            // If it came from a parent
            else {
                for (int i = 0; i < source.getChildren().size(); i++) {
                    if (!visited.contains(source.getChildren().get(i))) {
                        // Recursively check

                        if (!BayesBall(network, source.getChildren().get(i).getName(), destination.getName(), evidence,
                                source.getName(), visited)) {
                            return false;
                        }
                    }

                }
                return true;
            }

        }

    }

    /**
     * Determines if the specified node is an ancestor of the given query node or is
     * part of the evidence.
     * This method uses a breadth-first search strategy to explore the node's
     * descendants until the target
     * node or any evidence node is found.
     * 
     * @param evidence Array of evidence node names.
     * @param varName  The name of the starting node to check for ancestry.
     * @param query    The target node name to check if it is a descendant of the
     *                 starting node.
     * @return true if the starting node is an ancestor of the target node or is
     *         part of the evidence, false otherwise.
     */
    public boolean ancestor(String[] evidence, String varName, String query) {
        // Is name the ancestor of query or any of the evidence?

        Queue<AlgNode> nextNodes = new LinkedList<>();
        AlgNode firstNode = (AlgNode) this.getNode(varName);
        nextNodes.add(firstNode);

        while (!nextNodes.isEmpty()) {

            AlgNode curr_node = nextNodes.remove();
            String name = curr_node.getName();
            ArrayList<String> evidenceVars = new ArrayList<>();

            if (evidence != null) {
                evidenceVars = new ArrayList<>(Arrays.asList(evidence));
            }

            // Check if the node is in the evidence or if it's the query
            if (name.equals(query) || evidenceVars.contains(name)) {
                return true;
            }

            else if (!curr_node.getChildren().isEmpty()) {
                for (AlgNode kid : curr_node.getChildren()) {
                    nextNodes.add(kid);
                }
            }
        }
        return false;
    }

}