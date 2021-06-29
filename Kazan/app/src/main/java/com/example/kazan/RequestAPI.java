package com.example.kazan;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestAPI extends AsyncTask<Void, Void, String> {
    IOTask ioTask;
    int requestCode;
    String urlStr = "http://10.0.2.2:49801/api/";
    String method, jsonData;
    int resultCode = 0;

    public RequestAPI (IOTask ioTask, int requestCode, String urlStr, String method, String jsonData ){
        this.ioTask = ioTask;
        this.requestCode = requestCode;
        this.urlStr+=urlStr;
        this.method = method;
        this.jsonData = jsonData;

    }
    @Override
    protected String doInBackground(Void... voids) {
        if(method.equals("GET")){
            try {
                jsonData = getJsonFromServer();
                return jsonData;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            try {
                if(method.equals("DELETE")){
                    Log.d("Method ne", method);
                }
                URL url = new URL(urlStr);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestMethod(method);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setChunkedStreamingMode(0);
                try(OutputStream outputStream = connection.getOutputStream()){
                    byte[] bytes = jsonData.getBytes();
                    outputStream.write(bytes, 0, bytes.length);
                }
                resultCode = connection.getResponseCode();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                return  bufferedReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String getJsonFromServer() throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        return new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        ioTask.doIt(requestCode, s, resultCode);
    }
}
