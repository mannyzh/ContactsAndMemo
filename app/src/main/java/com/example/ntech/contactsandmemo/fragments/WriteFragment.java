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
import android.widget.ImageButton;

import com.example.ntech.contactsandmemo.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class WriteFragment extends Fragment {

    private Button saveButton, exitButton;
    private EditText fileName, filePassword, fileText;

    public WriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.write_fragment, container, false);

        fileName = (EditText) v.findViewById(R.id.text_filename);
        filePassword = (EditText) v.findViewById(R.id.text_password);
        fileText = (EditText) v.findViewById(R.id.text_write);

        saveButton = (Button) v.findViewById(R.id.button_save);
        exitButton = (Button) v.findViewById(R.id.button_exit);

        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String name = fileName.getText().toString();
                String pass = filePassword.getText().toString();
                String txt = fileText.getText().toString();

                if (name.length() > 0 && txt.length() > 0) {
                    writeToFile(name, pass, txt);

                    Snackbar snackbar = Snackbar
                            .make(getActivity().findViewById(android.R.id.content),"File saved!", Snackbar.LENGTH_SHORT);
                    snackbar.show();

                    exit();

                } else {
                    Snackbar snackbar = Snackbar
                            .make(getActivity().findViewById(android.R.id.content),"Could not save!", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Snackbar snackbar = Snackbar
                        .make(getActivity().findViewById(android.R.id.content),"Closing memo..", Snackbar.LENGTH_SHORT);
                snackbar.show();

                exit();
            }
        });

        fileName.requestFocus();

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
        FileListFragment fileListFragment = new FileListFragment();
        fragmentTransaction.replace(R.id.fragment_container_tab2, fileListFragment);
        fragmentTransaction.commit();
    }

    private void writeToFile(String FILE_NAME, String PASS, String TXT) {

        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){

            File f = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), FILE_NAME);

            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
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
}
