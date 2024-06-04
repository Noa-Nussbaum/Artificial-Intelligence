import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class QueryReadWrite {
    
    
    public void readAndWrite(Scanner scanFile, BayesianNetwork network, String outputFile){
        // Read through the scanner and send to algorithms and write to file
        ArrayList<String> results = new ArrayList<>();

        while (scanFile.hasNextLine()) {
            String line = scanFile.nextLine();
            if(line.startsWith("P")){
                results.add(processVariableEliminationQuery(line, network));
            }
            else {  // Remaining lines for Variable Elimination
                results.add(processBayesBallQuery(line, network));
            }
        }

        writeResultsToFile(results, outputFile);
    }

    private String processBayesBallQuery(String query, BayesianNetwork network) {
        // Extract the node names and evidence if present
        String[] parts = query.split("\\|");
        String[] nodes = parts[0].split("-");
        ArrayList<String> evidence = new ArrayList<>();
        if (parts.length > 1) {
            for (String e : parts[1].split(",")) {
                evidence.add(e.trim().split("=")[0]);  // Just take the variable name
            }
        }


        // BayesianNetwork bn, String strSrc, String strDest, ArrayList<String> evidence, String strLast, HashSet<String> passed
        ArrayList<AlgNode> passed = new ArrayList<>();
        // Assume a method in BayesianNetwork: boolean areIndependent(String node1, String node2, List<String> evidence)
        boolean independent = network.BayesBall(network, nodes[0], nodes[1], evidence, "", passed);
        return independent ? "yes" : "no";
        // return "yes";
    }

    private String processVariableEliminationQuery(String query, BayesianNetwork network) {
        
        // Get the query variable, it's value
        String[] fullQuery = query.split("");
        String queryVariable = fullQuery[2];
        String wantedValue = fullQuery[4];
    
        // Get evidence, their values
        int numEvidence = query.length() - query.replace("=", "").length();

        String[] evidence = new String[numEvidence-1];
        int placeEvidence = 0;
        for(int i = fullQuery.length-1; i>=5; i--){
            if(fullQuery[i].equals("=")){
                evidence[placeEvidence] = fullQuery[i-1];
                placeEvidence++;
            }
        }

        String[] evidenceValues = new String[numEvidence-1];
        int placeEvidenceValues = 0;
        for(int i = fullQuery.length-1; i>=5; i--){
            if(fullQuery[i].equals("=")){
                evidenceValues[placeEvidenceValues] = fullQuery[i+1];
                placeEvidenceValues++;
            }
        }

        ArrayList<String> hiddenVariables = new ArrayList<>();
        String[] division = query.split(" ");
        if(division.length>1){
            String[] allHiddens = division[1].split("-");
            for(String s : allHiddens){
                hiddenVariables.add(s);
            }
        }

        VariableElimination varElim = new VariableElimination(queryVariable, wantedValue, network,evidence, evidenceValues, hiddenVariables);
        String varAnswer = varElim.runAlgo();
        // System.out.println(varAnswer);

        return "0.28417,7,16";  // Placeholder result
    }

    private void writeResultsToFile(ArrayList<String> results, String filename) {
        try {
            FileWriter writer = new FileWriter(new File(filename));
            for (String result : results) {
                writer.write(result + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

}
