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
    public void createContextMenu(){
        CodeArea codeArea = this.getCurrentCodeArea();
        ContextMenu contextMenu = this.getContextMenu();
        MenuItem cut = new MenuItem("Cut");
        MenuItem copy = new MenuItem("Copy");
        MenuItem paste = new MenuItem("Paste");
        MenuItem undo = new MenuItem("Undo");
        MenuItem redo = new MenuItem("Redo");
        MenuItem selectAll = new MenuItem("Select All");
        contextMenu.getItems().addAll(cut, copy, paste, undo, redo, selectAll);
        cut.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                codeArea.cut();
            }
        });
        copy.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                codeArea.copy();
            }
        });
        paste.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                codeArea.paste();
            }
        });
        undo.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                codeArea.undo();
            }
        });
        redo.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                codeArea.redo();
            }
        });
        selectAll.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                codeArea.selectAll();
            }
        });

        codeArea.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.isSecondaryButtonDown()){
                    contextMenu.show(codeArea, event.getScreenX(),event.getScreenY());
                }
            }
        });
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
