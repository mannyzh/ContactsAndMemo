package com.example.ntech.contactsandmemo.activitys;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.ntech.contactsandmemo.fragments.ContactListFragment;
import com.example.ntech.contactsandmemo.fragments.FileListFragment;
import com.example.ntech.contactsandmemo.fragments.NewContactFragment;
import com.example.ntech.contactsandmemo.fragments.WriteFragment;
import com.example.ntech.contactsandmemo.preferences.Profile;
import com.example.ntech.contactsandmemo.preferences.AppSettings;
import com.example.ntech.contactsandmemo.random.PagerAdapter;
import com.example.ntech.contactsandmemo.R;

public class MainActivity extends AppCompatActivity {

    private Handler handler = new Handler();

    ImageButton imagebuttonNewMemo, imageButtonNewContact;
    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imagebuttonNewMemo = (ImageButton) findViewById(R.id.imageButtonNewMemo);
        //imagebuttonNewMemo.setVisibility(View.INVISIBLE);

        imageButtonNewContact = (ImageButton) findViewById(R.id.imageButtonNewContact);


        imageButtonNewContact.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

/*
                IntentIntegrator.initiateScan(MainActivity.this, R.layout.capture,
                        R.id.viewfinder_view, R.id.preview_view, false);
*/


                //imageButtonNewContact.setVisibility(View.INVISIBLE);
                viewPager.setCurrentItem(0);

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                NewContactFragment newContactFragment = new NewContactFragment();
                fragmentTransaction.replace(R.id.fragment_container_tab1, newContactFragment).addToBackStack(null);
                fragmentTransaction.commit();

            }
        });


        imagebuttonNewMemo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                //imagebuttonNewMemo.setVisibility(View.INVISIBLE);
                viewPager.setCurrentItem(1);

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                WriteFragment writeFragment = new WriteFragment();
                fragmentTransaction.replace(R.id.fragment_container_tab2, writeFragment).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Contacts"));
        tabLayout.addTab(tabLayout.newTab().setText("Memo"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);



        viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                /*
                if (tab.getPosition() == 1) {
                    imagebuttonNewMemo.setVisibility(View.VISIBLE);
                }
                */
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                /*
                if (tab.getPosition() == 1) {
                    imagebuttonNewMemo.setVisibility(View.INVISIBLE);
                }
                */
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                switch (tab.getPosition()) {
                    case 0:
                        final ContactListFragment contactListFragment = new ContactListFragment();
                        fragmentTransaction.replace(R.id.fragment_container_tab1, contactListFragment);
                    case 1:
                        final FileListFragment fileListFragment = new FileListFragment();
                        fragmentTransaction.replace(R.id.fragment_container_tab2, fileListFragment);

                    default:
                }
                fragmentTransaction.commit();
            }
        });

        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content),"Welcome to Contacts and Memo :)", Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_profile) {
            Intent i = new Intent(this, Profile.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, AppSettings.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}