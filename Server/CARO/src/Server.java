import DAO.Dao;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {

    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    HashMap<String, ServerThreads> useronline;
    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(4444);
        } catch (IOException e) {
            System.out.println("Could not listen on port: 4444");
        }

        System.out.println("Server started. Listening to the port 4444");

        while (true) {
            try {
                clientSocket = serverSocket.accept();
//                ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
//                ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
                
                new ServerThreads(clientSocket).start();
            } catch (IOException ex) {
                System.out.println("IO error " + ex.getMessage());
            }
        }
    }
}