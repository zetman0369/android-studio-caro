package com.example.baitaplon;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.example.baitaplon.model.Game;
import com.example.baitaplon.model.User;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Friend extends AppCompatActivity {
    Gson gson = new Gson();
    ListView listuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend);
        listuser = (ListView)findViewById(R.id.list);
        SharedPreferences thisUser = getSharedPreferences("thisUser", Context.MODE_PRIVATE);
        String json = thisUser.getString("thisUser", "");
        Gson gson = new Gson();
        User user = gson.fromJson(json, User.class);
        Client me = new Client();
        me.setWhatTodo("danhsachban");
        me.setUser(user);
        me.execute();
        Button timkiem = findViewById(R.id.timkiem);
        timkiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Client me = new Client();
                me.setWhatTodo("timkiemban");
                me.setUser(user);
                me.execute();
            }
        });
    }
    public class UserAdapter extends ArrayAdapter<User> {
        ArrayList<User> users;
        public UserAdapter(Context context, ArrayList<User> users) {
            super(context, 0, users);
            this.users = users;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            User user = gson.fromJson(String.valueOf(this.users.get(position)), User.class);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.user, parent, false);
            }
            TextView userName = (TextView) convertView.findViewById(R.id.ten);
            TextView userPoint = (TextView) convertView.findViewById(R.id.diem);
            TextView userStatus = (TextView) convertView.findViewById(R.id.trangthai);
            userName.setText(user.getUsername());
            userPoint.setText(Integer.toString(user.getPoint()));
            if(user.getStatus()==1){
                userStatus.setText("online");
            }else{
                userStatus.setText("offline");
            }
            return convertView;
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

        public String getWhatTodo() {
            return whatTodo;
        }

        public void setWhatTodo(String whatTodo) {
            this.whatTodo = whatTodo;
        }

        private String whatTodo;
        private String result;
        @Override
        protected String doInBackground(String... strings) {
            if(this.whatTodo=="danhsachban"){
                return danhsachban(this.user);
            }else{
                EditText nhapten = findViewById(R.id.nhapten);
                return timkiemban(this.user,nhapten.getText().toString());
            }
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ArrayList<User> list = (ArrayList<User>)gson.fromJson(s, ArrayList.class);
            UserAdapter adapter = new UserAdapter(getApplicationContext(), list);
            listuser.setAdapter(adapter);
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
        public String timkiemban(User user,String name){
            this.startCommunicating();
            String json = gson.toJson(user);
            String result = null;
            try {
                pw.println("timkiemban");
                pw.flush();
                pw.println(name);
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
        public String danhsachban(User user){
            this.startCommunicating();
            String json = gson.toJson(user);
            String result = null;
            try {
                pw.println("danhsachban");
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

