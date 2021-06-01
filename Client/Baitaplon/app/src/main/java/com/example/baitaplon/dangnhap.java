package com.example.baitaplon;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.baitaplon.model.Game;
import com.example.baitaplon.model.User;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class dangnhap extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button btnhuy;
        Button btdangnhap;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dangnhap);

        btnhuy = (Button)findViewById(R.id.bthuy);
        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        btdangnhap = (Button)findViewById(R.id.btdangnhap);
        btdangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edusername = findViewById(R.id.edusername);
                EditText edpassworddk = findViewById(R.id.edpassword);
                Client me = new Client();
                User user = new User();
                user.setUsername(edusername.getText().toString());
                user.setPassword(edpassworddk.getText().toString());
                me.setUser(user);
                me.execute();
            }
        });
    }
    class Client extends AsyncTask<String, Void, String> {
        Socket socket;
        String hostName = "10.0.2.2";
        int portNumber = 4444;
        PrintWriter pw = null;
        BufferedReader br = null;
        Gson gson = new Gson();
        private User user;
        private Game game;
        private String whatTodo;
        private String result;
        @Override
        protected String doInBackground(String... strings) {
            return dangnhap(this.user);
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String result = s;
            System.out.println(s);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(dangnhap.this);
            if(!s.equals("dangnhapthatbai")){
                SharedPreferences thisUser = getSharedPreferences("thisUser", Context.MODE_PRIVATE);
                SharedPreferences.Editor thisUserEditor = thisUser.edit();
                thisUserEditor.putString("thisUser", s);
                thisUserEditor.apply();
                alertDialogBuilder.setMessage("Đăng nhập thành công, chuyển về trang chính");
                alertDialogBuilder.setPositiveButton("Đồng ý",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }else{
                alertDialogBuilder.setMessage("Đăng nhập thất bại, sai mật khẩu hoặc tên tài khoản, vui lòng nhập lại");
                alertDialogBuilder.setPositiveButton("Đồng ý",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }
        public void startCommunicating(){
            try{
                socket = new Socket(hostName, portNumber);
                pw=new PrintWriter(socket.getOutputStream(),true);
                br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            }catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public String dangnhap(User user){
            this.startCommunicating();
            String json = gson.toJson(user);
            String result = null;
            try {
                pw.println("dangnhap");
                pw.flush();
                pw.println(json);
                pw.flush();
                result = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.endCommunicating();
            return result;
        }
        public void endCommunicating(){
            try{
                socket.close();
            }catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public Game getGame() {
            return game;
        }

        public void setGame(Game game) {
            this.game = game;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }
}
