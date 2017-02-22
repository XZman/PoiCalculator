/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helloworld.xzman.poicalculator;

/**
 * @author William Lyu
 */

public class BGCalculator {

    public static double calculate(CharSequence expression) throws Exception {
        String express = (String)expression;

        final int MAXSTACKSIZE = 100;
        double[] stack1 = new double[MAXSTACKSIZE]; // negative for left, positive for right
        boolean[] isbracket = new boolean[MAXSTACKSIZE]; // false for num, true for 
        int top1;
        char[] operator = new char[MAXSTACKSIZE];
        int top2;
        int expStart;

        top1 = 0;
        top2 = 0;
        stack1[top1] = 0;
        operator[top1] = '+';
        isbracket[top1] = false;

        expStart = 0;


        for (; ; ) {

            // get next token and store it into the stack

            // the token is a null
            if (expStart >= express.length())
                break;

            // illegal character
            if (!islegal(express.charAt(expStart)))
                throw new Exception("illegal character");

                // the token is a number
            else if (isNumber(express.charAt(expStart))) {
                int tokenEnd;
                tokenEnd = expStart;
                while (tokenEnd < express.length() && isNumber(express.charAt(tokenEnd))) {
                    ++tokenEnd;
                }
                --tokenEnd;

                Double tokenNum = new Double(express.substring(expStart, tokenEnd + 1)); // exception needed to be processed

                if (top1 >= MAXSTACKSIZE - 1) {
                    // throw an exception: the stack overflow
                }

                ++top1;
                stack1[top1] = tokenNum;
                isbracket[top1] = false;

                expStart = tokenEnd + 1;


            }

            // the token is an operator
            else if (isOperator(express.charAt(expStart))) {
                ++top2;
                operator[top2] = express.charAt(expStart);

                ++expStart;

                continue;
            }

            // the token is a bracket
            else if (isBracket(express.charAt(expStart))) {
                ++top1;
                isbracket[top1] = true;
                // left
                if (isLeftBracket(express.charAt(expStart))) {
                    stack1[top1] = -1;
                    ++expStart;
                    continue;
                }

                // right
                else {
                    stack1[top1] = 1;
                    --top1;
                    if (top1 <= -1) throw new Exception("Left bracket missing");
                    // ()
                    if  (isbracket[top1]) {
                        // the left is not a (
                        if (stack1[top1] >= 0)
                            throw new Exception("Left bracket missing");
                        else // the left is a (
                            --top1;
                    }
                    // (number)
                    else {
                        if (top1 - 1 <= -1 || !isbracket[top1 - 1]) throw new Exception("Left bracket missing");
                        double tmpValue = stack1[top1];
                        --top1;
                        isbracket[top1] = false;
                        stack1[top1] = tmpValue;

                    }

                    ++expStart;
                }


            }


            // process the stack

            char nextOp;
            if (expStart >= express.length() || !isOperator(express.charAt(expStart)))
                nextOp = '\0';
            else
                nextOp = express.charAt(expStart);


            while (true) {


                if (top1 <= top2) // only for two op calc
                {
                    throw new Exception("Too few operands, stack underflow");
                }
                if (isbracket[top1])
                    break;
                if (top1 - 1 >= 0 && isbracket[top1 - 1] == true) // the last is a bracket
                {
                    break;
                }
                if (top2 < 0) // no operators!
                {
                    break;
                }
                if (operatorLevel(nextOp) >= operatorLevel(operator[top2])) // the next operator has higher priority
                {
                    break;
                }

                double newValue = twoOpCalc(stack1[top1 - 1], stack1[top1], operator[top2]);

                --top2;
                --top1;
                stack1[top1] = newValue;
            }


        }

        if (top2 != -1) throw new Exception("Expression incomplete");
        if (top1 != 0) throw new Exception("Expression incomplete");
        if (isbracket[top1] == true) throw new Exception("Expression incomplete: illegal brackets");
        else
            return stack1[top1];

    }

    public static boolean isNumber(char ch) {
        return (ch >= '0' && ch <= '9') || ch == '.';
    }

    public static boolean isDecimal(CharSequence cs) {
        int dotCount = 0;
        for (int i = cs.length() - 1; i >= 0; i--) {
            if (!isNumber(cs.charAt(i)))
                break;
            if (cs.charAt(i) == '.')
                dotCount++;
        }
        if (dotCount == 1)
            return true;
        return false;
    }

    public static boolean isOperator(char ch) { return ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^'; }

    public static boolean isBracket(char ch) {
        return isLeftBracket(ch) || isRightBracket(ch);
    }

    public static boolean isLeftBracket(char ch) {
        return ch == '(' || ch == '[' || ch == '{';
    }

    public static boolean isRightBracket(char ch) {
        return ch == ')' || ch == ']' || ch == '}';
    }

    private static double twoOpCalc(double op1, double op2, char op) {
        switch (op) {
            case '+':
                return op1 + op2;
            case '-':
                return op1 - op2;
            case '*':
                return op1 * op2;
            case '/':
                return op1 / op2;
            case '^':
                return Math.pow(op1, op2);
        }
        // default return
        return 0;
    }

    private static double operatorLevel(char ch) {
        if (ch == '+' || ch == '-')
            return 0;
        else if (ch == '*' || ch == '/')
            return 1;
        else if (ch == '^')
            return 2;
        else
            return -1;
    }

    public static boolean islegal(char ch) {
        char[] test = "0123456789.[](){}+-*/^".toCharArray();
        for (int lv = 0; lv < test.length; ++lv) {
            if (ch == test[lv]) return true;
        }
        return false;
    }
}