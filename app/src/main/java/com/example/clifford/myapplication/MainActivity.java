package com.example.clifford.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class MainActivity extends AppCompatActivity {


    Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        try {
            // must populate the hashset
            File homedir = getFilesDir();
            File file = new File(homedir.getPath()+ "/SAVE.DAT");

            if (file.exists()) {
                System.out.println("It Exists!");
                FileInputStream filei = new FileInputStream(file);
                ObjectInputStream obji = new ObjectInputStream(filei);
                account = (Account) obji.readObject();

            } else {
                System.out.println("It Doesnt Exist!");
                account = new Account("stock");
            }
        }
        catch (Exception e){

            e.printStackTrace();
        }

        String [] names = new String[account.albums.size()];
        int i = 0;
        for ( Album a: account.albums ) {
            names[i] = a.getName();
            i++;
        }

        //get button

        Button albButton = (Button)findViewById(R.id.openAlb);

        ListAdapter cliffAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        final ListView cliffList = (ListView)findViewById(R.id.cliffList);
        cliffList.setAdapter(cliffAdapter);

        albButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick ( View view ){

                String item = (String)cliffList.getSelectedItem();

            }
        });

    }



    @Override
    public void onPause()
    {
        super.onPause();
        try {
            serialize();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    void serialize()
    {
        try {
            File homedir = getFilesDir();
            File file = new File(homedir.getPath()+ "/SAVE.DAT");
            //System.out.println(file.getAbsoluteFile());
            file.delete();
            file.createNewFile();

            FileOutputStream fileo = new FileOutputStream(file);
            ObjectOutputStream objo = new ObjectOutputStream(fileo);
            objo.writeObject(account);
            fileo.close();
            objo.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


}
