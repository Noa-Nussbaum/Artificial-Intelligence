import java.util.ArrayList;

public class NodeVariable {

    private ArrayList<String> values;
    private String varName;

    // Getters
    public ArrayList<String> getValues(){
        return this.values;
    }
    public String getName(){
        return this.varName;
    }

    // Setters
    public void setValues(ArrayList<String> vals){
        this.values = vals;
    }
    public void setName(String name){
        this.varName = name;
    }

    // Constructors
    public NodeVariable(ArrayList<String> vals, String name){
        if (vals == null || name == null) {
            throw new IllegalArgumentException("Values and name cannot be null");
        }
        this.values = new ArrayList<>(vals);
        this.varName = name;
    }

    public NodeVariable(){
        this.values = new ArrayList<>();
        this.varName = null;
    }
    // Copy 
    public NodeVariable(NodeVariable other){
        String name = other.getName();
        ArrayList<String> values = new ArrayList<>();
        for(String s : other.values){
            values.add(s);
        }
        this.setName(name);
        this.setValues(values);
    }

    // Print
    public void Print(){
        System.out.println("Name:" + this.varName + ", " + "Values: " + this.getValues().toString());
    }

    
}
