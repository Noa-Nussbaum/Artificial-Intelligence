import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;


public class BayesianNetwork {

    private Hashtable nodesList;

    // Getter
    public Hashtable getNodesList(){
        return this.nodesList;
    }

    public AlgNode getNode(String name){
        return (AlgNode) this.nodesList.get(name);
    }

    
    // Constructor
    public BayesianNetwork(){
        this.nodesList = new Hashtable<String, AlgNode>();
    }

    // Node setter
    public void setNode(String name, AlgNode node){
        this.nodesList.put(name, node);
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

public static boolean BayesBall(BayesianNetwork network, String StrSrc, String StrDest, ArrayList<String> evidence, String StrLast, ArrayList<AlgNode> passed) {
    AlgNode src = network.getNode(StrSrc);
    AlgNode dest = network.getNode(StrDest);
    AlgNode last = network.getNode(StrLast);

    // If the source is the destination or the nodes don't exist
    if (network.getNode(src.getName()).equals(network.getNode(dest.getName()))) return false;
    if (src == null || dest == null) return true; 


    //If the source is in the evidence
    if (evidence.contains(src.getName())) {
        System.out.println("contains " + src.getName());
        // If it came from child
        if (src.getChildren().contains(last)) {
            return true;
        }

        //If it came from a parent
        else {
            for (int i = 0; i < src.getParents().size(); i++) {
                /*
                    In order to prevent endless loop between the nodes, we will keep the nodes we can't
                    come back to in a memory.
                    Only if this is the first time we get to this node - go in, else move on
                 */
                if (!passed.contains(src.getParents().get(i))) {
                    passed.add(src);
                    if (!BayesBall(network, src.getParents().get(i).getName(), dest.getName(), evidence, src.getName(), passed))
                        return false;
                }
            }
            return true;
        }
    }

    //If the source is not in the evidence
    else {
        //If it came from a child
        if (src.getChildren().contains(last) || last == (null)) {
            for (int i = 0; i < src.getParents().size(); i++) {
                if (!passed.contains(src.getParents().get(i))) {
                    passed.add(src);
                    if (!BayesBall(network, src.getParents().get(i).getName(), dest.getName(), evidence, src.getName(), passed))
                        return false;
                }
            }
            for (int i = 0; i < src.getChildren().size(); i++) {
                if (!passed.contains(src.getChildren().get(i))) {
                    passed.add(src);
                    if (!BayesBall(network, src.getChildren().get(i).getName(), dest.getName(), evidence, src.getName(), passed))
                        return false;
                }
            }
            return true;
        }

        //If it came from a parent
        else {
            for (int i = 0; i < src.getChildren().size(); i++) {
                if (!passed.contains(src.getChildren().get(i))) {
                    if (!BayesBall(network, src.getChildren().get(i).getName(), dest.getName(), evidence, src.getName(), passed)){
                        return false;
                    }
                }

            }
            return true;
        }

    }

}

}