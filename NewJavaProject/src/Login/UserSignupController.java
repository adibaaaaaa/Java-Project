package Login;

import Database.DBConnectionUser;
import Database.InsertUserData;
import ExceptionHandling.InvalidEmailException;
import ExceptionHandling.InvalidNIDException;
import ExceptionHandling.InvalidPhoneException;
import ExceptionHandling.ValidationException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import SignUpInfo.UserSignupInfo;
import User.EmergencyPageController;
import User.UserRequests;
import java.sql.*;
import java.awt.Toolkit;
import javafx.scene.control.Alert;

public class UserSignupController implements Initializable {

    private Stage stage;
    private Scene scene;

    @FXML
    private ComboBox<String> Addressbox;

    ObservableList<String> list = FXCollections.observableArrayList("Barishal", "Bogra", "Brahmanbaria", "Chandpur", "Chattogram", "Comilla", "Cox's Bazar", "Dhaka", "Dinajpur", "Faridpur", "Feni", "Gazipur", "Gopalganj", "Habiganj", "Jamalpur", "Jessore", "Jhalokathi", "Jhenaidah", "Joypurhat", "Khagrachhari", "Khulna", "Kishoreganj", "Kurigram", "Kushtia", "Lakshmipur", "Madaripur", "Magura", "Manikganj", "Meherpur", "Moulvibazar", "Munshiganj", "Mymensingh", "Naogaon", "Narail", "Narsingdi", "Natore", "Netrokona", "Nilphamari", "Noakhali", "Pabna", "Panchagarh", "Patuakhali", "Pirojpur", "Rajbari", "Rajshahi", "Rangamati", "Rangpur", "Satkhira", "Shariatpur", "Sherpur", "Sirajganj", "Sunamganj", "Sylhet", "Tangail", "Thakurgaon");

    @FXML
    private TextField UserName;
    @FXML
    private TextField Name;
    @FXML
    private TextField Gender;
    @FXML
    private TextField Age;
    @FXML
    private TextField PhoneNum;
    @FXML
    private TextField Nid;
    @FXML
    private TextField Email;
    @FXML
    private TextField EmgContact;
    @FXML
    private TextField Blood;
    @FXML
    private TextField Allergy;

    private String Address;

    public void LoginPageLink(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Login/UserLogin.fxml"));
        scene = new Scene(root);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }

    public void EmergencyPageLink(ActionEvent event) throws IOException {

        String userName = UserName.getText();
        String name = Name.getText();
        String gender = Gender.getText();
        String age = Age.getText();
        String phnNum = PhoneNum.getText();
        String nid = Nid.getText();
        String email = Email.getText();
        String emgContact = EmgContact.getText();
        String blood = Blood.getText();
        String allergy = Allergy.getText();

        if (userName.isEmpty() || name.isEmpty() || gender.isEmpty()
                || age.isEmpty() || phnNum.isEmpty() || nid.isEmpty()
                || email.isEmpty() || emgContact.isEmpty()
                || blood.isEmpty() || allergy.isEmpty()) {

            showError("Incomplete Form", "Missing Information",
                    "Please fill all fields before signing up.");
            return;
        }

        try {
            validateUserInfo(email, phnNum, nid);
        } catch (ValidationException e) {
            Toolkit.getDefaultToolkit().beep();
            showError("Validation Error", "Invalid Input", e.getMessage());
            return;
        }

        int userId = CollectInfo(userName, name, gender, age,
                phnNum, nid, email, emgContact, blood, allergy);

        if (userId == -1) {
            return; // signup failed
        }

        // LOAD PAGE PROPERLY (like login controller)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/EmergencyPage.fxml"));
        Parent root = loader.load();

        EmergencyPageController controller = loader.getController();

        // Create UserRequests object
        UserRequests newUser = new UserRequests(userId, userName);

        // Pass user to emergency page
        controller.setUser(newUser);
        controller.setlabel(name);

        scene = new Scene(root);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private void showError(String message1, String message2, String message3) {
        Toolkit.getDefaultToolkit().beep();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(message1);
        alert.setHeaderText(message2);
        alert.setContentText(message3);
        alert.showAndWait();

    }

    public void initialize(URL location, ResourceBundle resources) {
        Addressbox.setItems(list);

        UserName.textProperty().addListener((obs, oldText, newText) -> clearStyles());
        Email.textProperty().addListener((obs, oldText, newText) -> clearStyles());
        PhoneNum.textProperty().addListener((obs, oldText, newText) -> clearStyles());
        Nid.textProperty().addListener((obs, oldText, newText) -> clearStyles());

    }

    public void AddressInfo(ActionEvent event) {
        Address = Addressbox.getValue();
    }

    public int CollectInfo(String userName, String name, String gender, String age,
            String phnNum, String nid, String email,
            String emgContact, String blood, String allergy) {

        UserSignupInfo usi = new UserSignupInfo(userName, name, gender, age,
                phnNum, nid, email, emgContact, Address, blood, allergy);

        Connection con = new DBConnectionUser().connect();
        InsertUserData Insert = new InsertUserData(con, usi);

        String success = Insert.insert();

        if (success.equals("SUCCESS")) {

            // Get ID of newly inserted user
            String query = "SELECT id FROM user_info WHERE username = ?";

            try (PreparedStatement pst = con.prepareStatement(query)) {
                pst.setString(1, userName);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    return rs.getInt("id");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            handleDuplicate(success);
        }

        return -1; // failed
    }

    private void handleDuplicate(String message) {

        clearStyles(); // reset previous red borders

        if (message.contains("username")) {
            markError(UserName);
        } else if (message.contains("email")) {
            markError(Email);
        } else if (message.contains("phone_num")) {
            markError(PhoneNum);
        } else if (message.contains("nid")) {
            markError(Nid);
        }

        showError("Duplicate Entry",
                "Already Exists",
                "This information already exists. Please use different value.");
    }

    private void markError(TextField field) {
        field.setStyle("-fx-border-color: red; -fx-border-width: 2;");
    }

    private void clearStyles() {
        UserName.setStyle("-fx-background-color: transparent; -fx-border-color: FDFAF6; -fx-border-width: 0px 0px 2px 0px; -fx-text-fill:white;");
        Email.setStyle("-fx-background-color: transparent; -fx-border-color: FDFAF6; -fx-border-width: 0px 0px 2px 0px; -fx-text-fill:white;");
        PhoneNum.setStyle("-fx-background-color: transparent; -fx-border-color: FDFAF6; -fx-border-width: 0px 0px 2px 0px; -fx-text-fill:white;");
        Nid.setStyle("-fx-background-color: transparent; -fx-border-color: FDFAF6; -fx-border-width: 0px 0px 2px 0px; -fx-text-fill:white;");
    }

    private void validateUserInfo(String email, String phnNum, String nid) throws ValidationException {

        // Phone number validation: must be 11 digits
        if (!phnNum.matches("^01[0-9]{9}$")) {
            throw new InvalidPhoneException("Phone number must start with 01 and be 11 digits!");
        }
        // Email validation using simple regex
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new InvalidEmailException("Invalid email format!");
        }

        // NID validation: must be 10-17 digits
        if (!nid.matches("\\d{10,17}")) {
            throw new InvalidNIDException("NID must be 10-17 digits!");
        }

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
