import java.io.*;
import java.util.Scanner;


public class Ex1 {
    
    // Read from the file
    static String inputFileName = "input.txt";
    static Scanner scanFile;
    static File inputFile = new File(inputFileName);

    // If error while reading the file
    static {
        try {
            scanFile = new Scanner(inputFile);
        } catch (FileNotFoundException e) {
            System.out.println("File reading error");
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException{
        
        // Retrieve XML file name from first row of input file
        String XMLFile = scanFile.nextLine();
        BayesianNetwork network = XMLReaderUtil.XMLReader(XMLFile);
        network.print();
        
        // Retrieve queries and write results into output file
        QueryReadWrite queryReadWrite = new QueryReadWrite();
        queryReadWrite.readAndWrite(scanFile, network, "outputFile");
        scanFile.close();

    }
}
