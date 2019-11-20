package com.example.trip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.trip.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Trips extends AppCompatActivity {

    Button btnSignIn ,btnRegister;
    RelativeLayout rootLayout;

    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        CalligraphyConfig.get( new CalligraphyConfig.Builder()
//                                              .setDefaultFontPath("fonts/")  );
        setContentView(R.layout.activity_trips);

        auth=FirebaseAuth.getInstance();
        db =FirebaseDatabase.getInstance();
        users = db.getReference("users");

        btnRegister=(Button)findViewById(R.id.regsterbtn);
        btnSignIn=(Button)findViewById(R.id.signIn);
        rootLayout=(RelativeLayout)findViewById(R.id.rootLayout);
//////////////////////////////////////////////////////////////////////signIn/////////////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showRegisterDialogue();
            }
        });


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginDialogue();

            }
        });



    }

    private void showLoginDialogue() {

        AlertDialog.Builder dialog= new AlertDialog.Builder(this);
        dialog.setTitle("SIGN IN");
        dialog.setMessage("please use email to sign in");

        LayoutInflater inflater = LayoutInflater.from(this);
        View login_layout = inflater.inflate(R.layout.layout_login
                ,null);

        EditText editEmail= login_layout.findViewById(R.id.editEmail);
        EditText editPassword= login_layout.findViewById(R.id.editPassword);

        dialog.setView(login_layout);
        dialog.setPositiveButton("SIGN IN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(TextUtils.isEmpty(editEmail.getText().toString())){
                    Snackbar.make(rootLayout,"please enter email adress",Snackbar.LENGTH_SHORT)
                            .show();
                    return;

                }

                if(TextUtils.isEmpty(editPassword.getText().toString())){
                    Snackbar.make(rootLayout,"please enter email adress",Snackbar.LENGTH_SHORT)
                            .show();
                    return;

                }

                auth.signInWithEmailAndPassword(editEmail.getText().toString(),editPassword.getText().toString())
                                      .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                          @Override
                                          public void onSuccess(AuthResult authResult) {
                                              startActivity(new Intent(Trips.this,Welcome.class));
                                              finish();

                                          }
                                      })
                                      .addOnFailureListener(new OnFailureListener() {
                                          @Override
                                          public void onFailure(@NonNull Exception e) {
                                              Snackbar.make(rootLayout,"failed",Snackbar.LENGTH_SHORT)
                                              .show();
                                          }
                                      });


            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void  showRegisterDialogue(){
        AlertDialog.Builder dialog= new AlertDialog.Builder(this);
        dialog.setTitle("REGISTER");
        dialog.setMessage("please use email to register");

        LayoutInflater inflater = LayoutInflater.from(this);
        View register_layout = inflater.inflate(R.layout.layout_register,null);

        EditText editEmail= register_layout.findViewById(R.id.editEmail);
        EditText editPassword= register_layout.findViewById(R.id.editPassword);
        EditText editName= register_layout.findViewById(R.id.name);
        EditText editPhoneNbr= register_layout.findViewById(R.id.PhoneNbr);

        dialog.setView(register_layout);
        dialog.setPositiveButton("REGISTER" ,new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                if(TextUtils.isEmpty(editEmail.getText().toString())){
                    Snackbar.make(rootLayout,"please enter email adress",Snackbar.LENGTH_SHORT)
                            .show();
                    return;

                }

                if(TextUtils.isEmpty(editPhoneNbr.getText().toString())){
                    Snackbar.make(rootLayout,"please enter phone number",Snackbar.LENGTH_SHORT)
                            .show();
                    return;

                }

                if(TextUtils.isEmpty(editPassword.getText().toString())){
                    Snackbar.make(rootLayout,"please enter your password",Snackbar.LENGTH_SHORT)
                            .show();
                    return;

                }
                if(editEmail.getText().toString().length()<6){
                    Snackbar.make(rootLayout,"password too short",Snackbar.LENGTH_SHORT)
                            .show();
                    return;

                }

                auth.createUserWithEmailAndPassword(editEmail.getText().toString(),editPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {

                                User user =new User();
                                user.setEmail(editEmail.getText().toString());
                                user.setPassword(editPassword.getText().toString());
                                user.setName(editName.getText().toString());
                                user.setPhone(editPhoneNbr.getText().toString());

//                                users.child(FirebaseAuth.getInstance().getUid())
//                                        .setValue(user)
//                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void aVoid) {
//
//                                                Snackbar.make(rootLayout, "register successful", Snackbar.LENGTH_SHORT)
//                                                        .show();
//
//                                            }
//                                        })
//                                        .addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                Snackbar.make(rootLayout, "registration failed", Snackbar.LENGTH_SHORT)
//                                                        .show();
//                                            }
//                                        });

                            }
                        });

            }
        });
        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
