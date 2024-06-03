import java.util.ArrayList;

public class Node {
    
    public static final Object ELEMENT_NODE = null;
    // Each Node has children and parents and a correlating factor
    private ArrayList<Node> children;
    private ArrayList<Node> parents;
    private NodeFactor factor;
    private String name;
    private ArrayList<String> values;




    // Getters
    public ArrayList<Node> getChildren(){
        return this.children;
    }

    public ArrayList<Node> getParents(){
        return this.parents;
    }

    public NodeFactor getFactor(){
        return this.factor;
    }

// Necessary?
    public String getName(){
        return this.name;
    }
    public ArrayList<String> getValues(){
        return this.values;
    }
    




    // Setters
    public void setChild(Node child){
        this.children.add(child);
    }

    public void setParent(Node parent){
        this.parents.add(parent);
    }

    public void setFactor(NodeFactor factor){
        this.factor = factor;
    }
    // Necessary?
    public void setName(String name){
        this.name = name;
    }
    public void setValues(String value){
        this.values.add(value);
    }

    // Constructors 
    public Node(){
        this.children = new ArrayList<>();
        this.parents = new ArrayList<>();
        this.values = new ArrayList<>();
    }

    public Node(String name, ArrayList<String> values){
        this.children = new ArrayList<>();
        this.parents = new ArrayList<>();
        this.name = name;
        this.values = values;
    }



    // Print function
    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        // Node name
        builder.append("Node ");
        builder.append(this.getName() + " {");

        // Append parent nodes
        builder.append("Parents=[");
        for (int i = 0; i < parents.size(); i++) {
            String parent = this.parents.get(i).getName();
            builder.append(parent).append(", ");
        }
        if (!parents.isEmpty()) {
                builder.setLength(builder.length() - 2); // Remove last comma and space
            }
        builder.append("], ");
    

        // Append child nodes
        builder.append("Children=[");
        for (int i = 0; i < children.size(); i++) {
            String child = this.children.get(i).getName();
            builder.append(child).append(", ");
        }
        if (!children.isEmpty()) {
                builder.setLength(builder.length() - 2); // Remove last comma and space
            }
        builder.append("], ");

        builder.append("Values=[");
        for (int i = 0; i < values.size(); i++) {
            String value = values.get(i);
            builder.append(value).append(", ");
        }
        if (!values.isEmpty()) {
                builder.setLength(builder.length() - 2); // Remove last comma and space
            }
        builder.append("], ");

        // // Append factor
        builder.append("Factor=").append(factor);

        builder.append("}");

        return builder.toString();
    }

    public Object getNodeType() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNodeType'");
    }
}
    

