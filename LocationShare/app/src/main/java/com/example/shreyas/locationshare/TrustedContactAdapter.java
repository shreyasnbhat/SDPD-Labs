package com.example.shreyas.locationshare;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by shreyas on 4/2/18.
 */

public class TrustedContactAdapter extends RecyclerView.Adapter<ContactViewHolder> {

    private ArrayList<Contact> contactList = new ArrayList<>();
    private Context context;
    private DBHandler dbHandler;

    public TrustedContactAdapter(ArrayList<Contact> historyList, Context context, DBHandler handler) {
        this.contactList = historyList;
        this.context = context;
        this.dbHandler = handler;
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View contactView = inflater.inflate(R.layout.contact_format, parent, false);
        return new ContactViewHolder(contactView, context);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, final int position) {
        String name = contactList.get(position).getName();
        final String number = contactList.get(position).getNumber();
        Contact contactItem = new Contact(name, number);
        holder.setContactView(contactItem);

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHandler.deleteContactById(ContactFormat.format(number));
                contactList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, contactList.size());
            }
        });

    }

}
