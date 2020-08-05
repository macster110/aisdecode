package aisDecode;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import layout.AisDecodeView;

public class AisDecodeApp extends Application {
   public static void main(String[] args) {
       launch(args);
   }
   
   @Override
   public void start(Stage primaryStage) {
       primaryStage.setTitle("AIS Data Converter");
     
       
       AisDecodeControl aisDecodeControl= new AisDecodeControl(); 
       
       AisDecodeView aisDecodeView = new AisDecodeView(primaryStage, aisDecodeControl); 
       
              
       StackPane root = new StackPane();
       root.getChildren().add(aisDecodeView);
       Scene scene = new Scene(root, 400, 500);
       primaryStage.setScene(scene);
       
       JMetro jMetro = new JMetro(Style.LIGHT);
       jMetro.setScene(scene); 
       
       primaryStage.show();
   }
}