package com.synir.controllers;

import com.synir.SceneHandler;
import com.synir.models.POJO.ScheduleInfo;
import com.synir.models.POJO.UserSession;
import com.synir.models.Service.ScheduleInfoService;
import com.synir.models.Service.UserSessionService;
import com.synir.utility.ApplicationContextUtils;
import com.synir.utility.Helper;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;


@Component
public class LoginVC {
    @FXML
    public Label usernameLabel;
    @FXML
    public Label passwordLabel;
    @FXML
    public TextField textField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Button submitBtn;
    @FXML
    public Button cancelBtn;

    @Autowired
    private UserSessionService userservice;
    @Autowired
    private ScheduleInfoService scheduleInfoService;
    
    @FXML
    public void initialize(){}

    /**
     * Handle submit button action. Load user session and checks if current user is valid.
     */
    @FXML
    public void handleSubmitButtonAction(){
        //Register beans and acquires user session bean
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("Beans.xml");
        Helper helper = (Helper)applicationContext.getBean("Helper");
        helper.setService(this.scheduleInfoService);
        UserSession session = (UserSession) applicationContext.getBean("UserSession");
        String usernameinput = this.textField.getText();
        String passwordinput = this.passwordField.getText();

        /*
        * Connect to database here.
        * */

//        UserSession us = new UserSession();
//        us.setUsername("Yichao3");
//        us.setLevel(0);
//        us.setPassword("123456");
//        userservice.add(us);

        //System.out.println(userservice.findByUsernameAndPassword("Yichao","12345"));

//        Date date = new Date();
//        String strDateFormat = "yyyy-MM-dd HH:mm:ss";
//        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
//
//        ScheduleInfo scheduleInfo = new ScheduleInfo();
//        scheduleInfo.setCreator("Yichao");
//        scheduleInfo.setTitle("untitle");
//        scheduleInfo.setLevel(0);
//        scheduleInfo.setUuid("asdasd");
//        scheduleInfo.setTimestamp(sdf.format(date));
//        scheduleInfoService.add(scheduleInfo);

        //If username amd password matches. Updates home screen
        UserSession info = userservice.findByUsernameAndPassword(usernameinput,passwordinput);
        if(info != null){
            session.setLevel(info.getLevel());
            session.setUsername(info.getUsername());
            Scene scene = submitBtn.getScene();
            Window window = scene.getWindow();
            Stage stage = (Stage) window;
            SceneHandler sceneHandler = (SceneHandler) applicationContext.getBean("SceneHandler");
            sceneHandler.setStage(stage);
            sceneHandler.updateScene("/views/home.fxml");
        }
    }

    /**
     * This methods clears all input fields when cancel button is clicked.
     */
    @FXML
    public void handleCancelButtonAction(){
        textField.clear();
        passwordField.clear();
    }
}
