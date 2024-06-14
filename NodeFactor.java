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
    private ArrayList<NodeVariable> factorNodeVariables;

    // Getters
    public ArrayList<AlgNode> getNodes(){
        return this.nodes;
    }
    public ArrayList<Double> getProbabilities(){
        return this.probs;
    }
    public ArrayList<String> getVariableValues(){
        return this.variableValues;
    }

    // Returns arrayList of variables
    public ArrayList<NodeVariable> getVariablesObjects(){
        return this.factorNodeVariables;
    }

    private void initialize() {
        this.probs = new ArrayList<Double>();
        this.variableValues = new ArrayList<String>();
        this.nodes = new ArrayList<AlgNode>();
        this.factorNodeVariables = new ArrayList<NodeVariable>();
    }
    

    // Constructors
    public NodeFactor(){
        initialize();
    }

    public NodeFactor(ArrayList<Double> probabilities, ArrayList<NodeVariable> variables, ArrayList<String> values){
        initialize();
        this.probs = probabilities;
        this.factorNodeVariables = variables;
        this.nodes = new ArrayList<AlgNode>();
        this.variableValues = values;
        valuePerProb();
    }

    public NodeFactor(ArrayList<Double> probabilities, ArrayList<AlgNode> nodes){
        initialize();
        this.nodes = nodes;
        this.factorNodeVariables = new ArrayList<NodeVariable>();

        for(int i=0; i<nodes.size(); i++){
            NodeVariable vari = new NodeVariable(nodes.get(i).getValues(), nodes.get(i).getName());
            this.factorNodeVariables.add(vari);
        }
        this.probs = probabilities;
        // Arrange the rows
        valuePerProb();
       
    }

    public String StringArrayToString(String[] array) {
        String answer = "";
        for (int i = 0; i < array.length; i++) {
            answer += array[i];
        }
        return answer;
    }


    private void valuePerProb() {
        if (this.probs.isEmpty() || this.factorNodeVariables.isEmpty()) return;
        String[] probIndexes = new String[this.probs.size()];
        NodeVariable last_var = this.factorNodeVariables.get(this.factorNodeVariables.size()-1);
        String[] values = last_var.getValues().toArray(new String[0]);
        int value_index = 0;
        for (int i = 0; i<probIndexes.length; i++){
            probIndexes[i] = values[value_index];
            value_index = (value_index+1) % values.length;
        }

        if(this.factorNodeVariables.size() > 1){
            int var_index = this.factorNodeVariables.size()-2;
            while( var_index >= 0){
                String first_str = probIndexes[0];
                NodeVariable var = this.factorNodeVariables.get(var_index);
                values = var.getValues().toArray(new String[0]);
                value_index = 0;
                for (int i = 0; i<probIndexes.length; i++){
                    if(probIndexes[i].equals(first_str)){
                        probIndexes[i] = values[value_index] + "," + probIndexes[i];
                        value_index = (value_index+1) % values.length;
                    }
                    else{
                        int previous_value_index = (value_index-1)%values.length;
                        if (previous_value_index<0) { previous_value_index+=values.length; }
                        probIndexes[i] = values[previous_value_index] + "," + probIndexes[i];
                    }
                }
                var_index--;
            }
        }

        this.variableValues = new ArrayList<>(Arrays.asList(probIndexes));
    }

    // Factor print function
    @Override
    public String toString() {
        String build = "NodeVariables=\n";
        for (NodeVariable var : factorNodeVariables){
            build += var.getName() + " " + var.getValues() + "\n";
        } 
        build += "\n" + "Value Probability\n";
        for (NodeVariable var : this.factorNodeVariables){
            build += var.getName() + " ";
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

        String answer[] = new String[this.factorNodeVariables.size()];
        for(int i=0; i<this.factorNodeVariables.size(); i++){
            answer[i] = this.factorNodeVariables.get(i).getName();
        }
        
        return answer;
    }


    // public NodeFactor copy(NodeFactor other){
    //     NodeFactor copy_factor = new NodeFactor( this.getProbabilities(),this.getVariablesObjects(), this.getVariableValues());
    //     return copy_factor;
    // }

      // Deep Copy Constructor
      public NodeFactor(NodeFactor other) {
        this.probs = new ArrayList<>(other.probs); // Deep copy of probabilities
        this.variableValues = new ArrayList<>(other.variableValues); // Deep copy of variable values
        
        // Deep copy of nodes - assuming AlgNode has a proper copy constructor
        // this.nodes = new ArrayList<>();
        // for (AlgNode node : other.nodes) {
        //     this.nodes.add(new AlgNode(node)); // Assumes AlgNode has a copy constructor
        // }
        
        // Deep copy of NodeVariables - assuming NodeVariable has a proper copy constructor
        this.factorNodeVariables = new ArrayList<>();
        for (NodeVariable var : other.factorNodeVariables) {
            this.factorNodeVariables.add(new NodeVariable(var)); // Assumes NodeVariable has a copy constructor
        }
        
        // Ensure any computed properties are correctly initialized
        valuePerProb(); // Recompute any derived values if necessary
    }

    public void setVariableValues(ArrayList<String> vals){
        this.variableValues = vals;
    }
    public void setProbabilities(ArrayList<Double> probs){
        this.probs = probs;
    }
    public void setVariables(ArrayList<NodeVariable> vars){
        this.factorNodeVariables = vars;
    }


    

    


    
}