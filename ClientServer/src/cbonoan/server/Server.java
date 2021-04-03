package cbonoan.server;

import cbonoan.Message;
import cbonoan.MessageType;
import cbonoan.client.Client;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private static final ArrayList<ClientListener> clients = new ArrayList<>();
    private static final ExecutorService pool = Executors.newFixedThreadPool(40);

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //Create server socket
        ServerSocket serverSocket = new ServerSocket(3390);
        System.out.println("Server Started on port: " + serverSocket.getLocalPort() + " waiting for clients to connect");

        while(true) {
            Socket socket = serverSocket.accept();
            ClientListener tmpClient = new ClientListener(socket);
            clients.add(tmpClient);
            pool.execute(tmpClient);
        }
    }

    public static void broadcast(Message m) throws IOException {
        for(ClientListener c : clients) {
            if(c.getSocket().isClosed()) {
                clients.remove(c);
            } else {
                c.sendMessageToClient(m);
            }
        }
    }
}
