package com.littlepanda.roadeo.ui.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.littlepanda.roadeo.Common;
import com.littlepanda.roadeo.LoginActivity;
import com.littlepanda.roadeo.R;
import com.littlepanda.roadeo.UserHome;

import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    EditText uname, upass;
    Button updatebtn;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        uname = root.findViewById(R.id.updt_username);
        upass = root.findViewById(R.id.updt_password);
        updatebtn = root.findViewById(R.id.update_btn);

        volly_Fetch_Credz();


        return root;


    }

    public void volly_Fetch_Credz() {

        final ProgressDialog pd;
        pd = new ProgressDialog(getActivity());
        pd.setCancelable(false);
        pd.setTitle("Logging in...");
        //  pd.show();

        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, Common.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("******", response);
//                Toast.makeText(getContext(), "Cmon' " + response, Toast.LENGTH_SHORT).show();

                pd.dismiss();

                if (!response.trim().equals("failed")) {

                    String array[] = response.split("#");
                    uname.setText(array[0].trim());
                    upass.setText(array[1].trim());


                    updatebtn.setOnClickListener(new View.OnClickListener() {


                        @Override
                        public void onClick(View v) {

//                            if (!uname.getText().equals("")) {
//
//                                uname.setError("enter new name");
//
//                            } else if (!upass.getText().equals("")) {
//
//                                upass.setError("enter new password");
//
//                            } else {
                            volly_Update_Credz();
//                            }
                        }
                    });


                } else {
                    Toast.makeText(getContext(), "Failed To fetch Account", Toast.LENGTH_LONG).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(getContext(), "my Error :" + error, Toast.LENGTH_LONG).show();
                Log.i("My Error", "" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> map = new HashMap<String, String>();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                map.put("key", "fetchcredential");
                map.put("reg_id", prefs.getString("reg_id", "101"));


                return map;
            }
        };
        queue.add(request);
    }

    public void volly_Update_Credz() {


        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, Common.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("******", response);


                if (response.trim().equals("updated")) {
                    Toast.makeText(getContext(), " Updated Account ", Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(getContext(), "Failed To Update Account", Toast.LENGTH_LONG).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), "my Error :" + error, Toast.LENGTH_LONG).show();
                Log.i("My Error", "" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> map = new HashMap<String, String>();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                map.put("key", "updatecredential");
                map.put("reg_id", prefs.getString("reg_id", "101"));
                map.put("newuname", uname.getText().toString().trim());
                map.put("newpass", upass.getText().toString().trim());
                return map;
            }
        };
        queue.add(request);
    }
}