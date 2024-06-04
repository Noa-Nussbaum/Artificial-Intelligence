import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

// import javafx.util.Pair;

public class NodeFactor {

    // Create array list of probabililities
    private ArrayList<Double> probs;

    // Create array list of indexes
    private ArrayList<String> place;

    // Create hashmap of variables
    private ArrayList<AlgNode> vars;


    // Constructors
    public NodeFactor(){
        this.probs = new ArrayList<Double>();
        this.place = new ArrayList<String>();
        this.vars = new ArrayList<AlgNode>();
    }

    public NodeFactor(ArrayList<Double> probabilities, ArrayList<AlgNode> variables){
        this.probs = probabilities;
        this.place = new ArrayList<String>();
        this.vars = variables;
    }

    public NodeFactor(ArrayList<Double> probabilities, ArrayList<AlgNode> variables, ArrayList<String> places){
        this.probs = probabilities;
        this.place = places;
        this.vars = variables;
    }


    // Setters
    // Add new values
    // public void setVariable(String name, ArrayList<String> vals){

    // }

    @Override
public String toString() {
    StringBuilder builder = new StringBuilder();
    
    // Adding a header for better readability
    // builder.append(String.format("%-10s %-30s %-40s\n", "Variable", "Values", "Probabilities"));
    builder.append("\n-----------------------------------------------------------------------------------\n");

    // Loop through each variable and print its corresponding values and probabilities
    for (AlgNode var : vars) {
        // Retrieve the variable's name
        String varName = var.getName();
        
        // Format values into a cleaner, comma-separated string without brackets
        String valuesFormatted = var.getValues().toString().replaceAll("\\[|\\]", "");
        
        builder.append(varName+ " [" + valuesFormatted + "]\n");

    }

    // Prepare to append probabilities associated with this variable
    StringBuilder probBuilder = new StringBuilder();
    for (Double p : probs) {  // Assuming `getProbabilities()` method exists in AlgNode
    // probBuilder.append(String.format("%.2f, ", p));
    probBuilder.append(p + ", ");
    }
    // Remove the trailing comma and space
    if (probBuilder.length() > 0) {
        probBuilder.setLength(probBuilder.length() - 2);
    }

    // builder.append(String.format("%-40s\n","[" + probBuilder.toString() + "]"));
    builder.append("[" + probBuilder.toString() + "]");

    return builder.toString();
}


    
}
