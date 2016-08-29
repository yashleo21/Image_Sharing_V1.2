package com.roflcopter.starter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class UserProfile extends AppCompatActivity {

    String profileName;
    ImageView userPic;
    EditText userName;
    LinearLayout linearLayout;
    Application currentRecord;
    private ArrayList<Application> applications;
    ListView listTesting;

    public ArrayList<Application> getApplications() {
        return applications;
    }

    public UserProfile() {
        applications = new ArrayList<Application>();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userPic = (ImageView)findViewById(R.id.userPic);
        userName = (EditText) findViewById(R.id.niggerName);
        linearLayout = (LinearLayout) findViewById(R.id.progressbar_view);
//        linearLayout.setVisibility(View.INVISIBLE);
        listTesting = (ListView) findViewById(R.id.listTesting);

        final ArrayAdapter<Application> listAdapter = new CustomAdapter(UserProfile.this,R.layout.gird_item_layout,getApplications());
        listTesting.setAdapter(listAdapter);


        Intent i = getIntent();
        profileName=i.getStringExtra("Username");
        userName.setText(profileName);

        setTitle(profileName + "'s " + "Profile");

        //Fetch Profile Pic
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("ProfileInfo");
        query.whereEqualTo("username",profileName);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    if(objects.size()>0){
                        for(ParseObject object : objects){

                            ParseFile file = (ParseFile)object.get("ProfilePic");
                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if(e==null){
                                        Bitmap image = BitmapFactory.decodeByteArray(data,0,data.length);
                                        userPic.setImageBitmap(image);
                                    }
                                }
                            });

                        }
                    }
                }
            }
        });

        ParseQuery<ParseObject> queryy = new ParseQuery<ParseObject>("Images");
        queryy.whereEqualTo("username", profileName);
        queryy.orderByDescending("createdAt");

        queryy.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        for (ParseObject object : objects) {
                            ParseFile file = (ParseFile) object.get("image");
                            linearLayout.setVisibility(View.VISIBLE);
                            listTesting.setVisibility(View.VISIBLE);
                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if (e == null) {
                                        Bitmap image = BitmapFactory.decodeByteArray(data, 0, data.length);

                                        currentRecord = new Application();
                                        currentRecord.setBitmap(image);
                                        applications.add(currentRecord);
                                        listAdapter.notifyDataSetChanged();
//                                            ImageView imageView = new ImageView(getApplicationContext());
//                                            imageView.setImageBitmap(image);
//                                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(800, 1000);
//                                            layoutParams.setMargins(0, 150, 0, 150);
//                                            imageView.setLayoutParams(layoutParams);
//                                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//                                            imageView.setBackgroundResource(R.drawable.borderforpic);
//                                            imageView.setPadding(7, 7, 7, 7);

//                                            linearLayout.addView(imageView);
                                    }
                                }
                            });

                        }
                    }
                    else
                        linearLayout.setVisibility(View.INVISIBLE);
                    currentRecord = new Application();
                    Bitmap Icon = BitmapFactory.decodeResource(getResources(),R.drawable.noimage);
                    currentRecord.setBitmap(Icon);
                    listAdapter.notifyDataSetChanged();
                }
            }
        });


    }

}
