package com.example.internship_task.Connection;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.example.internship_task.database.DatabaseHandler;
import com.example.internship_task.model.RequestModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class HttpConnection extends AsyncTask<String, String, String> {
    ProgressDialog progressDialog;
    Context context;
    TextView view;
    String postData;
    String methode;
    String Stringurl;
    DatabaseHandler databaseHandler ;
    Map<String, List<String>> headerFields;
   String requestHeaders;
    public static ConnectionListener connectionListener;

    public HttpConnection(Context context, TextView view, String postData, String methode) {
        this.context = context;
        this.view = view;
        this.postData = postData;
        this.methode = methode;
        databaseHandler    = new DatabaseHandler(context);
    }

    @Override
    protected String doInBackground(String... params) {
        String response = "";
        if (methode.equals("post")) {
            response = postMethod(postData, params);

        } else {
            response = getMethod(params);

        }

        return response;
    }

    private String postMethod(String postData, String... params) {
        HttpURLConnection urlConnection = null;
        String response = "";
//        Log.e("Exception", "post"+"   dfd");
        try {
            JSONObject jsonPostData = format_data(postData);

//            jsonPostData.put("password", "pistol");
//            jsonPostData.put("email", "eve.holt@reqres.in");
            Stringurl =params[0];
            URL url = new URL(params[0]);
//            URL url = new URL("https://reqres.in/api/register");
            urlConnection = (HttpURLConnection) url.openConnection();
            if (!postData.equals("")){
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setChunkedStreamingMode(0);
                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                        out, "UTF-8"));
                writer.write(jsonPostData.toString());
                writer.flush();
            }
            requestHeaders = urlConnection.getRequestProperties().toString();
            headerFields = urlConnection.getHeaderFields();
//
            int code = urlConnection.getResponseCode();
            response = "           Response code : " + code + " \n";
            if (code != 201 && code != 200) {
                throw new IOException("Invalid response from server: " + code);
            }

            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                response += line;
            }
            saveData(response,headerFields.toString());
        } catch (Exception e) {
            Log.e("Exception at postMethod", e.getMessage());
            e.printStackTrace();
            return e.getMessage();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }


        return response;
    }

    JSONObject format_data(String data) {
        JSONObject jsonPostData = new JSONObject();
        String lines[] = data.split("\n");
        for (String line : lines) {
            String words[] = line.split(" ");

            try {
                //to handle the wrong space or newline
                if (!words[0].equals(""))
                    jsonPostData.put(words[0], words[1]);
            } catch (JSONException e) {
                Log.e("ERROR jsonPostData", e.getMessage());
                e.printStackTrace();
            }

        }
        return jsonPostData;
    }


    private String getMethod(String... params) {
        HttpURLConnection urlConnection = null;
        String response = "";

        try {
            Stringurl =params[0];
//            URL url = new URL(params[0]);
            URL url = new URL("https://reqres.in/api/users?page=2");
//            URL url = new URL("https://documentation.bonitasoft.com/create-your-first-project-web-rest-api-and-maven");
//            URL url = new URL("https://techcrunch.com/wp-json/wp/v2/posts?per_page=100&context=embed");

            urlConnection = (HttpURLConnection) url.openConnection();
            // headerResponse
//            urlConnection.
            Log.e("tagggggggggggggggggggggg", urlConnection.getRequestProperties().toString());
            requestHeaders = urlConnection.getRequestProperties().toString();
            headerFields = urlConnection.getHeaderFields();

            int code = urlConnection.getResponseCode();
            response = "           Response code : " + code + " \n\n";
            if (code != 200) {
                throw new IOException("Invalid response from server: " + code);
            }

            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));

            String line;
            while ((line = rd.readLine()) != null) {
                response += line;
            }
            saveData(response,headerFields.toString());


        } catch (Exception e) {

            Log.e("Exception at getMethod", e.getMessage());
            e.printStackTrace();
            return e.getMessage();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

        }



//        Log.e("response :", response.toString());
        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        response = response.replaceAll(",", ",\n");
        view.setText(response);
        progressDialog.dismiss();
        if (connectionListener != null)
            connectionListener.onConnectionListener(response, headerFields,requestHeaders);

//        Log.e("header", headerFields.toString());

    }

    private void saveData(String response, String headerFields) {
        databaseHandler.addRequest(new RequestModel(methode, Stringurl,response,headerFields,   requestHeaders ));
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("processing results");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public interface ConnectionListener {
        void onConnectionListener(String response, Map<String, List<String>> headerFields,String requestHeaders);
    }
}
