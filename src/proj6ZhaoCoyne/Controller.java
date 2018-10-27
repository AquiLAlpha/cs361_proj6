/*
File: Controller.java
CS361 Project 5
Names: Yi Feng, Matt Jones, Danqing Zhao
Date: 10/12/18
 */

package proj6ZhaoCoyne;

import javafx.beans.property.SimpleListProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.fxmisc.flowless.VirtualizedScrollPane;

import java.io.File;
//TODO: erorr with closing tab and then compiling

/**
 * Controller is the main controller for the application.
 * It itself doesn't handle much. What it does is delegate
 * tasks to either of the sub controllers, FileMenuController or
 * EditMenuController.
 *
 * @author Yi Feng
 * @author Iris Lian
 * @author Chris Marcello
 * @author Evan Savillo
 * @author Matt Jones
 * @author Danqing Zhao
 */
public class Controller {
    @FXML
    private TabPane tabPane;

    @FXML
    private MenuItem closeMenuItem;
    @FXML
    private MenuItem saveMenuItem;
    @FXML
    private MenuItem saveAsMenuItem;

    private ContextMenu contextMenu;

    @FXML
    private Menu editMenu;

    @FXML
    private Button compileButton;

    @FXML
    private Button compileRunButton;

    @FXML
    private Button stopButton;

    @FXML
    private VBox consoleBox;

    @FXML
    private Stage primaryStage;

    private FileMenuController fileMenuController = new FileMenuController();
    private EditMenuController editMenuController = new EditMenuController();
    private ContextMenuController contextMenuController = new ContextMenuController();
    private ToolbarController toolbarController = new ToolbarController();

    private IOConsole ioConsole = new IOConsole();

    /**
     * Handles the About button action.
     * Creates a dialog window that displays the authors' names.
     */
    @FXML
    private void handleAboutMenuItemAction() {
        fileMenuController.handleAboutMenuItemAction();
    }

    /**
     * Handles the New button action.
     * Opens a text area embedded in a new tab.
     * Sets the newly opened tab to the the topmost one.
     */
    @FXML
    private void handleNewMenuItemAction() {
        fileMenuController.handleNewMenuItemAction();
    }

    /**
     * Handles the open button action.
     * Opens a dialog in which the user can select a file to open.
     * If the user chooses a valid file, a new tab is created and the file
     * is loaded into the text area.
     * If the user cancels, the dialog disappears without doing anything.
     */
    @FXML
    private void handleOpenMenuItemAction() {
        fileMenuController.handleOpenMenuItemAction();
    }

    /**
     * Handles the close button action.
     * If the current text area has already been saved to a file, then
     * the current tab is closed.
     * If the current text area has been changed since it was last saved to a file,
     * a dialog appears asking whether you want to save the text before closing it.
     */
    @FXML
    private void handleCloseMenuItemAction() {
        fileMenuController.handleCloseMenuItemAction();
    }

    /**
     * Handles the Save As button action.
     * Shows a dialog in which the user is asked for the name of the file into
     * which the contents of the current text area are to be saved.
     * If the user enters any legal name for a file and presses the OK button
     * in the dialog,
     * then creates a new text file by that name and write to that file all the current
     * contents of the text area so that those contents can later be reloaded.
     * If the user presses the Cancel button in the dialog, then the dialog closes
     * and no saving occurs.
     */
    @FXML
    private void handleSaveAsMenuItemAction() {
        fileMenuController.handleSaveAsMenuItemAction();
    }

    /**
     * Handles the save button action.
     * If a text area was not loaded from a file nor ever saved to a file,
     * behaves the same as the save as button.
     * If the current text area was loaded from a file or previously saved
     * to a file, then the text area is saved to that file.
     */
    @FXML
    private void handleSaveMenuItemAction() {
        fileMenuController.handleSaveMenuItemAction();
    }

