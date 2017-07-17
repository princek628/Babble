package org.babble.babble;

import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.famoussoft.libs.JSON.JSONArray;
import com.famoussoft.libs.JSON.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class ChatActivity extends AppCompatActivity {

    int total=0;
    JSONArray message=null;
    ImageButton btnSend;
    EditText etmsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        btnSend=(ImageButton) findViewById(R.id.btnMessageButton);
        etmsg=(EditText) findViewById(R.id.etMessage);
        Intent intent=getIntent();
        final String id=intent.getStringExtra("userid");
        downloadData();
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String chat=etmsg.getText().toString();
                if(!chat.equals("")) {
                    sendmsg(chat, id);
                }
                else{
                    Toast.makeText(ChatActivity.this,"Empty message,Post not sent...",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void sendmsg(final String message, final String uid){
        RequestQueue queue = Volley.newRequestQueue(ChatActivity.this);  // this = context
        String url = "http://api.mrasif.in/demo/gchat/send.php?userid="+uid+"&msg="+URLEncoder.encode(message);
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
                                Toast.makeText(ChatActivity.this,"Message post successful...",Toast.LENGTH_SHORT).show();
                                Intent chatIntent=new Intent(getApplicationContext(),ChatActivity.class);
                                chatIntent.putExtra("userid",uid);
                                startActivity(chatIntent);
                                finish();
                            }
                            else{
                                Toast.makeText(ChatActivity.this,"Failed to post!! Try Again...",Toast.LENGTH_LONG).show();
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
                Map<String, String>  params = new HashMap<String, String>();
                //params
                return params;
            }
        };
        queue.add(postRequest);

    }

    private void downloadData(){
            RequestQueue queue = Volley.newRequestQueue(ChatActivity.this);  // this = context
            String url = "http://api.mrasif.in/demo/gchat/getmsg.php";
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jobj = new JSONObject(response);
                                loadList(jobj);
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
                    Map<String, String>  params = new HashMap<String, String>();
                    //params
                    return params;
                }
            };
            queue.add(postRequest);
    }

    private void loadList(JSONObject data) {
        // TODO Auto-generated method stub
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.form);
        mainLayout.removeAllViews();
        int total=Integer.parseInt(data.getString("total").toString());
        this.total=total;
        JSONArray jarray=new JSONArray(data.getJSONArray("msg").toString());
        message=jarray;
        for (int i=0; i<total; i++) {
            JSONObject jobj=new JSONObject(jarray.getJSONObject(i).toString());
            String msg = jobj.getString("msg").toString();
            String uid = jobj.getString("userid").toString();

            LinearLayout ll = new LinearLayout(this);
            ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            ll.setOrientation(LinearLayout.VERTICAL);
            ll.setPadding(0, 0, 0, 2);

            LinearLayout sll = new LinearLayout(this);
            sll.setId(i);
            sll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            sll.setPadding(5, 5, 5, 5);
            sll.setOrientation(LinearLayout.VERTICAL);
            //sll.setBackgroundColor(Color.WHITE);

            TextView tvTitle = new TextView(this);
            tvTitle.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            tvTitle.setText(Html.fromHtml("<b><i>"+uid+"</i></b>"+" : "+msg));
            tvTitle.setTextColor(Color.BLACK);

            sll.addView(tvTitle);

            ll.addView(sll);
            mainLayout.addView(ll);
        }
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
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.settings)
            Toast.makeText(ChatActivity.this,"Settings will be available later...!",Toast.LENGTH_LONG).show();
        if(id==R.id.logOut)
        {
            Toast.makeText(ChatActivity.this,"Logging you out...!",Toast.LENGTH_LONG).show();
            Intent signintent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(signintent);
        }
        if(id==R.id.exit3){
            //Toast.makeText(ChatActivity.this,"Exiting application...!",Toast.LENGTH_LONG).show();
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
