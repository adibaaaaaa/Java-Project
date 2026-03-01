/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Login;

import Database.DBConnectionUser;
import User.EmergencyPageController;
import User.UserRequests;
import java.sql.Connection;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



/**
 * FXML Controller class
 *
 * @author Wellcome
 */
public class UserLoginController {

    @FXML
    private TextField username;

    @FXML
    private Label ErrorLabel;
    
    private UserRequests currentUser;
    
    private String labelname;

    @FXML
    public void initialize() {
        // Clear error while typing
        username.textProperty().addListener((obs, oldText, newText) -> ErrorLabel.setText(""));

        // Validate on focus lost
        username.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                checkUsernameInDatabase(username.getText().trim());
            }
        });
    }

    private void checkUsernameInDatabase(String enteredUsername) {
        if (enteredUsername.isEmpty()) {
            ErrorLabel.setText("Username cannot be empty");
            return;
        }

        String query = "SELECT * FROM user_info WHERE username = ?";
        DBConnectionUser dbUser = new DBConnectionUser();

        try (Connection con = dbUser.connect();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, enteredUsername);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                labelname=rs.getString("name");
                ErrorLabel.setText("");
                
                int userId = rs.getInt("id");   // get correct id from database
                String usernameFromDB = rs.getString("username");

                currentUser = new UserRequests(userId, usernameFromDB);
            } else {
                ErrorLabel.setText("Invalid username");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            ErrorLabel.setText("Database error");
        }
    }

    @FXML
    public void LoadEmergencyPage(ActionEvent event) throws IOException {
        // Only load page if username is valid
        if (!ErrorLabel.getText().isEmpty()) return;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/EmergencyPage.fxml"));
        Parent root = loader.load();

        // get controller of EmergencyPage
        EmergencyPageController controller = loader.getController();

        // pass logged in user
        controller.setUser(currentUser);
        controller.setlabel(labelname);
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    public void GoBack(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("roleSelection.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();

    }
}