/**
 * retrieve RSS data from website and display
 * @author: kris su
 */

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.effect.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.event.*; 
import javafx.animation.*;
import javafx.geometry.*;
import java.io.*;
import java.util.*;

import javafx.collections.*;
import javafx.scene.control.cell.*;
import javafx.scene.control.TableColumn.*;
import javafx.util.converter.*;

import nu.xom.*;

public class RSS_Reader extends Application 
{
    public static void main(String[] args) 
    {
        // Automatic VM reset, thanks to Joseph Rachmuth.
        try
        {
            launch(args);
            System.exit(0);
        }
        catch (Exception error)
        {   
            error.printStackTrace();
            System.exit(0);
        }
    }

    public void start(Stage mainStage) 
    {
        mainStage.setTitle("Comic viewer");
        mainStage.getIcons().add(new Image("assets/title.png"));
        mainStage.setMaximized(true);
        BorderPane root = new BorderPane();

        ScrollPane sp = new ScrollPane();

        VBox vb = new VBox();
        vb.setPadding( new Insets(16) );
        vb.setSpacing(16);
        vb.setAlignment( Pos.CENTER_LEFT);

        Scene mainScene = new Scene(root);
        mainScene.getStylesheets().add("assets/stylesheet.css");
        mainStage.setScene(mainScene);


        Builder parser = new Builder();
        // creates data structure from file
        try
        {
            //Change the rss link here to customize
            Document doc = parser.build( "http://comicfeeds.chrisbenard.net/view/pennyarcade/default" );

            Element rootElement = doc.getRootElement();

            Element channelElement = rootElement.getFirstChildElement("channel");

            Elements itemList = channelElement.getChildElements("item");

            for (int i = 0; i < 3; i++)
            {
                Element item = itemList.get(i);

                String title = item.getFirstChildElement("title").getValue();

                String pubDate = item.getFirstChildElement("pubDate").getValue();

                String description = item.getFirstChildElement("description").getValue();

                //substring the description to get the url of images
                String all = description; // shortened it 
                String s = "<img src=\"";
                int ix = all.indexOf(s)+s.length();
                String url = all.substring(ix, all.indexOf("\"", ix+1));

                Label label = new Label(title + "\n");
                label.setId("title");
                Label label2 = new Label(pubDate + "\n");
                label2.setId("pubDate");
                ImageView image = new ImageView(url);
                
                image.setFitWidth(1200);
                image.setPreserveRatio(true);
                
                vb.getChildren().addAll(label, label2, image);
            }

        }
        catch (Exception error)
        {
            error.printStackTrace();
            System.exit(1);
        }

        //menu
        MenuBar menuBar = new MenuBar();
        Menu menuHome = new Menu("Home");
        menuBar.getMenus().add(menuHome);

        MenuItem menuItemAbout = new MenuItem("About");
        menuItemAbout.setOnAction( 
            new EventHandler<ActionEvent>()
            {
                public void handle(ActionEvent event)
                {
                    Alert infoAlert = new Alert(AlertType.INFORMATION);
                    infoAlert.setTitle("About/help");
                    infoAlert.setGraphic(new ImageView(new Image("assets/about.png",64,64,true,true)));
                    infoAlert.setHeaderText(null);

                    String message = "\nThis program will update comic automatically.\n"
                                        + "Have fun!";
                    infoAlert.setContentText(message);
                    infoAlert.showAndWait();
                }
            }
        );
        menuItemAbout.setGraphic( new ImageView( new Image("assets/information.png", 16, 16, true, true) ) );
        menuItemAbout.setAccelerator(
            new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN) );
        menuHome.getItems().add(menuItemAbout);

        Menu menuItemQuit = new Menu("Quit");
        menuItemQuit.setOnAction( 
            new EventHandler<ActionEvent>()
            {
                public void handle(ActionEvent event)
                {
                    mainStage.close();
                }
            }
        );
        menuItemQuit.setGraphic( new ImageView( new Image("assets/logout.png", 16, 16, true, true) ) );
        menuItemQuit.setAccelerator(
            new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN) );
        menuHome.getItems().add(menuItemQuit);

        //assembly all together
        sp.setContent(vb);
        root.setTop(menuBar);
        root.setCenter(sp);
        // custom code above --------------------------------------------
        mainStage.show();
    }

}
