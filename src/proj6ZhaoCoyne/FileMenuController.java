/*
File: FiletMenuController.java
CS361 Project 5
Names: Yi Feng, Matt Jones, Danqing Zhao
Date: 10/12/18
 */

package proj6ZhaoCoyne;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.control.*;
import javafx.scene.Node;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * FileMenuController contains the handler methods for the MenuItems
 * found in the file menu of our IDE. It also contains a series of helper
 * methods for these handlers, dealing with saving and loading files
 * as well as closing/opening tabs. The FileMenuController has no
 * direct link to the FXML, and relies on the Controller to act as an
 * intermediary.
 *
 * @author Yi Feng
 * @author Iris Lian
 * @author Chris Marcello
 * @author Evan Savillo
 * @author Matt Jones
 * @author Danqing Zhao
 */
public class FileMenuController {
    private TabPane tabPane;
    private Stage primaryStage;

    /**
     * a HashMap mapping the tabs and associated files
     */
    private Map<Tab, File> tabFileMap = new HashMap<>();
    private int untitledCounter = 1;

    public Map<Tab, File> getMap() {
        return tabFileMap;
    }

    /**
     * Handles the About button action.
     * Creates a dialog window that displays the authors' names.
     */
    public void handleAboutMenuItemAction() {
        // create a information dialog window displaying the About text
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);

        // enable to close the window by clicking on the x on the top left corner of
        // the window
        Window window = dialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(event -> window.hide());

        // set the title and the content of the About window
        dialog.setTitle("About");
        dialog.setHeaderText("Authors");
        dialog.setContentText(
                "Yi Feng,\n Matt Jones,\n Danqing Zhao");

