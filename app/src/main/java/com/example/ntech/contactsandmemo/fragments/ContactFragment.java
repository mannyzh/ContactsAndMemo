package com.example.ntech.contactsandmemo.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ntech.contactsandmemo.R;
import com.example.ntech.contactsandmemo.random.DatabaseHelper;

public class ContactFragment extends Fragment {
    DatabaseHelper contactsDB;
    TextView tv;
    String lol;
    final static String DATA_RECEIVE = "contact";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.contact_fragment, container, false);

        tv = (TextView) v.findViewById(R.id.text_write);
        contactsDB = new DatabaseHelper(v.getContext());


        return v;
    }
    @Override
    public void onStart() {
        super.onStart();

        Bundle args = getArguments();
        if (args != null) {
            Cursor data = contactsDB.getContact(args.getString(DATA_RECEIVE));

            if(data.getCount() == 0) {
                Snackbar snackbar = Snackbar
                        .make(getActivity().findViewById(android.R.id.content),"Database was empty :(", Snackbar.LENGTH_SHORT);
                snackbar.show();
            } else {
                while(data.moveToNext()){
                    lol += data.getString(0);
                }
                tv.setText(lol);
            }

        }
    }
}
