package com.example.virtualtryonapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ContentPage extends AppCompatActivity {
    private ArrayList<String> lproductId,lproductBrand,lproductName,lproductPrice,lproductImageLink;
    RecyclerView recyclerView;
    ContentPageAdapter adapter;
    ProgressDialog dialog;
    String url="https://fathomless-refuge-03183.herokuapp.com/api/product";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_page);

        recyclerView = findViewById(R.id.product_recycler);
        lproductId = new ArrayList<>();
        lproductBrand = new ArrayList<>();
        lproductName = new ArrayList<>();
        lproductPrice = new ArrayList<>();
        lproductImageLink = new ArrayList<>();

        adapter = new ContentPageAdapter(getApplicationContext(),lproductId,lproductName,lproductImageLink,lproductBrand,lproductPrice);
        /*works fine
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);

         */

         dialog= new ProgressDialog(ContentPage.this, R.style.AlertDialog);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Loading....");
        dialog.show();


        recyclerView.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(getApplicationContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(divider);

        //layoutManager = new LinearLayoutManager(getApplicationContext());
        //Rview.setLayoutManager(layoutManager);
        //DividerItemDecoration divider = new DividerItemDecoration(getApplicationContext(), layoutManager.getOrientation());
       // Rview.addItemDecoration(divider);
        getData();



    }

    public void getData(){

        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
        //JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONArray response) {
                //Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                try {
                    for(int i=0;i<response.length();i++){
                        JSONObject obj=response.getJSONObject(i);
                        //Toast.makeText(getApplicationContext(),obj.toString(),Toast.LENGTH_LONG).show();
                        try{
                           lproductId.add(obj.getString("base64"));
                        } catch (JSONException e) {
                            lproductId.add("-");
                            e.printStackTrace();
                        }
                        try{
                            lproductBrand.add(obj.getString("productBrand"));
                        } catch (JSONException e) {
                            lproductBrand.add("-");
                            e.printStackTrace();
                        }
                        try{
                            lproductName.add(obj.getString("productName"));
                        } catch (JSONException e) {
                            lproductName.add("-");
                            e.printStackTrace();
                        }
                        try{
                            lproductImageLink.add(obj.getString("productImageLink"));
                        } catch (JSONException e) {
                            lproductImageLink.add("-");
                            e.printStackTrace();
                        }
                        try{
                            lproductPrice.add(obj.get("productPrice").toString());
                        } catch (JSONException e) {
                            lproductPrice.add("-");
                            e.printStackTrace();
                        }


                    }

                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                    //recyclerViewAdapter.show_suggestions(username);
                    //spinner.setVisibility(View.GONE);

                    //recyclerViewAdater.mShowShimmer = false;
                    /*
                    recyclerViewAdapter.notifyDataSetChanged();
                    //recyclerViewAdapter.show_suggestions(username);
                    spinner.setVisibility(View.GONE);

                     */



                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "An exception occurred", Toast.LENGTH_LONG).show();
                    //spinner.setVisibility(View.GONE);
                    Log.d("user_list", "onResponse: " + e.getLocalizedMessage());
                    dialog.dismiss();
                    e.printStackTrace();

                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                //spinner.setVisibility(View.GONE);
                if (error instanceof NoConnectionError || error instanceof TimeoutError) {
                    Toast.makeText(getApplicationContext(), "Check Your Internt Connection Please!", Toast.LENGTH_SHORT).show();

                }
                else if (error instanceof AuthFailureError) {
                    // Error indicating that there was an Authentication Failure while performing the request
                    Toast.makeText(getApplicationContext(), "This error is case2", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    //Indicates that the server responded with a error response
                    Toast.makeText(getApplicationContext(), "This error is server error", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    //Indicates that there was network error while performing the request
                    Toast.makeText(getApplicationContext(), "This error is case4", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    // Indicates that the server response could not be parsed
                    Toast.makeText(getApplicationContext(), "This error is case5", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                Log.e("user list", "onErrorResponse: " + error);
            }
        });
        /*
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Token " + token);
                return map;
            }
        };

         */


        requestQueue.add(jsonArrayRequest);
        jsonArrayRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        /*
        Rview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

                int totalCount, pastItemCount, visibleItemCount;
                if (dy > 0) {
                    totalCount = layoutManager.getItemCount();
                    pastItemCount = layoutManager.findFirstVisibleItemPosition();
                    visibleItemCount = layoutManager.getChildCount();
                    if ((pastItemCount + visibleItemCount) >= totalCount) {
                        Log.d(TAG, "onScrolled: " + nextUrl);
                        if (!nextUrl.equals("null") && !isNextBusy)
                            getNextDdos();
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }

            }
        });

         */

    }
}
