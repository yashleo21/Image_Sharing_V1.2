/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.roflcopter.starter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity implements View.OnKeyListener{

  EditText editUser;
  EditText editPass;
  TextView textSignUp;
  Button butLog;
  ImageView imageView;
  TextView editText;
  RelativeLayout relativeLayout;
  ConnectivityManager connectivityManager;
  public void changeButText(View v){
    if(textSignUp.getText().equals("Sign up")) {
      textSignUp.setText("Log in");
      butLog.setText("Sign up");
    }
    else {
      textSignUp.setText("Sign up");
      butLog.setText("Log in");
    }

  }

  public void devOp(View v){

  }

  public void doSomething(View v) {
    ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    if(activeNetwork!=null) {

      if (butLog.getText().equals("Log in")) {

        if (editUser.getText().toString().equalsIgnoreCase("") || editPass.getText().toString().equalsIgnoreCase("")) {
          Toast.makeText(MainActivity.this, "Username or password is empty", Toast.LENGTH_SHORT).show();
        } else {
          if (editUser.getText().length() < 3 || editPass.getText().length() < 3) {
            Toast.makeText(MainActivity.this, "Minimum three characters or digits for a username/password", Toast.LENGTH_SHORT).show();
          } else {
            ParseUser.logInInBackground(editUser.getText().toString(), editPass.getText().toString(), new LogInCallback() {
              @Override
              public void done(ParseUser user, ParseException e) {
                if (user != null) {
                  Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                  showUserList();
                } else {
                  Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                  e.printStackTrace();
                }
              }
            });
          }
        }
      } else {
        if (editUser.getText().toString().equalsIgnoreCase("") || editPass.getText().toString().equalsIgnoreCase("")) {
          Toast.makeText(MainActivity.this, "Username or password is empty", Toast.LENGTH_SHORT).show();
        } else {
          if (editUser.getText().length() < 3 || editPass.getText().length() < 3) {
            Toast.makeText(MainActivity.this, "Minimum three characters or digits for a username/password", Toast.LENGTH_SHORT).show();
          } else {
            //Toast.makeText(MainActivity.this, "??", Toast.LENGTH_SHORT).show();
            ParseUser newUser = new ParseUser();
            newUser.setUsername(editUser.getText().toString());
            newUser.setPassword(editPass.getText().toString());
            newUser.signUpInBackground(new SignUpCallback() {
              @Override
              public void done(ParseException e) {
                if (e == null) {
                  Toast.makeText(MainActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                  showUserList();
                } else {
                  Toast.makeText(MainActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                }
              }
            });
          }


        }
      }
    }
    else {
      Toast.makeText(MainActivity.this, "No internet connection. Please try again.", Toast.LENGTH_SHORT).show();
    }
  }
public void showUserList(){
  Intent i = new Intent(MainActivity.this,UserList.class);
  startActivity(i);

}
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    MobileAds.initialize(getApplicationContext(), "ca-app-pub-8981265957772699/4211531163");
    AdView mAdView = (AdView) findViewById(R.id.adView);
    AdRequest adRequest = new AdRequest.Builder().build();
    mAdView.loadAd(adRequest);
    connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
    ParseAnalytics.trackAppOpenedInBackground(getIntent());
    editUser = (EditText) findViewById(R.id.editUser);
    editPass = (EditText) findViewById(R.id.editPass);
    textSignUp = (TextView) findViewById(R.id.textSignUp);
    butLog = (Button) findViewById(R.id.butLog);
    butLog.setText("Log in");
    editUser.setOnKeyListener(this);
    editPass.setOnKeyListener(this);
    relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
//    relativeLayout.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View view) {
//        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
//      }
//    });
    if(ParseUser.getCurrentUser()!=null){
      showUserList();
    }
  }

  @Override
  public void onBackPressed()
  {
    new AlertDialog.Builder(this)
            .setTitle("Are you sure?")
            .setMessage("Do you want to Exit?")
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int i) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                  finishAffinity();
                }
              }
            })
            .setNegativeButton("No",null)
            .show();
//    super.onBackPressed();
  }


//  @Override
//  public boolean onCreateOptionsMenu(Menu menu) {
//    // Inflate the menu; this adds items to the action bar if it is present.
//    getMenuInflater().inflate(R.menu.menu_main, menu);
//    return true;
//  }
//
//  @Override
//  public boolean onOptionsItemSelected(MenuItem item) {
//    // Handle action bar item clicks here. The action bar will
//    // automatically handle clicks on the Home/Up button, so long
//    // as you specify a parent activity in AndroidManifest.xml.
//    int id = item.getItemId();
//
//    //noinspection SimplifiableIfStatement
//    if (id == R.id.share) {
//      Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//      startActivityForResult(i,1);
//      return true;
//    }
//
//    return super.onOptionsItemSelected(item);
//  }

  @Override
  public boolean onKey(View view, int i, KeyEvent keyEvent) {

    if(i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction()==KeyEvent.ACTION_DOWN){
      doSomething(view);
    }
    return false;
  }


}
