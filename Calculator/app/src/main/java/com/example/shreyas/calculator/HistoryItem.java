package com.example.shreyas.calculator;

/**
 * Created by shreyas on 20/1/18.
 */

public class HistoryItem {

    private String expression;
    private String result;

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public HistoryItem(String expression, String result) {
        this.expression = expression;
        this.result = result;
    }
}
