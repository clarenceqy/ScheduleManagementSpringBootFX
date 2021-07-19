package com.synir.controllers;

import com.synir.SceneHandler;
import com.synir.models.POJO.ScheduleInfo;
import com.synir.models.POJO.UserSession;
import com.synir.utility.ApplicationContextUtils;
import com.synir.utility.Helper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class CheckScheduleVC {
    @FXML
    private ListView<String> schedulelistview;
    @FXML
    private AnchorPane anchorPane;

    private Helper helper;
    private UserSession userSession;
    private ObservableList<String> infolist;
    private SceneHandler sceneHandler;
    private StackPane rightpane;

    public void setPane(StackPane rightpane){
        this.rightpane = rightpane;
        System.out.println(this.rightpane == null);
    }

    public void initialize(){

        ApplicationContext applicationContext = ApplicationContextUtils.getApplicationContext();
        helper = (Helper) applicationContext.getBean("Helper");
        userSession = (UserSession) applicationContext.getBean("UserSession");


        infolist = FXCollections.observableArrayList();
        sceneHandler = (SceneHandler) applicationContext.getBean("SceneHandler");
        List<ScheduleInfo> scheduleList = helper.getScheduleList(userSession.getUsername(), userSession.getLevel());
        for (ScheduleInfo info: scheduleList){
            infolist.add(info.buildString());
        }

        schedulelistview.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                ListCell<String> list=new ListCell<String>(){
                    @Override
                    protected void updateItem(String arg0, boolean arg1) {
                        super.updateItem(arg0, arg1);
                        if(arg1==false){
                            HBox hbox=new HBox(10);
                            hbox.setAlignment(Pos.BASELINE_LEFT);//对齐显示
                            Button bt=new Button("查看");
                            bt.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent event) {
                                    helper.setId(scheduleList.get(getIndex()).getId());
                                    helper.setTitle(scheduleList.get(getIndex()).getTitle());
                                    helper.setCreator(scheduleList.get(getIndex()).getCreator());
                                    helper.setYear(scheduleList.get(getIndex()).getYear());
                                    helper.setMonth(scheduleList.get(getIndex()).getMonth());
                                    sceneHandler.updatePane_addSchedule(sceneHandler.getPane(), scheduleList.get(getIndex()).getUuid());
                                    event.consume();
                                }
                            });
                            Label la=new Label(arg0);
                            hbox.getChildren().addAll(la,bt);//添加
                            this.setGraphic(hbox);//启动
                        }
                    }

                };
                return list;
            }
        });
        schedulelistview.setItems(infolist);
    }
}
