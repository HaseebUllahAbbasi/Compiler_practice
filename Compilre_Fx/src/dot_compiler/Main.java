package dot_compiler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Main_Screen.fxml")));
        primaryStage.setTitle("Parser");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        Controller controller = new Controller();
        controller.setStage(primaryStage);
    }
    public static void main(String[] args) {
        launch(args);
    }
}
