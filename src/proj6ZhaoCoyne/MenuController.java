package proj6ZhaoCoyne;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;

public class MenuController {
    private TabPane tabPane;
    public TabPane getTabPane(){
        return this.tabPane;
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
