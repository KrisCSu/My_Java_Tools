/**
 * @author: Chenyang Su
 * @date: 12/05/2016
 * 
 * Strengthened version of Prof. Lee Stemkoski's work 'SVG_Creator'
 * able to draw rectangle, circle and line for now
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

public class SVG_CreatorV2 extends Application 
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

    Document svgDoc;
    Element svgRoot;

    public void start(Stage mainStage) 
    {
        mainStage.setTitle("SVG Image Editor");
        mainStage.getIcons().add(new Image("assets/title.png"));
        BorderPane root = new BorderPane();

        // remove this line if using stylesheet!
        //root.setStyle( "-fx-background-color: rgb(80%,80%,100%);" );

        Scene mainScene = new Scene(root, 600, 800);
        mainScene.getStylesheets().add("assets/stylesheet.css");
        mainStage.setScene(mainScene);

        // custom code below --------------------------------------------

        // canvas setup
        Canvas canvas = new Canvas(400,400);
        GraphicsContext context = canvas.getGraphicsContext2D();

        root.setCenter(canvas);
        //root.getChildren().add(canvas);

        context.setFill(Color.WHITE);
        context.setLineWidth(1);
        context.setStroke(Color.WHITE);

        context.fillRect(0,0, 400,400);
        context.strokeRect(0,0, 400,400);

        // XML setup
        String namespace = "http://www.w3.org/2000/svg";
        svgRoot = new Element("svg", namespace);
        svgRoot.addAttribute( new Attribute("version", "1.1") );
        svgRoot.addAttribute( new Attribute("width", "400") );
        svgRoot.addAttribute( new Attribute("height", "400") );
        svgDoc = new Document(svgRoot);

        // TabView setup
        TabPane tabby = new TabPane();
        tabby.setTabMinWidth(75);
        Tab tab1 = new Tab(" Rectangle ");
        tab1.setClosable(false);
        Tab tab2 = new Tab(" Circle ");
        tab2.setClosable(false);
        Tab tab3 = new Tab("Line");
        tab3.setClosable(false);

        tabby.getTabs().addAll(tab1, tab2, tab3);

        // Rectangle tab
        TextField rectX = new TextField("10");
        TextField rectY = new TextField("10");
        TextField rectW = new TextField("200");
        TextField rectH = new TextField("100");
        ColorPicker rectFC = new ColorPicker( Color.YELLOW );
        TextField rectBS = new TextField("4");
        ColorPicker rectBC = new ColorPicker( Color.BLUE );

        Button rectButton = new Button("Add Rectangle");
        rectButton.setOnAction(
            event ->
            {
                // get values from fields

                Double x = Double.parseDouble(rectX.getText());
                Double y = Double.parseDouble(rectY.getText());
                Double w = Double.parseDouble(rectW.getText());
                Double h = Double.parseDouble(rectH.getText());

                Color fc = rectFC.getValue();
                Double bs = Double.parseDouble(rectBS.getText());
                Color bc = rectBC.getValue();

                // draw to canvas

                context.setFill(fc);
                context.setLineWidth(bs);
                context.setStroke(bc);

                context.fillRect(x,y, w,h);
                context.strokeRect(x,y, w,h);

                // add to XML

                Element rect = new Element("rect", namespace);
                rect.addAttribute( new Attribute("x", x.toString()) );
                rect.addAttribute( new Attribute("y", y.toString()) );
                rect.addAttribute( new Attribute("width", w.toString()) );
                rect.addAttribute( new Attribute("height", h.toString()) );
                rect.addAttribute( new Attribute("fill", toHexString(fc)) );
                rect.addAttribute( new Attribute("stroke-width", bs.toString()) );
                rect.addAttribute( new Attribute("stroke", toHexString(bc)) );
                svgRoot.appendChild(rect);
            }
        );

        VBox vb1 = new VBox();
        vb1.setSpacing(16);
        vb1.setAlignment(Pos.CENTER);

        GridPane grid1 = new GridPane();
        grid1.setAlignment(Pos.CENTER);
        grid1.setVgap(4);
        grid1.setHgap(4);

        tab1.setContent(vb1);

        grid1.addRow(1, new Label("Top-Left X:"), rectX);
        grid1.addRow(2, new Label("Top-Left Y:"), rectY);
        grid1.addRow(3, new Label("Width:"), rectW);
        grid1.addRow(4, new Label("Height:"), rectH);
        grid1.addRow(5, new Label("Fill Color:"), rectFC);
        grid1.addRow(6, new Label("Border Size:"), rectBS);
        grid1.addRow(7, new Label("Border Color:"), rectBC);

        vb1.getChildren().addAll(grid1, rectButton);

        //circle tab
        TextField circleX = new TextField("50");
        TextField circleY = new TextField("50");
        TextField diameter = new TextField("25");
        ColorPicker circleFC = new ColorPicker( Color.YELLOW );
        TextField circleBS = new TextField("4");
        ColorPicker circleBC = new ColorPicker( Color.BLUE );

        Button circleButton = new Button("Add Circle");
        circleButton.setOnAction(
            event ->
            {
                // get values from fields

                Double x = Double.parseDouble(circleX.getText());
                Double y = Double.parseDouble(circleY.getText());
                Double d = Double.parseDouble(diameter.getText());
                Color fc = circleFC.getValue();
                Double bs = Double.parseDouble(circleBS.getText());
                Color bc = circleBC.getValue();

                // draw to canvas

                context.setFill(fc);
                context.setLineWidth(bs);
                context.setStroke(bc);

                x = x - (d/2);
                y = y - (d/2);
                context.fillOval(x, y, d, d);
                context.strokeOval(x, y, d, d);

                // add to XML

                Element circle = new Element("circle", namespace);
                circle.addAttribute( new Attribute("x", x.toString()) );
                circle.addAttribute( new Attribute("y", y.toString()) );
                circle.addAttribute( new Attribute("diameter", d.toString()) );
                circle.addAttribute( new Attribute("fill", toHexString(fc)) );
                circle.addAttribute( new Attribute("stroke-width", bs.toString()) );
                circle.addAttribute( new Attribute("stroke", toHexString(bc)) );
                svgRoot.appendChild(circle);
            }
        );

        VBox vb2 = new VBox();
        vb2.setSpacing(16);
        vb2.setAlignment(Pos.CENTER);

        GridPane grid2 = new GridPane();
        grid2.setAlignment(Pos.CENTER);
        grid2.setVgap(4);
        grid2.setHgap(4);
        tab2.setContent(vb2);

        grid2.addRow(1, new Label("Center X-coord:"), circleX);
        grid2.addRow(2, new Label("Center Y-coord:"), circleY);
        grid2.addRow(3, new Label("Diameter:"), diameter);
        grid2.addRow(4, new Label("Fill Color:"), circleFC);
        grid2.addRow(5, new Label("Border Size:"), circleBS);
        grid2.addRow(6, new Label("Border Color:"), circleBC);
        vb2.getChildren().addAll(grid2, circleButton);

        //line tab
        TextField lineX1 = new TextField("50");
        TextField lineY1 = new TextField("50");
        TextField lineX2 = new TextField("150");
        TextField lineY2 = new TextField("150");
        TextField lineBS = new TextField("4");
        ColorPicker lineBC = new ColorPicker( Color.BLUE );

        Button lineButton = new Button("Add Line");
        lineButton.setOnAction(
            event ->
            {
                // get values from fields
                Double x1 = Double.parseDouble(lineX1.getText());
                Double x2 = Double.parseDouble(lineX2.getText());
                Double y1 = Double.parseDouble(lineY1.getText());
                Double y2 = Double.parseDouble(lineY2.getText());

                Double bs = Double.parseDouble(circleBS.getText());
                Color bc = circleBC.getValue();

                // draw to canvas
                context.setLineWidth(bs);
                context.setStroke(bc);
                context.strokeLine(x1, y1, x2, y2);

                // add to XML

                Element line = new Element("line", namespace);
                line.addAttribute( new Attribute("x1", x1.toString()) );
                line.addAttribute( new Attribute("y1", y1.toString()) );
                line.addAttribute( new Attribute("x2", x2.toString()) );
                line.addAttribute( new Attribute("y2", y2.toString()) );
                line.addAttribute( new Attribute("stroke-width", bs.toString()) );
                line.addAttribute( new Attribute("stroke", toHexString(bc)) );
                svgRoot.appendChild(line);
            }
        );

        VBox vb3 = new VBox();
        vb3.setSpacing(16);
        vb3.setAlignment(Pos.CENTER);

        GridPane grid3 = new GridPane();
        grid3.setAlignment(Pos.CENTER);
        grid3.setVgap(4);
        grid3.setHgap(4);
        tab3.setContent(vb3);

        grid3.addRow(1, new Label("Point1 X-coord:"), lineX1);
        grid3.addRow(2, new Label("Point1 Y-coord:"), lineY1);
        grid3.addRow(3, new Label("Point2 X-coord:"), lineX2);
        grid3.addRow(4, new Label("Point2 Y-coord:"), lineY2);
        grid3.addRow(5, new Label("Line Width:"), lineBS);
        grid3.addRow(6, new Label("Line Color:"), lineBC);
        vb3.getChildren().addAll(grid3, lineButton);

        //Menu
        MenuBar menuBar = new MenuBar();
        Menu menuHome = new Menu("Home");
        menuBar.getMenus().add(menuHome);

        Menu menuImage = new Menu("Image");
        menuBar.getMenus().add(menuImage);

        MenuItem about = new MenuItem("About");
        about.setOnAction( 
            new EventHandler<ActionEvent>()
            {
                public void handle(ActionEvent event)
                {
                    Alert infoAlert = new Alert(AlertType.INFORMATION);
                    infoAlert.setTitle("About this program");
                    infoAlert.setGraphic(new ImageView(new Image("assets/about.png",64,64,true,true)));
                    infoAlert.setHeaderText(null);

                    String message = "Looking forward to see your creative work!";
                    infoAlert.setContentText(message);
                    infoAlert.showAndWait();
                }
            }
        );
        about.setGraphic( new ImageView( new Image("assets/about.png", 16, 16, true, true) ) );
        menuHome.getItems().add(about);

        MenuItem quit = new MenuItem("Quit");
        quit.setOnAction( 
            new EventHandler<ActionEvent>()
            {
                public void handle(ActionEvent event)
                {
                    mainStage.close();
                }
            }
        );
        quit.setGraphic( new ImageView( new Image("assets/quit.png", 16, 16, true, true) ) );
        menuHome.getItems().add(quit);

        VBox topBox = new VBox();
        topBox.setAlignment(Pos.CENTER);

        topBox.getChildren().addAll(menuBar, tabby);
        root.setTop(topBox);

        MenuItem newSVG = new MenuItem("New SVG Image");
        newSVG.setOnAction(
            event ->
            {
                // clear canvas
                context.setFill(Color.WHITE);
                context.setLineWidth(1);
                context.setStroke(Color.WHITE);
                context.fillRect(0,0, 400,400);
                context.strokeRect(0,0, 400,400);

                // clear XML element
                svgRoot.removeChildren();
            }
        );
        newSVG.setGraphic( new ImageView( new Image("assets/new.png", 16, 16, true, true) ) );
        menuImage.getItems().add(newSVG);

        MenuItem saveSVG = new MenuItem("Save SVG Image");
        saveSVG.setOnAction(
            event ->
            {
                FileChooser chooser = new FileChooser();
                FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("SVG Files", "*.svg");
                chooser.getExtensionFilters().add(filter);
                File f = chooser.showSaveDialog(mainStage);

                if (f == null)
                    return;

                try
                {
                    FileOutputStream fos = new FileOutputStream(f);
                    Serializer serial = new Serializer(fos, "ISO-8859-1");
                    serial.setIndent(4);
                    serial.setMaxLength(64);
                    serial.write(svgDoc);
                    fos.close();
                }
                catch (Exception error)
                {
                    System.out.println("Unable to print to file.");
                    error.printStackTrace();
                    System.exit(1);
                }
            }
        );
        saveSVG.setGraphic( new ImageView( new Image("assets/save.png", 16, 16, true, true) ) );
        menuImage.getItems().add(saveSVG);

        MenuItem loadSVG = new MenuItem("Load SVG Image");
        loadSVG.setOnAction(
            event ->
            {
                FileChooser chooser = new FileChooser();
                FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("SVG Files", "*.svg");
                chooser.getExtensionFilters().add(filter);
                File f = chooser.showOpenDialog(mainStage);

                if (f == null)
                    return;

                // clear canvas
                context.setFill(Color.WHITE);
                context.setLineWidth(1);
                context.setStroke(Color.WHITE);
                context.fillRect(0,0, 400,400);
                context.strokeRect(0,0, 400,400);

                try
                {
                    Builder parser = new Builder();
                    svgDoc = parser.build(f);

                    // for debugging purposes: print parsed data
                    // Serializer serial = new Serializer(System.out, "ISO-8859-1");
                    // serial.setIndent(4);
                    // serial.setMaxLength(64);
                    // serial.write(svgDoc);

                    svgRoot = svgDoc.getRootElement();
                    Elements shapeList = svgRoot.getChildElements();

                    for (int i = 0; i < shapeList.size(); i++)
                    {
                        Element shapeElement = shapeList.get(i);
                        String tagName = shapeElement.getLocalName();

                        if (tagName.equals("rect"))
                        {
                            Double x = Double.parseDouble( shapeElement.getAttributeValue("x") );
                            Double y = Double.parseDouble( shapeElement.getAttributeValue("y") );
                            Double w = Double.parseDouble( shapeElement.getAttributeValue("width") );
                            Double h = Double.parseDouble( shapeElement.getAttributeValue("height") );
                            Color fc = Color.web( shapeElement.getAttributeValue("fill") );
                            Double bs = Double.parseDouble( shapeElement.getAttributeValue("stroke-width") );
                            Color bc = Color.web( shapeElement.getAttributeValue("stroke") );

                            context.setFill(fc);
                            context.setLineWidth(bs);
                            context.setStroke(bc);
                            context.fillRect(x,y, w,h);
                            context.strokeRect(x,y, w,h);
                        }
                        else
                        {
                            System.out.println();
                        }
                    }

                }
                catch (Exception error)
                {
                    error.printStackTrace();
                }

            }
        );
        loadSVG.setGraphic( new ImageView( new Image("assets/load.png", 16, 16, true, true) ) );
        menuImage.getItems().add(loadSVG);

        //root.getChildren().addAll(newSVG, saveSVG, loadSVG);
        // custom code above --------------------------------------------
        mainStage.show();
    }

    public static String toHexString(Color c)
    {
        String hex = String.format( "#%02X%02X%02X",
                (int)( c.getRed() * 255 ),
                (int)( c.getGreen() * 255 ),
                (int)( c.getBlue() * 255 ) );
        return hex;
    }

}