    /**
     * Handles the Exit button action.
     * Stops the running process
     * and exits the program when the Exit button is clicked.
     */
    @FXML
    void handleExitMenuItemAction() {
        toolbarController.handleStop(ioConsole);
        fileMenuController.handleExitMenuItemAction();
    }

    /**
     * Handles the Undo button action.
     * Undo the actions in the text area.
     */
    @FXML
    private void handleUndoMenuItemAction() {
        editMenuController.handleUndoMenuItemAction();
    }

    /**
     * Handles the Redo button action.
     * Redo the actions in the text area.
     */
    @FXML
    private void handleRedoMenuItemAction() {
        editMenuController.handleRedoMenuItemAction();
    }

    /**
     * Handles the Cut button action.
     * Cuts the selected text.
     */
    @FXML
    private void handleCutMenuItemAction() {
        editMenuController.handleCutMenuItemAction();
    }

    /**
     * Handles the Copy button action.
     * Copies the selected text.
     */
    @FXML
    private void handleCopyMenuItemAction() {
        editMenuController.handleCopyMenuItemAction();
    }

    /**
     * Handles the Paste button action.
     * Pastes the copied/cut text.
     */
    @FXML
    private void handlePasteMenuItemAction() {
        editMenuController.handlePasteMenuItemAction();
    }

    /**
     * Handles the SelectAll button action.
     * Selects all texts in the text area.
     */
    @FXML
    private void handleSelectAllMenuItemAction() {
        editMenuController.handleSelectAllMenuItemAction();
    }

    /**
     * Handles the Compile button action.
     * If there is a saved file in the current tab, compile the file.
     * If there is not a saved file in the current tab, nothing happens.
     */
    @FXML
    private void handleCompile() throws  InterruptedException{
        File curFile = getCurrentFile();
        if (curFile != null) toolbarController.handleCompile(curFile, ioConsole, stopButton);
    }



    /**
     * Handles the Compile and Run button action.
     * If there is a saved file in the current tab, compile the file.
     * If code compiles successfully, the code will be run.
     * If there is not a saved file in the current tab, nothing happens.
     */
    @FXML
    private void handleCompileRun() throws  InterruptedException{
        File curFile = getCurrentFile();
        if (curFile != null) toolbarController.handleCompileRun(curFile, ioConsole, stopButton);
    }

    /**
     * Handles the Stop button action.
     * Will stop any code compilation or running process.
     */
    @FXML
    private void handleStop() {
        toolbarController.handleStop(ioConsole);
    }

    /**
     * Reads in the application's main stage.
     * For use in Filechooser dialogs
     */
    void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * This function is called after the FXML fields are populated.
     * Initializes the tab file map with the default tab.
     * and passes necessary items to fileMenuController and editMenuController.
     * Disable saveAs, save, close menu items and edit menu when tabless.
     * Add the console box at the bottom of the window.
     */
    public void initialize() {
        fileMenuController.receiveFXMLElements(tabPane, primaryStage);
        editMenuController.receiveFXMLElements(tabPane);


        //Create an initial tab
        this.handleNewMenuItemAction();

        SimpleListProperty<Tab> listProperty =
                new SimpleListProperty<>(tabPane.getTabs());

        // the saveAs, save, and close menu items are disabled when there is no tab
        saveAsMenuItem.disableProperty().bind(listProperty.emptyProperty());
        saveMenuItem.disableProperty().bind(listProperty.emptyProperty());
        closeMenuItem.disableProperty().bind(listProperty.emptyProperty());

        // the edit menu is disabled when there is no tab
        editMenu.disableProperty().bind(listProperty.emptyProperty());

        // the compile and compile&run buttons are disabled when there is no tab
        compileButton.disableProperty().bind(listProperty.emptyProperty());
        compileRunButton.disableProperty().bind(listProperty.emptyProperty());
        stopButton.setDisable(true);


        // add the console to the VBox at the bottom
        consoleBox.getChildren().add(new VirtualizedScrollPane<>(ioConsole));
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
    private File getCurrentFile() {
        return fileMenuController.getCurrentFile();
    }

}