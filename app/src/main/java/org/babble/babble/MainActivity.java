package org.babble.babble;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.famoussoft.libs.JSON.JSONArray;
import com.famoussoft.libs.JSON.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText etUser,etPass;
    Button btnLogin,btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etUser = (EditText) findViewById(R.id.etUser);
        etPass = (EditText) findViewById(R.id.etPass);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String uid=etUser.getText().toString();
                String pwd=etPass.getText().toString();
                if(!uid.equals("") && !pwd.equals("")) {
                    loginUser(uid,pwd);
                }
                else{
                    Toast.makeText(MainActivity.this,"Empty credentials, Please fill both...",Toast.LENGTH_LONG).show();
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUpIntent = new Intent(getApplicationContext(),SignupActivity.class);
                startActivity(signUpIntent);
            }
        });
    }


    private void loginUser( final String email, final String password){

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);  // this = context
        String url = "http://api.mrasif.in/demo/gchat/login.php?";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jobj = new JSONObject(response);
                            String rslt=jobj.getString("status").toString();
                            if(rslt.equals("true"))
                            {
                                Toast.makeText(MainActivity.this,"Login successful...",Toast.LENGTH_LONG).show();
                                Intent chatIntent=new Intent(MainActivity.this,ChatActivity.class);
                                chatIntent.putExtra("userid",email);
                                startActivity(chatIntent);
                                finish();
                            }
                            else{
                                Toast.makeText(MainActivity.this,"Wrong Credentials entered!! Try Again...",Toast.LENGTH_LONG).show();
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
                params.put("userid",email);
                params.put("pass",password);
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
        getMenuInflater().inflate(R.menu.menu_signin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.signUp){
            Toast.makeText(MainActivity.this,"you clicked on SignUp...!",Toast.LENGTH_LONG).show();
            Intent signUpIntent = new Intent(getApplicationContext(),SignupActivity.class);
            startActivity(signUpIntent);
        }
        if(id==R.id.exit1){
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
