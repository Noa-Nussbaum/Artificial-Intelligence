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
        this.values = vals;
        this.varName = name;
    }

    public NodeVariable(){
        this.values = new ArrayList<>();
        // this.varName = null;
    }

    // Print
    public void Print(){
        System.out.println("Name:" + this.varName + ", " + "Values: " + this.getValues().toString());
    }

    
}
