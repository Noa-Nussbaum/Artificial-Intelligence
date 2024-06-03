import java.util.Hashtable;


public class BayesianNetwork {

    private Hashtable nodesList;

    // Getter
    public Hashtable getNodesList(){
        return this.nodesList;
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

    }