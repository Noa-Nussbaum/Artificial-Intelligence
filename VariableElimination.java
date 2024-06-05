import java.util.ArrayList;
import java.util.List;

public class VariableElimination {
    
    private String[] relevantVariables;
    private ArrayList<NodeFactor> factors;
    private int additions;
    private int multiplications;
    private boolean immediate;
    private String answer;

    public VariableElimination(String Query, String QueryValue, BayesianNetwork network, String[] evidence, String[] evidenceValues, ArrayList<String> hidden){
        // network.print();
        ArrayList<NodeFactor> factors = new ArrayList<>();
        List<AlgNode> allNodes = new ArrayList<>(network.getNodesList().values());


        for(AlgNode node : allNodes){
            factors.add(node.getFactor());
        }

        // System.out.println(factors);

        factors = NodeFactor.restrict(factors, evidence, evidenceValues);

    }

    public String run(){
        return answer = "Nisht";
    }
}
