package webcrawler.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**
 *
 * @author Dusko
 */
public final class NamedTextField extends HBox {
    
    private final Text name;
    private final TextField field;

    public NamedTextField(String name) {
        this.name  = new Text();
        this.field = new TextField();
        
        super.getChildren().addAll(this.name, this.field);
    }

    @Override
    public ObservableList<Node> getChildren() {
        // We return the list of the children as unmodifiable
        return FXCollections.unmodifiableObservableList(super.getChildren());
    }
    
}
