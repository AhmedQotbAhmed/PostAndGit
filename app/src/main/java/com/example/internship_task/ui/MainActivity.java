package com.example.internship_task.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.internship_task.Connection.HttpConnection;
import com.example.internship_task.Connection.broadcast.NetworkState;
import com.example.internship_task.R;

import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements NetworkState.ReceiverListener, HttpConnection.ConnectionListener {
    TextView lbl_response;
    EditText edtUri;
    EditText edtPost;
    String methode;
    Spinner spinnerMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //find the view
        spinnerMethod = findViewById(R.id.spinner);
        lbl_response = findViewById(R.id.lbl_response);
        edtPost = findViewById(R.id.editTextTextMultiLine);
        edtUri = findViewById(R.id.edt_uri);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.method, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        spinnerMethod.setAdapter(adapter);
        spinnerMethodListener();
        HttpConnection.connectionListener = MainActivity.this;

    }

    void spinnerMethodListener() {

        spinnerMethod.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        if (position == 1) {  //post
                            methode="post";
                            findViewById(R.id.linear_post_body).setVisibility(View.VISIBLE);
                        } else {  // get
                            methode="get";

                            findViewById(R.id.linear_post_body).setVisibility(View.GONE);
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
    }

    public void SendClickListener(View view) {
        // create object of My Connection class and execute it
        if (checkConnection()) {
            String postString = edtPost.getText().toString() + "";
            HttpConnection connection = new HttpConnection(MainActivity.this, lbl_response, postString,methode);
            connection.execute(edtUri.getText().toString());

        } else {
            Toast.makeText(this, "Not Connected to Internet", Toast.LENGTH_LONG).show();
        }

    }


    private boolean checkConnection() {

        // initialize intent filter
        IntentFilter intentFilter = new IntentFilter();

        // add action
        intentFilter.addAction("android.new.conn.CONNECTIVITY_CHANGE");

        // register receiver
        registerReceiver(new NetworkState(), intentFilter);

        // Initialize listener
        NetworkState.Listener = MainActivity.this;

        // Initialize connectivity manager
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Initialize network info
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        // get connection status
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        return isConnected;
    }


    @Override
    public void onNetworkChange(boolean isConnected) {
        checkConnection();
    }

        @Override
        public void onConnectionListener(String response, Map<String, List<String>> headerFields,String requestHeaders) {
            ( (Button)findViewById(R.id.body_btn)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String _response = response.replaceAll(",", ",\n");
                    lbl_response.setText(_response);
                }
            });
            
            ( (Button)findViewById(R.id.res_hd_btn)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String out = headerFields.toString().replaceAll(",", ",\n");
                    lbl_response.setText(out);
                }
            });

            ( (Button)findViewById(R.id.req_hd_btn)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   String _response = requestHeaders.replaceAll(",", ",\n");
                    lbl_response.setText(_response);
                }
            });
        }



    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("edtUri", edtUri.getText().toString());
        savedInstanceState.putString("lbl_response", lbl_response.getText().toString());
        savedInstanceState.putString("edtPost", edtPost.getText().toString());

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        edtUri.setText(savedInstanceState.getString("edtUri"));
        lbl_response.setText(savedInstanceState.getString("lbl_response"));
        edtPost.setText(savedInstanceState.getString("edtPost"));
    }

    public void goHistory(View view) {
        startActivity(new Intent(this,HistoryActivity.class));
    }
}