# Artificial-Intelligence

This repository contains the implementation of the Variable Elimination algorithm as part of an artificial intelligence study focusing on Bayesian networks. Below is an overview of the structure and functionality of the various components included in this project.

## Variable Elimination Algorithm
![Variable Elimination Illustration](https://github.com/Noa-Nussbaum/Artificial-Intelligence/assets/76524924/db52b7b8-e914-4c40-854c-83a90e861b92)

### Main Components

#### `Ex1`
The main driver file which initializes the Bayesian Network from an XML file, processes input queries from a text file, and writes the results to an output file.

#### `AlgNode`
Represents a node in the Bayesian Network with the following attributes:
- **Parents**: A list of parent nodes
- **Children**: A list of child nodes
- **Factor (CPT)**: The Conditional Probability Table associated with the node
- **Variables**: States of the node, such as True (T), False (F), etc.

#### `NodeFactor`
Manages the Conditional Probability Table (CPT) for a node, encapsulating the probabilistic logic specific to that node.
- **Probabilities**:
- **Nodes**:
- **List of variables**: variable objects arrayList
- **Optional variable values**: such as True or False
getVars function returns the list of variable names

#### `BayesianNetwork`
Holds and manages the entire network comprising various `AlgNode` instances. It serves as the backbone of the application, linking all nodes together. Contains the Bayes Ball function. Also has list of node names for comfort. Function to check if variable is an ancestor of given list of variables.

#### `XMLReaderUtil`
Responsible for parsing XML files to construct the Bayesian Network, ensuring each node is properly initialized with its respective data.

#### `QueryReadWrite`
Processes the input file to extract queries and writes the computed results to the output file. This component bridges user inputs and algorithmic outputs.

#### `NodeVariable`
Holds the name and possible values (like True or False) that the variable has.

#### `VariableElimination`
Runs the variable elimination algorithm. 
- **easyToAnswer**: checks if any of the factors contains the final answer


### Setup and Usage
To run this project, ensure you have the following setup:
- Java JDK 8 or newer installed and properly set up.
- Clone this repository to your local machine.
- Compile and run `Ex1.java` with the path to your XML configuration and query input files.
- Add the relevant XML file and input.txt file into the same folder as the project.
