package org.unipi.group15;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import user.UserSessionService;
import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {
    private static final Logger logger = LogManager.getLogger(App.class);
    private static UserSessionService session;
    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        logger.info("Start application!");
        scene = new Scene(loadFXML("primary"));
        stage.setOnCloseRequest(e -> close());
        stage.setScene(scene);
        stage.setTitle("FUT - Application");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("../../../img/football-ball.png")));
        stage.show();
    }

    public static UserSessionService getSession() {
        return session;
    }
    public static void setSession(UserSessionService s){
        session = s;
    }

    static void setRoot(String fxml) {
        try {
            scene.setRoot(loadFXML(fxml));
        } catch (IOException e) {
            logger.error("Exceptions occure:", e);
        }
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

    public void close(){
        if (session != null)
            session.clear();
    }
}