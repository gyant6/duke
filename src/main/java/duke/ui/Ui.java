package duke.ui;

import duke.DialogBox;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

public class Ui {

    public static String welcomeMsg = "Hello Patrick! This is Spongebob!\n" + "What can I do for you?\n";

    public static void showMessage(VBox dialogContainer, Image dukeImage, String msg) {
        dialogContainer.getChildren().addAll(
                DialogBox.getDukeDialog(msg, dukeImage)
        );
    }

    public static void showLoadingError(VBox dialogContainer, Image image) {
        String errorMsg = "Data found could not be found or created.\n"
                + "Please restart Duke and check your file structure.\n";
        showMessage(dialogContainer, image, errorMsg);
    }
}
