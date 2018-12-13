package com.example.clifford.myapplication;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.*;
import java.nio.file.Paths;

public class MainActivity extends AppCompatActivity {


    GlobalClass globalVariable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        String[] PERMISSIONS = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        ActivityCompat.requestPermissions(this,
                PERMISSIONS,
                1);

        globalVariable = (GlobalClass) getApplicationContext();
        try {
            // must populate the hashset
            File homedir = getFilesDir();
            File file = new File(homedir.getPath()+ "/SAVE.DAT");
            if (file.exists()) {
                System.out.println("It Exists!");
                FileInputStream filei = new FileInputStream(file);
                ObjectInputStream obji = new ObjectInputStream(filei);
                globalVariable.setAccount((Account) obji.readObject());

                /* magic */
                    //p.bmap = BitmapFactory.decodeByteArray(p.bytemap, 0, p.bytemap.length);
                    //System.out.println("trying to show photo");
                    //ImageView maxView = (ImageView)findViewById(R.id.maxView);
                    //maxView.setImageBitmap(p.bmap);
                /* magic*/

            } else {
                System.out.println("It Doesnt Exist!");
                Account account = new Account("stock");
                account.createAlbum("stock");
                Album album = account.getAlbums().get(0);

                account.addPhotoToAlbum(album,"akame");
                account.addPhotoToAlbum(album,"avl");
                account.addPhotoToAlbum(album,"bnha");
                account.addPhotoToAlbum(album,"pepe");
                account.addPhotoToAlbum(album,"sponge");

                Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.akame);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, stream);

                account.getMasterphotolist().get("akame").bytemap= stream.toByteArray();
                account.getMasterphotolist().get("akame").addTag("location","New York",0);

                image = BitmapFactory.decodeResource(getResources(), R.drawable.avl);
                stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, stream);

                account.getMasterphotolist().get("avl").bytemap= stream.toByteArray();
                account.getMasterphotolist().get("avl").addTag("location","New Jersey",0);

                image = BitmapFactory.decodeResource(getResources(), R.drawable.bnha);
                stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, stream);

                account.getMasterphotolist().get("bnha").bytemap= stream.toByteArray();
                account.getMasterphotolist().get("bnha").addTag("location","Newport",0);

                image = BitmapFactory.decodeResource(getResources(), R.drawable.pepe);
                stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, stream);

                account.getMasterphotolist().get("pepe").bytemap= stream.toByteArray();

                image = BitmapFactory.decodeResource(getResources(), R.drawable.sponge);
                stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, stream);

                account.getMasterphotolist().get("sponge").bytemap= stream.toByteArray();
                account.getMasterphotolist().get("sponge").addTag("location","Newfoundland",0);

                globalVariable.setAccount(account);

                album = null;



            }
        }
        catch (Exception e){

                e.printStackTrace();
            }


        String [] names = new String [globalVariable.getAccount().albums.size()];

        int i = 0;

        for ( Album a: globalVariable.getAccount().albums ){

            names[i] = a.getName();
            i++;
        }

        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, names);

        final ListView myListView = (ListView)findViewById(R.id.CliffView);

        myListView.setAdapter(adapter);
        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Entering file picker");
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
            }
        });
        */

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long arg3) {
                view.setSelected(true);

                for (int j = 0; j < parent.getChildCount(); j++)
                    parent.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);

                // change the background color of the selected element
                view.setBackgroundColor(Color.LTGRAY);
                for(Album a : globalVariable.getAccount().albums)
                {
                    if (a.getName().equals(globalVariable.getAccount().getAlbums().get(position).name))
                    {
                        globalVariable.setAlbum(a);
                        break;
                    }
                }

                System.out.println(globalVariable.getAccount().getAlbums().get(position).name);
            }
        });

        Button delete = (Button) findViewById(R.id.DeleteAlb);
        delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                if(globalVariable.getAlbum() !=null) {
                    showDeleteItemDialog(MainActivity.this, globalVariable.getAlbum().name);
                    globalVariable.setAlbum(null);
                }
            }
        });

        Button create = (Button) findViewById(R.id.CreateAlb);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddItemDialog(MainActivity.this);
                globalVariable.setAlbum(null);
            }
        });

        Button rename = (Button) findViewById(R.id.RenameAlb);
        rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(globalVariable.getAlbum()!=null)
                {
                    showRenameItemDialog(MainActivity.this,globalVariable.getAlbum().name);
                    globalVariable.setAlbum(null);
                }
            }
        });

        Button search = (Button) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchView.class);
                startActivity(intent);
            }
        });

        Button open = (Button) findViewById(R.id.OpenAlb);
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(globalVariable.getAlbum()!=null) {
                    Intent intent = new Intent(MainActivity.this, AlbumView.class);
                    startActivity(intent);
                }
            }
        });


    }

    private void showRenameItemDialog(Context c, final String s){
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Renaming album \"" +s+"\"")
                .setView(taskEditText)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());
                        globalVariable.getAccount().renameAlbum(s,task);

                        String [] names = new String [ globalVariable.getAccount().albums.size()];
                        int i = 0;
                        for ( Album a:  globalVariable.getAccount().albums ){

                            names[i] = a.getName();
                            i++;
                        }
                        ListAdapter adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_expandable_list_item_1, names);
                        final ListView myListView = (ListView)findViewById(R.id.CliffView);
                        myListView.setAdapter(adapter);

                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }


    private void showAddItemDialog(Context c) {
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Add a new album")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());
                        if ( globalVariable.getAccount().createAlbum(task))
                        {
                            System.out.println("It worked!");
                            String [] names = new String [ globalVariable.getAccount().albums.size()];
                            int i = 0;
                            for ( Album a:  globalVariable.getAccount().albums ){

                                names[i] = a.getName();
                                i++;
                            }
                            ListAdapter adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_expandable_list_item_1, names);
                            final ListView myListView = (ListView)findViewById(R.id.CliffView);
                            myListView.setAdapter(adapter);
                        }
                        else
                        {
                            System.out.println("It failed!");
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    private void showDeleteItemDialog(Context c, final String s) {

        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Deleting album \"" +s+"\"")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        globalVariable.getAccount().deleteAlbum(s);

                            String [] names = new String [ globalVariable.getAccount().albums.size()];
                            int i = 0;
                            for ( Album a:  globalVariable.getAccount().albums ){

                                names[i] = a.getName();
                                i++;
                            }
                            ListAdapter adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_expandable_list_item_1, names);
                            final ListView myListView = (ListView)findViewById(R.id.CliffView);
                            myListView.setAdapter(adapter);

                    }
                    })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        try{
        System.out.println("Entering activity result");
        if (requestCode == 10) {

            System.out.println(data.getData().toString());
            globalVariable.getAccount().addPhotoToAlbum( globalVariable.getAlbum(),data.getData().toString());
            Photo p =  globalVariable.getAccount().getMasterphotolist().get(data.getData().toString());
            System.out.println("Creating a photo");
            //ImageView maxView = (ImageView)findViewById(R.id.maxView);

            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
            p.bmap = bitmap;

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            p.bytemap = stream.toByteArray();

           // maxView.setImageBitmap(bitmap);
           // System.out.println("showing photo");

        }}
        catch(Exception e)
        {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
            System.out.println("serializing");
            file.delete();
            file.createNewFile();

            FileOutputStream fileo = new FileOutputStream(file);
            ObjectOutputStream objo = new ObjectOutputStream(fileo);
            objo.writeObject(globalVariable.getAccount());
            fileo.close();
            objo.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            1);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
