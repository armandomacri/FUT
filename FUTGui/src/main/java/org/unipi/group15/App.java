package org.unipi.group15;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import user.UserSessionService;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {
    private static UserSessionService session;
    private static Scene scene;
    private static Stage s;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"));
        stage.setOnCloseRequest(e -> close());
        stage.setScene(scene);
        s = stage;
        stage.show();
    }

    public static UserSessionService getSession() {
        return session;
    }
    public static void setSession(UserSessionService s){
        session = s;
    }
    public static void setHeight(double height){ s.setHeight(height); }
    public static void setWidth(double width){ s.setWidth(width); }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

    public void close(){

    }

}