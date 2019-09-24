package com.example.vsion_lite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.vsion_lite.fragment.Teacher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    private static final String TAG = "a";
    Button ok;
    TextView label_user, laberl_pass, regist;
    private EditText pass, user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mp;

    private RequestQueue mq;

    private boolean log;
//    private FirebaseUser currentUser = mAuth.getCurrentUser();

    public String usuarios[] = {"user1", "user2", "frank", "chuy"};
    public String contraseña[] = {"123", "456", "frank123", "dios"};

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("VISION-DASC Lite");

        mAuth = FirebaseAuth.getInstance();

        pass = (EditText) findViewById(R.id.pass);
        user = (EditText) findViewById(R.id.user);

        ok = (Button) findViewById(R.id.ok);


        regist = (TextView) findViewById(R.id.registro);

        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent facil = new Intent(getApplicationContext(), registro.class);
                startActivity(facil);//Inicia la actividad
            }
        });

     /*   if (mAuth.getCurrentUser() != null) {
            Intent facil = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(facil);//Inicia la actividad

            finish();
        }
*/
      /*  mp = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() != null){
                    //startActivity(new Intent(Login.this, MainActivity.class));
                     Intent facil = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(facil);//Inicia la actividad
                }else{
                    Toast toast2 = Toast.makeText(getApplicationContext(), "Usuario Incorrecto", Toast.LENGTH_SHORT);
                    toast2.show();
                }
              //  Intent facil = new Intent(getApplicationContext(), MainActivity.class);
                //startActivity(facil);//Inicia la actividad
            }
        };
*/


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                //loginAc();
                //sigUp();

                String correoO = user.getText().toString();
                String passO = pass.getText().toString();

                if (TextUtils.isEmpty(user.getText().toString()) || TextUtils.isEmpty(pass.getText().toString()) ) {
                    Toast.makeText(getApplicationContext(), "Faltan parametros", Toast.LENGTH_LONG).show();
                } else {


                    mq = Volley.newRequestQueue(getApplication());
                    // checkUs(correoO, carreraO);
                    String url = String.format("https://visionliteapi.fjavpra.now.sh/api/v1/checkAlumLog/" + correoO + "/" + passO);
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        JSONArray jsonArray = response.getJSONArray("results");
                                        if (jsonArray.length() >= 1) {
                                            System.out.println("usuario encontrado");
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject hit = jsonArray.getJSONObject(i);
                                                String id = hit.getString("id");
                                                String name = hit.getString("name");
                                                String correo = hit.getString("correo");
                                                int sexo = hit.getInt("sexo");
                                                String carreraId = hit.getString("CarreraId");

                                                SharedPreferences preferencias = getSharedPreferences("datos", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = preferencias.edit();
                                                editor.putString("NameD", name);
                                                editor.putString("CorreoD", correo);
                                                editor.putString("ID", id);
                                                editor.putString("CarreraId", carreraId);
                                                editor.putInt("SexoD", sexo);
                                                editor.putInt("LOG", 1);
                                                editor.apply();
                                            }
                                            // Toast.makeText(getApplication(), "Usuario en uso", Toast.LENGTH_SHORT).show();
                                            Intent facil = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(facil);//Inicia la actividad
                                            finish();
                                        } else {
                                            Toast.makeText(getApplication(), "Datos incorrectos", Toast.LENGTH_SHORT).show();
                                            System.out.println("datos incorrectos");
                                        }
                                    } catch (JSONException e) {
                                        // Toast.makeText(getApplication(), "No hay respuesta", Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                }

                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });
                    mq.add(request);

                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sharedPref = getSharedPreferences("datos", Context.MODE_PRIVATE);
        String Valor = sharedPref.getString("NameD", "");
        int log = sharedPref.getInt("LOG",0);
        //Toast.makeText(getApplicationContext(), "el us es " + Valor, Toast.LENGTH_LONG).show();
        if (log == 1){
          //  Toast.makeText(getApplicationContext(),"Usaurio log", Toast.LENGTH_LONG).show();
            Intent facil = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(facil);//Inicia la actividad
            finish();
        }else if(log == 2 ) {
            Toast logeOff = Toast.makeText(getApplicationContext(), "Usuario logeOff", Toast.LENGTH_SHORT);
        }

        /*
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            Toast loge = Toast.makeText(getApplicationContext(), "Usuario log", Toast.LENGTH_SHORT);
            loge.show();
            Intent facil = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(facil);//Inicia la actividad
        }else{
            Toast logeOff = Toast.makeText(getApplicationContext(), "Usuario logeOff", Toast.LENGTH_SHORT);
            logeOff.show();
        }
        //updateUI(currentUser);
        */
    }














/*
  private void sigUp() {
      //mAuth = FirebaseAuth.getInstance();
      String userSs =  user.getText().toString();
      String passSs  = pass.getText().toString();
      Toast toast1 = Toast.makeText(getApplicationContext(), userSs, Toast.LENGTH_SHORT);
      toast1.show();
      mAuth.signInWithEmailAndPassword(userSs, passSs)
              .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {
                      if (task.isSuccessful()) {
                          // Sign in success, update UI with the signed-in user's information
                          Log.d(TAG, "signInWithEmail:success");
                          FirebaseUser user = mAuth.getCurrentUser();
                          Toast toast1 = Toast.makeText(getApplicationContext(), "Usuario correcto", Toast.LENGTH_SHORT);
                          toast1.show();
                          log = true;
                          if (log){
                              Intent facil = new Intent(getApplicationContext(), MainActivity.class);
                              startActivity(facil);//Inicia la actividad
                          }
                          //Intent facil = new Intent(getApplicationContext(), MainActivity.class);
                          //startActivity(facil);//Inicia la actividad
                       //   updateUI(user);
                      } else {
                          // If sign in fails, display a message to the user.
                          Log.w(TAG, "signInWithEmail:failure", task.getException());
                          Toast.makeText(getApplicationContext(), "Authentication failed. , invalido",
                                  Toast.LENGTH_SHORT).show();
                          //updateUI(null);
                      }

                      // ...
                  }

              });

  }

    private void loginAc(){
          //  mAuth = FirebaseAuth.getInstance();
            String userS = "franjap2@gmail.com";
                    //user.getText().toString();
            String passS  = "123456";
                     //pass.getText().toString();
            mAuth.createUserWithEmailAndPassword(userS, passS)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }

                            // ...
                        }
                    });
        }
        */
    }

