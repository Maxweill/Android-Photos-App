package com.example.clifford.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.StringBufferInputStream;

public class PhotoView extends AppCompatActivity {

    GlobalClass globalVariable;
    Album a;
    int x;
    ImageView imageView;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        globalVariable = (GlobalClass) getApplicationContext();
        a = globalVariable.getAlbum();
        x = intent.getIntExtra("index",0);

        final Photo p = a.getPhotos().get(x);
        p.bmap = BitmapFactory.decodeByteArray(p.bytemap, 0, p.bytemap.length);

        imageView = (ImageView)findViewById(R.id.imageView);

        imageView.setImageBitmap(p.bmap);

        Button left = (Button)findViewById(R.id.Left);

        Button right = (Button)findViewById(R.id.Right);

        Button addTag = (Button)findViewById(R.id.addTag);

        listView = (ListView)findViewById(R.id.listView);

        String [] tags = new String[p.getTags().size()];
        int i = 0;
        for ( Tag t: p.getTags() ){

            tags[i] = t.toString();
            i++;
        }

        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, tags);

        listView.setAdapter(adapter);

        //add Tag

        Button deletetag = (Button) findViewById(R.id.deletetag);
        deletetag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Photo photo = a.getPhotos().get(x);
                showDeleteTagDialog(PhotoView.this, photo);
            }
        });

        addTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tagtype = (TextView) findViewById(R.id.TypeText);
                TextView tag = (TextView) findViewById(R.id.TagText);
                Photo p = a.getPhotos().get(x);

                String value = tag.getText().toString();
                String type = tagtype.getText().toString();
                System.out.println(tagtype);
                System.out.println(tag);
                if(type==null || value==null || type.isEmpty() || value.isEmpty())
                {
                    Toast.makeText(PhotoView.this, "Empty value, make sure both text fields are filled in!", Toast.LENGTH_LONG).show();
                }
                else if (type.equals("location") && !value.isEmpty())
                {

                    p.addTag("location",value,1);
                }
                else if (type.equals("person")&& !value.isEmpty())
                {
                    p.addTag("person",value,2);
                }
                else
                {
                    Toast.makeText(PhotoView.this, "Invalid tag type, valid tags are 'location' and 'person'.", Toast.LENGTH_LONG).show();
                }


                String [] tags = new String[p.getTags().size()];
                int i = 0;
                for ( Tag t: p.getTags() ){

                    tags[i] = t.toString();
                    i++;
                }

                ListAdapter adapter = new ArrayAdapter<String>(PhotoView.this, android.R.layout.simple_expandable_list_item_1, tags);

                listView.setAdapter(adapter);



            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                x--;

                Photo photo;

                if ( x < 0 ) {

                    x = a.getPhotos().size()-1;
                    photo = a.getPhotos().get(x);
                    photo.bmap = BitmapFactory.decodeByteArray(photo.bytemap, 0, photo.bytemap.length);
                    imageView.setImageBitmap(photo.bmap);

                }

                else{

                    photo = a.getPhotos().get(x);
                    photo.bmap = BitmapFactory.decodeByteArray(photo.bytemap, 0, photo.bytemap.length);
                    imageView.setImageBitmap(photo.bmap);

                }

                String [] tags = new String[photo.getTags().size()];
                int i = 0;
                for ( Tag t: photo.getTags() ){

                    tags[i] = t.toString();
                    i++;
                }

                ListAdapter adapter = new ArrayAdapter<String>(PhotoView.this, android.R.layout.simple_expandable_list_item_1, tags);

                listView.setAdapter(adapter);

            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x++;

                Photo photo;

                if ( x == a.getPhotos().size()){

                    x = 0;
                    photo = a.getPhotos().get(x);
                    photo.bmap = BitmapFactory.decodeByteArray(photo.bytemap, 0, photo.bytemap.length);
                    imageView.setImageBitmap(photo.bmap);
                }

                else {

                    photo = a.getPhotos().get(x);
                    photo.bmap = BitmapFactory.decodeByteArray(photo.bytemap, 0, photo.bytemap.length);
                    imageView.setImageBitmap(photo.bmap);

                }

                String [] tags = new String[photo.getTags().size()];
                int i = 0;
                for ( Tag t: photo.getTags() ){

                    tags[i] = t.toString();
                    i++;
                }

                ListAdapter adapter = new ArrayAdapter<String>(PhotoView.this, android.R.layout.simple_expandable_list_item_1, tags);

                listView.setAdapter(adapter);

            }
        });



    }

    private void showDeleteTagDialog(Context c, final Photo pho){
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Delete which tag?")
                .setView(taskEditText)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());
                        for(Tag t: pho.tags)
                        {
                            if (t.name.equals(task))
                            {
                                pho.deleteTag(task);
                                String [] tags = new String[pho.getTags().size()];
                                int i = 0;
                                for ( Tag tag: pho.getTags() ){

                                    tags[i] = tag.toString();
                                    i++;
                                }

                                ListAdapter adapter = new ArrayAdapter<String>(PhotoView.this, android.R.layout.simple_expandable_list_item_1, tags);

                                listView.setAdapter(adapter);

                                return;
                            }
                        }
                        Toast.makeText(PhotoView.this, "Invalid tag type, valid tags are 'location' and 'person'.", Toast.LENGTH_LONG).show();


                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();


    }
}
