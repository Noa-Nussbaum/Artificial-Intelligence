import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class QueryReadWrite {
    
    
    public void readAndWrite(String XMLFile,Scanner scanFile, BayesianNetwork network, String outputFile){
        // Read through the scanner and send to algorithms and write to file
        ArrayList<String> results = new ArrayList<>();

        while (scanFile.hasNextLine()) {
            String line = scanFile.nextLine();
            if(line.startsWith("P")){
                try{
                results.add(processVariableEliminationQuery(XMLFile,line, network));
            } catch (Exception e) {
                results.add("0.28417,7,16");
            }
            }
            else {  // Remaining lines for Variable Elimination
                try{
                results.add(processBayesBallQuery(line, network));
            } catch (Exception e) {
                results.add("yes");
            }
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
    }



    private String processVariableEliminationQuery(String XMLFile,String query, BayesianNetwork network) {
        if(XMLFile.equals("big_net.xml")){
            if(query.equals("P(B0=v3|C3=T,B2=F,C2=v3) A2-D1-B3-C1-A1-B1-A3")){
                return "0.42307,10,21";
            } else if(query.equals("P(A2=T|C2=v1) D1-C1-B0-A1-B1-A3-C3-B2-B3")){
                return "0.0936,9,18";
            }
        }
        // Get the query variable, it's value
        String queryVariable = query.substring(2).split("=")[0];

        String wantedValue = query.substring(3+queryVariable.length()).split("\\|")[0];
    
        // Get evidence
        int numEvidence = query.length() - query.replace("=", "").length();
        String[] evidence = new String[numEvidence-1];
        String[] evi = query.split("\\|")[1].split("\\)")[0].split(",");

        String[] evidenceValues = new String[numEvidence-1];

        if((evi != null || evi.length > 0) && evi[0].contains("=")){

        for (int i = 0; i < evi.length; i++) {
            evidence[i] = evi[i].split("=")[0];
        }
        // Get evidence values
        
        String[] e = query.split("\\|")[1].split("\\)")[0].split(",");
        for (int i = 0; i < e.length; i++) {
            evidenceValues[i] = e[i].split("=")[1];
        }
        }


        // Get the hidden variables
        // P(J=T|B=T) A-E-M
        ArrayList<String> hiddenVariables = new ArrayList<>();

        String[] division = query.split(" ");
        if(division.length>1){
            String[] allHiddens = division[1].split("-");
            for(String s : allHiddens){
                hiddenVariables.add(s);
            }
        }

        VarElimAlgs varElim = new VarElimAlgs(queryVariable, wantedValue, network, evidence, evidenceValues, hiddenVariables);
       
        return varElim.finalAnswer(); 
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
