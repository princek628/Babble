package org.babble.babble;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.famoussoft.libs.JSON.JSONObject;

import java.util.HashMap;
import java.util.Map;



public class SignupActivity extends AppCompatActivity {
   private EditText etEmail,etName,etUser,etPass;
   private Button btnSignUp,btnLoginHere;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etName = (EditText) findViewById(R.id.etName);
        etUser = (EditText) findViewById(R.id.etUser);
        etPass = (EditText) findViewById(R.id.etPass);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnLoginHere = (Button) findViewById(R.id.btnLoginHere);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=etName.getText().toString();
                String uid=etUser.getText().toString();
                String pwd=etPass.getText().toString();
                String email=etEmail.getText().toString();
                if(!name.equals("") && !uid.equals("") && !pwd.equals("") && !email.equals("")) {
                    registerUser(name, uid, pwd, email);
                }else{
                    Toast.makeText(SignupActivity.this,"Please enter all the fields to Register...",Toast.LENGTH_LONG).show();
                }
            }
        });

        btnLoginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(signInIntent);
                finish();
            }
        });
    }

    private void registerUser(final String name, final String uid, final String password, final String email){
        RequestQueue queue = Volley.newRequestQueue(SignupActivity.this);  // this = context
        String url = "http://api.mrasif.in/demo/gchat/registration.php?userid="+uid+"&name="+name+"&pass="+password+"&email="+email;
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jobj = new JSONObject(response);
                            String rslt=jobj.getString("status");
                            //etUser.setText(rslt);
                            if(rslt.equals("true"))
                            {
                                Toast.makeText(SignupActivity.this,"Registration successful...",Toast.LENGTH_LONG).show();
                                Intent sIntent=new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(sIntent);
                                finish();
                            }
                            else{
                                Toast.makeText(SignupActivity.this,"Failed to Register or User already exists...",Toast.LENGTH_LONG).show();
                            }
                        }catch (Exception e){
                            System.out.println(e.getMessage().toString());
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                //params to login url
                Map<String, String>  params = new HashMap<String, String>();
                //params.put("userid", name);
                return params;
            }
        };
        queue.add(postRequest);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.info_title);
        builder.setCancelable(false);
        builder.setMessage(R.string.info_description);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                }
        );
        builder.setNegativeButton(R.string.cancel, null);
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_signup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.signIn){
            Toast.makeText(SignupActivity.this,"you clicked on SignIn...!",Toast.LENGTH_LONG).show();
            Intent signInIntent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(signInIntent);
            finish();
        }
        if(id==R.id.exit2){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.info_title);
            builder.setCancelable(false);
            builder.setMessage(R.string.info_description);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            moveTaskToBack(true);
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);
                        }
                    }
            );
            builder.setNegativeButton(R.string.cancel, null);
            builder.show();
        }
        return true;
    }
}
