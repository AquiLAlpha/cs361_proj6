package proj6ZhaoCoyne;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.NavigationActions;

import java.util.*;

public class FindAndReplaceWidget {

    private Iterator<int[]> indices = new ArrayList<int[]>().iterator();
    private String target, textToSearch;
    private EditMenuController menuController;
    private Button findButton;
    private Button replaceAllButton;
    private TextField userEntryTextField;

    public FindAndReplaceWidget(EditMenuController menuController) {
        this.menuController = menuController;
    }

    public String getTarget() {
        return this.target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTextToSearch() {
        return this.textToSearch;
    }

    public void setTextToSearch(String text) {
        this.textToSearch = text;
    }

    public boolean isEmpty() {
        return !indices.hasNext();
    }

    /**
     * Set up the Find and Replace widget. Shows the dialog window for the widget, and
     * intializes the textToSearch, findButton, replaceAllButton, and userEntryTextField fields.
     * Once this method is run, the find and replace widget can be operated by the user.
     */
    public void setupWidget() {
        CodeArea currentCodeArea = this.menuController.getCurrentCodeArea();
        Stage popupWindow  = new Stage();
        GridPane layout    = new GridPane();
        Scene scene        = new Scene(layout);

        textToSearch       = currentCodeArea.getText();
        findButton         = new Button("Find next");
        replaceAllButton   = new Button("Replace all");
        userEntryTextField = new TextField();

        layout.add(findButton,         0, 1);
        layout.add(replaceAllButton,   1, 1);
        layout.add(userEntryTextField, 0, 0, 2, 1);

        setupButtons();

        popupWindow.setScene(scene);
        popupWindow.showAndWait();
    }

    /**
     * Binds events to the widget's "find" and "replace all" buttons.
     */
    private void setupButtons() {
        findButton.setOnAction(event -> selectNextTarget());
        replaceAllButton.setOnAction(event -> replaceAllTargets());
    }

    /**
     * Selects the next word in the CodeArea that matches the search that user
     * has entered in the TextField of the FindAndReplaceWidget.
     */
    private void selectNextTarget() {
        CodeArea currentCodeArea   = menuController.getCurrentCodeArea();
        String   currentText       = currentCodeArea.getText();
        String   currentTarget     = userEntryTextField.getText();
        boolean  targetHasChanged  = !Objects.equals(this.getTarget(), currentTarget);
        boolean  srcTextHasChanged = !Objects.equals(this.getTextToSearch(), currentText);

        if(isEmpty() || targetHasChanged || srcTextHasChanged) {
            createIteratorFrom(currentText, currentTarget);
            setTarget(currentTarget);
            setTextToSearch(currentText);
        }
        int[] range = getNextRange();
        if(range != null) {
            // select the text in in the given range of the current code area
            currentCodeArea.moveTo(range[0]);
            currentCodeArea.moveTo(range[1], NavigationActions.SelectionPolicy.EXTEND);
        }
    }

    /**
     * replaces all substrings within the CodeArea that match the term the user
     * has entered in the TextField of the FindAndReplaceWidget with a replacement string.
     * The replacement string is obtained from a TextInputDialog which appears when this method
     * is called.
     */
    private void replaceAllTargets() {
        String source = menuController.getCurrentCodeArea().getText();
        String target = userEntryTextField.getText();

        TextInputDialog dialog = new TextInputDialog();
        dialog.setContentText("Replace with: ");
        dialog.setTitle(      "Replace All");
        dialog.setHeaderText( "Enter the text you would like to replaceAllTargets your selection");

        Optional<String> userSelection = dialog.showAndWait();
        if(!userSelection.isPresent()) {
            return;
        }

        String replacement = userSelection.get();
        String newText     = source.replace(target, replacement);
        menuController.handleSelectAllMenuItemAction();
        menuController.getCurrentCodeArea().replaceSelection(newText);
    }

    /**
     * @param source the string that is being searched for a given substring
     * @param substring the substring being searched for in the source string
     * @return a two element int[] array that contains the start and end indices of the target string
     * in the source, where the first element of the array is the start location, and the second element
     * is the end location. If the given source string does not contain the given substring,
     * the returned array will contain the values [-1, -1]
     */
    private int[] indicesOf(String source, String substring) {
        int startOfSubstring = source.indexOf(substring);
        if(startOfSubstring == -1 || "".equals(substring)) {
            return new int[] {-1, -1};
        }
        return new int[] {startOfSubstring, startOfSubstring + substring.length()};
    }

    /**
     *
     * @param idxAccumulator 
     * @param numPreviousChars
     * @param source
     * @param substring
     * @return
     */
    private List<int[]> findAllIndicesHelper(
            List<int[]> idxAccumulator, int numPreviousChars,
            String source, String substring) {

        int[] indicesOfSubstring = indicesOf(source, substring);
        int start = indicesOfSubstring[0];
        int end   = indicesOfSubstring[1];

        if (start == -1) {
            return idxAccumulator;
        }

        idxAccumulator.add(new int[] {start + numPreviousChars, end + numPreviousChars});
        String unsearchedText = source.substring(end, source.length());
        int numCharsSearched  = numPreviousChars + end;

        return findAllIndicesHelper(idxAccumulator, numCharsSearched, unsearchedText, substring);
    }

    private List<int[]> findAllIndices(String source, String substring) {
        List<int[]> result = new ArrayList<>();
        return findAllIndicesHelper(result, 0, source, substring);
    }

    /**
     *
     * @param source
     * @param target
     */
    private void createIteratorFrom(String source, String target) {
        this.indices = findAllIndices(source, target).iterator();
    }

    /**
     * Helper method.
     * @return the next int[] in the object's "indices" iterator. Returns null if the iterator is empty.
     */
    private int[] getNextRange() {
        if(!this.isEmpty()) {
            return indices.next();
        }
        return null;
    }
}
