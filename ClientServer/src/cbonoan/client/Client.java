package cbonoan.client;

import cbonoan.Message;
import cbonoan.MessageType;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static OutputStream outputStream;
    private static ObjectOutputStream objectOutputStream;

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        //TODO remove before production
        try {
            Thread.sleep(Integer.parseInt(args[0]));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Socket socket = new Socket("localhost", 3390);
        outputStream = socket.getOutputStream(); // Get output streams first and also flush streams
        outputStream.flush();
        objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.flush();

        // Write to server
        Message tmpMsg = new Message(MessageType.CONNECT, args[1], "Hello I am now Connected!");
        objectOutputStream.writeObject(tmpMsg);
        objectOutputStream.flush();

        ServerListener serverListener = new ServerListener(socket);
        serverListener.start();

        Scanner scanner = new Scanner(System.in);
        try {
            while (true) {
                String str = scanner.nextLine();
                Message tmp = new Message(MessageType.MESSAGE, args[1], str);
                objectOutputStream.writeObject(tmp);
                objectOutputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Close socket after done with it
        socket.close();
    }

}
