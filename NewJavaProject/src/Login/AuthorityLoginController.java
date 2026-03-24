/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Login;

import Authority.AuthorityDashboardController;
import Database.DBConnectionAuthority;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Nazifah
 */
public class AuthorityLoginController {

    @FXML
    private Label UsernameError;

    @FXML
    private Label PasswordError;

    @FXML
    private TextField username;

    @FXML
    private TextField password;
    @FXML
    private void hoverEnter(MouseEvent event) {
        Button btn = (Button) event.getSource();
        btn.setStyle("-fx-background-color: rgba(255,255,255,0.7); -fx-background-radius: 15; -fx-border-color: white; -fx-border-radius: 15;");
    }

    @FXML
    private void hoverExit(MouseEvent event) {
        Button btn = (Button) event.getSource();
        btn.setStyle("-fx-background-color: rgba(255,255,255,0.3); -fx-background-radius: 15; -fx-border-color: white; -fx-border-radius: 15;");
    }

    public void loadDashboard(ActionEvent event, String institutionName) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Authority/AuthorityDashboard.fxml"));
        Parent root = loader.load();

        // Get controller of dashboard
        AuthorityDashboardController controller = loader.getController();

        // Send institution name to dashboard
        controller.setInstituteName(institutionName);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void GoBack(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("AuthoritySignup.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();

    }

    @FXML
    public void CollectInfo(ActionEvent event) throws IOException {

        String userName = username.getText();
        String passWord = password.getText();

        checkLogin(userName, passWord, event);
    }

    private void checkLogin(String enteredUsername, String enteredPassword, ActionEvent event) throws IOException {

        // Empty checks first
        if (enteredUsername.isEmpty()) {
            UsernameError.setText("Username cannot be empty");
            return;
        } else {
            UsernameError.setText("");
        }

        if (enteredPassword.isEmpty()) {
            PasswordError.setText("Password cannot be empty");
            return;
        } else {
            PasswordError.setText("");
        }

        String query = "SELECT * FROM authority_info WHERE username = ? AND pass = ?";
        DBConnectionAuthority dbA = new DBConnectionAuthority();

        try (Connection con = dbA.connect(); PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, enteredUsername);
            pst.setString(2, enteredPassword);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                // LOGIN SUCCESS
                String insName = rs.getString("inst_name");
                UsernameError.setText("");
                PasswordError.setText("");

                System.out.println("Login Successful!");
                loadDashboard(event, insName);

                // Load next scene here
            } else {
                // LOGIN FAILED
                UsernameError.setText("Invalid username or password");
                PasswordError.setText("Invalid username or password");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            PasswordError.setText("Database error");
        }
    }
}
