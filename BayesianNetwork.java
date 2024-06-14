import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;


public class BayesianNetwork {

    private final Hashtable nodesList;
    private final ArrayList<String> nodesNames;

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
    public void Print(){
        nodesList.forEach((name, node) -> {
            System.out.println(node.toString());
        }
        );
    }



    // This is incorect but use to modify
// public boolean BayesBall(BayesianNetwork network, String src, String destin, ArrayList<String> evidence, String prev, ArrayList<AlgNode> passed) {
//     AlgNode source = network.getNode(src);
//     AlgNode destination = network.getNode(destin);
//     AlgNode previous = prev.isEmpty() ? null : network.getNode(prev);

//     // Early exit if nodes don't exist
//     if (source == null || destination == null) return true; 

//     // Check if source is the destination
//     if (source.equals(destination)) return false;

//     // Check if the node has already been processed
//     if (passed.contains(source)) {
//         return true;
//     }

//     // Add the current node to passed
//     passed.add(source);

//     if (evidence.contains(source.getName())) {
//         if (source.getChildren().contains(previous)) {
//             return true;
//         } else {
//             return source.getParents().stream()
//                          .noneMatch(parent -> !BayesBall(network, parent.getName(), destin, evidence, source.getName(), passed));
//         }
//     } else {
//         boolean fromChild = source.getChildren().contains(previous) || previous == null;
//         if (fromChild) {
//             boolean parentCheck = source.getParents().stream()
//                                      .noneMatch(parent -> !BayesBall(network, parent.getName(), destin, evidence, source.getName(), passed));
//             boolean childrenCheck = source.getChildren().stream()
//                                        .noneMatch(child -> !BayesBall(network, child.getName(), destin, evidence, source.getName(), passed));
//             return parentCheck && childrenCheck;
//         } else {
//             return source.getChildren().stream()
//                          .noneMatch(child -> !BayesBall(network, child.getName(), destin, evidence, source.getName(), passed));
//         }
//     }
// }



// If no path to the destination is active, return true (independent)
// Improve this. for example - retrieve parents and kids at the beginning

public boolean BayesBall(BayesianNetwork network, String src, String destin, ArrayList<String> evidence, String prev, ArrayList<AlgNode> passed) {
    // Retrieve source, destination, and previous nodes
    AlgNode source = network.getNode(src);
    AlgNode destination = network.getNode(destin);
    AlgNode previous = network.getNode(prev);

    // If the source is the destination or the nodes don't exist
    if (source == null || destination == null) return true; 
    if (source.getName().equals(destination.getName())) return false;


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