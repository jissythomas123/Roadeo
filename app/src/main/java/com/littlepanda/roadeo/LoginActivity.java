package com.littlepanda.roadeo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    Button btnlog;
    TextView reg;
    String UNAME, PASS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.SEND_SMS,

        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }



        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        btnlog = findViewById(R.id.login_btnlog);
        reg = findViewById(R.id.login_signup);

        btnlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UNAME = username.getText().toString();
                PASS = password.getText().toString();
                if (UNAME.isEmpty()) {
                    username.requestFocus();
                    username.setError("enter username");
                } else if (PASS.isEmpty()) {
                    password.requestFocus();
                    password.setError("enter password");
                } else {

                    volly_call_login();
                }

            }
        });
    }


    public void volly_call_login() {

        final ProgressDialog pd;
        pd = new ProgressDialog(LoginActivity.this);
        pd.setCancelable(false);
        pd.setTitle("Logging in...");
        //  pd.show();

        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, Common.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("******", response);
//                Toast.makeText(getApplicationContext(), "Cmon' " + response, Toast.LENGTH_SHORT).show();

                pd.dismiss();

                if (!response.trim().equals("failed")) {

                    String data = response;
                    //String arr[] = data.trim().split(":");
                    pd.dismiss();


                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("reg_id", response.trim());
                    editor.commit();


//                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("reg_id", response.trim()).apply();

                    Toast.makeText(getApplicationContext(), "Successfully Logged In", Toast.LENGTH_LONG).show();

                    startActivity(new Intent(getApplicationContext(), UserHome.class));
//                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                } else {
                    Toast.makeText(getApplicationContext(), "Invalid username or password !", Toast.LENGTH_LONG).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "my Error :" + error, Toast.LENGTH_LONG).show();
                Log.i("My Error", "" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> map = new HashMap<String, String>();
//                SharedPreferences sp=getSharedPreferences("booking_info", Context.MODE_PRIVATE);
                map.put("key", "userlogin");
                map.put("userid", UNAME.trim());
                map.put("upass", PASS.trim());

                return map;
            }
        };
        queue.add(request);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

}
