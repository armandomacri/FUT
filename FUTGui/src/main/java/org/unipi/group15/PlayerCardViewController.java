package org.unipi.group15;

import javafx.embed.swing.SwingFXUtils;
import levelDB.StoreImage;
import bean.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import neo4j.Neo4jComment;
import neo4j.Neo4jPlayerCard;
import user.UserSessionService;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class PlayerCardViewController {
    public static Player player;
    public static final Neo4jComment neo4jcomment = new Neo4jComment();
    public static Neo4jPlayerCard neo4jcard = new Neo4jPlayerCard();
    public final UserSessionService userSession = App.getSession();

    @FXML private Label likeLabel1;

    @FXML private Button likeButton;

    @FXML private Label usernameLabel;

    @FXML private Label userIdLabel;

    @FXML private Text playerName;

    @FXML private Text overall;

    @FXML private Text position;

    @FXML private Label attr1;

    @FXML private Label attr1Stat;

    @FXML private ProgressBar attr1Bar;

    @FXML private Label attr2;

    @FXML private Label attr2Stat;

    @FXML private ProgressBar attr2Bar;

    @FXML private Label attr3;

    @FXML private Label attr3Stat;

    @FXML private ProgressBar attr3Bar;

    @FXML private Label attr4;

    @FXML private Label attr4Stat;

    @FXML private ProgressBar attr4Bar;

    @FXML private Label attr5;

    @FXML private Label attr5Stat;

    @FXML private ProgressBar attr5Bar;

    @FXML private Label attr6;

    @FXML private Label attr6Stat;

    @FXML private ProgressBar attr6Bar;

    @FXML private Text extendedName;

    @FXML private Text club;

    @FXML private Text league;

    @FXML private Text nationality;

    @FXML private Text dateOfBirth;

    @FXML private Text height;

    @FXML private Text weight;

    @FXML private Text quality;

    @FXML private Text revision;

    @FXML private Text weakFoot;

    @FXML private Text skillMoves;

    @FXML private Text prefFoot;

    @FXML private ImageView playerCardImg;

    @FXML private ImageView nationalityImg;

    @FXML private ImageView clubImg;

    @FXML private Button showComments;

    public void setPlayer(Player p){
        player = p;
        //System.out.println(p.toString());
    }

    @FXML
    private void initialize() throws IOException {
        usernameLabel.setText(userSession.getUsername());
        userIdLabel.setText(userSession.getUserId());
        countLike();
        checkLike();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(player.getDateOfBirth());

        StoreImage si = new StoreImage();

        String[] img = player.getImages();

        playerCardImg.setImage(SwingFXUtils.toFXImage(si.findImg(img[0]), null));
        nationalityImg.setImage(SwingFXUtils.toFXImage(si.findImg(img[1]), null));
        clubImg.setImage(SwingFXUtils.toFXImage(si.findImg(img[2]), null));
        extendedName.setText(player.getPlayerExtendedName());
        playerName.setText(player.getPlayerName());
        overall.setText(player.getOverall().toString());
        position.setText(player.getPosition());
        club.setText(player.getClub());
        league.setText(player.getLeague());
        nationality.setText(player.getNationality());
        dateOfBirth.setText(date);
        height.setText(player.getHeight());
        weight.setText(player.getWeight());
        quality.setText(player.getQuality());
        revision.setText(player.getRevision());
        weakFoot.setText(player.getWeakFoot().toString());
        skillMoves.setText(player.getSkillMoves().toString());
        prefFoot.setText(player.getPrefFoot());

        if(player.getPosition().equals("GK")){
            attr1.setText("Diving");
            attr1Stat.setText(player.getGkDiving().toString());
            attr1Bar.setProgress(player.getGkDiving().doubleValue()/100);
            attr2.setText("Reflexes");
            attr2Stat.setText(player.getGkReflexe().toString());
            attr2Bar.setProgress(player.getGkReflexe().doubleValue()/100);
            attr3.setText("Handling");
            attr3Stat.setText(player.getGkHandling().toString());
            attr3Bar.setProgress(player.getGkHandling().doubleValue()/100);
            attr4.setText("Speed");
            attr4Stat.setText(player.getGkSpeed().toString());
            attr4Bar.setProgress(player.getGkSpeed().doubleValue()/100);
            attr5.setText("Kicking");
            attr5Stat.setText(player.getGkKicking().toString());
            attr5Bar.setProgress(player.getGkKicking().doubleValue()/100);
            attr6.setText("Positioning");
            attr6Stat.setText(player.getGkPositioning().toString());
            attr6Bar.setProgress(player.getGkPositioning().doubleValue()/100);
        }
        else{
            attr1.setText("Pace");
            attr1Stat.setText(player.getPace().toString());
            attr1Bar.setProgress(player.getPace().doubleValue()/100);
            attr2.setText("Shooting");
            attr2Stat.setText(player.getShooting().toString());
            attr2Bar.setProgress(player.getShooting().doubleValue()/100);
            attr3.setText("Passing");
            attr3Stat.setText(player.getPassing().toString());
            attr3Bar.setProgress(player.getPassing().doubleValue()/100);
            attr4.setText("Dribbling");
            attr4Stat.setText(player.getDribbling().toString());
            attr4Bar.setProgress(player.getDribbling().doubleValue()/100);
            attr5.setText("Defending");
            attr5Stat.setText(player.getDefending().toString());
            attr5Bar.setProgress(player.getDefending().doubleValue()/100);
            attr6.setText("Physicality");
            attr6Stat.setText(player.getPhysicality().toString());
            attr6Bar.setProgress(player.getPhysicality().doubleValue()/100);
        }
    }

    @FXML
    private void countLike() {
        Integer numLikes = neo4jcard.countLikes(Integer.valueOf(player.getPlayerId()));
        likeLabel1.setText(String.valueOf(numLikes));
    }

    @FXML
    private void checkLike() {
        boolean existLikes = neo4jcard.checkLikes(Integer.valueOf(userSession.getUserId()), Integer.valueOf(player.getPlayerId()));
        if (existLikes)
            likeButton.setDisable(true);
    }

    @FXML
    private void addLike() {
        neo4jcard.createLike(Integer.valueOf(userSession.getUserId()), Integer.valueOf(player.getPlayerId()));
        likeButton.setDisable(true);
        countLike();
    }

    @FXML
    private void goToComments() {
        CommentsPageController cpc = new CommentsPageController();
        cpc.setPlayerId(player);
        App.setRoot("comments");
    }

    @FXML
    private void goToSearchPlayer() {
        App.setRoot("searchPlayer");
    }


}
