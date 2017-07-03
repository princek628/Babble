package org.babble.babble;

import android.content.Intent;
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

                loginUser(etUser.getText().toString(),etPass.getText().toString());
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
        String url = "http://api.mrasif.in/demo/gchat/login.php?userid="+email+"&pass="+password;
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jobj = new JSONObject(response);
                            String rslt=jobj.getString("status").toString();
                            //etUser.setText(rslt);
                            if(rslt.equals("true"))
                            {
                                Toast.makeText(MainActivity.this,"Login successful...",Toast.LENGTH_LONG).show();
                                Intent chatIntent=new Intent(MainActivity.this,ChatActivity.class);
                                chatIntent.putExtra("userid",email);
                                startActivity(chatIntent);
                                finish();
                            }
                            else{
                                Toast.makeText(MainActivity.this,"Failed to Login!! Try Again...",Toast.LENGTH_LONG).show();
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
                //params.put("userid"email, );
                //params.put("pass",password )    ;
                return params;
            }
        };
        queue.add(postRequest);
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
            Toast.makeText(MainActivity.this,"you clicked on Exit...!",Toast.LENGTH_LONG).show();
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
        return true;
    }
}
