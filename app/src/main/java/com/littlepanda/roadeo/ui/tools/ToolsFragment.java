package com.littlepanda.roadeo.ui.tools;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ToolsFragment extends Fragment {

    private ToolsViewModel toolsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        toolsViewModel =
                ViewModelProviders.of(this).get(ToolsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tools, container, false);
//        final TextView textView = root.findViewById(R.id.text_tools);
//        toolsViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        volly_Fetch_Vehicle();

        return root;
    }

    public void volly_Fetch_Vehicle() {

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
//                    uname.setText(array[0].trim());
//                    upass.setText(array[1].trim());


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
                map.put("key", "fetchvehicledata");
                map.put("reg_id", prefs.getString("reg_id", "101"));


                return map;
            }
        };
        queue.add(request);
    }
}