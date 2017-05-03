package com.scryptan.clientserver;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;
import retrofit2.http.Path;


public class MainActivity extends AppCompatActivity {
    private static String LOG_TAG = "MainActivity";
    private final String baseUrl = "http://192.168.1.113:3000/";
    private TextView lastnameF;
    private String lastnameS, firstnameS;
    private User userFromServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lastnameF = (TextView) findViewById(R.id.lastnameF);
    }

    public void sendPOST(View view) {
        EditText lastname = (EditText) findViewById(R.id.lastname);
        EditText firstname = (EditText) findViewById(R.id.firstname);
        lastnameS = lastname.getText().toString();
        firstnameS = firstname.getText().toString();
        new MyAsyncTask().execute("");
    }

    class MyAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            UserService service = retrofit.create(UserService.class);
            Call<User> call = service.fetchUser(firstnameS, lastnameS);
            try {
                Response<User> userResponse = call.execute();
                userFromServer = userResponse.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        public String TAG="ALEI SUKA";
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                lastnameF.setText(userFromServer.fullName);
            }catch (Exception e){
                Log.e(TAG, "onPostExecute: "+e.toString());
                Log.i(TAG, "onPostExecute: "+userFromServer.lastName);
                Log.i(TAG, "onPostExecute: "+userFromServer.firstName);
                Log.i(TAG, "onPostExecute: "+userFromServer.fullName);
            }
        }
    }
    public class User {
        public String firstName;
        public String lastName;
        public String fullName;
    }

    public interface noUserService {
        @POST("/{firstName}/{lastName}")
        Call<MainActivity.User> fetchUser(@Path("firstName") String firstName,
                                          @Path("lastName") String lastName);
    }

}