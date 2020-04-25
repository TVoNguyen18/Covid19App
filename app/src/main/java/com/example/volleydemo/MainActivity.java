package com.example.volleydemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;


public class MainActivity extends AppCompatActivity {


    RequestQueue requestQueue;  //STEP 1
    String dataToDisplay = "";
    private final String API_KEY = "5357987e";

    EditText searchBar;
    EditText yearSearchBar;
    ImageView posterImageView;
    TextView plotTextView;
    TextView ratedTextView;
    TextView runtimeTextView;
    TextView imdbTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBar = (EditText)findViewById(R.id.search_bar);
        plotTextView = (TextView)findViewById(R.id.text_view);



        Log.d("onCreate" , "Application loading");

        final int min = 1;
        final int max = 20;
        final int random = new Random().nextInt((max - min) + 1) + min;

        requestQueue = Volley.newRequestQueue(this); //STEP 2

    }


    public void fetchData(View view) {
        final TextView textView = (TextView) findViewById(R.id.text_view);
        final String searched = searchBar.getText().toString();
        //String year = yearSearchBar.getText().toString();
        // ...

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.covid19api.com/summary";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //plotTextView.setText("Response is: " + response);
                        try {
                            JSONObject responseObject = new JSONObject(response);
                            JSONArray countriesArray = responseObject.getJSONArray("Countries");

                            for (int i = 0 ; i < countriesArray.length(); i++) {
                                JSONObject currentCountry = countriesArray.getJSONObject(i);

                                String currentCountryString = currentCountry.getString("Country").toLowerCase();

                                String countrySearched = searchBar.getText().toString().toLowerCase();

                                if (currentCountryString.equals(countrySearched)) {
                                    plotTextView.setText("Country Code: " + currentCountry.getString("CountryCode")
                                            + "\n\nTotal cases: " + currentCountry.getString("TotalConfirmed")
                                            + "\n\nDate Retrieved: " + currentCountry.getString("Date"));
                                }
                            }
                        } catch (Exception e) {
                            plotTextView.setText("Country not found!");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }
}
