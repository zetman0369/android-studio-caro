package com.example.baitaplon;

import android.graphics.drawable.Drawable;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GamePlayActivity extends AppCompatActivity {
    static final int numberOfcolumns = 12;
    static final int numberOfrows = 21;
    private int numberOfcells = numberOfcolumns*numberOfrows;
    private String[] symbols = new String[numberOfcells];
    private String[] valueKeeper = new String[numberOfcells];
    static int positionFistClick = -1;
    static int positionSecondClick = -1;
    static int previousClick = -1;
    static int turn=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameplay);
        GridView cells = findViewById(R.id.GridCell);
        cells.setNumColumns(12);
        symbols=this.initStringArr(symbols);
        valueKeeper = symbols;
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.cell,symbols);
        cells.setAdapter(adapter);
        cells.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
}
