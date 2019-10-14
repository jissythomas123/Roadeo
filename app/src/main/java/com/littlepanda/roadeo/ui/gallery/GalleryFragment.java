package com.littlepanda.roadeo.ui.gallery;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;


    ListView insurance_list, pollution_list;
    String ins_id[], ins_name[], pol_id[], pol_name[];

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
//        galleryViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });


        insurance_list = root.findViewById(R.id.insur_list);
        pollution_list = root.findViewById(R.id.pollu_list);


        volley_getagencylist();


        return root;
    }

    public void volley_getagencylist() {


        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest request = new StringRequest(Request.Method.POST, Common.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("******", response);
//                Toast.makeText(getApplicationContext(), "Cmon' " + response, Toast.LENGTH_SHORT).show();


                if (!response.trim().equals("nodata")) {

                    String fulldata[] = response.split("#");
                    ins_id = fulldata[0].split("@");
                    ins_name = fulldata[1].split("@");
                    pol_id = fulldata[2].split("@");
                    pol_name = fulldata[3].split("@");

//                    Toast.makeText(getContext(), "aasa  " + ins_name[0], Toast.LENGTH_SHORT).show();

                    ArrayAdapter insurance_adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, ins_name);
                    insurance_list.setAdapter(insurance_adapter);

                    ArrayAdapter pollution_adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, pol_name);
                    pollution_list.setAdapter(pollution_adapter);


                    //Choose insurance agency.....................................................................................

                    insurance_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


                            new AlertDialog.Builder(getContext())
                                    .setTitle("Apply For Insurance")
                                    .setMessage("Are you sure you want to send application to " + ins_name[position] + " ?")

                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            Toast.makeText(getContext(), "Applying ...", Toast.LENGTH_SHORT).show();
                                            volley_apply_Insu(ins_id[position].toString().trim());

                                        }
                                    })

                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();


                        }
                    });

                    //Choose Pollution agency......................................................................................

                    pollution_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


                            new AlertDialog.Builder(getContext())
                                    .setTitle("Apply For Insurance")
                                    .setMessage("Are you sure you want to send application to " + pol_name[position] + " ?")

                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            Toast.makeText(getContext(), "Applying ...", Toast.LENGTH_SHORT).show();
                                            volley_apply_Pollu(pol_id[position].trim());

                                        }
                                    })

                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();


                        }
                    });

                } else {
                    Toast.makeText(getContext(), "No List Found !", Toast.LENGTH_LONG).show();

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
//                SharedPreferences sp=getSharedPreferences("booking_info", Context.MODE_PRIVATE);
                map.put("key", "get_agency_list");

                return map;
            }
        };
        queue.add(request);
    }

    //......................................................send ins agency request............................


    public void volley_apply_Insu(final String Insurance_ID) {


        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest request = new StringRequest(Request.Method.POST, Common.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("******", response);
//                Toast.makeText(getApplicationContext(), "Cmon' " + response, Toast.LENGTH_SHORT).show();


                if (response.trim().equals("success")) {

                    Toast.makeText(getContext(), "Succesfully applied for insurance", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getContext(), "Failed to Apply !", Toast.LENGTH_LONG).show();

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

                map.put("key", "apply_insurance");
                map.put("reg_id", prefs.getString("reg_id", "101"));
                map.put("ins_id", Insurance_ID.trim());


                return map;
            }
        };
        queue.add(request);
    }

    public void volley_apply_Pollu(final String Pollution_ID) {


        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest request = new StringRequest(Request.Method.POST, Common.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("******", response);
//                Toast.makeText(getApplicationContext(), "Cmon' " + response, Toast.LENGTH_SHORT).show();


                if (response.trim().equals("success")) {

                    Toast.makeText(getContext(), "Succesfully applied for Pollution", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getContext(), "Failed to Apply !", Toast.LENGTH_LONG).show();

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

                map.put("key", "apply_pollution");
                map.put("reg_id", prefs.getString("reg_id", "101"));
                map.put("ins_id", Pollution_ID.trim());


                return map;
            }
        };
        queue.add(request);
    }


}