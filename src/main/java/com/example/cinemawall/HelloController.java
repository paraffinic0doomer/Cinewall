package com.example.cinemawall;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class HelloController {

    public TextField verificationCodeField;
    public Button next;
    public Button verifyButton;
    public Button loginbtn;
    public Hyperlink forgotPass;
    public Button nextButton;
    public TextField verificationField;
    public PasswordField newPasswordField;
    public PasswordField confirmNewPasswordField;
    public Button resetPasswordButton;
    public Button buttonlogIn;
    @FXML
    private TextField fullnameField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button buttonSignUp;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/cinema";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";

    public int verificationCode;
    String fullname;
    String username;
    String email;
    String password;
    String confirmPassword;
    HomeController homeController=new HomeController();


    @FXML
    public void sendIDto(String email) {
        String userID = null;

        try (Connection connection = createConnection()) {
            String query = "SELECT username FROM login WHERE email = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, email);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        userID = rs.getString("username"); // Retrieve the username
                    }
                }
            }

            if (userID != null) {
                // Pass userID to the HomeController
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cinemawall/HomePage.fxml"));// Replace "HomeView.fxml" with your actual FXML file name
                Parent root = loader.load();

                HomeController homeController = loader.getController();
                homeController.setUserID(userID); // Pass the userID to the controller

                // Load the new scene
                Stage stage = (Stage) buttonlogIn.getScene().getWindow(); // Replace 'yourCurrentWindow' with the actual reference
                stage.setScene(new Scene(root));
                stage.centerOnScreen();
                stage.setFullScreen(true);

                homeController.loadHomepage();
                stage.show();


            } else {
                showAlert("Error", "No account found with this email!", Alert.AlertType.ERROR);
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            showAlert("Error", "Database connection failed. Please try again.", Alert.AlertType.ERROR);
        }
    }



    public void handleSignup(ActionEvent actionEvent) {
         fullname = fullnameField.getText().trim();
         username = usernameField.getText().trim();
         email = emailField.getText().trim();
        password = passwordField.getText();
        confirmPassword = confirmPasswordField.getText();

        if (!validateInputs(fullname, username, email, password, confirmPassword)) {
            return;
        }

        try (Connection connection = createConnection()) {
            if (isUserExists(connection, email, username)) {
                showAlert("Error", "Username or email already exists!", Alert.AlertType.ERROR);
                return;
            }

            String hashedPassword = hashPassword(password);

            if (!isUserExists(connection, email, username)&&validateInputs(fullname, username, email, password, confirmPassword)) {
                verificationCode = generateVerificationCode();
                sendVerificationCode(email, verificationCode);
                navigateToVerificationPage();
            } else {
                showAlert("Error", "An error occurred. Please try again.", Alert.AlertType.ERROR);
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            showAlert("Error", "Database connection failed. Please try again.", Alert.AlertType.ERROR);
        }
    }

    private boolean validateInputs(String fullname, String username, String email, String password, String confirmPassword) {
        if (fullname.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "All fields are required!", Alert.AlertType.ERROR);
            return false;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Error", "Passwords do not match!", Alert.AlertType.ERROR);
            return false;
        }

        if (password.length() < 6) {
            showAlert("Error", "Password must be at least 6 characters long!", Alert.AlertType.ERROR);
            return false;
        }

        if (!isValidEmail(email)) {
            showAlert("Error", "Invalid email format!", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    public boolean isUserExists(Connection connection, String email, String username) throws SQLException {
        String query = "SELECT COUNT(*) FROM login WHERE email = ? OR username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, username);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public boolean insertUser(Connection connection, String fullname, String email, String username, String password) throws SQLException {
        String query = "INSERT INTO login (fullname, email, username, password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, fullname);
            stmt.setString(2, email);
            stmt.setString(3, username);
            stmt.setString(4, password);

            return stmt.executeUpdate() > 0;
        }
    }

    private Random random = new Random();  // Declare Random once

    public int generateVerificationCode() {
        int code = random.nextInt(900000) + 100000; // 6-digit code
        return code;
    }


    public void sendVerificationCode(String email, int code) {
        final String senderEmail = "studyhardufkinmoron@gmail.com"; // Your email
        final String senderPassword = "ztyz xdfw bohg qfmo"; // Your email password

        // Set up email properties for SMTP
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Create a session and authenticate
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Create a new MimeMessage (javax.mail)
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail)); // Set the sender's email
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email)); // Set the recipient's email
            message.setSubject("Your Verification Code"); // Set subject
            message.setText("Your verification code is: " + code); // Set the body of the message

            // Send the message
            Transport.send(message); // Send the email using the session
            showAlert("Success", "A verification code has been sent to your email.", Alert.AlertType.INFORMATION);

        } catch (MessagingException e) {
            e.printStackTrace(); // Log the exception
            showAlert("Error", "Failed to send verification email. Please try again.", Alert.AlertType.ERROR);
        }
    }

    public void navigateToVerificationPage() throws IOException {
        // Set the generated verification code in the current controller instance
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cinemawall/verification_signup.fxml"));
        Parent root = loader.load();

        // Get the controller for the verification page
        HelloController verificationController = loader.getController();

        // Pass the verification code to the verification page (same controller)
        verificationController.setVerificationCode(this.verificationCode);
        verificationController.setFullnameField(this.fullname);
        verificationController.setUsernameField(this.username);
        verificationController.setEmailField(this.email);
        verificationController.setPasswordField(this.password);


        // Transition to the verification page
        Stage stage = (Stage) next.getScene().getWindow();
        Scene verificationScene = new Scene(root);
        stage.setScene(verificationScene);
        stage.centerOnScreen();
        stage.show();
    }

    private void setVerificationCode(int verificationCode) {
        this.verificationCode = verificationCode;
    }
    public void setFullnameField(String fullname) {
        this.fullname=fullname;
    }
    public void setUsernameField(String username) {
        this.username=username;
    }
    public void setEmailField(String email) {
        this.email=email;
    }
    public void setPasswordField(String password) {
        this.password=password;
    }

    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@[\\w-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public Connection createConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Unchanged login function
    public void handleLogin(ActionEvent actionEvent) {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Both fields are required!", Alert.AlertType.ERROR);
            return;
        }

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT id, password FROM login WHERE email = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, email);

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    if (storedPassword.equals(password)) {
                        showAlert("Success", "Login successful!", Alert.AlertType.INFORMATION);
                        sendIDto(email);
                    } else {
                        showAlert("Error", "Incorrect password!", Alert.AlertType.ERROR);
                    }
                } else {
                    showAlert("Error", "No account found with this email!", Alert.AlertType.ERROR);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database connection failed. Please try again.", Alert.AlertType.ERROR);
        }
    }

    public void loginpage(ActionEvent actionEvent) throws IOException {
        Parent root=FXMLLoader.load(getClass().getResource("/com/example/cinemawall/hello-view.fxml"));
        Stage stage = (Stage) loginbtn.getScene().getWindow();
        Scene loginScene = new Scene(root);
        stage.setScene(loginScene);
        stage.centerOnScreen();
        stage.show();
    }


    public void signup(ActionEvent actionEvent) throws IOException {
        Parent root=FXMLLoader.load(getClass().getResource("/com/example/cinemawall/signupPage.fxml"));
        Stage stage = (Stage) buttonSignUp.getScene().getWindow();
        Scene signupScene = new Scene(root);
        stage.setScene(signupScene);
        stage.centerOnScreen();
        stage.show();
    }

    public void verification_handle(ActionEvent actionEvent) {
        // Get the verification code entered by the user
        String enteredCode = verificationCodeField.getText().trim();

        // Check if the entered code matches the generated code
        if (enteredCode.isEmpty()) {
            showAlert("Error", "Please enter the verification code.", Alert.AlertType.ERROR);
            return;
        }

        // Debugging: Print both the generated and entered code

        try {
            int enteredCodeInt = Integer.parseInt(enteredCode); // Parse the entered code as an integer

            // Check if the codes match
            if (enteredCodeInt == verificationCode) {
                // Proceed with further logic
                try (Connection connection = createConnection()) {

                    if (insertUser(connection, fullname, email, username, password)) {
                        showAlert("Success", "Sign up successful!", Alert.AlertType.INFORMATION);
                        navigateToLoginPage(); // Navigate to the login page after successful signup
                    } else {
                        showAlert("Error", "An error occurred while inserting user. Please try again.", Alert.AlertType.ERROR);
                    }
                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                    showAlert("Error", "Database connection failed. Please try again.", Alert.AlertType.ERROR);
                }
            } else {
                showAlert("Error", "Invalid verification code. Please try again.", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Verification code must be a number.", Alert.AlertType.ERROR);
        }
    }

    private void navigateToLoginPage() throws IOException {
        Parent root=FXMLLoader.load(getClass().getResource("/com/example/cinemawall/hello-view.fxml"));
        Scene loginScene = new Scene(root);
        Stage stage=(Stage)verifyButton.getScene().getWindow();
        stage.setScene(loginScene);
        stage.centerOnScreen();
        stage.show();
    }
    private void navigateToLoginPagefromforgotpassword() throws IOException {
        Parent root=FXMLLoader.load(getClass().getResource("/com/example/cinemawall/hello-view.fxml"));
        Scene loginScene = new Scene(root);
        Stage stage=(Stage) resetPasswordButton.getScene().getWindow();
        stage.setScene(loginScene);
        stage.centerOnScreen();
        stage.show();
    }

    public void handleForgotPassword(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/cinemawall/forgot_password.fxml"));
            Stage stage = (Stage) forgotPass.getScene().getWindow();
            Scene forgotPasswordScene = new Scene(root);
            stage.setScene(forgotPasswordScene);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load the forgot password page.", Alert.AlertType.ERROR);
        }
    }

    public void handleSend(ActionEvent actionEvent) {
        String emailInput = emailField.getText().trim();

        if (emailInput.isEmpty()) {
            showAlert("Error", "Please enter your email address!", Alert.AlertType.ERROR);
            return;
        }

        if (!isValidEmail(emailInput)) {
            showAlert("Error", "Invalid email format!", Alert.AlertType.ERROR);
            return;
        }

        try (Connection connection = createConnection()) {
            String query = "SELECT COUNT(*) FROM login WHERE email = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, emailInput);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        this.email = emailInput; // Store the email in the controller
                        verificationCode = generateVerificationCode();
                        sendVerificationCode(emailInput, verificationCode);
                    } else {
                        showAlert("Error", "No account found with this email!", Alert.AlertType.ERROR);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database error occurred. Please try again.", Alert.AlertType.ERROR);
        }
    }

    public void handleNext(ActionEvent actionEvent) {
        String enteredCode = verificationField.getText().trim();

        if (enteredCode.isEmpty()) {
            showAlert("Error", "Please enter the verification code!", Alert.AlertType.ERROR);
            return;
        }

        try {
            int enteredCodeInt = Integer.parseInt(enteredCode);

            if (enteredCodeInt == verificationCode) {
                // Proceed to the reset password page
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cinemawall/reset_password.fxml"));
                    Parent root = loader.load();

                    // Get the controller for the reset password page
                    HelloController resetPasswordController = loader.getController();
                    resetPasswordController.setEmailField(this.email); // Pass the email to the reset password page
                    Stage stage = (Stage) nextButton.getScene().getWindow();
                    Scene resetPasswordScene = new Scene(root);
                    stage.setScene(resetPasswordScene);
                    stage.centerOnScreen();
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert("Error", "Unable to load the reset password page.", Alert.AlertType.ERROR);
                }
            } else {
                showAlert("Error", "Invalid verification code. Please try again.", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Verification code must be a number.", Alert.AlertType.ERROR);
        }
    }


    public void handleResetPassword(ActionEvent actionEvent) {
        String newPassword = newPasswordField.getText().trim();
        String confirmPassword = confirmNewPasswordField.getText().trim();
        System.out.println(email);

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Error", "Please enter both new password fields!", Alert.AlertType.ERROR);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert("Error", "Passwords do not match!", Alert.AlertType.ERROR);
            return;
        }

        if (newPassword.length() < 6) {
            showAlert("Error", "Password must be at least 6 characters long!", Alert.AlertType.ERROR);
            return;
        }

        try (Connection connection = createConnection()) {
            String query = "UPDATE login SET password = ? WHERE email = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, newPassword); // Store the password as plain text
                stmt.setString(2, email); // Ensure 'email' contains the correct value

                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    showAlert("Success", "Password reset successful!", Alert.AlertType.INFORMATION);
                    navigateToLoginPagefromforgotpassword(); // Navigate to login page
                } else {
                    showAlert("Error", "Unable to reset password. Please try again.", Alert.AlertType.ERROR);
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            showAlert("Error", "Database error occurred. Please try again.", Alert.AlertType.ERROR);
        }
    }



}
