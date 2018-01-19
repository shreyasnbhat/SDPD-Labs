package com.example.shreyas.calculator;

import android.animation.Animator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView resultTextView;
    private TextView buttonZero, buttonOne, buttonTwo, buttonThree, buttonFour, buttonFive, buttonSix, buttonSeven, buttonEight, buttonNine, buttonAdd, buttonSubtract,
            buttonDivide, buttonMultiply, buttonEqual, buttonClear, buttonPoint, buttonHistory;
    private boolean isEqualClicked;
    private HashMap<Integer, String> idTextMap = new HashMap<>();
    private HashMap<Integer, Integer> operatorMap = new HashMap<>();

    String operandGenerator = "";
    float operandOne, operandTwo;
    int operator;
    boolean operatorIsSetFlag = false, errorFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateTextMap();
        populateOperatorMap();

        resultTextView = findViewById(R.id.result);
        buttonZero = findViewById(R.id.button0);
        buttonOne = findViewById(R.id.button1);
        buttonTwo = findViewById(R.id.button2);
        buttonThree = findViewById(R.id.button3);
        buttonFour = findViewById(R.id.button4);
        buttonFive = findViewById(R.id.button5);
        buttonSix = findViewById(R.id.button6);
        buttonSeven = findViewById(R.id.button7);
        buttonEight = findViewById(R.id.button8);
        buttonNine = findViewById(R.id.button9);
        buttonClear = findViewById(R.id.button_clear);
        buttonHistory = findViewById(R.id.history);
        buttonAdd = findViewById(R.id.button_add);
        buttonSubtract = findViewById(R.id.button_subtract);
        buttonMultiply = findViewById(R.id.button_multiply);
        buttonDivide = findViewById(R.id.button_divide);
        buttonPoint = findViewById(R.id.button_point);
        buttonEqual = findViewById(R.id.button_equal);

        resultTextView.setText("");

        buttonZero.setOnClickListener(this);
        buttonOne.setOnClickListener(this);
        buttonTwo.setOnClickListener(this);
        buttonThree.setOnClickListener(this);
        buttonFour.setOnClickListener(this);
        buttonFive.setOnClickListener(this);
        buttonSix.setOnClickListener(this);
        buttonSeven.setOnClickListener(this);
        buttonEight.setOnClickListener(this);
        buttonNine.setOnClickListener(this);
        buttonClear.setOnClickListener(this);
        buttonHistory.setOnClickListener(this);
        buttonAdd.setOnClickListener(this);
        buttonSubtract.setOnClickListener(this);
        buttonMultiply.setOnClickListener(this);
        buttonDivide.setOnClickListener(this);
        buttonPoint.setOnClickListener(this);
        buttonEqual.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Log.e("TAG", operandOne + " " + operator + " " + operandTwo);
        if (idTextMap.get(v.getId()).compareTo("9") <= 0 && idTextMap.get(v.getId()).compareTo("0") >= 0 && !errorFlag) {
            String toDisplayText = appendExistingTextViewString(v.getId());
            operandGenerator += idTextMap.get(v.getId());
            resultTextView.setText(toDisplayText);

            if (!operatorIsSetFlag) {
                operandOne = Float.parseFloat(operandGenerator);
            } else {
                operandTwo = Float.parseFloat(operandGenerator);
            }
        } else if (v.getId() == R.id.button_clear) {
            resultTextView.setText(idTextMap.get(v.getId()));
            operatorIsSetFlag = false;
            operandGenerator = "";
        } else if (v.getId() == R.id.button_equal) {
            // Add Equality Code
            String result = "";
            operandGenerator = "";

            int cx = resultTextView.getWidth();
            int cy = 0;

            float finalRadius = (float) Math.hypot(cx, cy);

            Animator anim =
                    ViewAnimationUtils.createCircularReveal(resultTextView, cx, cy, 0, finalRadius);

            resultTextView.setVisibility(View.VISIBLE);
            anim.start();

            switch (operator) {
                case 0:
                    result = String.valueOf(operandOne + operandTwo);
                    break;
                case 1:
                    result = String.valueOf(operandOne - operandTwo);
                    break;
                case 2:
                    result = String.valueOf(operandOne * operandTwo);
                    break;
                case 3:
                    result = String.valueOf(operandOne / operandTwo);
                    break;
                default:
                    resultTextView.setText(result);
            }

            resultTextView.setText(result);
            operandOne = Float.parseFloat(result);
            operandTwo = 0;
            operatorIsSetFlag = false;

        } else {
            // Operator was found
            if (!operatorIsSetFlag) {
                operator = operatorMap.get(v.getId());
                operandGenerator = "";
                resultTextView.setText(appendExistingTextViewString(v.getId()));
                operatorIsSetFlag = true;
            } else {
                resultTextView.setText("Error");
                errorFlag = true;
            }
        }
    }

    public void populateTextMap() {
        idTextMap.put(R.id.button0, "0");
        idTextMap.put(R.id.button1, "1");
        idTextMap.put(R.id.button2, "2");
        idTextMap.put(R.id.button3, "3");
        idTextMap.put(R.id.button4, "4");
        idTextMap.put(R.id.button5, "5");
        idTextMap.put(R.id.button6, "6");
        idTextMap.put(R.id.button7, "7");
        idTextMap.put(R.id.button8, "8");
        idTextMap.put(R.id.button9, "9");
        idTextMap.put(R.id.button_point, ".");
        idTextMap.put(R.id.button_clear, "");
        idTextMap.put(R.id.button_divide, "/");
        idTextMap.put(R.id.button_multiply, "x");
        idTextMap.put(R.id.button_subtract, "-");
        idTextMap.put(R.id.button_add, "+");
        idTextMap.put(R.id.button_point, ".");
        idTextMap.put(R.id.button_equal, "=");
    }

    public void populateOperatorMap() {
        operatorMap.put(R.id.button_divide, 3);
        operatorMap.put(R.id.button_multiply, 2);
        operatorMap.put(R.id.button_subtract, 1);
        operatorMap.put(R.id.button_add, 0);
    }

    public String appendExistingTextViewString(int id) {
        String currentTextDisplayed = resultTextView.getText().toString();
        return currentTextDisplayed + idTextMap.get(id);
    }
}
