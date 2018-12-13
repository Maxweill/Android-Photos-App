package com.example.clifford.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.List;


public class SearchView extends AppCompatActivity {

    GlobalClass globalVariable;
    List results;
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        globalVariable = (GlobalClass) getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_view);


        ListView myList = (ListView)findViewById(R.id.results);

        String [] names = new String[0];
        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, names);
        myList.setAdapter(adapter);






        Button search = (Button) findViewById(R.id.searchButton);
        search.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                System.out.println("Searching");
                showSearchDialog(SearchView.this);

            }

        });


    }

    private void showSearchDialog(Context c) {
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Search for photos")
                .setView(taskEditText)
                .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());

                        String tag1 = null;
                        String val1 = null;
                        String tag2 = null;
                        String val2 = null;
                        int type = 0;

                        boolean multiple = false;

                        if ( task.indexOf(" AND ") >=0) {

                            multiple = true;

                            String [] firstSplit = task.split(" ");

                            if ( firstSplit.length != 3 ) {

                                //error
                                return;

                            }

                            String [] Firstpair = firstSplit[0].split("=");

                            if (Firstpair.length != 2){

                                //error
                                return;
                            }

                            String [] Secondpair = firstSplit[2].split("=");

                            tag1 = Firstpair[0];
                            val1 = Firstpair[1];
                            tag2 = Secondpair[0];
                            val2 = Secondpair[1];
                            type = 1;

                        }

                        else if ( task.indexOf(" OR ") >=0){

                            multiple = true;

                            String [] firstSplit = task.split(" ");


                            if ( firstSplit.length != 3 )
                                return;

                            String [] Firstpair = firstSplit[0].split("=");

                            if ( Firstpair.length != 2 ){

                                //error

                                return;
                            }

                            String [] Secondpair = firstSplit[2].split("=");

                            if ( Secondpair.length != 2 ){

                                //error

                                return;
                            }

                            tag1 = Firstpair[0];
                            val1 = Firstpair[1];
                            tag2 = Secondpair[0];
                            val2 = Secondpair[1];
                            type = 2;

                        }

                        else {

                            String [] split = task.split("=");


                            tag1 = split[0];
                            val1 = split[1];
                        }



                        List<Photo> searchresults = globalVariable.getAccount().search(tag1, val1, tag2, val2, type);

                        /****************************************/


                            ListView myList = (ListView)findViewById(R.id.results);

                            String [] names = new String[searchresults.size()];
                            int i = 0;
                            for ( Photo p: searchresults ){
                                names[i] = p.location;
                                i++;
                            }
                            ListAdapter adapter = new ArrayAdapter<String>(SearchView.this, android.R.layout.simple_expandable_list_item_1, names);
                            myList.setAdapter(adapter);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

}
