package com.example.clifford.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

public class AlbumView extends AppCompatActivity {
    GlobalClass globalVariable;
    Album a;
    Photo photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        globalVariable = (GlobalClass) getApplicationContext();
        a = globalVariable.getAlbum();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_view);
        System.out.println(a.name);

        final ListView myList = (ListView)findViewById(R.id.pictureview);

        final String [] names = new String[a.photos.size()];
        int i = 0;
        for ( Photo p: a.photos ){
            names[i] = p.location;
            i++;
        }


       class CustomAdapter extends BaseAdapter{


           @Override
           public int getCount() {
               return names.length;
           }

           @Override
           public Object getItem(int position) {
               return null;
           }

           @Override
           public long getItemId(int position) {
               return 0;
           }

           @Override
           public View getView(int position, View convertView, ViewGroup parent) {

               convertView = getLayoutInflater().inflate(R.layout.customlayout,null);
               ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView);
               TextView textView = (TextView)convertView.findViewById(R.id.text);

               Photo p = globalVariable.getAlbum().photos.get(position);
               p.bmap = BitmapFactory.decodeByteArray(p.bytemap, 0, p.bytemap.length);

               imageView.setImageBitmap(p.bmap);
               textView.setText(names[position]);

               return convertView;
           }


       }

       CustomAdapter customAdapter = new CustomAdapter();
        myList.setAdapter(customAdapter);

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long arg3) {
                view.setSelected(true);

                for (int j = 0; j < parent.getChildCount(); j++)
                    parent.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);

                // change the background color of the selected element
                view.setBackgroundColor(Color.LTGRAY);
                photo = globalVariable.getAlbum().photos.get(position);
                System.out.println(photo.location);
            }
        });

        Button delete = (Button) findViewById(R.id.RemovePhoto);
        delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                if(photo !=null) {
                    showDeleteItemDialog(AlbumView.this, photo);
                    photo = null;
                }
            }
        });


        Button add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                System.out.println("Entering file picker");
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
            }

        });

        Button move = (Button) findViewById(R.id.MovePhoto);
        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(photo !=null) {
                    showMoveItemDialog(AlbumView.this, photo);
                    photo = null;
                }
            }
        });


        Button display = (Button) findViewById(R.id.display);
        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlbumView.this, PhotoView.class);
                intent.putExtra("index",a.photos.indexOf(photo));
                startActivity(intent);
            }
        });







    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        try{
            System.out.println("Entering activity result");
            if (requestCode == 10) {

                System.out.println(data.getData().toString());
                globalVariable.getAccount().addPhotoToAlbum(a,data.getData().toString());
                Photo p =  globalVariable.getAccount().getMasterphotolist().get(data.getData().toString());
                System.out.println("Creating a photo");
                //ImageView maxView = (ImageView)findViewById(R.id.maxView);

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                p.bmap = bitmap;

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                p.bytemap = stream.toByteArray();

                // maxView.setImageBitmap(bitmap);
                /************/
                final String [] names = new String[a.photos.size()];

                ListView myList = (ListView)findViewById(R.id.pictureview);
                int i = 0;
                for ( Photo ph: a.photos ){
                    names[i] = ph.location;
                    i++;
                }

                class CustomAdapter extends BaseAdapter{


                    @Override
                    public int getCount() {
                        return names.length;
                    }

                    @Override
                    public Object getItem(int position) {
                        return null;
                    }

                    @Override
                    public long getItemId(int position) {
                        return 0;
                    }

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {

                        convertView = getLayoutInflater().inflate(R.layout.customlayout,null);
                        ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView);
                        TextView textView = (TextView)convertView.findViewById(R.id.text);

                        Photo p = globalVariable.getAlbum().photos.get(position);
                        p.bmap = BitmapFactory.decodeByteArray(p.bytemap, 0, p.bytemap.length);

                        imageView.setImageBitmap(p.bmap);
                        textView.setText(names[position]);

                        return convertView;
                    }


                }
                CustomAdapter adapter = new CustomAdapter();
                myList.setAdapter(adapter);

                /**************************/
                // System.out.println("showing photo");

            }}
        catch(Exception e)
        {

        }
    }


    private void showDeleteItemDialog(Context c, final Photo del) {
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Deleting photo\"" +del.location+"\"")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        globalVariable.getAccount().removePhotoFromAlbum(globalVariable.getAlbum(),del);
                        /************/
                            final String [] names = new String[a.photos.size()];

                            ListView myList = (ListView)findViewById(R.id.pictureview);
                            int i = 0;
                            for ( Photo p: a.photos ){
                                names[i] = p.location;
                                i++;
                            }

                        class CustomAdapter extends BaseAdapter{


                            @Override
                            public int getCount() {
                                return names.length;
                            }

                            @Override
                            public Object getItem(int position) {
                                return null;
                            }

                            @Override
                            public long getItemId(int position) {
                                return 0;
                            }

                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {

                                convertView = getLayoutInflater().inflate(R.layout.customlayout,null);
                                ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView);
                                TextView textView = (TextView)convertView.findViewById(R.id.text);

                                Photo p = globalVariable.getAlbum().photos.get(position);
                                p.bmap = BitmapFactory.decodeByteArray(p.bytemap, 0, p.bytemap.length);

                                imageView.setImageBitmap(p.bmap);
                                textView.setText(names[position]);

                                return convertView;
                            }


                        }

                        CustomAdapter adapter = new CustomAdapter();
                        myList.setAdapter(adapter);


                        /**************************/
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    private void showMoveItemDialog(Context c, final Photo move) {
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Moving photo \"" +move.location+"\" to which directory?")
                .setView(taskEditText)
                .setPositiveButton("Move", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());
                        Album temp=null;
                        for(Album alb: globalVariable.getAccount().albums)
                        {
                            if (alb.getName().equals(task))
                            {
                                temp = alb;
                                a.move(alb,move);
                                break;
                            }
                        }
                        if (temp==null)
                        {
                            Toast.makeText(AlbumView.this, "No such album exists. Please note albums are CASE SENSITIVE.", Toast.LENGTH_SHORT).show();
                        }
                        /************/
                        final String [] names = new String[a.photos.size()];

                        ListView myList = (ListView)findViewById(R.id.pictureview);
                        int i = 0;
                        for ( Photo p: a.photos ){
                            names[i] = p.location;
                            i++;
                        }

                        class CustomAdapter extends BaseAdapter{


                            @Override
                            public int getCount() {
                                return names.length;
                            }

                            @Override
                            public Object getItem(int position) {
                                return null;
                            }

                            @Override
                            public long getItemId(int position) {
                                return 0;
                            }

                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {

                                convertView = getLayoutInflater().inflate(R.layout.customlayout,null);
                                ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView);
                                TextView textView = (TextView)convertView.findViewById(R.id.text);

                                Photo p = globalVariable.getAlbum().photos.get(position);
                                p.bmap = BitmapFactory.decodeByteArray(p.bytemap, 0, p.bytemap.length);

                                imageView.setImageBitmap(p.bmap);
                                textView.setText(names[position]);

                                return convertView;
                            }


                        }

                        CustomAdapter adapter = new CustomAdapter();
                        myList.setAdapter(adapter);


                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

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
}

