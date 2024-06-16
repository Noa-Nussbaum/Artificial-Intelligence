import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;

public class VarElimAlgs {

    // Label these using thing to write @params
    // in the is easy check add the set immediate function

    // Relevant variables for the query
    private String[] relevantVariables;
    // List of factors involved in the elimination process
    private ArrayList<NodeFactor> factors;
    // Counter for additions performed during probability computations
    private int additions;
    // Counter for multiplications performed during probability computations
    private int multiplications;
    // The Bayesian network on which the variable elimination is performed
    private final BayesianNetwork network;
    // The query variable
    private String queryName;
    // Evidence variables provided to the query
    private String[] evidence;
    // Irrelevant variables that do not affect the query outcome
    private String[] irrelevant;
    // Values associated with the evidence variables
    private String[] evidenceVals;
    // Hidden variables to be eliminated
    private ArrayList<String> hiddens;
    // The final answer after computation
    private String finalAnswer;
    // The specific value of the query variable to compute the probability for
    private String queryval;

    public VarElimAlgs(String Query, String QueryValue, BayesianNetwork network, String[] evidence,
            String[] evidenceValues, ArrayList<String> hidden) {
        this.network = network;
        this.queryName = Query;
        this.queryval = QueryValue;
        this.evidence = evidence;
        this.factors = new ArrayList<>();
        this.additions = 0;
        this.multiplications = 0;
        this.hiddens = hidden;
        this.evidenceVals = evidenceValues;

        whichVariables();

        this.factors = beginFactors();

        this.varElim();

    }

    /**
     * Reverses an ArrayList of strings in place.
     * 
     * @param list The ArrayList to reverse.
     */
    public void reverseArrayList(ArrayList<String> list) {
        if (list == null || list.size() <= 1) {
            return; // No need to reverse if the list is null or too short
        }

        int left = 0; // Start pointer
        int right = list.size() - 1; // End pointer

        while (left < right) {
            // Swap the elements at left and right indices
            Collections.swap(list, left, right);

            // Move the pointers towards each other
            left++;
            right--;
        }
    }

    /**
     * Removes a specific string from an array and returns a new array.
     * 
     * @param original The original array.
     * @param remove   The string to be removed.
     * @return A new array excluding the removed string.
     */
    public String[] removeString(String[] original, String remove) {
        // Make sure it's in the array
        if (containsString(original, remove) == false) {
            return original;
        }
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

    /**
     * Checks if an array contains a specific target string.
     * 
     * @param array  The array to check.
     * @param target The string to find.
     * @return true if the array contains the string, false otherwise.
     */
    public boolean containsString(String[] array, String target) {
        return Arrays.asList(array).contains(target);
    }

    /**
     * Determines the relevant variables for the elimination process based on the
     * network's structure and the given evidence.
     * Retrieve variables names we need for algorithm
     */
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
        // return allVarsNames;
        this.relevantVariables = allVarsNames;
    }

    /**
     * Checks if a quick answer is available based on the current factors and the
     * query.
     * 
     * @return "Yes" if a quick answer is possible, "No" otherwise.
     */
    public String easyToAnswer() {
        String[] list = new String[evidence.length + 1];
        for (int i = 0; i < evidence.length; i++) {
            list[i] = evidence[i];
        }

        list[evidence.length] = queryName;
        for (int i = 0; i < factors.size(); i++) {
            NodeFactor fac = factors.get(i);
            if (fac.getVariablesObjects().size() == list.length) {
                int flag = 1;
                for (int j = 0; j < fac.getVars().length; j++) {

                    String varName = fac.getVars()[j];
                    if (containsString(list, varName) == false) {
                        flag = 0;
                        break;
                    }
                }
                if (flag == 1) {
                    return "Yes";
                }
            }
        }

        return "No";
    }

