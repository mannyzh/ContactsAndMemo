package com.example.ntech.contactsandmemo.fragments;


import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.ntech.contactsandmemo.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class ReadFragment extends Fragment {

    private EditText textbox, textRename;
    private Button saveButton;

    public ReadFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.read_fragment, container, false);

        textRename = (EditText)v.findViewById(R.id.textRename);

        textbox = (EditText)v.findViewById(R.id.text_read);
        saveButton = (Button)v.findViewById(R.id.buttonSave);

        final String file_item = this.getArguments().getString("file_item");
        read(file_item);

        textRename.setText(file_item);

        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            //On click function
            public void onClick(View view) {

                writeToFile(file_item, "", textbox.getText().toString());

                Snackbar snackbar = Snackbar
                        .make(getActivity().findViewById(android.R.id.content),"File saved!", Snackbar.LENGTH_SHORT);
                snackbar.show();

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentManager.popBackStack();
                final FileListFragment fileListFragment = new FileListFragment();
                fragmentTransaction.replace(R.id.fragment_container_tab2, fileListFragment);
                fragmentTransaction.commit();
            }
        });

        textbox.requestFocus();

        return v;
    }

    private void writeToFile(String FILE_NAME, String PASS, String TXT) {

        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){

            File f = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), FILE_NAME);
            File f2 = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), textRename.getText().toString());

            f.delete();

            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(f2, true));
                bw.write(TXT);
                bw.close();

            } catch (Exception pokemon) {
                Snackbar snackbar = Snackbar
                        .make(getActivity().findViewById(android.R.id.content),"Something bad. Probably missing write permission or concurrent " +
                                "access to file: " + pokemon, Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        }
    }

    private void read(String FILE_NAME) {
        String tmp = "", tmpLine = "";

        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){

            File f = new File(getContext().getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), FILE_NAME);

            try {
                BufferedReader bf = new BufferedReader(new FileReader(f));
                while((tmpLine = bf.readLine()) != null){
                    tmp += tmpLine + "\n";
                }
                bf.close();
                textbox.setText(tmp);

            } catch (Exception pokemon) {
                Snackbar snackbar = Snackbar
                        .make(getActivity().findViewById(android.R.id.content),"Something bad. Probablly the file does not exist: " + pokemon, Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        }
    }
}
