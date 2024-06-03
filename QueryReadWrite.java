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
                System.out.println("p");
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

        // Assume a method in BayesianNetwork: boolean areIndependent(String node1, String node2, List<String> evidence)
        // boolean independent = network.areIndependent(nodes[0], nodes[1], evidence);
        // return independent ? "yes" : "no";
        return "yes";
    }

    private String processVariableEliminationQuery(String query, BayesianNetwork network) {
        // Implement variable elimination logic here
        // String answer = VariableElimination(query, network);
        // For now, returning a placeholder result
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
