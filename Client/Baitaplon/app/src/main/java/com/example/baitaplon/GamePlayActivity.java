package com.example.baitaplon;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GamePlayActivity extends AppCompatActivity {
    static final int numberOfcolumns = 12;
    static final int numberOfrows = 21;
    protected int numberOfcells = numberOfcolumns*numberOfrows;
    protected String[] symbols = new String[numberOfcells];
    protected String[] valueKeeper = new String[numberOfcells];
    static int positionFistClick = -1;
    static int positionSecondClick = -1;
    static int previousClick = -1;
    static int turn=0;
    private String CHAT_SERVER_IP = "10.0.2.2";
//    private String CHAT_SERVER_IP = "192.168.1.52";
    private Socket client;
    private PrintWriter printwriter;
    private BufferedReader bufferedReader;
    protected GridView cells;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameplay);
        cells = findViewById(R.id.GridCell);
        cells.setNumColumns(12);
        symbols=this.initStringArr(symbols);
        valueKeeper = symbols;
        MoveOperator moveOperator = new MoveOperator();
        moveOperator.execute();
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.cell,symbols);
        cells.setAdapter(adapter);
    }

    protected String[] initStringArr(String[] arr){
        for(int i=0;i<numberOfcells;i++){
            arr[i]="";
        }
        return arr;
    }

    protected void resetValueKeeper(){
        for(int i=0;i<numberOfcells;i++){
            if(valueKeeper[i]=="-1")valueKeeper[i]="";
        }
    }

    private boolean checkWin(int position){
        int k=0;
        int currentColumn = -1;
        int currentRow = -1;
        Integer[][] arr = new Integer[numberOfcolumns][numberOfrows];
        for(int i=0;i<numberOfcolumns;i++){
            for(int j=0;j<numberOfrows;j++){
                if(position == k){
                    currentColumn=i;
                    currentRow=j;
                }
                if(valueKeeper[k]=="0"||valueKeeper[k]=="1")arr[i][j]=Integer.parseInt(valueKeeper[k++]);
            }
        }
        int count=0;
        int valueOfSymbol = -1;
        for(int i=currentColumn-4;i<currentColumn+4;i++){
            if(arr[i][currentRow]!=null){
                if(valueOfSymbol==-1){
                    valueOfSymbol=arr[i][currentRow];
                }else if(arr[i][currentRow]!=valueOfSymbol){
                    valueOfSymbol=arr[i][currentRow];
                    count=0;
                }
                count+=1;
            }else{
                count=0;
            }
        }
        if(count==5)return true;
        return false;
    }
    private class MoveOperator extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                client = new Socket(CHAT_SERVER_IP, 4444); // Creating the server socket.

                if (client != null) {
                    printwriter = new PrintWriter(client.getOutputStream(), true);
                    bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    printwriter.println("choigame");
                    printwriter.flush();
                    printwriter.println();
                } else {
                    System.out.println("Server has not bean started on port 4444.");
                }
            } catch (UnknownHostException e) {
                System.out.println("Faild to connect server " + CHAT_SERVER_IP);
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("Faild to connect server " + CHAT_SERVER_IP);
                e.printStackTrace();
            }
            return null;
        }

        /**
         * Following method is executed at the end of doInBackground method.
         */
        @Override
        protected void onPostExecute(Void result) {
            cells.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final Sender sender = new Sender(); // Initialize chat sender AsyncTask.
                    sender.setPosition(Integer.toString(position));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        sender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        sender.execute();
                    }
                    TextView cell = (TextView) view;
                    cell.setBackgroundResource(R.drawable.vien2);
                    if(valueKeeper[position]=="-1"){
                        positionSecondClick=position;
                    }else{
                        resetValueKeeper();
                        positionSecondClick=-1;
                    }
                    valueKeeper[position]="-1";
                    positionFistClick = position;
                    TextView previousItem = (TextView) parent.getChildAt(previousClick);;
                    if(positionFistClick==positionSecondClick) {
                        cell.setBackgroundResource(R.drawable.vien);
                        if (turn == 0) {
                            valueKeeper[position]="0";
                            cell.setText("X");
                            turn = 1;
                        } else {
                            valueKeeper[position]="1";
                            cell.setText("O");
                            turn = 0;
                        }
                        cell.setClickable(false);
                    }
                    if(previousClick != -1 && previousClick != positionFistClick){
                        previousItem.setBackgroundResource(R.drawable.vien);
                    }

//                if(checkWin(position))System.out.println("xczvnvsvnfisufudhuhuhu");
                    previousClick = position;
                }
            });
            Receiver receiver = new Receiver(); // Initialize chat receiver AsyncTask.
            receiver.execute();
        }
    }
    /**
     * This AsyncTask continuously reads the input buffer and show the chat
     * message if a message is availble.
     */
    private class Receiver extends AsyncTask<Void, Void, Void> {

        private String position;

        @Override
        protected Void doInBackground(Void... params) {
            while (true) {
                try {

                    if (bufferedReader.ready()) {
                        position = bufferedReader.readLine();
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                }
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            TextView cell = (TextView) cells.getChildAt(Integer.parseInt(position));
            cell.setBackgroundResource(R.drawable.vien);
            cell.setText("X");
            cell.setClickable(false);
        }
    }

    /**
     * This AsyncTask sends the chat message through the output stream.
     */
    private class Sender extends AsyncTask<Void, Void, Void> {
        String position = null;
        @Override
        protected Void doInBackground(Void... params) {
            printwriter.write(position);
            printwriter.flush();
            return null;
        }

        public void setPosition(String position) {
            this.position = position;
        }
    }
}
