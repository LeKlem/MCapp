package com.pm.projetpkmn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity {
    TextView tv;
    LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ll = (LinearLayout)findViewById(R.id.layout);
        tv = findViewById(R.id.tv1);
        RequestTask rt = new RequestTask();
        rt.execute();
    }

    private class RequestTask extends AsyncTask<Integer, Void, ArrayList<String>> {
        ArrayList<blog_post> blogList;

        @Override
        protected ArrayList<String> doInBackground(Integer... nb) {
            ArrayList<String> array = new ArrayList<String>();
            requete();
            return array;
        }
        private String requete() {
            ArrayList<blog_post> blogList;
            String response = "";
            try {
                HttpURLConnection connection = null;
                URL url = new
                        URL("https://hytale.com/api/blog/post/published");
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
                JSONArray toDecode = new JSONArray(response);
                blogList = decodeJSON(toDecode);
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
        private ArrayList<blog_post> decodeJSON(JSONArray jsa) throws Exception {
            blogList = new ArrayList<blog_post>();
            blog_post blog;
            int i = 0;
            while(i < jsa.length()){
                blog = new blog_post();
                JSONObject obj = jsa.getJSONObject(i);
                blog.setAuthor(obj.getString("author"));
                blog.setTitle(obj.getString("title"));
                JSONObject img = obj.getJSONObject("coverImage");
                blog.setImgUrl(img.getString("s3Key"));
                blogList.add(blog);
                i++;
            }
            return blogList;
        }
        protected void onPostExecute(ArrayList<String> array) {
            display_blog(blogList);
        }
    }
    protected void display_blog(ArrayList<blog_post> blogList){
        //generatedId = View.generateViewId();
        int i = 0;
        while(i < blogList.size()) {
            blog_post post = blogList.get(i);
            CardView cv = new CardView(this);
            cv.setId(i);
            cv.setCardElevation(10);
            cv.setOnClickListener(openDetails);
            cv.setPreventCornerOverlap(true);
            cv.setCardBackgroundColor(0);
            cv.setUseCompatPadding(true);
            ImageView iv = new ImageView(this);
            iv.setMinimumWidth(200);
            iv.setMaxWidth(200);
            iv.setMinimumHeight(200);
            iv.setMaxHeight(200);
            Picasso.get().load(post.getImgUrl()).into(iv);
            iv.setContentDescription("tu le c");
            TextView tv = new TextView(this);
            tv.setText(post.getTitle());
            cv.addView(iv);
            cv.addView(tv);
            ll.addView(cv);
            i++;
        }
    }
    private View.OnClickListener openDetails = new View.OnClickListener() {
        public void onClick(View v) {
            Log.d("clicked", String.valueOf(v.getId()));
        }
    };


}