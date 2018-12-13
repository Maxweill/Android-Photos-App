package com.example.clifford.myapplication;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

        Photo p = a.getPhotos().get(x);
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




            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                x--;

                if ( x < 0 ) {

                    x = a.getPhotos().size()-1;
                    Photo p = a.getPhotos().get(x);
                    p.bmap = BitmapFactory.decodeByteArray(p.bytemap, 0, p.bytemap.length);
                    imageView.setImageBitmap(p.bmap);

                }

                else{

                    Photo p = a.getPhotos().get(x);
                    p.bmap = BitmapFactory.decodeByteArray(p.bytemap, 0, p.bytemap.length);
                    imageView.setImageBitmap(p.bmap);

                }

      /*          String [] tags = new String[p.getTags().size()];
                int i = 0;
                for ( Tag t: p.getTags() ){

                    tags[i] = t.toString();
                    i++;
                }

                ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, tags);

                listView.setAdapter(adapter); */

            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x++;

                if ( x == a.getPhotos().size()){

                    x = 0;
                    Photo p = a.getPhotos().get(x);
                    p.bmap = BitmapFactory.decodeByteArray(p.bytemap, 0, p.bytemap.length);
                    imageView.setImageBitmap(p.bmap);
                }

                else {

                    Photo p = a.getPhotos().get(x);
                    p.bmap = BitmapFactory.decodeByteArray(p.bytemap, 0, p.bytemap.length);
                    imageView.setImageBitmap(p.bmap);

                }

            }
        });



    }
}
