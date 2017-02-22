package com.helloworld.xzman.poicalculator;

import android.widget.TextView;

/**
 * Created by XZman on 4/19/16.
 */
public class TextUpdater {

    private static final CharSequence clear = "AC";
    private static final CharSequence backspace = "<-";
    private static final char leftBracket = '(';
    private static final char rightBracket = ')';
    private static final char pow = '^';
    private static final char add = '+';
    private static final char subtract = '-';
    private static final char multiply = '*';
    private static final char divide = '/';
    private static final char dot = '.';
    private static final char equal = '=';

    private boolean textClear = true;

    private TextView target;

    public TextUpdater(TextView t) {
        setTarget(t);
    }

    public void setTarget(TextView t) {
        target = t;
    }

    public void updateText(CharSequence token) throws Exception {

        // token is a CharSequence
        if (token.equals(clear)) {
            target.setText("0");
            textClear = true;
            return;
        }

        // resolve initial condition
        if (textClear) {
            target.setText("");
            textClear = false;
        }


        if (token.equals(backspace) && !target.getText().equals("0")) {
            backspace();
            return;
        }

        // token is a character
        char tok = token.charAt(0);
        char lastDigit = target.length() > 0 ? target.getText().charAt(target.length() - 1) : ' ';

        // token is a number
        if (tok >= '0' && tok <= '9') {
            target.append(tok + "");
            return;
        }

        // other tokens
        switch (tok) {
            case leftBracket:
                if (!BGCalculator.isNumber(tok))
                    target.append(tok + "");
                break;
            case rightBracket:
                if (!BGCalculator.isOperator(lastDigit))
                    target.append(tok + "");
                break;
            case pow:
            case add:
            case subtract:
            case multiply:
            case divide:
                if (BGCalculator.isOperator(lastDigit)) {
                    if (lastDigit != tok) {
                        backspace();
                        target.append(tok + "");
                    }
                }
                else {
                    target.append(tok + "");
                }

                break;
            case dot:
                if (!BGCalculator.isDecimal(target.getText()))
                    target.append(tok + "");
                break;
            case equal:
                String expression = "";
                for (int lv = 0; lv < target.getText().length(); ++lv)
                    if (BGCalculator.islegal(target.getText().charAt(lv))) {
                        expression += target.getText().charAt(lv);
                    }
                    else {
                        target.setText("Syntax Error");
                        break;
                    }
                textClear = true;
                String result = String.format("%.15g", BGCalculator.calculate(expression));
                target.append("=" + calcFormat(result));
                /*
                try {
                    target.setText("" + BGCalculator.calculate(target.getText()));
                }
                catch (Exception e) {
                    target.setText(Arrays.toString(e.getStackTrace()));
                }*/
                break;
        }

    }

    private String calcFormat(String num) {
        double epsilon = 10e-9;
        int digitLen;
        double value = Double.parseDouble(num);
        for (int lv = 0; lv < num.length(); ++lv)
            if (num.charAt(lv) == 'e' || num.charAt(lv) == 'E' || num.charAt(lv) == 'i')
                return num;
        for (digitLen = 0; ; ++digitLen) {
            if (Math.abs(value - Math.floor(value)) < epsilon)
                break;
            value *= 10;
        }
        int dotPos = -1;
        for (dotPos = 0; dotPos < num.length() && num.charAt(dotPos) != '.'; ++dotPos)
            ;
        if (dotPos == -1 || num.length() - 1 - (dotPos + 1) + 1 <= digitLen) return num;
        else if (digitLen == 0) return num.substring(0, dotPos);
        else return num.substring(0, dotPos + digitLen + 1);
    }

    private void backspace() {
        target.setText(target.getText().subSequence(0, target.length() - 1));
    }
}
