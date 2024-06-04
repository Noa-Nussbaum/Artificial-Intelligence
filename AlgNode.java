import java.util.ArrayList;

public class AlgNode {
    
    public static final Object ELEMENT_NODE = null;
    // Each Node has children and parents and a correlating factor
    private ArrayList<AlgNode> children;
    private ArrayList<AlgNode> parents;
    private NodeFactor factor;
    private String name;
    private ArrayList<String> values;
    private boolean shaded;




    // Getters
    public ArrayList<AlgNode> getChildren(){
        return this.children;
    }

    public ArrayList<AlgNode> getParents(){
        return this.parents;
    }

    public NodeFactor getFactor(){
        return this.factor;
    }
    public boolean isShaded(){
        return this.shaded;
    }
// Necessary?
    public String getName(){
        return this.name;
    }
    public ArrayList<String> getValues(){
        return this.values;
    }
    




    // Setters
    public void setChild(AlgNode child){
        this.children.add(child);
    }

    public void setParent(AlgNode parent){
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
    public void setShaded(boolean shading){
        this.shaded = shading;
    }

    // Constructors 
    public AlgNode(){
        this.children = new ArrayList<>();
        this.parents = new ArrayList<>();
        this.values = new ArrayList<>();
        this.shaded = false;
    }

    public AlgNode(String name, ArrayList<String> values){
        this.children = new ArrayList<>();
        this.parents = new ArrayList<>();
        this.name = name;
        this.values = values;
        this.shaded = false;
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
    

