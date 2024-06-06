import java.util.ArrayList;
import java.util.Arrays;


public class NodeFactor {

    // Create array list of probabililities
    private ArrayList<Double> probs; 

    // Create array list of values the factor has
    private ArrayList<String> variableValues; // -> 'indexes'

    // Create arraylist of nodes
    private ArrayList<AlgNode> nodes; 

    // Create list of variables and their possible values
    private static ArrayList<NodeVariable> factorVariables;

    // Getters
    public ArrayList<AlgNode> getNodes(){
        return this.nodes;
    }

    // Returns arrayList of variables
    public ArrayList<NodeVariable> getVariablesObjects(){
        return this.factorVariables;
    }

    // Constructors
    public NodeFactor(){
        this.probs = new ArrayList<Double>();
        this.variableValues = new ArrayList<String>();
        this.nodes = new ArrayList<AlgNode>();
        this.factorVariables = new ArrayList<NodeVariable>();
    }

    public NodeFactor(ArrayList<Double> probabilities, ArrayList<AlgNode> nodes){
        this.nodes = nodes;
        this.factorVariables = new ArrayList<NodeVariable>();
        this.variableValues = new ArrayList<String>();
        for(AlgNode node : nodes){
            NodeVariable newVar = new NodeVariable();
            newVar.setName(node.getName());
            newVar.setValues(node.getValues());
            factorVariables.add(newVar);
            for(String value : node.getValues()){
                variableValues.add(value);
            }
        }
        this.probs = probabilities;
        // Arrange the rows
        valuePerProb();
    }

    // Set the rows and their values in the factor
    private void valuePerProb() {
        // Initialize values and probabilities
        String[] rowNames = new String[this.probs.size()];
        String[] values = nodes.get(nodes.size()-1).getValues().toArray(new String[0]);
       
        
        int value_index = 0;
        for (int i = 0; i<rowNames.length; i++){
            rowNames[i] = values[value_index];
            value_index = (value_index+1) % values.length;
        }

        if(this.nodes.size() > 1){
            int var_index = this.nodes.size()-2;
            while( var_index >= 0){
                String first_str = rowNames[0];
                values = nodes.get(var_index).getValues().toArray(new String[0]);
                value_index = 0;
                for (int i = 0; i<rowNames.length; i++){
                    if(rowNames[i].equals(first_str)){
                        rowNames[i] = values[value_index] + "," + rowNames[i];
                        value_index = (value_index+1) % values.length;
                    }
                    else{
                        int previous_value_index = (value_index-1)%values.length;
                        if (previous_value_index<0) { previous_value_index+=values.length; }
                        rowNames[i] = values[previous_value_index] + "," + rowNames[i];
                    }
                }
                var_index--;
            }
        }

        this.variableValues = new ArrayList<>(Arrays.asList(rowNames));
    }

    // Factor print function
    @Override
    public String toString() {
        String build = "Variables=\n";
        for (AlgNode node : nodes){
            build += node.getName() + " " + node.getValues() + "\n";
        } 
        build += "\n" + "Value Probability\n";
        for (AlgNode node : nodes){
            build += node.getName() + " ";
        } 
        build +="\n";
        for(int i=0; i<probs.size(); i++)
        build +=  variableValues.get(i).toString() +" | " + probs.get(i) + "\n";

        return build;
    }

    // Checks if the given variable name is in this factor's variables
    public boolean isVarInVars(String name){
        for(int i = 0; i< this.getVars().length; i++){
            if(name.equals(this.getVars()[i])){
                return true;
            }
        }
        return false;
    }

    // Returns array of variables that this factor has
    public String[] getVars(){
        String[] answer = new String[this.nodes.size()];
        for(int i = 0; i< this.nodes.size(); i++){
            answer[i] = this.nodes.get(i).getName();
        }
        
        return answer;
    }

    // Here we restrict the factors to only contain the variables with the values given in the evidence
    public static ArrayList<NodeFactor> restrict(ArrayList<NodeFactor> factors, String[] evidence, String[] evidenceValues){
        ArrayList<NodeFactor> answer = new ArrayList<NodeFactor>();

        // We need to iterate through each factor -> each evidence
        // If the factor contains the relevant evidence variable in vars -> then remove the rows and values that we don't need
        // Add to answer
        // Let's iterate through all the factors and only add to answer those which do not contain evidence

        // for every factor
        for(int i=0; i<factors.size(); i++){
            // System.out.println(factors.get(i).variableValues.get(0));
            // for every evidence
            for(int m=0; m<evidence.length; m++){
                if(factors.get(i).isVarInVars(evidence[m])){
                    // for every row in factor
                    for(int j=0; j<factors.get(i).probs.size(); j++){
                        
                    }

                    
                    
                }
            }
        }



        return answer;
    }

    
}
