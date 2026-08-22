package za.ac.cput.queuelessqms1.gui;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
/**
 *
 * @author yanto
 */
public class HomeScreen extends Application{
     @Override
    public void start(Stage stage) {
        Label hospitalLabel = new Label("Groote Schuur Hospital");
        hospitalLabel.setFont(Font.font("Inter", FontWeight.BOLD, 14));
        hospitalLabel.setTextFill(Color.web("2F3E5C"));
        
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(16, 24, 16, 24));
        topBar.setStyle("-fx-border-color: transparent transparent #E0E0E0 transparent; -fx-border-width: 1;");
        topBar.getChildren().add(hospitalLabel);
        
        Image logoImage = new Image("file:C:/Users/yanto/OneDrive/Pictures/QueueLess/queueless_logo.jpeg");
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(320);
        logoView.setFitHeight(320);
        logoView.setPreserveRatio(true);
        
        Text appName = new Text("QueueLess");
        appName.setFont(Font.font("Inter", FontWeight.BOLD, 42));
        appName.setFill(Color.web("#2F3E5C"));
        
        
        Text tagline = new Text("Smarter Queues, Better Care");
        tagline.setFont(Font.font("Inter", FontWeight.NORMAL, 18));
        tagline.setFill(Color.web("#67AC76"));
        
        Text hint = new Text("Click anywhere to login");
        hint.setFont(Font.font("Inter", FontWeight.NORMAL, 13));
        hint.setFill(Color.web("#888888"));
        
        VBox content = new VBox(8);
        content.setAlignment(Pos.CENTER);
        content.getChildren().addAll(logoView, appName, tagline, hint);
        
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #FFFFFF;");
        root.setTop(topBar);
        root.setCenter(content);
        root.setOpacity(0);
        
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(3), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        SequentialTransition sequence = new SequentialTransition(fadeIn, pause);
        sequence.play();
        
        root.setOnMouseClicked(e -> {
            //try {
            //    Stage loginStage = new Stage();
            //    new LoginScreen().start(loginStage);
            //    stage.close();
            //} catch (Exception ex) {
            //    ex.printStackTrace();
            //}
        });
        
        Scene scene = new Scene(root, 900, 650);
        stage.setTitle("QueueLess");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }
 
    public static void main(String[] args) {
        launch(args);
    }

}
