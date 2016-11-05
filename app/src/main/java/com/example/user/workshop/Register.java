package com.example.user.workshop;

import android.net.nsd.NsdManager;
import android.os.AsyncTask;
import android.speech.tts.Voice;
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

public class Register extends AppCompatActivity {

    private EditText display;
    private EditText user;
    private EditText pass;
    private EditText confirm;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        display = (EditText) findViewById(R.id.display);
        user = (EditText) findViewById(R.id.user);
        pass = (EditText) findViewById(R.id.pass);
        confirm = (EditText) findViewById(R.id.confirm);
setListener();;
    }

    protected void setListener(){
        Button btnLogin = (Button) findViewById(R.id.btRegister);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    new Register1(user.getText().toString(),
                            pass.getText().toString(),
                            confirm.getText().toString(),
                            display.getText().toString()).execute();
                }

            }



        });
    }

    private boolean validate() {
        //TODO validate form
        String username = user.getText().toString();
        String password = pass.getText().toString();
        String passwordConfirm = confirm.getText().toString();
        String displayName = display.getText().toString();

        if (username.isEmpty()) return false;

        if (password.isEmpty()) return false;

        if (passwordConfirm.isEmpty())
            return false;

        if (!password.equals(passwordConfirm))
            return false;

        if (displayName.isEmpty()) return false;
        return false;
    }

    private class Register1 extends AsyncTask<Void,Void,String> {
        private String username;
        private String password;
        private String passwordCon;

        public Register1(String usernamer, String displayName, String passwordCon, String password) {
            this.username = usernamer;
            this.displayName = displayName;
            this.passwordCon = passwordCon;
            this.password = password;
        }

        private String displayName;

        @Override
        protected void onPreExecute(String s) {
            super.onPreExecute(s);

            try {
                JSONObject rootobj = new JSONObject(s);
                if (rootobj.has("result")){
                    JSONObject resultObj = rootobj.getJSONObject("result");
                    if (resultObj.getInt("result")==1){
                        Toast.makeText(Register.this, resultObj.getString("result_desc") , Toast.LENGTH_SHORT)
                                finish();

                    }else {
                        Toast.makeText(Register.this,resultObj.getString("result_desc") , Toast.LENGTH_SHORT)
                    }
                }
            }catch (JSONException ex){

            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(Register.this, s ,Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            Request request;
            Response response;

            RequestBody requesBody = new FormBody.Builder()
                    .add("username", username)
                    .add("password", password)
                    .add("password_con", passwordCon)
                    .add("display_name", displayName)
                    .build();
            request = new Request.Builder()
                    .url("http://kimhun55.com/pollservices/signup.php")
                    .post(requesBody)
                    .build();

            try {
                response = client.newCall(request).execute();

                if(response.isSuccessful()){
                    return response.body().string();
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }

            return null;


        }

    }

}
