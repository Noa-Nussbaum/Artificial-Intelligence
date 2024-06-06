import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;


public class BayesianNetwork {

    private Hashtable nodesList;
    private ArrayList<String> nodesNames;

    // Getters
    public Hashtable getNodesList(){
        return this.nodesList;
    }
    public ArrayList<String> getNodeNames(){
        return this.nodesNames;
    }


    public AlgNode getNode(String name){
        return (AlgNode) this.nodesList.get(name);
    }

    
    // Constructor
    public BayesianNetwork(){
        this.nodesList = new Hashtable<String, AlgNode>();
        this.nodesNames = new ArrayList<String>();
    }

    // Node setter
    public void setNode(String name, AlgNode node){
        this.nodesList.put(name, node);
        this.nodesNames.add(name);
    }

    // Print function
    @SuppressWarnings("unchecked")
    public void print(){
        nodesList.forEach((name, node) -> {
            System.out.println(node.toString());
        }
        );
    }

    //  * This function is the algorithm Bayes-Ball
    //  * we will split the algorithm to four cases:
    //  * 1. the current node is given and came from child
    //  * 2. the current node is given and came from parent
    //  * 3. the current node is NOT given and came from child
    //  * 4. the current node is NOT given and came from parent

// If no path to the destination is active, return true (independent)

public static boolean BayesBall(BayesianNetwork network, String src, String destin, ArrayList<String> evidence, String prev, ArrayList<AlgNode> passed) {
    // Retrieve source, destination, and previous nodes
    AlgNode source = network.getNode(src);
    AlgNode destination = network.getNode(destin);
    AlgNode previous = network.getNode(prev);

    // If the source is the destination or the nodes don't exist
    if (network.getNode(source.getName()).equals(network.getNode(destination.getName()))) return false;
    if (source == null || destination == null) return true; 


    //If the source is in the evidence
    if (evidence.contains(source.getName())) {
        // If it came from child
        if (source.getChildren().contains(previous)) {
            return true;
        }

        //If it came from a parent
        else {
            for (int i = 0; i < source.getParents().size(); i++) {
                if (!passed.contains(source.getParents().get(i))) {
                    passed.add(source);
                    if (!BayesBall(network, source.getParents().get(i).getName(), destination.getName(), evidence, source.getName(), passed))
                        return false;
                }
            }
            return true;
        }
    }

    //If the source is not in the evidence
    else {
        //If it came from a child
        if (source.getChildren().contains(previous) || previous == (null)) {
            for (int i = 0; i < source.getParents().size(); i++) {
                if (!passed.contains(source.getParents().get(i))) {
                    passed.add(source);
                    if (!BayesBall(network, source.getParents().get(i).getName(), destination.getName(), evidence, source.getName(), passed))
                        return false;
                }
            }
            for (int i = 0; i < source.getChildren().size(); i++) {
                if (!passed.contains(source.getChildren().get(i))) {
                    passed.add(source);
                    if (!BayesBall(network, source.getChildren().get(i).getName(), destination.getName(), evidence, source.getName(), passed))
                        return false;
                }
            }
            return true;
        }

        //If it came from a parent
        else {
            for (int i = 0; i < source.getChildren().size(); i++) {
                if (!passed.contains(source.getChildren().get(i))) {
                    if (!BayesBall(network, source.getChildren().get(i).getName(), destination.getName(), evidence, source.getName(), passed)){
                        return false;
                    }
                }

            }
            return true;
        }

    }

}


    public boolean ancestor(String[] evidence, String varName, String query){
        // Is name the ancestor of query or any of the evidence?

        Queue<AlgNode> nextNodes = new LinkedList<>();
        AlgNode firstNode = (AlgNode) this.getNode(varName);
        nextNodes.add(firstNode);

        while (!nextNodes.isEmpty()){

            AlgNode curr_node = nextNodes.remove();
            String name = curr_node.getName();
            ArrayList<String> evidenceVars=new ArrayList<>();

            if(evidence != null) {
                evidenceVars = new ArrayList<>(Arrays.asList(evidence));
            }

            // Check if the node is in the evidence or if it's the query
            if(name.equals(query) || evidenceVars.contains(name)){
                return true;
            }

            else if(!curr_node.getChildren().isEmpty()){
                for(AlgNode kid: curr_node.getChildren()){nextNodes.add(kid);}
            }
        }
        return false;
    }

}