    /**
     * Initializes factors for the elimination process based on the relevant
     * variables.
     * 
     * @return A list of initialized factors.
     */
    public ArrayList<NodeFactor> beginFactors() {

        // Retrieve relevant factors
        ArrayList<NodeFactor> answer = new ArrayList<>();

        for (int i = 0; i < this.relevantVariables.length; i++) {
            answer.add(new NodeFactor(this.network.getNode(relevantVariables[i]).getFactor()));
        }

        this.factors = answer;

        // If one of the factors contains this exact query value then we're done
        if (easyToAnswer() == "Yes") {
            return answer;
        }

        for (int i = 0; i < answer.size(); i++) {
            NodeFactor fac = answer.get(i);
            for (String s : fac.getVars()) {
                fac = answer.get(i);
                if (containsString(evidence, s)) {
                    if (fac.getVars().length == 1) {
                        answer.remove(i);
                        i = i - 1;
                        break;
                    } else {
                        // Make sure we do this for all evidence in the factor
                        int indexOfEvidence = findIndex(evidence, s);
                        int indexOfVariable = findIndex(fac.getVars(), s);
                        NodeFactor newFac = restrict(fac, this.evidenceVals[indexOfEvidence],
                                fac.getVariablesObjects().get(indexOfVariable));
                        answer.set(i, newFac);
                    }
                }

            }
        }

        for (int i = 0; i < answer.size(); i++) {
            for (int j = 0; j < answer.get(i).getVars().length; j++) {
                // Remove any factors we know aren't relevant if they're in the list
                if (containsString(this.irrelevant, answer.get(i).getVars()[j])) {
                    answer.remove(i);
                    i = i - 1;
                    break;

                }
            }
        }

        return answer;
    }

    /**
     * Restricts a factor by a given variable and its value.
     * 
     * @param factor   The factor to restrict.
     * @param value    The value of the variable to restrict by.
     * @param variable The variable to restrict.
     * @return A new restricted factor.
     */
    public NodeFactor restrict(NodeFactor factor, String value, NodeVariable variable) {
        NodeFactor answer = new NodeFactor(factor);

        ArrayList<String> list = new ArrayList<>(Arrays.asList(factor.getVars()));

        int index = list.indexOf(variable.getName());

        ArrayList<String> indexes = new ArrayList<>();
        ArrayList<Double> probs = new ArrayList<>();
        ArrayList<NodeVariable> vars = answer.getVariablesObjects();
        vars.remove(index);

        // start with indexes: only keep those that have the value in that index
        for (int j = 0; j < factor.getVariableValues().size(); j++) {
            String[] arrayOfValues = factor.getVariableValues().get(j).split(","); // T,T,F

            if (arrayOfValues[index].equals(value)) {
                indexes.add(factor.getVariableValues().get(j));
                probs.add(factor.getProbabilities().get(j));
            }
        }
        ArrayList<String> indexesFinal = new ArrayList<>();

        for (int i = 0; i < indexes.size(); i++) {
            String[] parts = indexes.get(i).split(",");

            StringBuilder result = new StringBuilder();
            for (int j = 0; j < parts.length; j++) {
                if (j != index) { // Skip the index to remove
                    result.append(parts[j]).append(",");
                }
            }

            // Remove the last comma if necessary
            if (result.length() > 0) {
                result.setLength(result.length() - 1);
            }
            indexesFinal.add(result.toString());

        }

        answer.setVariableValues(indexesFinal);
        answer.setProbabilities(probs);
        answer.setVariables(vars);

        return answer;

    }

