import DAO.DatabaseAccess;
import Model.User;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerThreads extends Thread{
  User user;
  private Socket clientSocket;
  DatabaseAccess dtbsAccess;
  String json;
  public ServerThreads(Socket clientSocket){
    super();
    this.clientSocket = clientSocket;
  }

  @Override
  public void run(){

    try {
        PrintWriter pw = new PrintWriter(clientSocket.getOutputStream(),true);
        BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        dtbsAccess = new DatabaseAccess();
        Boolean communicating = true;
        Gson gson = new Gson();
//        while(communicating){
            String whatToDo = br.readLine();
            System.out.println(whatToDo);
            if(whatToDo!=null){
                switch(whatToDo){
                    case "dangky":
                        System.out.println("1");
                        json = br.readLine();
                        user = gson.fromJson(json, User.class);
                        System.out.println(user.getUsername());
                        if(dtbsAccess.isExist(user)){
                            System.out.println("2");
                            pw.println("dangkythatbai");
                            pw.flush();
                        }else{
                            System.out.println("3");
                            pw.println("dangkythanhcong");
                            pw.flush();
                            dtbsAccess.register(user);
                        }
                        break;
                    case "dangnhap":
                        json = br.readLine();
                        user = gson.fromJson(json, User.class);
                        user = dtbsAccess.logIn(user);
                        if(user!=null){
                            json = gson.toJson(user);
                            pw.println(json);
                            pw.flush();
                            dtbsAccess.setStatus(user.getId(),1);
                        }else{
                            pw.println("dangnhapthatbai");
                            pw.flush();
                        }
                        break;
                    case "dangxuat":
                        json = br.readLine();
                        user = gson.fromJson(json, User.class);
                        dtbsAccess.logOut(user);
                        pw.println("dangxuatthanhcong");
                        pw.flush();
                        break;
                    case "endCommunicating":
                        communicating = false;
                        break;
                }
            }
//        }
        clientSocket.close();

    } catch (IOException e) {
    }
  }
}