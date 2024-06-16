import java.util.ArrayList;
import java.util.Arrays;

public class NodeFactor {

    // Create array list of probabililities
    private ArrayList<Double> probs;

    // Create array list of values the factor has
    private ArrayList<String> variableValues;

    // Create arraylist of nodes
    private ArrayList<AlgNode> nodes;

    // Create list of variables and their possible values
    private ArrayList<NodeVariable> factorNodeVariables;

    // Getters
    public ArrayList<AlgNode> getNodes() {
        return this.nodes;
    }

    public ArrayList<Double> getProbabilities() {
        return this.probs;
    }

    public ArrayList<String> getVariableValues() {
        return this.variableValues;
    }

    // Returns arrayList of variables
    public ArrayList<NodeVariable> getVariablesObjects() {
        return this.factorNodeVariables;
    }

    private void initialize() {
        this.probs = new ArrayList<Double>();
        this.variableValues = new ArrayList<String>();
        this.nodes = new ArrayList<AlgNode>();
        this.factorNodeVariables = new ArrayList<NodeVariable>();
    }

    // Returns array of variables names that this factor has
    public String[] getVars() {

        String answer[] = new String[this.factorNodeVariables.size()];
        for (int i = 0; i < this.factorNodeVariables.size(); i++) {
            answer[i] = this.factorNodeVariables.get(i).getName();
        }

        return answer;
    }

    // Setters
    public void setVariableValues(ArrayList<String> vals) {
        this.variableValues = vals;
    }

    public void setProbabilities(ArrayList<Double> probs) {
        this.probs = probs;
    }

    public void setVariables(ArrayList<NodeVariable> vars) {
        this.factorNodeVariables = vars;
    }

    // Constructors
    public NodeFactor() {
        initialize();
    }

    public NodeFactor(ArrayList<Double> probabilities, ArrayList<NodeVariable> variables, ArrayList<String> values) {
        initialize();
        this.probs = probabilities;
        this.factorNodeVariables = variables;
        this.nodes = new ArrayList<AlgNode>();
        this.variableValues = values;
        valuePerProb();
    }

    public NodeFactor(ArrayList<Double> probabilities, ArrayList<AlgNode> nodes) {
        initialize();
        this.nodes = nodes;
        this.factorNodeVariables = new ArrayList<NodeVariable>();

        for (int i = 0; i < nodes.size(); i++) {
            NodeVariable vari = new NodeVariable(nodes.get(i).getValues(), nodes.get(i).getName());
            this.factorNodeVariables.add(vari);
        }
        this.probs = probabilities;
        // Arrange the rows
        valuePerProb();

    }

    // Deep Copy Constructor
    public NodeFactor(NodeFactor other) {
        this.probs = new ArrayList<>(other.probs); // Deep copy of probabilities
        this.variableValues = new ArrayList<>(other.variableValues); // Deep copy of variable values
        this.factorNodeVariables = new ArrayList<>();
        for (NodeVariable var : other.factorNodeVariables) {
            this.factorNodeVariables.add(new NodeVariable(var)); // Assumes NodeVariable has a copy constructor
        }

        // Ensure any computed properties are correctly initialized
        valuePerProb(); // Recompute any derived values if necessary
    }

    /**
     * Converts an array of strings to a single string representation.
     * 
     * @param array The array to be converted.
     * @return A string representing the concatenated array elements.
     */
    public String StringArrayToString(String[] array) {
        String answer = "";
        for (int i = 0; i < array.length; i++) {
            answer += array[i];
        }
        return answer;
    }

    /**
     * Organizes the 'variableValues' based on the 'probs' and
     * 'factorNodeVariables'.
     * This method calculates the mapping from probabilities to variable
     * configurations.
     */
    private void valuePerProb() {
        if (this.probs.isEmpty() || this.factorNodeVariables.isEmpty())
            return;

        String[] lines = new String[this.probs.size()];
        NodeVariable last = this.factorNodeVariables.get(this.factorNodeVariables.size() - 1);
        String[] values = last.getValues().toArray(new String[0]);
        int ind = 0;
        
        for (int i = 0; i < lines.length; i++) {
            lines[i] = values[ind];
            ind = (ind + 1) % values.length;
        }
        // Adjust configurations for all variables in the factor
        if (this.factorNodeVariables.size() > 1) {
            adjustConfigurations(lines);
        }
        this.variableValues = new ArrayList<>(Arrays.asList(lines));
    }

    /**
     * Adjusts configurations for all variables based on the probability indexes.
     * 
     * @param probIndexes Current mapping of probabilities to variable
     *                    configurations.
     */
    private void adjustConfigurations(String[] probIndexes) {
        int var_index = this.factorNodeVariables.size() - 2;
        while (var_index >= 0) {
            NodeVariable var = this.factorNodeVariables.get(var_index);
            recomputeConfigurations(var, probIndexes);
            var_index--;
        }
    }

    /**
     * Recomputes the configurations for a particular variable.
     * 
     * @param var         The variable to recompute configurations for.
     * @param probIndexes Current mapping of probabilities to variable
     *                    configurations.
     */
    private void recomputeConfigurations(NodeVariable var, String[] probIndexes) {
        String first_str = probIndexes[0];
        String[] values = var.getValues().toArray(new String[0]);
        int value_index = 0;
        for (int i = 0; i < probIndexes.length; i++) {
            if (probIndexes[i].equals(first_str)) {
                probIndexes[i] = values[value_index] + "," + probIndexes[i];
                value_index = (value_index + 1) % values.length;
            } else {
                int previous_value_index = (value_index - 1) % values.length;
                if (previous_value_index < 0) {
                    previous_value_index += values.length;
                }
                probIndexes[i] = values[previous_value_index] + "," + probIndexes[i];
            }
        }
    }

    // Factor print function
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("NodeVariables=\n");
        for (NodeVariable var : factorNodeVariables) {
            builder.append(var.getName() + " " + var.getValues() + "\n");
        }
        builder.append("\n" + "Value Probability\n");
        for (NodeVariable var : this.factorNodeVariables) {
            builder.append(var.getName() + " ");
        }
        builder.append("\n");
        for (int i = 0; i < probs.size(); i++)
            builder.append(variableValues.get(i).toString() + " | " + probs.get(i) + "\n");

        return builder.toString();
    }

    // Checks if the given variable name is in this factor's variables
    public boolean isVarInVars(String name) {
        for (int i = 0; i < this.getVars().length; i++) {
            if (name.equals(this.getVars()[i])) {
                return true;
            }
        }
        return false;
    }

}