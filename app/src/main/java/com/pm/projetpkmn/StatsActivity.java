package com.pm.projetpkmn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity {
    String API_KEY = "dc19ace9-0a20-467a-b498-42e29a529bf3";
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        String pseudo = i.getStringExtra("pseudo");
        setContentView(R.layout.activity_game);
        tv = findViewById(R.id.tv1);
    }

    public void start(View view) {
        EditText et = findViewById(R.id.input);
        String pseudo = et.getText().toString();
        RequestTask rt = new RequestTask();
        rt.execute(pseudo);
    }


    private class RequestTask extends AsyncTask<String, Void, ArrayList<String>> {
        // Le corps de la tâche asynchrone (exécuté en tâche de fond)

        @Override
        protected ArrayList<String> doInBackground(String... pseudo) {
            ArrayList<String> array = new ArrayList<String>();
            String str = requete(pseudo[0]);
            array.add(str);
            return array;
        }
        private String requete(String pseudo) {
            String response = "";
            try {
                HttpURLConnection connection = null;
                URL url = new
                        URL("https://api.mojang.com/users/profiles/minecraft/"+ URLEncoder.encode(pseudo,"utf-8"));
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String ligne = bufferedReader.readLine() ;
                while (ligne!= null){
                    response+=ligne;
                    ligne = bufferedReader.readLine();
                }
                JSONObject toDecode = new JSONObject(response);
                response = getUUID(toDecode);
                Log.d("uuid", String.valueOf(response.length()));
                if(response.length() > 1){
                    HttpURLConnection connection2 = null;
                    URL url2 = new
                            URL("https://api.hypixel.net/player?uuid="+ URLEncoder.encode(response,"utf-8")+"&key=" + URLEncoder.encode(API_KEY, "utf-8"));
                    connection2 = (HttpURLConnection) url2.openConnection();
                    connection2.setRequestMethod("GET");
                    InputStream inputStream2 = connection2.getInputStream();
                    InputStreamReader inputStreamReader2 = new InputStreamReader(inputStream2);
                    bufferedReader = new BufferedReader(inputStreamReader2);
                    String str = bufferedReader.readLine();
                    response = "";
                    while (str!= null){
                        response+=str;
                        str = bufferedReader.readLine();
                    }
                    toDecode = new JSONObject(response);
                    //response = decodeJSON(toDecode);
                }
            } catch (UnsupportedEncodingException e) {
                response = "problème d'encodage";
            } catch (MalformedURLException e) {
                response = "problème d'URL ";
            } catch (IOException e) {
                response = "problème de connexion ";
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
        private String decodeJSON(JSONObject jso) throws Exception {
            String response = "";

            return response;
        }
        private String getUUID(JSONObject jso) throws Exception {
            return jso.getString("id");
        }



        // Méthode appelée lorsque la tâche de fond sera terminée
        protected void onPostExecute(ArrayList<String> array) {
            tv.setText(array.get(0));
        }
    }
}