package dot_package;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main1
{

    public void start(ActionEvent actionEvent) throws IOException
    {
            ((Node) actionEvent.getSource()).getScene().getWindow().hide();
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Pane root = loader.load(getClass().getResource("main_screen.fxml").openStream());


            Scene scene = new Scene(root);
            primaryStage.setTitle("Main Screen : Parser ");
            primaryStage.setScene(scene);
            primaryStage.show();

    }
}
