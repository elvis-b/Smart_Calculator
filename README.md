# Smart Calculator

The Smart Calculator is a command-line calculator application developed in Java that provides advanced functionality for arithmetic calculations and variable assignments. This calculator employs a combination of algorithms and data structures to deliver efficient computation and flexible usage, including the utilization of infix to postfix conversion and the evaluation of postfix expressions using the Shunting Yard algorithm.

The application was developed as part of the Java Backend Developer track on Hyperskill: https://hyperskill.org/projects/42?track=12


## Key Features

- Arithmetic Operations:

The calculator supports basic arithmetic operations such as addition (+), subtraction (-), multiplication (*), and division (/).

It utilizes the Shunting Yard algorithm to convert infix expressions into postfix notation, facilitating efficient evaluation and ensuring proper order of operations.

- Variable Assignment:

Users can assign values to variables for later use within expressions. Variables are stored in a hashmap data structure for fast retrieval and manipulation.
The application employs regular expressions to validate variable names and numerical inputs, ensuring accurate processing of assignments.

- Command Support:

The calculator provides helpful commands for user interaction, including /help to display usage instructions and /exit to terminate the program gracefully.

- Error Handling:

Robust error handling mechanisms are implemented to detect and handle various error scenarios, such as unknown variables or invalid expressions.
Custom exceptions are utilized to provide informative error messages and guide users towards resolving issues effectively.




## Algorithms and Data Structures

- Infix to Postfix Conversion:

Infix arithmetic expressions entered by the user are converted to postfix notation using the Shunting Yard algorithm.
This conversion simplifies expression evaluation by removing the need for parentheses and ensuring a clear order of operations.

- Stack-Based Evaluation of Postfix Expressions:

Postfix expressions, obtained through infix to postfix conversion, are evaluated using a stack-based algorithm.
The algorithm iterates through the postfix expression, pushing operands onto the stack and performing operations when encountering operators, resulting in efficient and accurate evaluation.

- HashMap for Variable Storage:

Variables are stored in a hashmap data structure, allowing for fast retrieval and modification of variable values during computation.
Hashing enables constant-time lookup of variables, optimizing performance when processing expressions involving multiple variables.

![image](https://github.com/elvis-b/Smart_Calculator/assets/57047129/82c5658f-002e-4604-bea5-8f4c8eed11c3)

![image](https://github.com/elvis-b/Smart_Calculator/assets/57047129/0170cf5e-2de8-42a4-a362-15e6939755ea)

