package com.example.user.workshop;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Login extends AppCompatActivity {

    private Button btnRegis;
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btLogin);
        btnRegis = (Button) findViewById(R.id.btRe);

        setEvent();

    }

    private void setEvent() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()){
                    new login(etUsername.getText().toString(),
                            etPassword.getText().toString()).execute();
                    Intent i = new Intent(Login.this, NewsDetail.class);
                    startActivity(i);

                }else {

                }
            }
        });

        btnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Register.class);
                startActivity(i);
            }
        });
    }


    private boolean validate() {

        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        if (username.isEmpty()) return false;
        if (password.isEmpty()) return false;
        return true;
    }

    class login extends AsyncTask<Void, Void, String> {


        private String username;
        private String password;

        public login(String username, String password){
            this.username = username;
            this.password = password;
        }

        @Override
        protected String doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();
            Request request;
            Response response;

            RequestBody requestBody = new FormBody.Builder()
                    .add("username", username)
                    .add("password", password)
                    .build();
            request = new Request.Builder()
                    .url("http://kimhun55.com/pollservices/login.php")
                    .post(requestBody)
                    .build();

            try{
                response = client.newCall(request).execute();

                if (response.isSuccessful()){
                    return response.body().string();
                }
            }catch (IOException ex){
                ex.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String s){
            super.onPostExecute(s);
            try {
                JSONObject rootobj = new JSONObject(s);
                if (rootobj.has("result")){
                    JSONObject resultobj = rootobj.getJSONObject("result");
                    if (resultobj.getInt("result") == 1){
                        Toast.makeText(Login.this, resultobj.getString("result_desc"), Toast.LENGTH_SHORT).show();
                        setEvent();
                    }else {
                        Toast.makeText(Login.this, resultobj.getString("result_desc"), Toast.LENGTH_SHORT).show();
                    }
                }
            }catch (JSONException ex){

            }

        }

    }

}
