package com.littlepanda.roadeo.ui.send;

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
import com.littlepanda.roadeo.R;

import java.util.HashMap;
import java.util.Map;

public class SendFragment extends Fragment {

    private SendViewModel sendViewModel;


    EditText edtcomp;
    Button btncomp;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sendViewModel = ViewModelProviders.of(this).get(SendViewModel.class);
        View root = inflater.inflate(R.layout.fragment_send, container, false);
        final TextView textView = root.findViewById(R.id.text_send);
//        sendViewModel.getText().observe(this, new Observer<String>() {
//                    @Override
//                    public void onChanged(@Nullable String s) {
//                        textView.setText(s);
//            }
//        });

        edtcomp = root.findViewById(R.id.comp_edt);
        btncomp = root.findViewById(R.id.compbtn);

        btncomp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String complaintdescrp = edtcomp.getText().toString();

                if (complaintdescrp.equals("")) {
                    edtcomp.setError("Cannot be blank");
                } else {
                    volly_reg_complaint(complaintdescrp);

                }
            }
        });
        return root;
    }

    public void volly_reg_complaint(final String descp) {


        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, Common.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("******", response);


                if (response.trim().equals("success")) {
                    Toast.makeText(getContext(), " Complaint registered ", Toast.LENGTH_SHORT).show();

                    edtcomp.getText().clear();

                } else {

                    Toast.makeText(getContext(), "Failed To register Complaint", Toast.LENGTH_LONG).show();

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
                map.put("key", "regcomplaint");
                map.put("reg_id", prefs.getString("reg_id", "101"));
                map.put("descp", descp.toString().trim());
                return map;
            }
        };
        queue.add(request);
    }
}