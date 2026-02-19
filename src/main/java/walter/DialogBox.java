package walter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;

/**
 * A custom control using FXML is defined as a class that extends {@link javafx.scene.layout.HBox}.
 * This control represents a dialog box consisting of an ImageView to represent the speaker's face
 * and a label containing text from the speaker.
 */
public class DialogBox extends HBox {

    private Label text;
    private ImageView displayPicture;

    /**
     * Creates a new DialogBox with the given text and image.
     *
     * @param s The text to display.
     * @param i The image to display.
     */
    public DialogBox(String s, Image i) {
        text = new Label(s);
        displayPicture = new ImageView(i);

        // Styling
        text.setWrapText(true);
        text.setPadding(new Insets(10));
        text.setMinHeight(Region.USE_PREF_SIZE);

        displayPicture.setFitWidth(50.0);
        displayPicture.setFitHeight(50.0);

        // Crop the profile picture as a circle
        Circle clip = new Circle(25, 25, 25);
        displayPicture.setClip(clip);

        this.setAlignment(Pos.TOP_RIGHT);
        this.setPadding(new Insets(10));
        this.setSpacing(10);
        this.getChildren().addAll(text, displayPicture);
    }

    /**
     * Flips the dialog box such that the ImageView is on the left and text on the right.
     */
    private void flip() {
        this.setAlignment(Pos.TOP_LEFT);
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        FXCollections.reverse(tmp);
        this.getChildren().setAll(tmp);
    }

    public static DialogBox getUserDialog(String s, Image i) {
        var db = new DialogBox(s, i);
        db.text.setStyle("-fx-background-color: #a6d9f7; -fx-background-radius: 10; "
                + "-fx-text-fill: black; -fx-font-family: 'Courier New';");
        return db;
    }

    public static DialogBox getWalterDialog(String s, Image i) {
        var db = new DialogBox(s, i);
        db.flip();
        boolean isError = s.contains("You clearly don't know who you're talking to")
                || s.contains("Invalid date format")
                || s.contains("Enter a valid number")
                || s.contains("That task number does not exist");
        if (isError) {
            db.text.setStyle("-fx-background-color: #8b0000; -fx-text-fill: white; "
                    + "-fx-background-radius: 10; -fx-font-family: 'Courier New';");
        } else {
            db.text.setStyle("-fx-background-color: #065535; -fx-background-radius: 10; "
                    + "-fx-text-fill: white; -fx-font-family: 'Courier New';");
        }
        return db;
    }
}