        // enable to resize the About window
        dialog.setResizable(true);
        dialog.showAndWait();
    }

    /**
     * Handles the New button action.
     * Opens a text area embedded in a new tab.
     * Sets the newly opened tab to the the topmost one.
     */
    public void handleNewMenuItemAction() {
        Tab newTab = createNewTab("untitled" + (untitledCounter++) + ".txt",
                new VirtualizedScrollPane<>(new JavaCodeArea()));
        this.tabFileMap.put(newTab, null);
    }

    /**
     * Handles the open button action.
     * Opens a dialog in which the user can select a file to open.
     * If the user chooses a valid file, a new tab is created and the file
     * is loaded into the text area.
     * If the user cancels, the dialog disappears without doing anything.
     */
    public void handleOpenMenuItemAction() {
        // create a fileChooser
        FileChooser fileChooser = new FileChooser();
        File openFile = fileChooser.showOpenDialog(this.primaryStage);

        if (openFile != null) {
            // Case: file is already opened in another tab
            // Behavior: switch to that tab
            for (Map.Entry<Tab, File> entry : this.tabFileMap.entrySet()) {
                if (entry.getValue() != null) {
                    if (entry.getValue().equals(openFile)) {
                        this.tabPane.getSelectionModel().select(entry.getKey());
                        return;
                    }
                }
            }

            String contentOpenedFile = this.getFileContent(openFile);

            // Case: current text area is in use and shouldn't be overwritten
            // Behavior: generate new tab and open the file there

            Tab newTab = createNewTab(openFile.getName(), new VirtualizedScrollPane<>(
                    new JavaCodeArea()));
            this.getCurrentCodeArea().replaceText(contentOpenedFile);
            this.tabFileMap.put(newTab, openFile);
        }
    }

    /**
     * Handles the close button action.
     * If the current text area has already been saved to a file, then
     * the current tab is closed.
     * If the current text area has been changed since it was last saved to a file,
     * a dialog appears asking whether you want to save the text before closing it.
     */
    public void handleCloseMenuItemAction() {
        if (!this.isTables()) {
            this.closeTab(this.getCurrentTab());
        }
    }

    /**
     * Handles the Save As button action.
     * Shows a dialog in which the user is asked for the name of the file into
     * which the contents of the current text area are to be saved.
     * If the user enters any legal name for a file and presses the OK button
     * in the dialog,
     * then creates a new text file by that name and write to that file all the current
     * contents of the text area so that those contents can later be reloaded.
     * If the user presses the Cancel button in the dialog, then
     * the dialog closes and no saving occurs.
     *
     * @return false if the user has clicked cancel
     */
    public boolean handleSaveAsMenuItemAction() {
        // create a fileChooser and add file extension restrictions
        FileChooser fileChooser = new FileChooser();

        // file where the text content is to be saved
        File saveFileDialog = fileChooser.showSaveDialog(this.primaryStage);
        if (saveFileDialog != null) {
            // get the selected tab from the tab pane
            Tab selectedTab = this.getCurrentTab();

            // get the text area embedded in the selected tab window
            // save the content of the active text area to the selected file
            CodeArea activeCodeArea = this.getCurrentCodeArea();
            if (this.saveFileTo(activeCodeArea.getText(), saveFileDialog)) {
                // set the title of the tab to the name of the saved file
                selectedTab.setText(saveFileDialog.getName());

                // map the tab and the associated file
                this.tabFileMap.put(selectedTab, saveFileDialog);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Handles the save button action.
     * If a text area was not loaded from a file nor ever saved to a file,
     * behaves the same as the save as button.
     * If the current text area was loaded from a file or previously saved
     * to a file, then the text area is saved to that file.
     *
     * @return false if the user has clicked cancel
     */
    public boolean handleSaveMenuItemAction() {
        // get the selected tab from the tab pane
        Tab selectedTab = this.tabPane.getSelectionModel().getSelectedItem();

        // get the text area embedded in the selected tab window
        CodeArea activeCodeArea = this.getCurrentCodeArea();

        // if the tab content was not loaded from a file nor ever saved to a file
        // save the content of the active text area to the selected file path
        if (this.tabFileMap.get(selectedTab) == null) {
            return this.handleSaveAsMenuItemAction();
        }
        // if the current text area was loaded from a file or previously saved to a file,
        // then the text area is saved to that file
        else {
            return this.saveFileTo(activeCodeArea.getText(),
                    this.tabFileMap.get(selectedTab));
        }
    }

    /**
     * Handles the Exit button action.
     * Exits the program when the Exit button is clicked.
     */
    public void handleExitMenuItemAction() {
        ArrayList<Tab> tablist = new ArrayList<>(this.tabFileMap.keySet());
        for (Tab tab : tablist) {
            this.tabPane.getSelectionModel().select(tab);
            if (!this.closeTab(tab)) {
                return;
            }
        }
        Platform.exit();
    }

    /**
     * Helper function to save the input string to a specified file.
     *
     * @param content String that is saved to the specified file
     * @param file    File that the input string is saved to
     * @return boolean If the file is successfully saved, return true; else, return false.
     */
    private boolean saveFileTo(String content, File file) {
        if (!tabPane.getTabs().isEmpty()) {
            try {
                // open a file, save the content to it, and close it
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(content);
                fileWriter.close();
                return true;
            } catch (IOException e) {
                UserErrorDialog userErrorDialog = new UserErrorDialog(
                        UserErrorDialog.ErrorType.SAVING_ERROR, file.getName());
                userErrorDialog.showAndWait();
                return false;
            }
        }
        return false;
    }

    /**
     * Helper function to get the text content of a specified file.
     *
     * @param file File to get the text content from
     * @return the text content of the specified file
     */
    private String getFileContent(File file) {
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(file.toURI())));
        } catch (IOException e) {
            UserErrorDialog userErrorDialog = new UserErrorDialog(
                    UserErrorDialog.ErrorType.READING_ERROR, file.getName());
            userErrorDialog.showAndWait();

        }
        return content;
    }

    /**
     * Helper function to check if the content of the specified TextArea
     * has changed from the specified File.
     *
     * @param codeArea TextArea to compare with the the specified File
     * @param file     File to compare with the the specified TextArea
     * @return Boolean indicating if the TextArea has changed from the File
     */
    private boolean ifFileChanged(CodeArea codeArea, File file) {
        String codeAreaContent = codeArea.getText();
        String fileContent = this.getFileContent((file));
        return !codeAreaContent.equals(fileContent);
    }

    /**
     * Helper function to handle closing tag action.
     * Removed the tab from the tab file mapping and from the TabPane.
     *
     * @param tab Tab to be closed
     */
    private void removeTab(Tab tab) {
        this.tabPane.getSelectionModel().selectPrevious();
        this.tabFileMap.remove(tab);
        this.tabPane.getTabs().remove(tab);
    }

    /**
     * Helper function to handle closing tab action.
     * Checks if the text content within the tab window should be saved.
     *
     * @param tab Tab to be closed
     * @return true if the tab content has not been saved to any file yet,
     * or have been changed since last save.
     */
    private boolean tabHasUnsavedChanges(Tab tab) {
        // check whether the embedded text has been saved or not
        if (this.tabFileMap.get(tab) == null) {
            return true;
        }
        // check whether the saved file has been changed or not
        else {
            VirtualizedScrollPane vsp = (VirtualizedScrollPane) tab.getContent();
            return this.ifFileChanged((CodeArea) vsp.getContent(),
                    this.tabFileMap.get(tab));
        }
    }

    /**
     * Helper function to handle closing tab action.
     * <p>
     * If the text embedded in the tab window has not been saved yet,
     * or if a saved file has been changed, asks the user if to save
     * the file via a dialog window.
     *
     * @param tab Tab to be closed
     * @return true if the tab is closed successfully; false if the user clicks cancel.
     */
    private boolean closeTab(Tab tab) {
        // if the file has not been saved or has been changed
        // pop up a dialog window asking whether to save the file
        if (this.tabHasUnsavedChanges(tab)) {
            Alert alert = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Want to save before close?",
                    ButtonType.YES,
                    ButtonType.NO,
                    ButtonType.CANCEL
            );
            alert.setTitle("Alert");

            Optional<ButtonType> result = alert.showAndWait();
            // if user presses Yes button, save the file and close the tab
            if (result.get() == ButtonType.YES) {
                if (this.handleSaveMenuItemAction()) {
                    this.removeTab(tab);
                    return true;
                } else
                    return false;
            }
            // if user presses No button, close the tab without saving
            else if (result.get() == ButtonType.NO) {
                this.removeTab(tab);
                return true;
            } else return result.get() != ButtonType.CANCEL;
        }
        // if the file has not been changed, close the tab
        else {
            this.removeTab(tab);
            return true;
        }
    }

    /**
     * Simple helper method which returns the currently viewed tab
     *
     * @return currently viewed tab
     */
    private Tab getCurrentTab() {
        return this.tabPane.getSelectionModel().getSelectedItem();
    }

    /**
     * Simple helper method which returns the code area  within the currently viewed tab
     *
     * @return current viewed code area
     */
    private CodeArea getCurrentCodeArea() {
        Tab selectedTab = this.getCurrentTab();
        VirtualizedScrollPane vsp = (VirtualizedScrollPane) selectedTab.getContent();
        return (CodeArea) vsp.getContent();
    }

    /**
     * Simple helper method
     *
     * @return true if there aren't currently any tabs open, else false
     */
    private boolean isTables() {
        return this.tabPane.getTabs().isEmpty();
    }

    /**
     * create a new Tab
     * for use in handleNewMenuItemAction and handleOpenMenuItemAction
     *
     * @param tabText the tab text
     * @param content the content of the tab
     * @return Tab return the created tab
     */
    private Tab createNewTab(String tabText, Node content) {
        Tab newTab = new Tab();
        // set close action (clicking the 'x')
        newTab.setOnCloseRequest(event -> {
            event.consume();
            closeTab(newTab);
        });

        newTab.setText(tabText);
        newTab.setContent(content);

        // add the new tab to the tab pane
        // set the newly opened tab to the the current (topmost) one
        this.tabPane.getTabs().add(newTab);
        this.tabPane.getSelectionModel().select(newTab);

        return newTab;
    }

    /**
     * Simple helper method that gets the FXML objects from the
     * main controller for use by other methods in the class.
     */
    public void receiveFXMLElements(Object[] list) {
        tabPane = (TabPane) list[0];
        primaryStage = (Stage) list[1];
    }

    /**
     * helper function for handleCompile
     * if there is unsaved changes in the file, ask the user whether to save before
     * compile
     * if the user clicks yes, save the current tab return the saved file
     * if the user clicks no, (1) if the file has been saved before, return the old
     * saved file
     * (2) if the file has not been saved, return null
     * if the user clicks cancel, return null
     *
     * @return a File object of the current file
     */
    public File getCurrentFile() {
        Tab currentTab = this.tabPane.getSelectionModel().getSelectedItem();


        // if the current tab has unsaved changes
        // pop up Alert window to ask whether the user want to save before compile
        if (this.tabHasUnsavedChanges(currentTab)) {
            Alert alert = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Want to save before compile? If not," +
                            " the old saved version without unsaved changes" +
                            " will be compiled.",
                    ButtonType.YES,
                    ButtonType.NO,
                    ButtonType.CANCEL
            );
            alert.setTitle("Alert");
            Optional<ButtonType> result = alert.showAndWait();

            // if the user chooses yes,
            if (result.get() == ButtonType.YES) {
                if (handleSaveMenuItemAction()) return this.tabFileMap.get(currentTab);
                else return null;

            // if the user chooses no
            } else if (result.get() == ButtonType.NO) {
                if (this.tabFileMap.get(currentTab) != null)
                    return this.tabFileMap.get(currentTab);
                else return null;

            //if the user chooses cancel
            } else return null;

         // if the current tab doesn't have unsaved changes, return the file
         // of the current tab

        } else {
            return this.tabFileMap.get(currentTab);
        }
    }
}
