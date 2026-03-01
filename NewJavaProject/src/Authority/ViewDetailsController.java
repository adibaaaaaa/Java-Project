/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Authority;

import Database.DBConnectionUser;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;


/**
 * FXML Controller class
 *
 * @author Nazifah
 */
public class ViewDetailsController  {

    
    @FXML
    private Label nameLabel;
    
    @FXML
    private Label ageLabel;
    
    @FXML
    private Label genderLabel;
    
    @FXML
    private Label phoneLabel;
    
    @FXML
    private Label EmgCLabel;
    
    @FXML
    private Label bloodLabel;
    
    @FXML
    private Label allergyLabel;
    
    @FXML
    private Button closeButton;
    
    public void loadUserData(int userId) {

    try {
        DBConnectionUser db = new DBConnectionUser();
        Connection con = db.connect();

        String query = "SELECT * FROM user_info WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, userId);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {

            // Example labels — change according to your FXML
            nameLabel.setText(rs.getString("name"));
            phoneLabel.setText(rs.getString("phone_num"));
            ageLabel.setText(rs.getString("age"));
            genderLabel.setText(rs.getString("gender"));
            EmgCLabel.setText(rs.getString("emergency_contact"));
            bloodLabel.setText(rs.getString("blood_group"));
            allergyLabel.setText(rs.getString("allergy"));
            
            
            
        }

        con.close();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
    
    public void handleClose(ActionEvent event)
    {
         Stage stage = (Stage) closeButton.getScene().getWindow();
         
         stage.close();
    }
    
    
}