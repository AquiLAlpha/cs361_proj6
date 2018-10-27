package proj6ZhaoCoyne;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;

public class MenuController {
    private TabPane tabPane;
    public TabPane getTabPane(){
        return this.tabPane;
    }

    private ContextMenu contextMenu = new ContextMenu();

    public ContextMenu getContextMenu(){
        return this.contextMenu;
    }
    public void setTabPane(TabPane tabPane){
        this.tabPane = tabPane;
    }
    /**
     * @return currently viewed tab
     */
    public Tab getCurrentTab() {
        return this.tabPane.getSelectionModel().getSelectedItem();
    }


    /**
     * Simple helper method that gets the FXML objects from the
     * main controller for use by other methods in the class.
     */
    public void receiveFXMLElements(TabPane tabPane) {
        this.setTabPane(tabPane);
    }

    /**
     * Simple helper method which returns the code area  within the currently viewed tab
     *
     * @return current viewed code area
     */
    public CodeArea getCurrentCodeArea() {
        Tab selectedTab = this.getCurrentTab();
        VirtualizedScrollPane vsp = (VirtualizedScrollPane) selectedTab.getContent();
        return (CodeArea) vsp.getContent();
    }
}
