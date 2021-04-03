package cbonoan.server;

import cbonoan.Message;
import cbonoan.MessageType;

import java.io.*;
import java.net.Socket;

public class ClientListener implements Runnable {
    private final Socket socket;
    private InputStream inputStream;
    private ObjectInputStream objectInputStream;
    private OutputStream outputStream;
    private ObjectOutputStream objectOutputStream;
    private String name;

    public ClientListener(Socket socket) {
        this.socket = socket;

        try {
            this.outputStream = this.socket.getOutputStream();
            this.outputStream.flush();
            this.objectOutputStream = new ObjectOutputStream(outputStream);
            this.objectOutputStream.flush();

            this.inputStream = this.socket.getInputStream();
            this.objectInputStream = new ObjectInputStream(inputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        try {
            while(true) {
                Message tmpMsg = (Message) this.objectInputStream.readObject();
                switch (tmpMsg.getType()) {
                    case CONNECT:
                        Server.broadcast(new Message(MessageType.CONNECT, tmpMsg.getName()
                        , tmpMsg.getName() + " has joined the server!"));
                        this.setName(tmpMsg.getName());
                        break;
                    case MESSAGE:
                        Server.broadcast(tmpMsg);
                        break;
                    case EXIT:
                        break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Client Disconnected");
        } finally {
            try {
                // Close in this order
                objectOutputStream.close();
                objectInputStream.close();
                outputStream.close();
                inputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessageToClient(Message m) throws IOException {
        this.objectOutputStream.writeObject(m);
    }
}
