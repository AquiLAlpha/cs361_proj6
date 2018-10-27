/*
File: EditMenuController.java
CS361 Project 5
Names: Yi Feng, Matt Jones, Danqing Zhao
Date: 10/12/18
 */

package proj6ZhaoCoyne;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.NavigationActions;

import java.util.Objects;

/**
 * This class handles the Edit menu, as a helper to the main Controller.
 * This includes the individual handler methods for the MenuItems as
 * well as logic for determining deactivating the buttons when
 * appropriate.
 *
 * @author Yi Feng
 * @author Iris Lian
 * @author Chris Marcello
 * @author Evan Savillo
 * @author Matt Jones
 */
public class EditMenuController extends MenuController{

    /**
     * Handles the Undo button action.
     * Undo the actions in the text area.
     */
    public void handleUndoMenuItemAction() {
        this.getCurrentCodeArea().undo();
    }

    /**
     * Handles the Redo button action.
     * Redo the actions in the text area.
     */
    public void handleRedoMenuItemAction() {
        this.getCurrentCodeArea().redo();
    }

    /**
     * Handles the Cut button action.
     * Cuts the selected text.
     */
    public void handleCutMenuItemAction() {
        this.getCurrentCodeArea().cut();
    }

    /**
     * Handles the Copy button action.
     * Copies the selected text.
     */
    public void handleCopyMenuItemAction() {
        this.getCurrentCodeArea().copy();
    }

    /**
     * Handles the Paste button action.
     * Pastes the copied/cut text.
     */
    public void handlePasteMenuItemAction() {
        this.getCurrentCodeArea().paste();
    }

    /**
     * Handles the SelectAll button action.
     * Selects all texts in the text area.
     */
    public void handleSelectAllMenuItemAction() {
        this.getCurrentCodeArea().selectAll();
    }

    //public void handleFindAndReplace(Stage parent) {
    public void handleFindAndReplace() {
        Stage popupWindow = new Stage();
        GridPane layout   = new GridPane();
        Scene scene       = new Scene(layout);
        FindAndReplaceWidget findAndReplace = new FindAndReplaceWidget();

        CodeArea currentCodeArea = getCurrentCodeArea();
        String textToSearch      = currentCodeArea.getText();

        Button findButton       = new Button("Find next");
        Button replaceButton    = new Button("Replace");
        Button replaceAllButton = new Button("Replace all");
        TextField  userEntryTextField = new TextField();

        layout.add(findButton,         0, 1);
        layout.add(replaceButton,      1, 1);
        layout.add(replaceAllButton,   2, 1);
        layout.add(userEntryTextField, 0, 0, 3, 1);
        findButton.setOnAction(
            event -> {
                boolean targetHasChanged = !Objects.equals(userEntryTextField.getText(),
                                                           findAndReplace.getTarget()
                );
                if(findAndReplace.isEmpty() || targetHasChanged) {
                    findAndReplace.createIteratorFrom(textToSearch, userEntryTextField.getText());
                    findAndReplace.setTarget(userEntryTextField.getText());
                }
                int[] location = findAndReplace.getNextTargetLocation();
                if(location != null) {
                    currentCodeArea.moveTo(location[0]);
                    currentCodeArea.moveTo(location[1], NavigationActions.SelectionPolicy.EXTEND);
                }

            }

        );

        popupWindow.setScene(scene);
        popupWindow.showAndWait();

    }



    /**
     * Simple helper method that gets the FXML objects from the
     * main controller for use by other methods in the class.
     */
    public void receiveFXMLElements(TabPane tabPane) {
        this.setTabPane(tabPane);

    }
}
