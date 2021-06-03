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
import com.google.gson.internal.LinkedTreeMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Chart extends AppCompatActivity {
    ListView listuser;
    Gson gson = new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart);
        listuser = (ListView)findViewById(R.id.list);
        Client me = new Client();
        me.execute();
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
            Object getrow = this.users.get(position);
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
        private User user;
        private Game game;
        private String whatTodo;
        private String result;
        @Override
        protected String doInBackground(String... strings) {
            return bangxephang();
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
        public String bangxephang(){
            this.startCommunicating();
            try {
                pw.println("bangxephang");
                pw.flush();
                result = br.readLine();
                System.out.println(result);
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
    }
}
