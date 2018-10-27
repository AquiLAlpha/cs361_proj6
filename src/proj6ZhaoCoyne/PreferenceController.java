package proj6ZhaoCoyne;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
/**
 * PreferenceController is the controller for the pop-up window when users press preference in the
 * file menu.
 * It allows the users to pick color from several given selection to change the color of keywords and comments
 *
 * @author Danqing Zhao
 */
public class PreferenceController {
    /*
     * The absolute path is the location of source code
     */
    private static final String Absolute_Path = "C:\\Users\\Danqing Zhao\\Desktop\\CS361\\proj6\\cs361_proj6\\src";
    @FXML
    ChoiceBox keyword;

    @FXML
    ChoiceBox comment;

    @FXML
    Button confirmPreference;

    @FXML
    Button cancelPreference;

    private Scene primaryScene;

    /*
     * This method will take the value of choice box and write a css file, then reload the scene to change the style
     */
    @FXML
    private void handleSavePreferenceAction(){
        String preference = ".keyword {\n" +
                "    -fx-fill: "  +
                keyword.getValue() + ";\n" +
                "-fx-font-weight: bold;\n" +
                "}\n"+
                ".semicolon {\n" +
                "    -fx-font-weight: bold;\n" +
                "}\n" +
                ".paren {\n" +
                "    -fx-fill: teal;\n" +
                "    -fx-font-weight: bold;\n" +
                "}\n" +
                ".bracket {\n" +
                "    -fx-fill: teal;\n" +
                "    -fx-font-weight: bold;\n" +
                "}\n" +
                ".brace {\n" +
                "    -fx-fill: teal;\n" +
                "    -fx-font-weight: bold;\n" +
                "}\n" +
                ".string {\n" +
                "    -fx-fill: blue;\n" +
                "}\n" +
                ".comment {\n" +
                "\t-fx-fill: " +
                comment.getValue() +
                ";\n" +
                "}\n" +
                ".intcon {\n" +
                "    -fx-fill: firebrick;\n" +
                "}\n" +
                ".paragraph-box:has-caret {\n" +
                "    -fx-background-color: #f2f9fc;\n" +
                "}";
        File file = new File(Absolute_Path + "\\proj6ZhaoCoyne\\Preference.css");
        OutputStream outputStream = null;
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] bs = preference.getBytes();
        try {
            outputStream.write(bs);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) confirmPreference.getScene().getWindow();
        stage.close();
        this.primaryScene.getStylesheets().remove(1);
        this.primaryScene.getStylesheets().add("/proj6ZhaoCoyne/Preference.css");
        Stage mainStage = (Stage) this.primaryScene.getWindow();
        mainStage.setScene(this.primaryScene);
    }

    @FXML
    private void handleCloseAction(){
        Stage stage = (Stage) cancelPreference.getScene().getWindow();
        stage.close();
    }

    public void receiveScene(Scene scene){
        this.primaryScene = scene;
    }
}
