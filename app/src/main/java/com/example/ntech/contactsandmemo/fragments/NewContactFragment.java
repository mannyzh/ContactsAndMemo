package com.example.ntech.contactsandmemo.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.ntech.contactsandmemo.R;
import com.example.ntech.contactsandmemo.random.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class NewContactFragment extends Fragment {
    DatabaseHelper contactsDB;

    private Button saveButton, exitButton;
    private EditText contactName, phoneNumber, emailAddress, livingAddress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.new_contact_fragment, container, false);

        contactsDB = new DatabaseHelper(v.getContext());

        /*
        //Get User records from SQLite DB
        ArrayList<HashMap<String, String>> contactsList =  contactsDB.getAllUsers();
        */

        contactName = (EditText) v.findViewById(R.id.text_contact_name);
        phoneNumber = (EditText) v.findViewById(R.id.text_phone);
        emailAddress = (EditText) v.findViewById(R.id.text_email);
        livingAddress = (EditText) v.findViewById(R.id.text_living_address);

        saveButton = (Button) v.findViewById(R.id.button_save);
        exitButton = (Button) v.findViewById(R.id.button_exit);

        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String cN = contactName.getText().toString();
                String pN = phoneNumber.getText().toString();
                String eA = emailAddress.getText().toString();
                String lA = livingAddress.getText().toString();

                contactsDB.insertData(cN, pN, eA, lA);

                Snackbar snackbar = Snackbar
                        .make(getActivity().findViewById(android.R.id.content),"Contact added :)", Snackbar.LENGTH_SHORT);
                snackbar.show();
                exit();
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                exit();
            }
        });

        contactName.requestFocus();

        return v;
    }

    private void exit() {
        /*
        Finds the view of the imagebutton in the mainActivity and sets visibility back to true
                ImageButton ib = (ImageButton) getActivity().findViewById(R.id.imageButtonNewMemo);
        ib.setVisibility(View.VISIBLE);
        */


        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentManager.popBackStack();
        ContactListFragment contactListFragment = new ContactListFragment();
        fragmentTransaction.replace(R.id.fragment_container_tab1, contactListFragment);
        fragmentTransaction.commit();
    }
}
