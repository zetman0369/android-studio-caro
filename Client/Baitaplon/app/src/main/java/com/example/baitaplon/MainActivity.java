package com.example.baitaplon;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
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

public class MainActivity extends AppCompatActivity {
    private Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button choionline = findViewById(R.id.choionline);
        Button choivoimay = findViewById(R.id.choivoimay);
        Button hainguoichoi = findViewById(R.id.hainguoichoi);
        Button bangxephang = findViewById(R.id.bangxephang);
        Button banbe = findViewById(R.id.banbe);
        Button ketban = findViewById(R.id.ketban);
        choionline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuItem name = menu.findItem(R.id.name);
                if(name.isVisible()){

                }else{
                    Intent intent = new Intent(getApplicationContext(), dangnhap.class);
                    startActivity(intent);
                }
            }
        });
        choivoimay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        hainguoichoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GamePlayActivity.class);
                startActivity(intent);
            }
        });
        bangxephang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Chart.class);
                startActivity(intent);
            }
        });
        ketban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences thisUser = getSharedPreferences("thisUser", Context.MODE_PRIVATE);
                String json = thisUser.getString("thisUser", "");
                Gson gson = new Gson();
                User user = gson.fromJson(json, User.class);
                if(user!=null){
                    Intent intent = new Intent(getApplicationContext(), AddFriend.class);
                    startActivity(intent);
                }else{
                    Intent dangnhap = new Intent(getApplicationContext(), dangnhap.class);
                    startActivity(dangnhap);
                }
            }
        });
        banbe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences thisUser = getSharedPreferences("thisUser", Context.MODE_PRIVATE);
                String json = thisUser.getString("thisUser", "");
                Gson gson = new Gson();
                User user = gson.fromJson(json, User.class);
                if(user!=null){
                    Intent intent = new Intent(getApplicationContext(), Friend.class);
                    startActivity(intent);
                }else{
                    Intent dangnhap = new Intent(getApplicationContext(), dangnhap.class);
                    startActivity(dangnhap);
                }
            }
        });
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem dangnhap = menu.findItem(R.id.dangnhap);
        MenuItem dangky = menu.findItem(R.id.dangky);
        MenuItem name = menu.findItem(R.id.name);
        MenuItem dangxuat = menu.findItem(R.id.dangxuat);
        SharedPreferences thisUser = getSharedPreferences("thisUser", Context.MODE_PRIVATE);
        String json = thisUser.getString("thisUser", "");
        Gson gson = new Gson();
        User user = gson.fromJson(json, User.class);
        if (user!=null) {
            dangnhap.setVisible(false);
            dangky.setVisible(false);
            dangxuat.setVisible(true);
            name.setVisible(true);
            name.setTitle("Chào "+user.getUsername());
            name.setCheckable(false);
        } else {
            name.setVisible(false);
            dangxuat.setVisible(false);
            dangnhap.setVisible(true);
            dangky.setVisible(true);
        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.dangnhap:
                Intent dangnhap = new Intent(getApplicationContext(), dangnhap.class);
                startActivity(dangnhap);
                return true;
            case R.id.dangky:
                Intent dangky = new Intent(getApplicationContext(), dangky.class);
                startActivity(dangky);
                return true;
            case R.id.dangxuat:
                Client me = new Client();
                SharedPreferences thisUser = getSharedPreferences("thisUser", Context.MODE_PRIVATE);
                String json = thisUser.getString("thisUser", "");
                Gson gson = new Gson();
                User user = gson.fromJson(json, User.class);
                me.setUser(user);
                me.execute();
            default:
                return super.onOptionsItemSelected(item);
        }
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
            return dangxuat(this.user);
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String result = s;
            System.out.println(s);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
            if(s.equals("dangxuatthanhcong")){
                alertDialogBuilder.setMessage("Bạn có chắc muốn đăng xuất?");
                alertDialogBuilder.setPositiveButton("Đồng ý",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                SharedPreferences preferences = getSharedPreferences("thisUser", 0);
                                preferences.edit().remove("thisUser").commit();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        });
                alertDialogBuilder.setNegativeButton("Hủy",
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
        public String dangxuat(User user){
            this.startCommunicating();
            String json = gson.toJson(user);
            String result = null;
            try {
                pw.println("dangxuat");
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