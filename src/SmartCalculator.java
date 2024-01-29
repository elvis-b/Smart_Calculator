import java.awt.*;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmartCalculator {

    // Constants
    private final String UNKNOWN_OPERATOR = "Unknown operator";
    private final String UNKNOWN_VARIABLE = "Unknown variable";
    private final String INVALID_IDENTIFIER = "Invalid identifier";
    private final String INVALID_ASSIGNMENT = "Invalid assignment";
    private final String INVALID_EXPRESSION = "Invalid expression";

    // Map to store precedence levels for operators
    private final Map<String, Integer> precedenceMap = new HashMap<>();

    public SmartCalculator() {
        precedenceMap.put("+", 1);
        precedenceMap.put("-", 1);
        precedenceMap.put("*", 2);
        precedenceMap.put("/", 2);
        precedenceMap.put("^", 3);
        precedenceMap.put("(", 0);
        precedenceMap.put(")", 0);
    }

    private int getPrecedenceLevel(String string) {
        return precedenceMap.getOrDefault(string, -1);
    }

    // Patterns

    /**
     * operatorPattern: Pattern to match operators such as +, -, *, /, ^, (, and ).
     * baseValuePattern: Pattern to match base values, which can be numbers or variables.
     * valueOperatorPattern: Pattern to match values followed by operators.
     * variableAssignmentPattern: Pattern to match variable assignments in the form of "variable = value".
     * variableReferencePattern: Pattern to match variable references in the form of "variable = another_variable".
     */

    private final Pattern operatorPattern = Pattern.compile("([-+*/^()])");
    private final Pattern baseValuePattern = Pattern.compile("(-?\\w+)\\s?");
    private final Pattern valueOperatorPattern = Pattern.compile("([-+*/]+)\\s+(\\w+)");
    private final Pattern variableAssignmentPattern = Pattern.compile("([a-zA-Z]+)\\s*(=)\\s*(-?\\d+)\\b+");
    private final Pattern variableReferencePattern = Pattern.compile("([a-zA-Z]+)\\s*(=)\\s*([a-zA-Z]+)\\b+");

    // Method to check if the input matches an operator pattern
    private boolean isOperatorPatternMatch(String input) {
        return operatorPattern.matcher(input).find();
    }

    // Method to check if the input matches the base value pattern
    private boolean isBaseValuePatternMatch(String input) {
        return baseValuePattern.matcher(input).find();
    }

    // Method to check if the input matches the value followed by an operator pattern
    private boolean isValueOperatorPatternMatch(String input) {
        return valueOperatorPattern.matcher(input).find();
    }

    // Method to check if the input matches the variable assignment pattern
    private boolean isVariableAssignmentPatternMatch(String input) {
        return variableAssignmentPattern.matcher(input).matches();
    }

    // Method to check if the input matches the variable reference pattern
    private boolean isVariableReferencePatternMatch(String input) {
        return variableReferencePattern.matcher(input).matches();
    }

    public boolean isValidExpression(String input) {
        if (!input.contains("=")) {
            return isBaseValuePatternMatch(input);
        } else {
            return isValueOperatorPatternMatch(input) ||
                    isVariableAssignmentPatternMatch(input) ||
                    isVariableReferencePatternMatch(input);
        }
    }

    private void println(String text) {
        System.out.println(text);
    }

    private BigInteger getValue(String input) {
        try {
            return new BigInteger(input);
        } catch (Exception e) {
            return variables.getOrDefault(input, null);
        }
    }

    private final Map<String, BigInteger> variables = new HashMap<>();

    private void manageVariables(String input) {
        if (isVariableAssignmentPatternMatch(input)) {
            Matcher matcherVariables = variableAssignmentPattern.matcher(input);

            while (matcherVariables.find()) {
                var variable = matcherVariables.group(1).trim();
                var value = new BigInteger(matcherVariables.group(3));
                variables.put(variable, value);
            }
        } else if (isVariableReferencePatternMatch(input)) {
            Matcher matcherReferenceVariables = variableReferencePattern.matcher(input);

            while (matcherReferenceVariables.find()) {
                var variable = matcherReferenceVariables.group(1).trim();
                var value = matcherReferenceVariables.group(3);

                if (variables.containsKey(value)) {
                    variables.put(variable, variables.get(value));
                } else {
                    println(UNKNOWN_VARIABLE);
                }
            }
        } else println(INVALID_ASSIGNMENT);
    }

    private final Map<Integer, String> postfix = new HashMap<>();

    private void infixToPostfix(String input) {
        // Store the input expression on a map
        final Map<Integer, String> inputMap = new HashMap<>();

        var m = 0;
        var strings = input.toCharArray();

        for (var ch : strings) {
            String str = String.valueOf(ch);

            if (!isOperatorPatternMatch(str)) {
                inputMap.putIfAbsent(m, "");
                inputMap.put(m, String.format("%s%s", inputMap.get(m), str));
            } else {
                inputMap.put(++m, str);
                m++;
            }

        }

        // Store the operators during the conversion process
        var stack = new Stack<String>();

        var i = 0;

        postfix.clear();

        for (var entry : inputMap.entrySet()) {
            var value = entry.getValue();

            if (getPrecedenceLevel(value) < 0) {
                postfix.put(i++, value);
            } else if ("(".equals(value)) {
                stack.push(value);
            } else if (")".equals(value)) {
                while (!stack.isEmpty() && !"(".equals(stack.peek())) {
                    postfix.put(i++, stack.pop());
                }

                try {
                    stack.pop();
                } catch (Exception e) {
                    println(INVALID_EXPRESSION);
                    return;
                }
            } else {
                while (!stack.isEmpty() && getPrecedenceLevel(value) <= getPrecedenceLevel(stack.peek())) {
                    postfix.put(i++, stack.pop());
                }
                stack.push(value);
            }
        }

        while (!stack.isEmpty()) {
            if ("(".equals(stack.peek())) {
                println(INVALID_EXPRESSION);
                return;
            }
            postfix.put(i++, stack.pop());
        }
    }

    private BigInteger evaluatePostfix() {
        var stack = new Stack<BigInteger>();

        for (var element : postfix.entrySet()) {
            var value = element.getValue();

            if (getPrecedenceLevel(value) < 0) {
                stack.push(getValue(value));
            } else {
                var value1 = stack.pop();
                var value2 = stack.pop();

                switch (value) {
                    case "+":
                        performAddition(stack, value2, value1);
                        break;
                    case "-":
                        performSubtraction(stack, value2, value1);
                        break;
                    case "*":
                        performMultiplication(stack, value2, value1);
                        break;
                    case "/":
                        performDivision(stack, value2, value1);
                        break;
                    case "^":
                        performExponentiation(stack, value2, value1);
                        break;
                    default:
                        handleUnknownOperator();
                        break;
                }
            }
        }
        return stack.pop();
    }

    private void performAddition(Stack<BigInteger> stack, BigInteger value2, BigInteger value1) {
        stack.push(value2.add(value1));
    }

    private void performSubtraction(Stack<BigInteger> stack, BigInteger value2, BigInteger value1) {
        stack.push(value2.subtract(value1));
    }

    private void performMultiplication(Stack<BigInteger> stack, BigInteger value2, BigInteger value1) {
        stack.push(value2.multiply(value1));
    }

    private void performDivision(Stack<BigInteger> stack, BigInteger value2, BigInteger value1) {
        stack.push(value2.divide(value1));
    }

    private void performExponentiation(Stack<BigInteger> stack, BigInteger value2, BigInteger value1) {
        stack.push(value2.pow(value1.intValue()));
    }

    private void handleUnknownOperator() {
        println(UNKNOWN_OPERATOR);
    }

    public void calculate(String input) {
        if (input.contains("**") || input.contains("//")) {
            println(INVALID_EXPRESSION);
        } else if (isValidExpression(input)) {
            if (input.contains("=")) {
                manageVariables(input);
            } else if (!isOperatorPatternMatch(input)) {
                try {
                    println(getValue(input).toString());
                } catch (Exception e) {
                    println(UNKNOWN_VARIABLE);
                }
            } else {
                infixToPostfix(input);
                if (!postfix.isEmpty()) {
                    println(evaluatePostfix().toString());
                }
            }
        } else {
            println(INVALID_IDENTIFIER);
        }
    }

    public void help() {
        println("""
                Welcome to Smart Calculator!
                This program provides a versatile calculator that can handle arithmetic expressions, including addition, subtraction, multiplication, and division.
                You can also use exponentiation (^) and parentheses to specify the order of operations.
                Additionally, you can define and use variables in your calculations, making it easier to reuse values.
                To define a variable, use the assignment operator (=). For example: a = 10.
                You can then reference variables in expressions, such as a + b.
                Feel free to explore the capabilities of this calculator and perform various calculations!
                To exit the program, simply type '/exit'.""");
    }

    public void exit() {
        println("Bye!");
    }
}
