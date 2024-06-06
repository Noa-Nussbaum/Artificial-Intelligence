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
    private String[] irrelevant;

    public VariableElimination(String Query, String QueryValue, BayesianNetwork network, String[] evidence,
            String[] evidenceValues, ArrayList<String> hidden) {
        // network.print();
        this.network = network;
        this.queryName = Query;
        this.evidence = evidence;
        ArrayList<NodeFactor> factors = new ArrayList<>();
        List<AlgNode> allNodes = new ArrayList<>(network.getNodesList().values());
        whichVariables();

        beginFactors();
        for (AlgNode node : allNodes) {
            factors.add(node.getFactor());
        }

        factors = NodeFactor.restrict(factors, evidence, evidenceValues);

    }

    public String run() {
        return answer = "Nisht";
    }

    public String[] removeString(String[] original, String remove) {
        String[] answer = new String[original.length - 1];
        int index = 0;
        for (int i = 0; i < original.length; i++) {
            if (!original[i].equals(remove)) {
                answer[index] = original[i];
                index++;
            }
        }
        return answer;
    }

    public static boolean containsString(String[] array, String target) {
        return Arrays.asList(array).contains(target);
    }

    // Retrieve variables we need for algorithm
    public void whichVariables() {

        // Retrieve all variable names in the network
        String[] allVarsNames = new String[this.network.getNodesList().size()];
        for (int i = 0; i < this.network.getNodesList().size(); i++) {
            allVarsNames[i] = this.network.getNodeNames().get(i);
        }

        // Irrelevant variables
        ArrayList<String> irrelevant = new ArrayList<>();

        for (int i = 0; i < allVarsNames.length; i++) {

            // Evidence
            if (this.evidence.length > 0 && this.evidence != null) {

                if (containsString(evidence, allVarsNames[i])) {
                    continue;
                }
            }
            // Actual query
            if (this.queryName.equals(allVarsNames[i])) {
                continue;
            }
            // Only nodes that are ancestors of the query or the evidence stay in the list
            if (!network.ancestor(evidence, allVarsNames[i], queryName)) {
                irrelevant.add(allVarsNames[i]);
                continue;
            }
            // Variables that are ancestors that depend on the query stay in the list
            // Let's use our existing Bayes Ball algorithm to see
            ArrayList<String> evidenceInArray = new ArrayList<>(Arrays.asList(evidence));
            ArrayList<AlgNode> passed = new ArrayList<>();
            if (network.BayesBall(network, allVarsNames[i], queryName, evidenceInArray, "", passed)) {
                // System.out.println("this"+allVarsNames[i]);
                irrelevant.add(allVarsNames[i]);
            }
            // Remove irrelevant nodes
            if (!irrelevant.isEmpty()) {
                for (int j = 0; j < irrelevant.size(); j++) {
                    allVarsNames = removeString(allVarsNames, irrelevant.get(j));
                }
            }
        }

        // Set classes irrelevant variables to this one
        this.irrelevant = irrelevant.toArray(new String[0]);
        this.relevantVariables = allVarsNames;
        // return allVarsNames;
    }

    // If one of the factors contains the final answer to the query, return yes.
    // Otherwise return no
    public String easyToAnswer() {
        String[] list = new String[evidence.length+1];  
        for(int i=0; i<evidence.length; i++){
            list[i] = evidence[i];
        }
        int flag = 1;
        list[evidence.length] = queryName;
        for(int i=0; i<factors.size(); i++){
            NodeFactor fac = factors.get(i);
            if(fac.getVariablesObjects().size() == list.length){ 

                for(int j=0; j<fac.getVars().length; j++){

                    String varName = fac.getVars()[j];
                    if(containsString(list, varName) == false){
                        flag = 0;
                        break;
                    }
                }
                if(flag == 1){
                    return "Yes";
                }
            }
        }
        
        return "No";
    }

    public ArrayList<NodeFactor> beginFactors() {

        // Retrieve relevant factors
        ArrayList<NodeFactor> answer = new ArrayList<>();
        for (int i = 0; i < this.relevantVariables.length; i++) {
            answer.add(this.network.getNode(relevantVariables[i]).getFactor());
        }
        this.factors = answer;


        // If one of the factors contains this exact query value then we're done
        if (easyToAnswer() == "Yes") {
            return answer;
        }
        for(int i=0; i<answer.size(); i++){
            NodeFactor fac = answer.get(i);
            for(int j=0; j<fac.getVars().length; j++){
                if(containsString(evidence, fac.getVars()[j])){
                    if(fac.getVars().length==1){
                        answer.remove(i);
                        i--;
                        break;
                    }
                    else{
                        
                    }
                }
            }
        }






        return answer;
    }

}
