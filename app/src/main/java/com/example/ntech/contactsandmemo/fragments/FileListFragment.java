package com.example.ntech.contactsandmemo.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.ntech.contactsandmemo.R;

import java.io.File;

import java.util.ArrayList;

public class FileListFragment extends Fragment {

    private ListView lv;
    private ArrayAdapter<String> adapter;
    //private Button buttonNew;
    private final static String TAG_FRAGMENT = "write_fragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.file_list_fragment, container, false);

        ArrayList<String> FilesInFolder = GetFiles(getActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString());
        //ArrayList<String> FilesInFolder = GetFiles("/storage/emulated/0/Android/data/com.example.ntech.contactsandmemo/files/Documents");

        lv = (ListView)v.findViewById(R.id.file_list);

        adapter = new ArrayAdapter<String>(v.getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, FilesInFolder);

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                int itemPosition     = position;
                String  itemValue    = (String) lv.getItemAtPosition(position);

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("file_item", itemValue );
                ReadFragment rf = new ReadFragment();
                rf.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container_tab2, rf).addToBackStack(null).commit();

                Snackbar snackbar = Snackbar
                        .make(getActivity().findViewById(android.R.id.content),"Opening file... "+ itemValue, Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {

                final String  itemValue    = (String) lv.getItemAtPosition(position);
                final String positionToRemove = adapter.getItem(position);

                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                adb.setTitle("Delete file?");
                adb.setMessage("File: " + itemValue);
                adb.setNegativeButton("NO", null);
                adb.setPositiveButton("YES", new AlertDialog.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        adapter.remove(positionToRemove);

                        Snackbar snackbar = Snackbar
                                .make(getActivity().findViewById(android.R.id.content),"Deleting file... "+itemValue, Snackbar.LENGTH_SHORT);
                        snackbar.show();

                        File f = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), itemValue);
                        f.delete();
                        adapter.notifyDataSetChanged();
                    }});

                adb.show();
                return true;
            }
        });

        adapter.notifyDataSetChanged();
        return v;
    }

    public ArrayList<String> GetFiles(String DirectoryPath) {

        ArrayList<String> MyFiles = new ArrayList<String>();
        try {
            File f = new File(DirectoryPath);

            f.mkdirs();
            File[] files = f.listFiles();

            for (int i = 0; i < files.length; i++)
                MyFiles.add(files[i].getName());

        } catch (Exception pokemon) {
            Snackbar snackbar = Snackbar
                    .make(getActivity().findViewById(android.R.id.content),"0 files found error", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }

        return MyFiles;
    }
}
