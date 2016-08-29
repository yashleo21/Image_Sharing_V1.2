package com.roflcopter.starter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
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

public class UserFeed extends AppCompatActivity {
static String activeUsername;
    LinearLayout linearLayout;
    Application currentRecord;
    private ArrayList<Application> applications;
    ListView listTesting;

    public ArrayList<Application> getApplications() {
        return applications;
    }

    public UserFeed() {
        applications = new ArrayList<Application>();
    }

    public void setApplications(ArrayList<Application> applications) {
        this.applications = applications;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listTesting = (ListView) findViewById(R.id.listTesting);
        final ArrayAdapter<Application> listAdapter = new CustomAdapter(UserFeed.this,R.layout.gird_item_layout,getApplications());
        listTesting.setAdapter(listAdapter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      linearLayout = (LinearLayout) findViewById(R.id.progressbar_view);
        Intent i = getIntent();
        activeUsername = i.getStringExtra("Username");
//        Log.i("username:",activeUsername);

            setTitle(activeUsername + "'s " + "Photos");

            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Images");
        query.whereEqualTo("username", activeUsername);
        query.orderByDescending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
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
                }
            }
        });


        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==android.R.id.home) {
            finish();
        }
        return true;
    }
}
