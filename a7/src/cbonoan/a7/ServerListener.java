package cbonoan.a7;

import jforsythe.Message;
import jforsythe.MessageType;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerListener extends Thread{
    private List<String> usernames = new ArrayList<>(); // Will hold usernames and will be used to refresh Members list
    private Socket socket;
    private InputStream inputStream;
    private ObjectInputStream objectInputStream;
    Controller controller;

    public ServerListener(Socket socket, Controller controller) throws IOException {
        this.socket = socket;
        this.controller = controller;
        inputStream = socket.getInputStream();
        objectInputStream = new ObjectInputStream(inputStream);
    }

    @Override
    public void run() {
        try {
            while(true) {
                Message tmp = (Message) objectInputStream.readObject();
                MessageType msgType = tmp.getType();
                usernames.add(tmp.getName());
                switch(msgType) {
                    case CONNECT:
                        controller.addUser(String.format("%s\n", tmp.getName()));
                        break;
                    case MESSAGE:
                        controller.addMessage(String.format("%s: %s\n", tmp.getName(), tmp.getMessage()));
                        break;
                }
            }
        } catch (ClassNotFoundException | IOException e) {
            System.err.println("Disconnected from server");
        } finally {
            try {
                objectInputStream.close();
                inputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
