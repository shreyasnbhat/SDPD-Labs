package com.example.shreyas.calculator;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shreyas on 20/1/18.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryViewHolder> {

    private ArrayList<HistoryItem> historyList;
    private Context context;

    public HistoryAdapter(ArrayList<HistoryItem> historyList, Context context) {
        this.historyList = historyList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View historyView = inflater.inflate(R.layout.history_viewholder_format, parent, false);
        return new HistoryViewHolder(historyView, context);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        String expression = historyList.get(position).getExpression();
        String result = historyList.get(position).getResult();
        HistoryItem historyItemText = new HistoryItem(expression,result);
        holder.setHistoryView(historyItemText);
    }
}
