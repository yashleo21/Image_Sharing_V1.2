package com.roflcopter.starter;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserList extends AppCompatActivity {
    ListView listUser;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


        pb = (ProgressBar) findViewById(R.id.pbLoading);
        listUser = (ListView) findViewById(R.id.listUser);
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.addAscendingOrder("username");

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        final ArrayList<String> userNames = new ArrayList<String>();
                        for (ParseUser user : objects) {
                            userNames.add(user.getUsername());
                        }
                        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(UserList.this, android.R.layout.simple_list_item_1, userNames);
                        listUser.setAdapter(arrayAdapter);
                        listUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent = new Intent(UserList.this, UserProfile.class);
                                intent.putExtra("Username", userNames.get(i));
                                startActivity(intent);
                            }
                        });
                    }
                }
            }
        });
    }

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
        if (id == R.id.share) {
            try {
                new AlertDialog.Builder(UserList.this)
                        .setTitle("Select")
                        .setMessage("God damnit how to add icons instead of this shit text?")
                        .setPositiveButton("GALLERY", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(i, 1);
                            }
                        })
                        .setNegativeButton("CAMERA", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(takePicture, 1);
                            }
                        })
                        .show();

            }
            catch (Exception e){
                Toast.makeText(UserList.this, e.getMessage()+" While trying to share", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (id == R.id.logout) {
            new AlertDialog.Builder(this)
                    .setTitle("Are you sure?")
                    .setMessage("Do you want to Logout?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ParseUser.logOut();
                            Intent j = new Intent(UserList.this, MainActivity.class);
                            startActivity(j);
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();

        } else if (id == R.id.myupload) {
            Intent x = new Intent(UserList.this, UserFeed.class);
            x.putExtra("Username", ParseUser.getCurrentUser().getUsername());
            startActivity(x);
        }
        else if(id == R.id.myprofilepic){
            try {
                new AlertDialog.Builder(UserList.this)
                        .setTitle("Select")
                        .setMessage("God damnit how to add icons instead of this shit text?")
                        .setPositiveButton("GALLERY", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(i, 2);
                            }
                        })
                        .setNegativeButton("CAMERA", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(takePicture, 2);
                            }
                        })
                        .show();

            }
            catch (Exception e){
                Toast.makeText(UserList.this, e.getMessage()+" While trying to share", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Are you sure?")
                .setMessage("Do you want to Logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ParseUser.logOut();
                        finish();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            finishAffinity();
                        }
                    }
                })
                .setNegativeButton("No", null)
                .show();
//    super.onBackPressed();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
                Uri selectedImage = data.getData();
                pb.setVisibility(ProgressBar.VISIBLE);
                try {
                    Bitmap bitmapImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
//                Log.i("Image","Received");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmapImage.compress(Bitmap.CompressFormat.JPEG, 50, stream);

                    byte[] byteArray = stream.toByteArray();

                    ParseFile file = new ParseFile("image.jpg", byteArray);

                    ParseObject object = new ParseObject("Images");
                    object.put("username", ParseUser.getCurrentUser().getUsername());
                    object.put("image", file);
                    ParseACL parseACL = new ParseACL();
                    parseACL.setPublicReadAccess(true);

                    object.setACL(parseACL);
                    object.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(UserList.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                                pb.setVisibility(ProgressBar.INVISIBLE);
                            } else {
                                Toast.makeText(UserList.this, "There was a problem in uploading image.Try again", Toast.LENGTH_SHORT).show();
                                pb.setVisibility(ProgressBar.INVISIBLE);
                            }
                        }
                    });


                } catch (IOException e) {
                    Toast.makeText(UserList.this, e.getMessage()+" While trying  to upload", Toast.LENGTH_SHORT).show();
                }
            }
        else if(requestCode==2 && resultCode == RESULT_OK && data!=null){
                Uri profilePic = data.getData();
                pb.setVisibility(View.VISIBLE);
                try{
                    Bitmap profileImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),profilePic);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    profileImage.compress(Bitmap.CompressFormat.JPEG,50,stream);


                    byte[] bytes = stream.toByteArray();

                    final ParseFile file = new ParseFile("profilePic.jpg",bytes);
                    ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("ProfileInfo");
                    query.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if(e==null){
                                if(objects.size()>0){
                                    for(ParseObject object: objects){
                                        Log.i("Object does exist","Yep true.");
                                        object.put("ProfilePic",file);
                                        ParseACL parseACL = new ParseACL();
                                        parseACL.setPublicReadAccess(true);
                                        object.setACL(parseACL);
                                        object.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if(e==null){
                                                    Toast.makeText(UserList.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                                                    pb.setVisibility(ProgressBar.INVISIBLE);
                                                } else {
                                                    Toast.makeText(UserList.this, "There was a problem in uploading image.Try again!"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    pb.setVisibility(ProgressBar.INVISIBLE);
                                                }
                                                }
                                        });
                                    }
                                }
                                else{
                                    ParseObject object = new ParseObject("ProfileInfo");
                                    object.put("username",ParseUser.getCurrentUser().getUsername());
                                    object.put("ProfilePic",file);
                                    ParseACL parseACL = new ParseACL();
                                    parseACL.setPublicReadAccess(true);
                                    object.setACL(parseACL);

                                    object.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e == null) {
                                                Toast.makeText(UserList.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                                                pb.setVisibility(ProgressBar.INVISIBLE);
                                            } else {
                                                Toast.makeText(UserList.this, "There was a problem in uploading image.Try again", Toast.LENGTH_SHORT).show();
                                                pb.setVisibility(ProgressBar.INVISIBLE);
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });


                }
                catch (Exception e){
                    Toast.makeText(UserList.this, "lol error", Toast.LENGTH_SHORT).show();
                }
            }


    }
}
