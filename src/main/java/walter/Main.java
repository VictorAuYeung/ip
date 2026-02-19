package walter;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * A GUI for Walter using JavaFX.
 */
public class Main extends Application {

    private ScrollPane scrollPane;
    private VBox dialogContainer;
    private TextField userInput;
    private Button sendButton;
    private Scene scene;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));
    private Image walterImage = new Image(this.getClass().getResourceAsStream("/images/DaWalter.png"));

    private Walter walter = new Walter("data/walter.txt");

    @Override
    public void start(Stage stage) {
        // Step 1. Setting up required components

        // The container for the content of the chat to scroll.
        scrollPane = new ScrollPane();
        dialogContainer = new VBox();
        scrollPane.setContent(dialogContainer);

        userInput = new TextField();
        sendButton = new Button("Send");

        AnchorPane mainLayout = new AnchorPane();
        mainLayout.getChildren().addAll(scrollPane, userInput, sendButton);

        scene = new Scene(mainLayout);

        stage.setScene(scene);
        stage.show();

        // Step 2. Formatting the window to look as expected
        stage.setTitle("Walter");
        stage.setResizable(true);
        stage.setMinHeight(600.0);
        stage.setMinWidth(400.0);

        mainLayout.setPrefSize(400.0, 600.0);

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        scrollPane.setVvalue(1.0);
        scrollPane.setFitToWidth(true);

        // You will need to import `javafx.scene.layout.Region` for this.
        dialogContainer.setPrefHeight(Region.USE_COMPUTED_SIZE);
        dialogContainer.setPadding(new Insets(10));
        dialogContainer.setSpacing(10);
        dialogContainer.setStyle("-fx-background-color: #F4F4F4;");

        userInput.setPrefHeight(40.0);
        userInput.setStyle("-fx-background-radius: 20; -fx-padding: 0 15 0 15;");
        
        sendButton.setPrefHeight(40.0);
        sendButton.setPrefWidth(60.0);
        sendButton.setStyle("-fx-background-radius: 20; -fx-background-color: #007AFF; -fx-text-fill: white; -fx-font-weight: bold;");

        AnchorPane.setTopAnchor(scrollPane, 0.0);
        AnchorPane.setLeftAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 42.0);

        AnchorPane.setLeftAnchor(userInput, 1.0);
        AnchorPane.setBottomAnchor(userInput, 1.0);
        AnchorPane.setRightAnchor(userInput, 62.0);

        AnchorPane.setBottomAnchor(sendButton, 1.0);
        AnchorPane.setRightAnchor(sendButton, 1.0);

        // Step 3. Add functionality to handle user input.
        sendButton.setOnMouseClicked((event) -> {
            handleUserInput();
        });

        sendButton.setOnMouseEntered(e -> sendButton.setStyle("-fx-background-radius: 20; -fx-background-color: #0056b3; -fx-text-fill: white; -fx-font-weight: bold;"));
        sendButton.setOnMouseExited(e -> sendButton.setStyle("-fx-background-radius: 20; -fx-background-color: #007AFF; -fx-text-fill: white; -fx-font-weight: bold;"));

        userInput.setOnAction((event) -> {
            handleUserInput();
        });

        // Scroll down to the end every time dialogContainer's height changes.
        dialogContainer.heightProperty().addListener((observable) -> scrollPane.setVvalue(1.0));

        // Step 4. Display welcome message on startup
        String welcomeMessage = "Hello! I'm Walter\nWhat can I do for you?";
        dialogContainer.getChildren().add(
                DialogBox.getWalterDialog(welcomeMessage, walterImage)
        );
    }

    /**
     * Iteration 2: Creating two dialog boxes, one echoing user input and the other containing Duke's reply
     * and then appending them to the dialog container. Clears the user input after processing.
     */
    private void handleUserInput() {
        String userText = userInput.getText();
        String walterText = walter.getResponse(userInput.getText());
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(userText, userImage),
                DialogBox.getWalterDialog(walterText, walterImage)
        );
        userInput.clear();
    }
}
