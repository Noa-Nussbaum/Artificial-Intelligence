import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VariableElimination {
    
    private String[] relevantVariables;
    private ArrayList<NodeFactor> factors;
    private int additions;
    private int multiplications;
    private boolean immediate;
    private String answer;
    private BayesianNetwork network;
    private String queryName;
    private String[] evidence;


    public VariableElimination(String Query, String QueryValue, BayesianNetwork network, String[] evidence, String[] evidenceValues, ArrayList<String> hidden){
        // network.print();
        this.network = network;
        this.queryName = Query;
        this.evidence = evidence;
        ArrayList<NodeFactor> factors = new ArrayList<>();
        List<AlgNode> allNodes = new ArrayList<>(network.getNodesList().values());


        for(AlgNode node : allNodes){
            factors.add(node.getFactor());
        }
        whichVariables();
        factors = NodeFactor.restrict(factors, evidence, evidenceValues);

    }

    public String run(){
        return answer = "Nisht";
    }

    public String[] removeString(String[] original, String remove){
        String[] answer = new String[original.length-1];
        int index = 0;
        for(int i=0; i<original.length; i++){
            if(!original[i].equals(remove)){
                answer[index] = original[i];
                index++;
            }
        }
        return answer;
    }

// Retrieve variables we need for algorithm
    public String[] whichVariables(){

        // Retrieve all variable names in the network
        String[] allVarsNames = new String[this.network.getNodesList().size()];
        for(int i=0; i<this.network.getNodesList().size(); i++){
            allVarsNames[i] = this.network.getNodeNames().get(i);
        }

        // Irrelevant variables
        ArrayList<String> irrelevant = new ArrayList<>(); 

        for(int i=0; i<allVarsNames.length; i++){

            // Evidence 
            if(this.evidence.length > 0 && this.evidence != null){
                for(String e : evidence){
                    if(e.equals(allVarsNames[i])){
                        continue;
                    }
                }
            }
            // Actual query
            if(this.queryName.equals(allVarsNames[i])){
                continue;
            }
            // Only nodes that are ancestors of the query or the evidence stay in the list 
            if(!network.ancestor(evidence, allVarsNames[i], queryName)){
                irrelevant.add(allVarsNames[i]);
                continue;
            }
            // Variables that are ancestors that depend on the query stay in the list
            // Let's use our existing Bayes Ball algorithm to see
            ArrayList<String> evidenceInArray = new ArrayList<>(Arrays.asList(evidence));
            ArrayList<AlgNode> passed = new ArrayList<>();
             if(network.BayesBall(network, allVarsNames[i], queryName, evidenceInArray, "", passed)){
                irrelevant.add(allVarsNames[i]);
             }  
            //  Remove irrelevant nodes
            if(! irrelevant.isEmpty()){
                for(int j=0; j< irrelevant.size(); j++){
                    System.out.println(irrelevant.get(j));
                    allVarsNames = removeString(allVarsNames, irrelevant.get(j));
                }
            }

        }

        return allVarsNames;
     }




}
