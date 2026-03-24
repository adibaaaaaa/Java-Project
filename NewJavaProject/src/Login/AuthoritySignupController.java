
package Login;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import SignUpInfo.AuthoritySignupInfo;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Authority.*;
import Database.DBConnectionAuthority;
import Database.InsertAuthorityData;
import java.sql.Connection;
import java.awt.Toolkit;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class AuthoritySignupController implements Initializable {
    
    @FXML
    private ComboBox<String> InstitutionBox;
    @FXML
    private ComboBox<String> AddressBox;
    @FXML
    private TextField InstitutionName;
    @FXML
    private TextField Contact;
    @FXML
    private TextField Email;
    @FXML
    private TextField Address; 
    @FXML
    private TextField Username;
    @FXML
    private TextField Password;
    
    //This is for dropdown box selection store
    private String InstitutionType;
    private String Area;
    
    private Stage stage;
    
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
    
            
    
    ObservableList<String> list_inst= FXCollections.observableArrayList("Hospital","Police Station","Fire Service");
    ObservableList<String> list_address= FXCollections.observableArrayList("Barishal", "Bogra", "Brahmanbaria", "Chandpur", "Chattogram", "Comilla", "Cox's Bazar", "Dhaka", "Dinajpur", "Faridpur", "Feni", "Gazipur", "Gopalganj", "Habiganj", "Jamalpur", "Jessore", "Jhalokathi", "Jhenaidah", "Joypurhat", "Khagrachhari", "Khulna", "Kishoreganj", "Kurigram", "Kushtia", "Lakshmipur", "Madaripur", "Magura", "Manikganj", "Meherpur", "Moulvibazar", "Munshiganj", "Mymensingh", "Naogaon", "Narail", "Narsingdi", "Natore", "Netrokona", "Nilphamari", "Noakhali", "Pabna", "Panchagarh", "Patuakhali", "Pirojpur", "Rajbari", "Rajshahi", "Rangamati", "Rangpur", "Satkhira", "Shariatpur", "Sherpur", "Sirajganj", "Sunamganj", "Sylhet", "Tangail", "Thakurgaon");
    
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        InstitutionBox.setItems(list_inst);
        AddressBox.setItems(list_address);
        Username.textProperty().addListener((obs, oldText, newText) -> clearStyles());
        Email.textProperty().addListener((obs, oldText, newText) -> clearStyles());
       Contact.textProperty().addListener((obs, oldText, newText) -> clearStyles());
    }
    
    public void ComboBoxInfo(ActionEvent event)
    {
        InstitutionType=InstitutionBox.getValue();
        
        Area=AddressBox.getValue();
        
        
    }
    
    public void LoginPageLink(ActionEvent event)throws IOException
    {
         Parent root=FXMLLoader.load(getClass().getResource("AuthorityLogin.fxml"));
         Scene scene=new Scene(root);
         stage=(Stage)((Node)event.getSource()).getScene().getWindow();
         stage.setScene(scene);
         stage.show();
    }
    
    public boolean CollectInfo(String InstName, String contact, String email, String address,String username, String pass)
    {
        AuthoritySignupInfo asi= new AuthoritySignupInfo(InstitutionType,InstName,contact,email,address,Area,username,pass);
        
        Connection con= new DBConnectionAuthority().connect();
        
        InsertAuthorityData Insert= new InsertAuthorityData(con,asi);
        
        String success=Insert.insert();
        if(success.equals("SUCCESS"))
        {
            return true;
        }
        else
        {
            handleDuplicate(success);
             return false;
        }
        
    }
    
    private void markError(TextField field) {
    field.setStyle("-fx-border-color: red; -fx-border-width: 2;");
    }
    
    private void clearStyles() {
    Username.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); -fx-border-color: #f3f3eb; -fx-border-width: 1; -fx-text-fill: white; -fx-background-radius: 15; -fx-border-radius: 15;");
    Email.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); -fx-border-color: #f3f3eb; -fx-border-width: 1; -fx-text-fill: white; -fx-background-radius: 15; -fx-border-radius: 15;");
    Contact.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); -fx-border-color: #f3f3eb; -fx-border-width: 1; -fx-text-fill: white; -fx-background-radius: 15; -fx-border-radius: 15;");
    
    }
    
     private void handleDuplicate(String message) {

    clearStyles(); // reset previous red borders

    if (message.contains("username")) {
        markError(Username);
    }
    else if (message.contains("email")) {
        markError(Email);
    }
    else if (message.contains("contact")) {
        markError(Contact);
    }
    

    showError("Duplicate Entry",
            "Already Exists",
            "This information already exists. Please use different value.");
    }
    
    private void showError(String message1,String message2,String message3)
    {
         Toolkit.getDefaultToolkit().beep();
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(message1);
        alert.setHeaderText(message2);
        alert.setContentText(message3);
        alert.showAndWait();
        
    }
    
    private boolean isValidEmail(String email) {
    String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    return email.matches(emailRegex);
    }
    
    private boolean isValidPhone(String phone) {
    String phoneRegex = "^01[0-9]{9}$";
    return phone.matches(phoneRegex);
    }


    
    public void LoadAuthorityDashboard(ActionEvent event)throws IOException
    {
        String name=InstitutionName.getText();
        String contact=Contact.getText();
        String email=Email.getText();
        String address=Address.getText();
        String username=Username.getText();
        String pass=Password.getText();
        
         if (name.isEmpty() || contact.isEmpty() || email.isEmpty() ||
        address.isEmpty() || username.isEmpty() || pass.isEmpty() ||
        InstitutionType == null || Area == null)
        {
          showError("Incomplete Form","Missing Information","Please fill all fields before signing up.");
          return;
        }
         
         if(!isValidEmail(email))
         {
            showError("Invalid Email","Incorrect format","Please write correct email address");
            return;
         }
         
         if(!isValidPhone(contact))
         {
             showError("Incorrect Phone number","Incorrect area code or digit count","Please write accurate phone number");
             return;
         }
         
         
        
         
       boolean unique= CollectInfo(name,contact,email,address,username,pass);
       if(!unique)
       {
           
           return;
       }
        
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/Authority/AuthorityDashboard.fxml"));
        Parent root=loader.load();
        
        AuthorityDashboardController Controller= loader.getController();
        Controller.setInstituteName(name);
        
         Scene scene=new Scene(root);
         stage=(Stage)((Node)event.getSource()).getScene().getWindow();
         stage.setScene(scene);
         stage.show();
        
        
        
        
        
    }
    
    @FXML
    public void GoBack(ActionEvent event) throws IOException{
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("roleSelection.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        
    }   
    
}
