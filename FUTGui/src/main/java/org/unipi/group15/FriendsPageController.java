package org.unipi.group15;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import user.UserSessionService;

import java.io.IOException;

public class FriendsPageController {

    private final UserSessionService userSession = App.getSession();

    @FXML private Label userIdLabel;

    @FXML private Label usernameLabel;

    @FXML private ListView FriendsList;

    @FXML private Button SearchButton;

    @FXML private TableView SuggestedFriendLike;

    @FXML private TableView SuggestedFriend;

    @FXML
    private void searchFriend() throws IOException {
        App.setRoot("searchPlayer");
    }

}
