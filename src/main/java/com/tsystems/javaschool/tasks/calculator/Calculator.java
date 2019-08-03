package com.tsystems.javaschool.tasks.calculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Calculator {

    /**
     * Evaluate statement represented as string.
     *
     * @param statement mathematical statement containing digits, '.' (dot) as decimal mark,
     *                  parentheses, operations signs '+', '-', '*', '/'<br>
     *                  Example: <code>(1 + 38) * 4.5 - 1 / 2.</code>
     * @return string value containing result of evaluation or null if statement is invalid
     */
    public String evaluate(String statement) {
        // TODO: Implement the logic here
        if (statement == null || statement.isEmpty()) {
            return null;
        }

        statement = eliminateWrappingBrackets(statement);

        ArrayList<Character> operations;
        ArrayList<Character> operationsPlusMinus = new ArrayList<>();
        ArrayList<Character> operationsMultipleDivide = new ArrayList<>();

        List<String> numbersAndParenthesses = new ArrayList<>();
        int indexOfNextNumbersAndParenthesses = 0;


        int amountOfOpenedBrackets = 0;
        for (int i = 0; i < statement.length(); i++) {
            //detecting bad statement
            char checkingChar = statement.charAt(i);
            if (checkingChar == '(') {
                if (amountOfOpenedBrackets >= 0) {
                    amountOfOpenedBrackets++;
                } else {
                    return null;
                }
            } else if (checkingChar == ')') {
                if (amountOfOpenedBrackets > 0) {
                    amountOfOpenedBrackets--;
                } else {
                    return null;
                }
            } else if (!statement.substring(i, i+1).matches("[+\\-*\\/.]|[0-9]") ||
                       checkingChar == '.' && ((i != 0 && statement.substring(i-1, i).matches("[0-9]") &&
                       i+1 != statement.length() && statement.substring(i+1, i+2).matches("[0-9]")) || i == 0 || i+1 == statement.length()) &&
                       statement.substring(i, i+1).matches("[+\\-*\\/]") &&
                       i+1 != statement.length() && statement.substring(i+1, i+2).matches("[+\\-*\\/]")) {
                return null;
            }

            if (amountOfOpenedBrackets == 0) {
                if ((checkingChar == '+' || checkingChar  == '-') && i != 0) {
                    numbersAndParenthesses.add(statement.substring(indexOfNextNumbersAndParenthesses, i));
                    operationsPlusMinus.add(checkingChar);
                    indexOfNextNumbersAndParenthesses = i + 1;
                }
                if ((checkingChar == '*' || checkingChar  == '/') && i != 0) {
                    operationsMultipleDivide.add(checkingChar);
                }
            }
            if (!numbersAndParenthesses.isEmpty() && i == statement.length() - 1) {
                numbersAndParenthesses.add(statement.substring(indexOfNextNumbersAndParenthesses, statement.length()));
            }
        }
        operations = !operationsPlusMinus.isEmpty() ? operationsPlusMinus: operationsMultipleDivide;

        if (numbersAndParenthesses.isEmpty()) {
            numbersAndParenthesses = Arrays.asList(statement.split("[\\/*]"));
        }
        if (statement.charAt(0) == '-') {
            numbersAndParenthesses.set(0, "-" + numbersAndParenthesses.get(0));
        }

        if (numbersAndParenthesses.size() == 0) {
            return null;
        } else if (numbersAndParenthesses.size() == 1) {
            if (numbersAndParenthesses.get(0).matches("-?\\d+(\\.\\d+)?")) {
                return numbersAndParenthesses.get(0);
            } else {
                return null;
            }
        }

        double result = operations.contains('*') || operations.contains('/') ? 1: 0;
        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i) == '*') {
                if (i == 0) {
                    String prevValue = evaluate(numbersAndParenthesses.get(i));
                    if (prevValue == null) {
                        return null;
                    } else {
                        result = Double.parseDouble(prevValue);
                    }
                }
                String value = evaluate(numbersAndParenthesses.get(i+1));
                if (value == null) {
                    return null;
                } else {
                    result *= Double.parseDouble(value);
                }
            } else if (operations.get(i) == '/') {
                if (i == 0) {
                    String prevValue = evaluate(numbersAndParenthesses.get(i));
                    if (prevValue == null) {
                        return null;
                    } else {
                        result = Double.parseDouble(prevValue);
                    }
                }
                String value = evaluate(numbersAndParenthesses.get(i+1));
                if (value == null || value.matches("\\(*0\\)*")) {
                    return null;
                } else {
                    result /= Double.parseDouble(value);
                }
            }
        }

        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i) == '+') {
                if (i == 0) {
                    String prevValue = evaluate(numbersAndParenthesses.get(i));
                    if (prevValue == null) {
                        return null;
                    } else {
                        result += Double.parseDouble(prevValue);
                    }
                }
                String value = evaluate(numbersAndParenthesses.get(i+1));
                if (value == null) {
                    return null;
                } else {
                    result += Double.parseDouble(value);
                }
            } else if (operations.get(i) == '-') {
                if (i == 0) {
                    String prevValue = evaluate(numbersAndParenthesses.get(i));
                    if (prevValue == null) {
                        return null;
                    } else {
                        result = Double.parseDouble(prevValue);
                    }
                }
                String value = evaluate(numbersAndParenthesses.get(i+1));
                if (value == null) {
                    return null;
                } else {
                    result -= Double.parseDouble(value);
                }
            }
        }

        if (result == Math.round(result)) {
            return String.valueOf((int)result);
        } else {
            long intermediateValueToRound = (long)(result*100000);
            if (intermediateValueToRound % 10 < 5) {
                double roundedResult = (double)intermediateValueToRound/100000;
                if (intermediateValueToRound/10%10000 == 0) {
                    return String.valueOf((int)roundedResult);
                } else {
                    return String.valueOf(roundedResult);
                }
            } else {
                double intermediateValue = intermediateValueToRound/10 + 1;
                return String.valueOf(intermediateValue/10000);
            }
        }
    }

    private String eliminateWrappingBrackets(String statement) {
        int numberOfWrappingBrackets = 0;
        while (statement.charAt(numberOfWrappingBrackets) == '(' && statement.charAt(statement.length() - 1 - numberOfWrappingBrackets) == ')') {
            numberOfWrappingBrackets++;
        }
        return statement.substring(numberOfWrappingBrackets, statement.length() - numberOfWrappingBrackets);
    }

}
