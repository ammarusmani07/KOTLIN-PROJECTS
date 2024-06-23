package com.example.apiwork;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.apiwork.Model;
import com.example.apiwork.Adapter;
import com.example.apiwork.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Model> modelList;
    private Adapter modelAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.Recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        modelList = new ArrayList<>();
        modelAdapter = new Adapter(this, modelList);
        recyclerView.setAdapter(modelAdapter);

        fetchData();
    }

    private void fetchData() {
        // Replace "your_api_url" with the actual URL of your API
        String apiUrl = "https://jsonplaceholder.typicode.com/posts";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                apiUrl,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        parseJson(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error fetching data", error);
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }

    private void parseJson(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Model model = new Model(
                        jsonObject.getInt("userId"),
                        jsonObject.getInt("id"),
                        jsonObject.getString("title"),
                        jsonObject.getString("body")
                );
                modelList.add(model);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Notify the adapter that the data has changed
        modelAdapter.notifyDataSetChanged();
    }
}
