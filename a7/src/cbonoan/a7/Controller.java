package cbonoan.a7;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import jforsythe.Message;
import jforsythe.MessageType;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

public class Controller {
    @FXML
    TextField txtInput;

    @FXML
    TextArea txtOutput;

    @FXML
    TextArea txtMembers;

    private String name;
    private Socket socket;
    private OutputStream outputStream;
    private ObjectOutputStream objectOutputStream;

    public void initialize() throws IOException {
        TextInputDialog nameInput = new TextInputDialog("What is your name?");
        nameInput.setHeaderText("Welcome to the CMPS3390 Chat Room!");

        nameInput.showAndWait(); // Blocks use of application until user enters name

        name = nameInput.getResult();

        socket = new Socket("odin.cs.csub.edu", 3390);
        outputStream = socket.getOutputStream();
        outputStream.flush();
        objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.flush();

        ServerListener serverListener = new ServerListener(this.socket, this);
        serverListener.start();

        Message tmp = new Message(MessageType.CONNECT, name, "Hi " + name + "!");
        objectOutputStream.writeObject(tmp);
        objectOutputStream.flush();
    }

    public void onInputAction(ActionEvent actionEvent) throws IOException {
        Message tmp = new Message(MessageType.MESSAGE, name, txtInput.getText());
        txtInput.clear();

        objectOutputStream.writeObject(tmp);
        objectOutputStream.flush();
    }

    public void addMessage(String msg) {
        txtOutput.appendText(msg);
    }

    public void addUser(String user) {
        txtMembers.appendText(user);
    }

    public void refreshMemberList(List<String> usernames) {
        // Loop through user list and if user is not in mem
    }

    public void exit() throws IOException {
        objectOutputStream.close();
        outputStream.close();
        socket.close();
    }



}
