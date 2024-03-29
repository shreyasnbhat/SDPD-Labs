package com.example.shreyas.calculator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView resultTextView;
    private TextView revealFrameView;
    private TextView noHistoryTextView;
    private TextView buttonZero, buttonOne, buttonTwo, buttonThree, buttonFour, buttonFive, buttonSix, buttonSeven, buttonEight, buttonNine, buttonAdd, buttonSubtract,
            buttonDivide, buttonMultiply, buttonEqual, buttonClear, buttonPoint, buttonHistory;
    private boolean isHistoryPressed = false;
    private HashMap<Integer, String> idTextMap = new HashMap<>();
    private HashMap<Integer, Integer> operatorMap = new HashMap<>();
    private HashMap<Integer, String> operatorIDToTextMap = new HashMap<>();
    private ArrayList<HistoryItem> historyList = new ArrayList<>();

    String operandGenerator = "";
    float operandOne, operandTwo;
    int operator;
    boolean operatorIsSetFlag = false, errorFlag = false, operandOneIsSet = false, operandTwoIsSet = false, minusFlag = false;

    private RecyclerView historyRecyclerView;
    private HistoryAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateTextMap();
        populateOperatorMap();
        populateOperatorIDToTextMap();

        resultTextView = findViewById(R.id.result);
        noHistoryTextView = findViewById(R.id.no_history);
        revealFrameView = findViewById(R.id.reveal_frame);
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
        historyRecyclerView = findViewById(R.id.history_recycler_view);

        resultTextView.setText("");
        revealFrameView.setVisibility(View.INVISIBLE);
        noHistoryTextView.setVisibility(View.INVISIBLE);

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

        // Recycler View Stuff
        historyAdapter = new HistoryAdapter(historyList, this);
        historyRecyclerView.setAdapter(historyAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        historyRecyclerView.setLayoutManager(manager);
        historyRecyclerView.setHasFixedSize(true);
        historyRecyclerView.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onClick(View v) {

        if (idTextMap.get(v.getId()).compareTo("9") <= 0 && idTextMap.get(v.getId()).compareTo("0") >= 0 && !errorFlag && !isHistoryPressed) {
            String toDisplayText = appendExistingTextViewString(v.getId());
            operandGenerator += idTextMap.get(v.getId());
            resultTextView.setText(toDisplayText);

            if (!operatorIsSetFlag) {
                try {
                    operandOne = Float.parseFloat(operandGenerator);
                    operandOneIsSet = true;
                } catch (NumberFormatException e) {
                    Log.e("MainActivity", "Number Format Exception");
                    resultTextView.setText("Error");
                    errorFlag = true;
                }
            } else {
                try {
                    operandTwo = Float.parseFloat(operandGenerator);
                    operandTwoIsSet = true;
                } catch (NumberFormatException e) {
                    Log.e("MainActivity", "Number Format Exception");
                    resultTextView.setText("Error");
                    errorFlag = true;
                }
            }
        } else if (v.getId() == R.id.button_clear) {
            resultTextView.setText(idTextMap.get(v.getId()));
            operatorIsSetFlag = false;
            operandOneIsSet = false;
            operandTwoIsSet = false;
            operandGenerator = "";
            errorFlag = false;
            isHistoryPressed = false;
            operandOne = 0;
            operandTwo = 0;
            minusFlag = false;

            // Reveal Animation
            int cx = revealFrameView.getWidth() / 2;
            int cy = revealFrameView.getHeight();
            float finalRadius = (float) Math.hypot(cx, cy) + 5;
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(revealFrameView, cx, cy, 0, finalRadius);

            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    revealFrameView.setVisibility(View.INVISIBLE);
                    resultTextView.setVisibility(View.VISIBLE);
                }
            });

            historyRecyclerView.setVisibility(View.INVISIBLE);
            noHistoryTextView.setVisibility(View.INVISIBLE);
            revealFrameView.setVisibility(View.VISIBLE);
            anim.start();

        } else if (v.getId() == R.id.button_equal && !isHistoryPressed && operandTwoIsSet && !errorFlag) {
            // Add Equality Code
            operandGenerator = "";
            try {
                String result = formatString(solve(operandOne, operandTwo, operator));
                String operatorString = operatorIDToTextMap.get(operator);
                resultTextView.setText(result);

                historyRecyclerView.setVisibility(View.INVISIBLE);
                noHistoryTextView.setVisibility(View.INVISIBLE);
                addToHistory(formatString(operandOne) + operatorString + formatString(operandTwo), result);

                operandOne = Float.parseFloat(result);
                operandTwo = 0;
                operandTwoIsSet = false;
                operatorIsSetFlag = false;
                minusFlag = false;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage());
            }

        } else if (v.getId() == R.id.button_point && !isHistoryPressed && !errorFlag) {
            String toDisplayText = appendExistingTextViewString(v.getId());
            operandGenerator += idTextMap.get(v.getId());
            resultTextView.setText(toDisplayText);

        } else if (v.getId() == R.id.history) {
            if (historyList.size() > 0) {
                historyRecyclerView.setVisibility(View.VISIBLE);
                noHistoryTextView.setVisibility(View.INVISIBLE);
            } else {
                noHistoryTextView.setVisibility(View.VISIBLE);
                historyRecyclerView.setVisibility(View.INVISIBLE);
            }
            resultTextView.setVisibility(View.INVISIBLE);
            isHistoryPressed = true;

        } else if (!isHistoryPressed && v.getId() != R.id.button_equal && !errorFlag) {

            // Operator was found
            if (!operandOneIsSet) {
                int operatorFromMap = operatorMap.get(v.getId());
                if (operatorFromMap != 1) {
                    operandGenerator = idTextMap.get(v.getId());
                    resultTextView.setText("Error");
                    errorFlag = true;
                } else {
                    operandGenerator = idTextMap.get(v.getId());
                    resultTextView.setText(appendExistingTextViewString(v.getId()));
                }
            } else if (!operatorIsSetFlag) {
                try {
                    operator = operatorMap.get(v.getId());
                    operandGenerator = "";
                    resultTextView.setText(appendExistingTextViewString(v.getId()));
                    operatorIsSetFlag = true;
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            } else {
                if (operandTwoIsSet) {
                    try {
                        String updatedResult = formatString(solve(operandOne, operandTwo, operator));
                        operandOne = Float.parseFloat(updatedResult);
                        operandTwo = 0;
                        operandTwoIsSet = false;
                        operator = operatorMap.get(v.getId());
                        operandGenerator = "";
                        String resultTextViewString = updatedResult + idTextMap.get(v.getId());
                        resultTextView.setText(resultTextViewString);
                    } catch (Exception e) {
                        Log.e("MainActivity", e.getMessage());
                    }
                } else if(!errorFlag){
                    int operatorFromMap = operatorMap.get(v.getId());
                    if (operatorFromMap != 1) {
                        operator = operatorFromMap;
                        String resultText = resultTextView.getText().toString();
                        int sizeOfResultText = resultText.length();
                        resultText = resultText.substring(0, sizeOfResultText - 1) + idTextMap.get(v.getId());
                        resultTextView.setText(resultText);
                    } else {
                        if (!minusFlag) {
                            operandGenerator = idTextMap.get(v.getId());
                            resultTextView.setText(appendExistingTextViewString(v.getId()));
                            minusFlag = true;
                        } else {
                            resultTextView.setText("Error");
                            errorFlag = true;
                        }
                    }
                }
            }
        }

        Log.e("TAG", operandOne + " " + operator + " " + operandTwo);
        Log.e("TAG", "operandTwoSet " + operandTwoIsSet + " " + "operatorIsSetFlag" + operatorIsSetFlag + " operandOneIsSet " + operandOneIsSet);
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
        idTextMap.put(R.id.history, "D");
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

    public String formatString(float operand) {
        if (operand == (long) operand) {
            return String.format("%d", (long) operand);
        } else
            return String.format("%s", operand);
    }

    public void addToHistory(String expression, String result) {
        if (historyList.size() < 5) {
            historyList.add(new HistoryItem(expression, result));
            historyAdapter.notifyDataSetChanged();
        } else if (historyList.size() >= 5) {
            for (int i = 0; i <= 3; i++) {
                historyList.set(i, historyList.get(i + 1));
            }
            historyList.set(4, new HistoryItem(expression, result));
            historyAdapter.notifyDataSetChanged();
        }
    }

    public float solve(float operandOne, float operandTwo, int operator) throws Exception {
        switch (operator) {
            case 0:
                return operandOne + operandTwo;
            case 1:
                return operandOne - operandTwo;
            case 2:
                return operandOne * operandTwo;
            case 3:
                return operandOne / operandTwo;
        }

        throw new Exception("Unsupported Operation Exception");
    }

    public void populateOperatorIDToTextMap() {
        operatorIDToTextMap.put(3, " / ");
        operatorIDToTextMap.put(2, " x ");
        operatorIDToTextMap.put(1, " - ");
        operatorIDToTextMap.put(0, " + ");
    }
}
