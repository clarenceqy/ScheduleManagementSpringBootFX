package com.synir;

import com.synir.controllers.AddScheduleVC;
import com.synir.controllers.CheckScheduleVC;
import com.synir.models.POJO.UserSession;
import com.synir.utility.ApplicationContextUtils;
import com.synir.utility.Helper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SceneHandler {
    private Stage stage;
    private StackPane rightpane;

    public SceneHandler(){
        this(null);
    }

    public SceneHandler(Stage stage){
        this.stage = stage;
    }

    public void setStage(Stage stage){this.stage = stage;}

    public StackPane getPane(){
        return this.rightpane;
    }

    /**
     * This method sets up window parameters and prepares home view.
     */
    public void updateScene(String resourcename){
        try{
            //home view fxml path is declare in property file.
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/home.fxml"));
            Parent root = fxmlLoader.load();
            //Parent root = FXMLLoader.load(getClass().getResource(resourcename));
            Scene scene= new Scene(root, 450,600);
            stage.setResizable(true);
            stage.setMinWidth(450);
            stage.setMinHeight(600);
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * This method updates the right split pane on home page to check schedule view when checkschedule button is clicked.
     * @param parentNode StackPane
     */
    public void updatePane_checkSchedule(StackPane parentNode){
        try {
            //load checkschedule fxml file
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/checkschedule.fxml"));
            AnchorPane newnode= fxmlLoader.load();
            this.rightpane = parentNode;



            //setting up layouts for checkschedule pane
            //binding width and height propety with parent root(rightpane of splitpane)
            newnode.prefWidthProperty().bind(parentNode.prefWidthProperty());
            newnode.prefHeightProperty().bind(parentNode.prefHeightProperty());
            newnode.maxWidthProperty().bind(parentNode.maxWidthProperty());
            newnode.maxHeightProperty().bind(parentNode.maxHeightProperty());
            newnode.minWidthProperty().bind(parentNode.minWidthProperty());
            newnode.minHeightProperty().bind(parentNode.minHeightProperty());

            //removing current pane displayed on the right split pane and adding schedule pane to parent's child list
            parentNode.getChildren().remove(0);
            parentNode.getChildren().add(newnode);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void updatePane_addSchedule(StackPane parentNode,String UUID){
        try {
            ApplicationContext applicationContext = ApplicationContextUtils.getApplicationContext();
            Helper helper = (Helper) applicationContext.getBean("Helper");
            UserSession session = (UserSession) applicationContext.getBean("UserSession");
            String uid = UUID == null ? helper.generateUID() : UUID;
            helper.setScheduleUID(uid);
            if(UUID == null){helper.setId(-1);helper.setTitle("未命名");helper.setCreator(session.getUsername());}

            //load checkschedule fxml file
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/addschedule.fxml"));
            VBox newnode = fxmlLoader.load();



            //setting up layouts for checkschedule pane
            //binding width and height propety with parent root(rightpane of splitpane)

            newnode.prefWidthProperty().bind(parentNode.prefWidthProperty());
            newnode.prefHeightProperty().bind(parentNode.prefHeightProperty());
            newnode.maxWidthProperty().bind(parentNode.maxWidthProperty());
            newnode.maxHeightProperty().bind(parentNode.maxHeightProperty());
            newnode.minWidthProperty().bind(parentNode.minWidthProperty());
            newnode.minHeightProperty().bind(parentNode.minHeightProperty());

            //removing current pane displayed on the right split pane and adding schedule pane to parent's child list
            parentNode.getChildren().remove(0);
            parentNode.getChildren().add(newnode);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
