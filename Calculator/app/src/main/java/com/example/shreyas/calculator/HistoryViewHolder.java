package com.example.shreyas.calculator;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by shreyas on 20/1/18.
 */

public class HistoryViewHolder extends RecyclerView.ViewHolder {

    private TextView expressionTextView;
    private TextView resultTextView;
    private Context context;

    public HistoryViewHolder(View itemView, Context context) {
        super(itemView);
        this.expressionTextView = itemView.findViewById(R.id.expression_view);
        this.resultTextView = itemView.findViewById(R.id.result_view);
        this.context = context;
    }

    public void setHistoryView(HistoryItem historyItemText) {
        this.expressionTextView.setText(historyItemText.getExpression());
        this.resultTextView.setText(historyItemText.getResult());
    }

}
