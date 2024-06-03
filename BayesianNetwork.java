import java.util.ArrayList;
import java.util.Hashtable;


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

    public String BayesBall(String source, String destination, ArrayList<String> evidenceList){
        

        return "no";
    }

    }