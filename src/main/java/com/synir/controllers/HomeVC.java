package com.synir.controllers;

import com.synir.SceneHandler;
import com.synir.utility.ApplicationContextUtils;
import com.synir.utility.Helper;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;


/**
 * The type Home vc.
 */
@Component
public class HomeVC {
    @FXML
    public SplitPane splitpane;
    @FXML
    public AnchorPane leftpane;
    @FXML
    public StackPane rightpane;
    @FXML
    public AnchorPane layer1;

    private SceneHandler sceneHandler;

    @FXML
    public void initialize(){
        ApplicationContext applicationContext = ApplicationContextUtils.getApplicationContext();
        sceneHandler = (SceneHandler) applicationContext.getBean("SceneHandler");
    }

    /**
     * @Title: ${checkScheduleBtnClicked}
     * @Description: This method updates right split pane to checkschedule view.
     */
    @FXML
    public void checkScheduleBtnClicked(){
        sceneHandler.updatePane_checkSchedule(rightpane);
    }

    /**
     * @Title: ${addScheduleBtnClicked}
     * @Description: This method updates right split pane to addschedule view.
     */
    @FXML
    public void addScheduleBtnClicked(){
        sceneHandler.updatePane_addSchedule(rightpane,null);
    }
}
