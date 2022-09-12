package Client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;


public class Room extends Thread implements Initializable {
    
    //chat room
    @FXML
    public Label clientName;
    @FXML
    public Pane chat;
    @FXML
    public TextField msgField;
    @FXML
    public TextArea msgRoom;

    @FXML
    private ImageView online_users;
    
    //profile pane
    @FXML
    private Pane profile;
    @FXML
    private ImageView edit_icon;
    @FXML
    private ImageView proImage;
    @FXML
    private TextField eidt_fname;
    @FXML
    private TextField edit_phone;
    @FXML
    private TextField edit_email;
    @FXML
    private Button saveBtn;


    public boolean toggleChat = false, toggleProfile = false;

    BufferedReader reader;
    PrintWriter writer;
    Socket socket;
    public void connectSocket() {
        try {
            socket = new Socket("localhost", 8889);
            System.out.println("Socket is connected with server!");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            this.start();
        } catch (IOException e) {
            System.out.println("Exception in connectSocket method");
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                String msg = reader.readLine();
                if(msg.equalsIgnoreCase("bye")) {
                    break;
                }
                msgRoom.appendText(msg + "\n");
            }
            reader.close();
            writer.close();
            socket.close();
        } catch (Exception e) {
        }
    }
    
    @FXML
    public void handleSendEvent(MouseEvent event) throws Exception {
        send();
        String sql = "SELECT userName FROM users";
        ResultSet rs = DataBase.read_from_db(sql);
        while(rs.next())
        {
            System.out.println(rs.getString(2));
        }
    }

    public void send() {
        String msg = msgField.getText();
        writer.println(Controller.username + ": " + msg);
        msgRoom.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        msgRoom.appendText("Me: " + msg + "\n");
        msgField.setText("");
        if(msg.equalsIgnoreCase("BYE") || (msg.equalsIgnoreCase("logout"))) {
            System.exit(0);
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clientName.setText(Controller.username);
        connectSocket();
    }

    @FXML
    private void SaveHandler(ActionEvent event) throws Exception {
        String sql = "UPDATE users SET userName = '"+eidt_fname.getText()+"' "
                    + "AND email = '"+edit_email.getText()+"' "
                    + "AND phoneNum = '"+edit_phone.getText()+"' "
                    + "WHERE userName = '"+Controller.username+"'";
        int rowAffected = DataBase.update_db(sql);
        System.out.println(rowAffected);
        msgRoom.toFront();
        profile.toBack();
    }

    @FXML
    private void editHandler(MouseEvent event) throws Exception {
        profile.toFront();
        msgRoom.toBack();
        String sql = "SELECT userName, email,  phoneNum FROM users WHERE userName= '"+Controller.username+"'";
        ResultSet rs = DataBase.read_from_db(sql);
        eidt_fname.setText(rs.getString("userName"));
        edit_email.setText(rs.getString("email"));
        edit_phone.setText(rs.getString("phoneNum"));
    }
}