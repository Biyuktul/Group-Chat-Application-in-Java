package Client;

import Client.User;
import Server.Server;
import Server.ClientHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ToggleGroup;



public class Controller {
    //signup pane
    @FXML
    public Pane pnSignUp;
    @FXML
    public TextField regName;
    @FXML
    public TextField regPass;
    @FXML
    public TextField regEmail;
    @FXML
    public TextField regFirstName;
    @FXML
    public TextField regPhoneNo;
    @FXML
    public RadioButton male;
    @FXML
    public RadioButton female;
    @FXML
    public Label controlRegLabel;
    @FXML
    private ToggleGroup Gender;
    @FXML
    public Label nameExists;
    @FXML
    public Label checkEmail;
    @FXML
    private Label success;
    
    //signin pane
    public Pane pnSignIn;   
    @FXML
    public TextField userName;
    @FXML
    public TextField passWord;
    @FXML
    public Label loginNotifier;
    @FXML
    private Button btnsignup;

    
    
    public static String username, password, gender;
    public static ArrayList<String> loggedInUser = new ArrayList<>();
    @FXML
    private Button btnSignin;
    @FXML
    private Pane pnSignIn1;
    @FXML
    private TextField userName1;
    @FXML
    private PasswordField passWord1;
    @FXML
    private Button btnsignup1;
    @FXML
    private Label loginNotifier1;
    
    
    

    @FXML
    public void registration() throws Exception {
        if (!regName.getText().equalsIgnoreCase("")
                && !regPass.getText().equalsIgnoreCase("")
                && !regEmail.getText().equalsIgnoreCase("")
                && !regFirstName.getText().equalsIgnoreCase("")
                && !regPhoneNo.getText().equalsIgnoreCase("")
                && (male.isSelected() || female.isSelected())) {
            if(checkUser(regName.getText())) {
                if(checkEmail(regEmail.getText())) {
                    if (male.isSelected()) {
                        gender = "Male";
                    } else {
                        gender = "Female";
                    }
                    String sql = "INSERT INTO users(fullName,userName,password,email,phoneNum,gender)" + 
                            "VALUES('"+regFirstName.getText()+"','"+regName.getText()+"','"+regPass.getText()+"','"+regEmail.getText()+"','"+regPhoneNo.getText()+"','"+gender+"')";
                    DataBase.write_to_db(sql);
                    success.setOpacity(1);
                } else {
                    checkEmail.setOpacity(1);
                }
            } else {
                nameExists.setOpacity(1);
            }
        } else {
            controlRegLabel.setOpacity(1);
        }
    }

    /*
    * ccheckUser - function to check username existence.
    * @username: username tobe checked.
    * returns false if username exist or true if not exist. 
    */
    private boolean checkUser(String uname) throws Exception {
        String sql = "SELECT userName FROM users";
        ResultSet rs = DataBase.read_from_db(sql);
        while(rs.next())
        {
            if(rs.getString("userName").equalsIgnoreCase(uname)) {
                return false;
            }
        }
        return true;
    }

    /*
    * checkEmail - function to check email existence.
    * @email: the email tobe checked.
    * returns fasle if email alredy exist or true if not exist.
    */
    private boolean checkEmail(String email) throws Exception {
        String sql = "SELECT email FROM users";
        ResultSet rs = DataBase.read_from_db(sql);
        while(rs.next())
        {
            if(rs.getString("email").equalsIgnoreCase(email)) {
                return false;
            }
        }
        return true;
    }
    

    

    @FXML
    public void login() throws Exception {
        username = userName.getText();
        password = passWord.getText();
        boolean login = false;
        String sql = "SELECT userName, password FROM users WHERE userName = '"+username+"' AND password = '"+password+"'";
        ResultSet rs = DataBase.read_from_db(sql);
        while(rs.next())
        {
            if (rs.getString("userName").equals(username) && rs.getString("password").equals(password))
            {
                login = true;
                loggedInUser.add(username);
                System.out.println(username);
                break;
            }
        }
        if (login) {
            changeWindow();
        } else {
            loginNotifier.setOpacity(1);
        }
    }

    /*
    * changeWindow - function to change the window to chat room
    */
    public void changeWindow() {
        try {
            Stage stage = (Stage) userName.getScene().getWindow();
            Parent root = FXMLLoader.load(this.getClass().getResource("Room.fxml"));
            stage.setScene(new Scene(root, 330, 560));
            stage.setTitle(username + "");
            stage.setOnCloseRequest(event -> {
                System.exit(0);
            });
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void signupBtnHandler(ActionEvent event) {
        pnSignIn1.setVisible(false);
    }

    @FXML
    private void backToSignin(ActionEvent event) {
        pnSignUp.setVisible(false);
    }

}