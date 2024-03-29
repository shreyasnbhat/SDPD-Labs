package com.example.shreyas.locationshare;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by shreyas on 4/2/18.
 */

public class ContactViewHolder extends RecyclerView.ViewHolder {

    private TextView contactNumber;
    private TextView contactName;
    private TextView contactHeader;
    public ImageView deleteButton;
    private Context context;

    public ContactViewHolder(View itemView, Context context) {
        super(itemView);
        this.contactNumber = itemView.findViewById(R.id.contact_number_view);
        this.contactName = itemView.findViewById(R.id.contact_name_view);
        this.contactHeader = itemView.findViewById(R.id.contact_text_header);
        this.deleteButton = itemView.findViewById(R.id.button_delete);
        this.context = context;
    }

    public void setContactView(Contact contact) {
        this.contactNumber.setText(contact.getNumber());
        this.contactName.setText(contact.getName());
        this.contactHeader.setText(contact.getName().charAt(0) + "");
    }
}
