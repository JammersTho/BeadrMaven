import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Model;

import java.applet.Applet;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by JammersBlah on 25/05/2017.
 */
public class Main  extends Application {
    public static void main(String args[]) throws Exception
    {
        //View theView = new View();

        //Model theModel = new Model();

        //Controller theController = new Controller(theView, theModel);

        launch(args);

        //theView.setVisible(true);

        //TODO get the properties files from the Beadr project
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        URL resource = getClass().getResource("/main_screen.fxml");
        Parent root = FXMLLoader.load(resource);
        primaryStage.setTitle("Beadr");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }
}
