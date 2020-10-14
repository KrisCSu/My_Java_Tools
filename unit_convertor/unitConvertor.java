/**
 * lenght unit convertor
 * author: kris chenyang su
 * 
 * this program convert length value in different length unit
 * 
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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class unitConvertor extends Application
{
    int unit;

    /**
     * Constructor for objects of class unitConvertor
     */
    public static void main(String[] args)
    {
        try
        {
            launch(args);
            System.exit(0);
        }
        catch(Exception error)
        {
            error.printStackTrace();
            System.exit(0);
        }
    }
    
    public void start(Stage mainStage)
    {
        mainStage.setTitle("Unit Converter");
        
        BorderPane root = new BorderPane();
        
        Scene mainScene = new Scene(root, 600, 400);
        mainStage.setScene(mainScene);
        mainStage.getIcons().add( new Image("assets/title.png"));
        
        mainScene.getStylesheets().add("assets/stylesheet.css");
        
        //menubar created here
        MenuBar menuBar = new MenuBar();

        Menu menuInfo = new Menu("Information");
        menuInfo.setGraphic( new ImageView( new Image("assets/info.png") ) );
        menuBar.getMenus().add(menuInfo);

        MenuItem menuItemAbout = new MenuItem("About this program...");
        menuItemAbout.setOnAction( 
            new EventHandler<ActionEvent>()
            {
                public void handle(ActionEvent event)
                {
                    Alert infoAlert = new Alert(AlertType.INFORMATION);
                    infoAlert.setTitle("About this program");
                    infoAlert.setHeaderText(null);

                    String message = "This program convert units in a range 0-100. "
                                     + "Slide the slider to change the value, click different radio button to set unit. "
                                     + "Have fun!";
                    infoAlert.setContentText(message);
                    infoAlert.showAndWait();
                }
            }
        );
        menuItemAbout.setGraphic( new ImageView( new Image("assets/help.png") ) );
        menuItemAbout.setAccelerator(
            new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN) );
        menuInfo.getItems().add(menuItemAbout);

        MenuItem menuItemQuit = new MenuItem("Quit");
        menuItemQuit.setOnAction( 
            new EventHandler<ActionEvent>()
            {
                public void handle(ActionEvent event)
                {
                    mainStage.close();
                }
            }
        );
        menuItemQuit.setGraphic( new ImageView( new Image("assets/cancel.png") ) );
        menuItemQuit.setAccelerator(
            new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN) );
        menuInfo.getItems().add(menuItemQuit);
        
        //a VBox called playground to hold all buttons and labels
        VBox playGround = new VBox();
        playGround.setAlignment(Pos.CENTER);
        playGround.setSpacing(16);
        playGround.setPadding(new Insets(32));
        playGround.getStyleClass().add("playGround");
        
        //Hbox to hold all radio buttons
        HBox topRow = new HBox();
        topRow.setSpacing(16);
        topRow.setAlignment(Pos.CENTER);
        
        //a bunch of radio buttons
        ToggleGroup radioGroup = new ToggleGroup();

        radioGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) {
    
                RadioButton chk = (RadioButton)t1.getToggleGroup().getSelectedToggle(); // Cast object to radio button
                if(chk.getText()== "cm"){
                    unit = 1;
                }
                else if(chk.getText() == "in"){
                    unit = 2;
                }
                else if(chk.getText() == "ft"){
                    unit = 3;
                }
                else if(chk.getText() == "yd"){
                    unit = 4;
                }
                else if(chk.getText() == "m"){
                    unit = 5;
                }
            }
        });
        
        RadioButton rb1 = new RadioButton("cm");
        rb1.setToggleGroup(radioGroup);
        rb1.setSelected(true);        

        RadioButton rb2 = new RadioButton("in");
        rb2.setToggleGroup(radioGroup); 

        RadioButton rb3 = new RadioButton("ft");
        rb3.setToggleGroup(radioGroup); 
        
        RadioButton rb4 = new RadioButton("yd");
        rb4.setToggleGroup(radioGroup); 
        
        RadioButton rb5 = new RadioButton("m");
        rb5.setToggleGroup(radioGroup); 
        
        topRow.getChildren().addAll(rb1, rb2, rb3, rb4, rb5);
        
        //slider created put under buttons
        HBox midRow = new HBox();
        midRow.setAlignment(Pos.CENTER);
        
        Slider value = new Slider(0, 100, 42);
        value.setShowTickMarks(true);
        value.setShowTickLabels(true);
        value.setMajorTickUnit(10);
        //value.setBlockIncrement(0.1);
        value.setPrefWidth(400);
        
        midRow.getChildren().add(value);
        
        //labels initialized with interesting values
        Label cLabel = new Label("42.00 cm");
        Label inLabel = new Label("16.54 in");
        Label ftLabel = new Label("1.38 ft");
        Label ydLabel = new Label("0.46 yd");
        Label mLabel = new Label("0.42 m");
        
        value.valueProperty().addListener(
            new ChangeListener<Number>()
            {
                public void changed(ObservableValue ov, Number oldValue, Number newValue)
                {
                    double v = value.getValue();
                    int c;
                    if(unit == 1)
                    {
                        cLabel.setText((c2c(v)/100)+" cm");
                        inLabel.setText((c2in(v)/100)+" in");
                        ftLabel.setText((c2ft(v)/100)+" ft");
                        ydLabel.setText((c2yd(v)/100)+" yd");
                        mLabel.setText((c2m(v)/100)+" m");
                    }
                    else if(unit == 2)
                    {
                        //convert value to cm first
                        //same in other case shown below
                        v = v*2.54;
                        
                        inLabel.setText((c2in(v)/100)+" in");
                        cLabel.setText((c2c(v)/100)+" cm");
                        ftLabel.setText((c2ft(v)/100)+" ft");
                        ydLabel.setText((c2yd(v)/100)+" yd");
                        mLabel.setText((c2m(v)/100)+" m");
                    }
                    else if(unit == 3)
                    {
                        v = v*30.48;
                        
                        cLabel.setText((c2c(v)/100)+" cm");
                        inLabel.setText((c2in(v)/100)+" in");
                        ftLabel.setText((c2ft(v)/100)+" ft");
                        ydLabel.setText((c2yd(v)/100)+" yd");
                        mLabel.setText((c2m(v)/100)+" m");
                    }
                    else if(unit == 4)
                    {
                        v = v*91.44;
                        
                        cLabel.setText((c2c(v)/100)+" cm");
                        inLabel.setText((c2in(v)/100)+" in");
                        ftLabel.setText((c2ft(v)/100)+" ft");
                        ydLabel.setText((c2yd(v)/100)+" yd");
                        mLabel.setText((c2m(v)/100)+" m");
                    }
                    else if(unit == 5)
                    {
                        v = v*100;
                        
                        cLabel.setText((c2c(v)/100)+" cm");
                        inLabel.setText((c2in(v)/100)+" in");
                        ftLabel.setText((c2ft(v)/100)+" ft");
                        ydLabel.setText((c2yd(v)/100)+" yd");
                        mLabel.setText((c2m(v)/100)+" m");
                    }
                }
            }
        );
        
        VBox bottomRow = new VBox();
        bottomRow.setAlignment(Pos.CENTER);
        bottomRow.getChildren().addAll(cLabel, inLabel, ftLabel, ydLabel, mLabel);
        
        playGround.getChildren().addAll(topRow, midRow, bottomRow);
        
        root.setCenter(playGround);
        root.setTop(menuBar);
        mainStage.show();
    }
    
    //method to convert cm
    public double c2c(double c)
    {
        return Math.round(c*100);
    }
    
    //method to convert cm to inch
    public double c2in(double c)
    {
        return Math.round((c/2.54)*100);
    }
    
    //method to convert cm to ft
    public double c2ft(double c)
    {
        return Math.round((c/30.48)*100);
    }
    
    //method to convert cm to yard
    public double c2yd(double c)
    {
        return Math.round((c*0.010936)*100);
    }
    
    //method to convert cm to meter
    public double c2m(double c)
    {
        return Math.round((c*0.01)*100);
    }
}
//the end