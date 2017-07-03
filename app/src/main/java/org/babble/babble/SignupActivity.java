package org.babble.babble;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

/**
 * Created by peekay on 2/7/17.
 */

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
                //Toast.makeText(SignupActivity.this,"Registration will be available later...",Toast.LENGTH_LONG).show();
                submitform();
            }
        });

        btnLoginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(signInIntent);
            }
        });
    }

    private void submitform(){
        registerUser(etName.getText().toString(),etUser.getText().toString(),etPass.getText().toString(),
                etEmail.getText().toString());
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
                                sIntent.putExtra("userid",name);
                                startActivity(sIntent);
                                finish();
                            }
                            else{
                                Toast.makeText(SignupActivity.this,"Failed to Register!! Try Again...",Toast.LENGTH_LONG).show();
                            }
                            //tvResponse.setText(Html.fromHtml(jobj.getString("status")+"<br/>"+jobj.getString("name")));
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
                //params.put("pass", password);
                //params.put("email",email);
                return params;
            }
        };
        queue.add(postRequest);
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
            Toast.makeText(SignupActivity.this,"you clicked on Exit...!",Toast.LENGTH_LONG).show();
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
        return true;
    }
}
