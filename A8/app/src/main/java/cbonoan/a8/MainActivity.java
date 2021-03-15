package cbonoan.a8;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import jforsythe.Message;
import jforsythe.MessageType;

/**
 * Main driver class for a8
 * Purpose: Integrate chat app created in a7 into android device
 * @author Charles Bonoan
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity implements TextView.OnEditorActionListener {
    private EditText txtInput;
    private EditText txtOutput;
    private String name;
    private Socket socket;
    private OutputStream outputStream;
    private ObjectOutputStream objectOutputStream;
    private ServerListener serverListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Need in order to connect to chat server
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);
        this.txtInput = findViewById(R.id.txtInput);
        this.txtInput.setOnEditorActionListener(this);
        this.txtOutput = findViewById(R.id.txtOutput);

        getUserName();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        serverListener.running = false;
        try {
            objectOutputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connect() {
        try {
            socket = new Socket("odin.cs.csub.edu", 3390);
            outputStream = socket.getOutputStream();
            outputStream.flush();
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.flush();

            //Start server listener
            serverListener = new ServerListener(socket, txtOutput);
            serverListener.start();

            Message connect = new Message(MessageType.CONNECT, name, "Hi from Android");
            objectOutputStream.writeObject(connect);
            objectOutputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getUserName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("User Name");
        EditText userNameInput = new EditText(this);
        userNameInput.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(userNameInput);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                name = userNameInput.getText().toString();
                Log.d("USER NAME", name);
                //If user does not enter a name, run function again
                if(name.equals("")) getUserName();
                else connect();
            }
        });

        builder.show();
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        /* Append text in txtInput field into txtOutput field
           then clear out the txtInput field
        */
        if(event.getAction() == KeyEvent.ACTION_UP) {
            Message tmp = new Message(MessageType.MESSAGE, name, txtInput.getText().toString());
            try {
                objectOutputStream.writeObject(tmp);
                objectOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            txtInput.setText("");

            // Minimize keyboard after message is sent
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        return true;
    }
}