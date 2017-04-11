package com.example.ntech.contactsandmemo.fragments;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.ntech.contactsandmemo.R;
import com.example.ntech.contactsandmemo.random.DatabaseHelper;

import java.io.File;
import java.util.ArrayList;

public class ContactListFragment extends Fragment {
    DatabaseHelper contactsDB;
    ListView listView;
    ListAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.contact_list_fragment, container, false);

        contactsDB = new DatabaseHelper(v.getContext());
        listView = (ListView) v.findViewById(R.id.contact_list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                int itemPosition     = position;
                String  itemValue    = (String) listView.getItemAtPosition(position);

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Bundle bundle = new Bundle();
                bundle.putString("contact", itemValue );
                ContactFragment contactFragment = new ContactFragment();
                contactFragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.fragment_container_tab1, contactFragment).addToBackStack(null).commit();

                Snackbar snackbar = Snackbar
                        .make(getActivity().findViewById(android.R.id.content),"Opening contact... "+ itemValue, Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });

        ArrayList<String> list = new ArrayList<>();
        Cursor data = contactsDB.getAllRows();

        if(data.getCount() == 0) {
            Snackbar snackbar = Snackbar
                    .make(getActivity().findViewById(android.R.id.content),"Database was empty :(", Snackbar.LENGTH_SHORT);
            snackbar.show();
        } else {
            while(data.moveToNext()){
                list.add(data.getString(0));
                listAdapter = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_list_item_1, list);
                listView.setAdapter(listAdapter);
            }
        }

        return v;
    }

}