    /**
     * Finds the index of a given string in an array.
     * 
     * @param array  The array to search through.
     * @param target The string to find in the array.
     * @return The index of the string in the array, or -1 if not found.
     */
    public int findIndex(String[] array, String target) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(target)) {
                return i;
            }
        }
        return -1; // Return -1 if the target is not found
    }

    /**
     * This method creates a list of indexes of factors which contain the specified
     * hidden variable.
     * 
     * @param targetVar The name of the hidden variable to find in the factors.
     * @return A list of integers representing the indexes of factors containing the
     *         variable.
     */
    private ArrayList<Integer> findFactorIndexes(String targetVar) {
        ArrayList<Integer> foundIndexes = new ArrayList<>();
        // Loop through all factors
        for (int index = 0; index < this.factors.size(); index++) {
            // Check each variable in the current factor
            for (NodeVariable variable : this.factors.get(index).getVariablesObjects()) {
                if (variable.getName().equals(targetVar)) {
                    foundIndexes.add(index); // Add the index if the variable name matches
                    break; // Stop searching this factor as we found the variable
                }
            }
        }
        return foundIndexes;
    }

    /**
     * Sorts a list of factor indices based on the size of each factor (number of
     * rows) and returns the two smallest.
     * 
     * @param indices List of factor indices to sort.
     * @return Array containing the indices of the two smallest factors.
     */
    private int[] findTwoSmallestFactors(ArrayList<Integer> indices) {
        Integer[] sortedIndices = indices.toArray(new Integer[0]);
        int length = sortedIndices.length;

        // Bubble sort to sort factor indices by size (number of rows)
        for (int i = 0; i < length - 1; i++) {
            for (int j = 0; j < length - i - 1; j++) {
                if (compareFactors(sortedIndices[j], sortedIndices[j + 1]) == sortedIndices[j + 1]) {
                    int temp = sortedIndices[j];
                    sortedIndices[j] = sortedIndices[j + 1];
                    sortedIndices[j + 1] = temp;
                }
            }
        }

        int[] smallestTwo = { sortedIndices[0], sortedIndices[1] };
        return smallestTwo;
    }

    /**
     * Compares two factors by the number of rows and if equal, by the sum of ASCII
     * values of the variables' names.
     * 
     * @param index1 First factor index.
     * @param index2 Second factor index.
     * @return The index of the smaller factor.
     */
    private int compareFactors(int index1, int index2) {
        int rowsFactor1 = this.factors.get(index1).getProbabilities().size();
        int rowsFactor2 = this.factors.get(index2).getProbabilities().size();

        if (rowsFactor1 < rowsFactor2)
            return index1;
        else if (rowsFactor1 > rowsFactor2)
            return index2;
        else {
            int asciiFactor1 = sumAscii(this.factors.get(index1).getVariablesObjects());
            int asciiFactor2 = sumAscii(this.factors.get(index2).getVariablesObjects());
            return asciiFactor1 < asciiFactor2 ? index1 : index2;
        }
    }

    /**
     * Calculates the sum of ASCII values of all characters in all variable names.
     * 
     * @param variables List of variables to calculate ASCII sum.
     * @return Sum of ASCII values.
     */
    private int sumAscii(ArrayList<NodeVariable> variables) {
        int sum = 0;
        for (NodeVariable var : variables) {
            String name = var.getName();
            for (char ch : name.toCharArray()) {
                sum += ch;
            }
        }
        return sum;
    }

    /**
     * Finds the indices of probability strings that match a given regex pattern.
     * 
     * @param probIndices List of all probability indices as strings.
     * @param pattern     The regex pattern to match against the probability
     *                    indices.
     * @return An array containing the indices of matching probability strings.
     */
    private int[] findMatchingIndices(ArrayList<String> probIndices, String pattern) {
        ArrayList<Integer> matchingIndices = new ArrayList<>();

        // Loop through all indices and find matches to the regex pattern
        for (int i = 0; i < probIndices.size(); i++) {
            if (probIndices.get(i).matches(pattern)) {
                matchingIndices.add(i);
            }
        }

        // Convert ArrayList to array of ints for return
        return matchingIndices.stream().mapToInt(i -> i).toArray();
    }

    /**
     * Eliminates a variable from a specified factor.
     * 
     * @param factorIndex   The index of the factor from which the variable will be
     *                      eliminated.
     * @param targetVarName The name of the variable to eliminate.
     */
    public void eliminateVariable(int factorIndex, String targetVarName) {
        // Prepare new lists for the modified factor
        ArrayList<Double> newProbabilities = new ArrayList<>();
        ArrayList<String> newProbIndices = new ArrayList<>();
        ArrayList<NodeVariable> newVariables = new ArrayList<>();
        // Retrieve the factor based on index
        NodeFactor targetFactor = this.factors.get(factorIndex);
        ArrayList<Double> initialProbabilities = targetFactor.getProbabilities();
        ArrayList<String> initialProbIndices = targetFactor.getVariableValues();
        ArrayList<NodeVariable> initialVariables = targetFactor.getVariablesObjects();

        // Exclude the target variable from the new variables list
        for (NodeVariable variable : initialVariables) {
            if (!variable.getName().equals(targetVarName)) {
                newVariables.add(variable);
            }
        }

        // Identify the index of the variable to be eliminated in the factor's variables
        int varIndexToEliminate = 0;
        for (int i = 0; i < initialVariables.size(); i++) {
            if (initialVariables.get(i).getName().equals(targetVarName)) {
                varIndexToEliminate = i;
                break;
            }
        }

        // Elimination
        while (!initialProbabilities.isEmpty()) {
            String pattern = buildRegex(initialProbIndices.get(0), varIndexToEliminate);
            int[] indicesForElimination = findMatchingIndices(initialProbIndices, pattern);
            double sumProb = sumProbabilities(indicesForElimination, initialProbabilities);

            // Build new probability index string excluding the eliminated variable
            String newProbIndex = buildNewProbIndex(initialProbIndices.get(0), varIndexToEliminate);
            newProbabilities.add(sumProb);
            newProbIndices.add(newProbIndex);

            // Remove processed indices
            removeProcessedEntries(indicesForElimination, initialProbabilities, initialProbIndices);
        }

        // Remove the original factor
        this.factors.remove(factorIndex);

        // Add new factor if it's not empty
        if (newProbabilities.size() > 1) {
            NodeFactor newFactor = new NodeFactor(newProbabilities, newVariables, newProbIndices);
            this.factors.add(newFactor);
        }
    }

    private double sumProbabilities(int[] indicesForElimination, ArrayList<Double> probabilities) {
        boolean is_first = true;
        double sumProb = 0;
        for (int index : indicesForElimination) {
            if (is_first) {
                sumProb = probabilities.get(index);
                is_first = false;
            } else {
                sumProb += probabilities.get(index);
                this.additions++;
            }
        }
        return sumProb;
    }

    private String buildRegex(String firstProbIndex, int varIndex) {
        String[] parts = firstProbIndex.split(",");
        parts[varIndex] = "(.*)";
        return String.join(",", parts);
    }

    private String buildNewProbIndex(String probIndex, int varIndexToEliminate) {
        String[] parts = probIndex.split(",");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (i != varIndexToEliminate) {
                sb.append(parts[i]).append(",");
            }
        }
        return sb.toString().replaceAll(",$", "");
    }

    private void removeProcessedEntries(int[] indices, ArrayList<Double> probabilities, ArrayList<String> probIndices) {
        for (int i = indices.length - 1; i >= 0; i--) {
            int index = indices[i];
            probabilities.remove(index);
            probIndices.remove(index);
        }
    }

    private String calculateFinalResult() {

        // Retrieve the last factor in the list
        ArrayList<Double> probabilities = this.factors.get(0).getProbabilities();
        ArrayList<String> probabilityIndexes = this.factors.get(0).getVariableValues();

        // Find the numerator using the index of the query value in the probability
        // indexes
        double numerator = probabilities.get(probabilityIndexes.indexOf(this.queryval));

        // Calculate the denominator by summing all probability values
        double total = 0.0;
        for (double prob : probabilities) {
            total = total + prob;
        }

        // Update the addition counter to reflect the operations performed
        this.additions = this.additions + probabilityIndexes.size() - 1;

        DecimalFormat d1 = new DecimalFormat("#.#####");

        return d1.format(numerator / total);
    }

    private void varElim() {

        if (this.easyToAnswer() == "No") {
            while (this.hiddens.size() > 0) {
                String next = hiddens.get(0);
                ArrayList<Integer> list = findFactorIndexes(next); // indexes of factors with the first hidden
                                                                   // variable
                if (list.size() == 0) {
                    this.hiddens.remove(0);
                    continue;
                } else if (list.size() == 1) {
                    this.eliminateVariable(list.get(0), next);
                    continue;
                } else {
                    int[] inds = findTwoSmallestFactors(list);
                    this.join(next, inds[0], inds[1]);
                }

            }
            while (this.factors.size() > 1) {
                this.join(this.queryName, 0, 1);
            }

            String holdAns = calculateFinalResult();

            String answer = holdAns + "," + this.additions + "," + this.multiplications;
            this.finalAnswer = answer;
        } else {
            // If the answer already exists
            for (NodeFactor fac : this.factors) {
                // Find only relevant factors by checking number of variables
                if (fac.getVars().length == this.evidence.length + 1) {
                    // Check if all the variables are in it, including query name
                    if (containsString(fac.getVars(), this.queryName)) {
                        String[] forChecking = Arrays.copyOf(evidence, evidence.length + 1);
                        forChecking[evidence.length] = this.queryName;

                        if (EqualArrayOfString(fac.getVars(), forChecking)) {
                            this.factors.set(0, fac);
                            String holdAns = calculateFinalResultIfEasy();
                            String answer = holdAns + ",0,0";
                            this.finalAnswer = answer;
                            break;
                        }
                    }

                }
            }
        }
    }

    public String calculateFinalResultIfEasy() {

        String[] forChecking = Arrays.copyOf(evidence, evidence.length + 1);
        forChecking[evidence.length] = this.queryName;

        // Retrieve the last factor in the list
        ArrayList<Double> probabilities = this.factors.get(0).getProbabilities();
        ArrayList<String> probabilityIndexes = this.factors.get(0).getVariableValues();

        // Find the numerator using the index of the query value in the probability
        // indexes
        String index = "";
        int m = 0;
        for (int i = 0; i < forChecking.length; i++) {
            if (forChecking[i].equals(this.queryName)) {
                index += this.queryval + ",";
            } else {
                index += evidenceVals[m] + ",";
                m++;
            }
        }
        index = index.substring(0, index.length() - 1);

        double numerator = probabilities.get(probabilityIndexes.indexOf(index));

        return String.valueOf(numerator);
    }

    public boolean EqualArrayOfString(String[] array1, String[] array2) {
        if (array1.length != array2.length) {
            return false;
        }
        for (int i = 0; i < array1.length; i++) {
            if (!containsString(array1, array2[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Creates a combined index string representing a new row in a factor by merging
     * two rows.
     * It includes all indices from the first factor's row and only unique indices
     * from the second factor's row.
     *
     * @param firstIndexString The index string from the first factor.
     * @param secondIndexArray The index array from the second factor.
     * @param commonVarNames   List of names of common variables, to avoid
     *                         duplication.
     * @param secondVarNames   List of all variable names in the second factor.
     * @return A combined index string representing the new merged row.
     */
    private String createCombinedIndex(String firstIndexString, String[] secondIndexArray,
            ArrayList<String> commonVarNames, String[] secondVarNames) {
        StringBuilder answer = new StringBuilder(firstIndexString);

        // Append only unique variables from the second index array
        for (int i = 0; i < secondIndexArray.length; i++) {
            if (!commonVarNames.contains(secondVarNames[i])) {
                answer.append(",").append(secondIndexArray[i]);
            }
        }

        return answer.toString();
    }

    /**
     * Merges two lists of variables from different factors into a single list,
     * ensuring that variables common to both are not duplicated.
     *
     * @param variablesFromFirst  List of variables from the first factor.
     * @param variablesFromSecond List of variables from the second factor.
     * @param namesOfCommonVars   List of names of variables common to both factors.
     * @return A list containing the union of variables from both factors, without
     *         duplicates.
     */
    private ArrayList<NodeVariable> mergeVariables(ArrayList<NodeVariable> variablesFromFirst,
            ArrayList<NodeVariable> variablesFromSecond, ArrayList<String> namesOfCommonVars) {
        ArrayList<NodeVariable> mergedVariables = new ArrayList<>();

        // Add all variables from the first factor
        mergedVariables.addAll(variablesFromFirst);

        // Add only unique variables from the second factor
        for (NodeVariable variableFromSecond : variablesFromSecond) {
            if (!namesOfCommonVars.contains(variableFromSecond.getName())) {
                mergedVariables.add(variableFromSecond);
            }
        }

        return mergedVariables;
    }

    /**
     * Joins two factors based on their common variables and updates the list of
     * factors.
     * It multiplies probabilities for matching rows and creates a new factor.
     * 
     * @param hiddenVar   the variable to eliminate, used for context
     * @param firstIndex  index of the first factor in the list
     * @param secondIndex index of the second factor in the list
     */
    private void join(String hiddenVar, int firstIndex, int secondIndex) {

        NodeFactor firstFactor = this.factors.get(firstIndex);
        NodeFactor secondFactor = this.factors.get(secondIndex);
        String[] firstVarNames = firstFactor.getVars();
        String[] secondVarNames = secondFactor.getVars();
        ArrayList<Double> firstProbabilities = firstFactor.getProbabilities();
        ArrayList<Double> secondProbabilities = secondFactor.getProbabilities();
        ArrayList<NodeVariable> firstVariables = firstFactor.getVariablesObjects();
        ArrayList<NodeVariable> secondVariables = secondFactor.getVariablesObjects();
        ArrayList<String> firstProbIndices = firstFactor.getVariableValues();
        ArrayList<String> secondProbIndices = secondFactor.getVariableValues();
        ArrayList<NodeVariable> commonVariables = findMutualVariables(firstVariables, secondVariables);
        ArrayList<String> commonVarNames = extractVariableNames(commonVariables);
        ArrayList<Double> combinedProbabilities = new ArrayList<>();
        ArrayList<String> combinedProbIndices = new ArrayList<>();

        for (int i = 0; i < firstProbIndices.size(); i++) {
            String[] firstValues = firstProbIndices.get(i).split(",");
            Hashtable<String, String> commonValues = new Hashtable<>();
            String row = firstProbIndices.get(i);

            for (int j = 0; j < firstValues.length; j++) {
                if (commonVarNames.contains(firstVarNames[j])) {
                    commonValues.put(firstVarNames[j], firstValues[j]);
                }
            }

            for (int k = 0; k < secondProbIndices.size(); k++) {

                String[] secondValues = secondProbIndices.get(k).split(",");
                boolean match = true;
                for (int m = 0; m < secondValues.length; m++) {

                    if (commonVarNames.contains(secondVarNames[m]) &&
                            !commonValues.get(secondVarNames[m]).equals(secondValues[m])) {
                        match = false;
                    }
                }
                if (match) {
                    String combinedIndex = createCombinedIndex(row, secondValues, commonVarNames, secondVarNames);
                    combinedProbabilities.add(firstProbabilities.get(i) * secondProbabilities.get(k));
                    combinedProbIndices.add(combinedIndex);
                    this.multiplications++;
                }
            }
        }

        if (firstIndex > secondIndex) {
            this.factors.remove(firstIndex);
            this.factors.remove(secondIndex);
        } else {
            this.factors.remove(secondIndex);
            this.factors.remove(firstIndex);
        }

        if (combinedProbabilities.size() > 1) {
            ArrayList<NodeVariable> updatedVariables = mergeVariables(firstVariables, secondVariables, commonVarNames);
            NodeFactor newFactor = new NodeFactor(combinedProbabilities, updatedVariables, combinedProbIndices);
            this.factors.add(newFactor);
        }
    }

    /**
     * Converts an array of strings into a single string separated by commas.
     * 
     * @param array The array of strings to convert.
     * @return A single string representation of the array.
     */
    public String StringArrayToString(String[] array) {
        String answer = "";
        for (int i = 0; i < array.length; i++) {
            answer += array[i];
        }
        return answer;
    }

    /**
     * Finds the mutual (common) variables between two sets of variables from two
     * different factors.
     * 
     * @param firstVariables  List of variables from the first factor.
     * @param secondVariables List of variables from the second factor.
     * @return A list of variables that appear in both input lists.
     */
    private ArrayList<NodeVariable> findMutualVariables(ArrayList<NodeVariable> firstVariables,
            ArrayList<NodeVariable> secondVariables) {
        ArrayList<NodeVariable> answer = new ArrayList<>();
        ArrayList<String> secondVarNames = extractVariableNames(secondVariables);

        for (NodeVariable varFromFirst : firstVariables) {
            if (secondVarNames.contains(varFromFirst.getName())) {
                answer.add(varFromFirst);
            }
        }
        return answer;
    }

    public String finalAnswer() {
        return this.finalAnswer;
    }

    /**
     * Helper method to extract variable names from a list of Variable objects.
     * 
     * @param variables List of Variable objects.
     * @return A list of variable names as Strings.
     */
    private ArrayList<String> extractVariableNames(ArrayList<NodeVariable> variables) {
        ArrayList<String> variableNames = new ArrayList<String>();

        for (NodeVariable variable : variables) {
            variableNames.add(variable.getName());
        }
        return variableNames;
    }

}
