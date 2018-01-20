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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView resultTextView;
    private TextView revealFrameView;
    private TextView buttonZero, buttonOne, buttonTwo, buttonThree, buttonFour, buttonFive, buttonSix, buttonSeven, buttonEight, buttonNine, buttonAdd, buttonSubtract,
            buttonDivide, buttonMultiply, buttonEqual, buttonClear, buttonPoint, buttonHistory;
    private boolean isHistoryPressed = false;
    private HashMap<Integer, String> idTextMap = new HashMap<>();
    private HashMap<Integer, Integer> operatorMap = new HashMap<>();
    private ArrayList<HistoryItem> historyList = new ArrayList<>();

    String operandGenerator = "";
    float operandOne, operandTwo;
    int operator;
    boolean operatorIsSetFlag = false, errorFlag = false;

    private RecyclerView historyRecyclerView;
    private HistoryAdapter historyAdpater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateTextMap();
        populateOperatorMap();

        resultTextView = findViewById(R.id.result);
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
        historyAdpater = new HistoryAdapter(historyList, this);
        historyRecyclerView.setAdapter(historyAdpater);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        historyRecyclerView.setLayoutManager(manager);
        historyRecyclerView.setHasFixedSize(true);
        historyRecyclerView.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onClick(View v) {
        Log.e("TAG", operandOne + " " + operator + " " + operandTwo);
        if (idTextMap.get(v.getId()).compareTo("9") <= 0 && idTextMap.get(v.getId()).compareTo("0") >= 0 && !errorFlag && !isHistoryPressed) {
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
            errorFlag = false;
            isHistoryPressed = false;

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
            revealFrameView.setVisibility(View.VISIBLE);
            anim.start();

        } else if (v.getId() == R.id.button_equal && !isHistoryPressed) {
            // Add Equality Code
            String result = "";
            String operatorString = "";
            operandGenerator = "";

            switch (operator) {
                case 0:
                    result = String.valueOf(operandOne + operandTwo);
                    operatorString = " + ";
                    break;
                case 1:
                    result = String.valueOf(operandOne - operandTwo);
                    operatorString = " - ";
                    break;
                case 2:
                    result = String.valueOf(operandOne * operandTwo);
                    operatorString = " x ";
                    break;
                case 3:
                    result = String.valueOf(operandOne / operandTwo);
                    operatorString = " / ";
                    break;
                default:
                    resultTextView.setText(result);
            }

            resultTextView.setText(result);
            addToHistory(operandOne + operatorString + operandTwo, result);
            operandOne = Float.parseFloat(result);
            operandTwo = 0;
            operatorIsSetFlag = false;

        } else if (v.getId() == R.id.button_point && !isHistoryPressed) {
            String toDisplayText = appendExistingTextViewString(v.getId());
            operandGenerator += idTextMap.get(v.getId());
            resultTextView.setText(toDisplayText);
        } else if (v.getId() == R.id.history) {
            historyRecyclerView.setVisibility(View.VISIBLE);
            resultTextView.setVisibility(View.INVISIBLE);
            isHistoryPressed = true;
        } else if(!isHistoryPressed) {
            // Operator was found
            if (!operatorIsSetFlag) {
                try {
                    operator = operatorMap.get(v.getId());
                    operandGenerator = "";
                    resultTextView.setText(appendExistingTextViewString(v.getId()));
                    operatorIsSetFlag = true;
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
            } else {
                resultTextView.setText("Error");
                errorFlag = true;
                operandOne = 0;
                operandTwo = 0;
                operator = 0;
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
        idTextMap.put(R.id.history,"D");
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

    public void addToHistory(String expression, String result) {
        if (historyList.size() < 5) {
            historyList.add(new HistoryItem(expression, result));
            historyAdpater.notifyDataSetChanged();
        } else if (historyList.size() >= 5) {
            for (int i = 0; i <= 3; i++) {
                historyList.set(i, historyList.get(i + 1));
            }
            historyList.set(4, new HistoryItem(expression, result));
            historyAdpater.notifyDataSetChanged();
        }
    }
}
