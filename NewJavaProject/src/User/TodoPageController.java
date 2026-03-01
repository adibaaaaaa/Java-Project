package User;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class TodoPageController {

    @FXML
    private Label TodoLabel;

    @FXML
    private Label NottodoLabel;

    public void setTextT(String text) {
        animateText(TodoLabel, text);
    }

    public void setTextN(String text) {
        animateText(NottodoLabel, text);
    }

    private void animateText(Label label, String fullText) {

        String[] words = fullText.split(" ");
        label.setText("");

        Timeline timeline = new Timeline();
        
        for (int i = 0; i < words.length; i++) {
            final int index = i;
            KeyFrame keyFrame = new KeyFrame(
                    Duration.millis(300 * (i + 1)), // speed control
                    e -> label.setText(label.getText() + words[index] + " ")
            );
            timeline.getKeyFrames().add(keyFrame);
        }

        timeline.play();
    }
}