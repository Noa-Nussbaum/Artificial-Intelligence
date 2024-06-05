import java.util.ArrayList;
import java.util.Arrays;


public class NodeFactor {

    // Create array list of probabililities
    private ArrayList<Double> probs; 

    // Create array list of values the factor has
    private ArrayList<String> vals; // -> 'indexes'

    // Create arraylist of nodes
    private ArrayList<AlgNode> vars; 


    // Constructors
    public NodeFactor(){
        this.probs = new ArrayList<Double>();
        this.vals = new ArrayList<String>();
        this.vars = new ArrayList<AlgNode>();
    }

    public NodeFactor(ArrayList<Double> probabilities, ArrayList<AlgNode> variables){
        this.probs = probabilities;
        

        this.vals = new ArrayList<String>();
        this.vars = variables;
        for(AlgNode node : variables){
            for(String value : node.getValues()){
                vals.add(value);
            }
        }
        set_probsIndexes();
    }
// this method initialize the indexes (values of vars) in each row in the factor.
    private void set_probsIndexes() {
        String[] probIndexes = new String[this.probs.size()];
        String[] values = vars.get(vars.size()-1).getValues().toArray(new String[0]);
       
        
        int value_index = 0;
        for (int i = 0; i<probIndexes.length; i++){
            probIndexes[i] = values[value_index];
            value_index = (value_index+1) % values.length;
        }

        if(this.vars.size() > 1){
            int var_index = this.vars.size()-2;
            while( var_index >= 0){
                String first_str = probIndexes[0];
                values = vars.get(var_index).getValues().toArray(new String[0]);
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

        this.vals = new ArrayList<>(Arrays.asList(probIndexes));
    }
@Override
public String toString() {
    String build = "Variables=\n";
    for (AlgNode node : vars){
        build += node.getName() + " " + node.getValues() + "\n";
    } 
    build += "\n" + "Value Probability\n";
    for(int i=0; i<probs.size(); i++)
    build +=  vals.get(i).toString() +" | " + probs.get(i) + "\n";

    return build;
}

    
}
