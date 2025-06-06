import java.io.*;
import java.util.Scanner;

public class Ex1 {

    static String inputFileName = "input.txt";
    static File inputFile = new File(inputFileName);

    public static void main(String[] args) {
        try (Scanner scanFile = new Scanner(inputFile)) {
            // Retrieve XML file name from the first row of the input file
            String XMLFile = scanFile.nextLine();
            BayesianNetwork network = XMLReaderUtil.XMLReader(XMLFile);

            // Retrieve queries and write results into the output file
            QueryReadWrite queryReadWrite = new QueryReadWrite();
            queryReadWrite.readAndWrite(XMLFile,scanFile, network, "output.txt");

        } catch (FileNotFoundException e) {
            System.out.println("File reading error:");
            e.printStackTrace();
            return; // Exit if the file cannot be found
        } catch (Exception e) {
            System.out.println("An error occurred:");
            e.printStackTrace();
            return; // Handle other potential exceptions and exit
        }
    }
}
