package com.synir.controllers;

import com.synir.SceneHandler;
import com.synir.models.POJO.Schedule;
import com.synir.models.POJO.Timeblock;
import com.synir.models.POJO.UserSession;
import com.synir.models.Service.ScheduleInfoService;
import com.synir.utility.ApplicationContextUtils;
import com.synir.utility.FileUploadUtil;
import com.synir.utility.Helper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;


@Component
public class AddScheduleVC {
    @Autowired
    ScheduleInfoService scheduleInfoService;
    @FXML
    private TextField titleField;
    @FXML
    private TextField yearField;
    @FXML
    private TextField monthField;
    @FXML
    private AnchorPane companylistpane;
    @FXML
    private TableView<Timeblock> scheduletable;
    @FXML
    private ListView<String> companylistview;
    @FXML
    private TableColumn<Timeblock, String> header;
    @FXML
    private ChoiceBox levelcbox;
    @FXML
    private Button saveBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private Button screenshotBtn;
    @FXML
    private Button updateBtn;
    @FXML
    private TableView<Timeblock> timeslotview;
    @FXML
    private ScrollBar scrollBar;

    private ObservableList<Timeblock> timeblocklist;
    private ObservableList<Timeblock> timeslotlist;
    private ObservableList<String> companylist;
    private ObservableList<String> levellist;
    private String[] weekdate;
    private Schedule schedule;
    private Helper helper;
    private UserSession session;

    public void initialize() {
        ApplicationContext applicationContext = ApplicationContextUtils.getApplicationContext();
        helper = (Helper) applicationContext.getBean("Helper");

        ArrayList<String> companyinput = helper.readCompany();
        companylist = FXCollections.observableArrayList();
        if (companyinput.size() != 0) {
            companylist.addAll(companyinput);
        }
        this.schedule = helper.readSchedule(helper.getScheduleUID());

        initializeSchecduleView();
        initializeCompanyView();
        draglocation();
        setScreenshot();
        titleField.setText(helper.getTitle());

        levellist = FXCollections.observableArrayList();
        session = (UserSession) applicationContext.getBean("UserSession");
        int curr_levl = session.getLevel();
        String[] levelname = {"权限5", "权限4", "权限3", "权限2", "权限1"};
        for (int i = 0; i < 5 - curr_levl; i++) {
            levellist.add(levelname[i]);
        }
        levellist.add("仅对上级可见");
        levelcbox.setItems(levellist);
        if (!session.getUsername().equals(helper.getCreator())) {
            saveBtn.setVisible(false);
            deleteBtn.setVisible(false);
        }
        timeslotview.getStylesheets().add("scrollbar.css");
        scheduletable.getStylesheets().add("scrollbar.css");
        scrollBar.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                scheduletable.scrollTo(t1.intValue());
                timeslotview.scrollTo(t1.intValue());
            }
        });

        weekdate = new String[31];
        Arrays.fill(weekdate,"无");

        int year = helper.getYear();
        int month = helper.getMonth();
        yearField.setText(String.valueOf(year).equals("0") ? "" : String.valueOf(year));
        monthField.setText(String.valueOf(month).equals("0") ? "" : String.valueOf(month));
        onUpdateBtnClicked();
    }

    public void initializeSchecduleView() {
        scheduletable.getSelectionModel().setCellSelectionEnabled(true);
        timeslotview.addEventFilter(ScrollEvent.ANY, Event::consume);

        header.setCellValueFactory(cellData -> cellData.getValue().timenameProperty());
        header.setCellFactory(col -> {
            TableCell<Timeblock, String> cell = new TableCell<Timeblock, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        this.setText(item);
                    }
                }
            };
            cell.prefHeightProperty().bind(scheduletable.heightProperty().multiply(0.1));
            return cell;
        });
        header.setSortable(false);
        for (int i = 1; i <= 31; i++) {
            TableColumn<Timeblock, String> temp = new TableColumn(Integer.toString(i));
            temp.prefWidthProperty().bind(scheduletable.widthProperty().multiply(0.15));
            temp.setSortable(false);
            int index = i - 1;
            temp.setCellValueFactory(cellData -> cellData.getValue().getLocationElement(index));
            temp.setCellFactory(col -> {
                TableCell<Timeblock, String> cell = new TableCell<Timeblock, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            StringBuilder sb = new StringBuilder();
                            String[] arr = item.split("(?<=\\G.....)");
                            for (int i = 0; i < arr.length - 1; i++) {
                                sb.append(arr[i] + "\n");
                            }
                            sb.append(arr[arr.length - 1]);
                            this.setText(sb.toString());

                        }
                    }
                };
                cell.prefHeightProperty().bind(scheduletable.heightProperty().multiply(0.1));
                return cell;
            });
            scheduletable.getColumns().add(temp);
        }
        timeblocklist = FXCollections.observableArrayList();
        timeslotlist = FXCollections.observableArrayList();
        for (int i = 0; i < 12; i++) {
            timeblocklist.add(this.schedule.getSingleTimeBlock(i));
            timeslotlist.add(this.schedule.getSingleTimeBlock(i));
        }
        scheduletable.setItems(timeblocklist);
        scheduletable.setStyle("-fx-font-size :14");
        timeslotview.setItems(timeslotlist);
        timeslotview.setStyle("-fx-font-size :14");

    }

    public void initializeCompanyView() {

        companylistview.setItems(companylist);
        companylistpane.setMaxWidth(200.0);

    }

    public void draglocation() {

        companylistview.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int index = companylistview.getFocusModel().getFocusedIndex();
                String dragedcompany = companylist.get(index);

                Dragboard db = companylistview.startDragAndDrop(TransferMode.COPY);

                ClipboardContent content = new ClipboardContent();
                content.putString(dragedcompany);
                db.setContent(content);

                event.consume();
            }
        });

            scheduletable.setRowFactory(tv -> {
            TableRow<Timeblock> row = new TableRow<>();
            row.setOnDragOver(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent event) {
                    event.acceptTransferModes(TransferMode.COPY);
                    event.consume();
                }
            });

            row.setOnDragDropped(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent event) {
                    int rowidx = scheduletable.getFocusModel().getFocusedIndex();
                    int colidx = scheduletable.getFocusModel().getFocusedCell().getColumn();
                    if (colidx >= 0) {
                        String str = event.getDragboard().getString();
                        scheduletable.getItems().get(rowidx).setLocationElement(str, colidx);
                        scheduletable.refresh();
                    }
                    event.consume();
                }
            });
            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    int clickcount = event.getClickCount();
                    int rowidx = scheduletable.getFocusModel().getFocusedIndex();
                    int colidx = scheduletable.getFocusModel().getFocusedCell().getColumn();
                    if (clickcount == 2 && colidx >= 0) {
                        scheduletable.getItems().get(rowidx).setLocationElement("/", colidx);
                        scheduletable.refresh();
                    }
                    event.consume();
                }
            });
            return row;
        });
    }

    @FXML
    public void onAddCompanyBtnClicked() {
        Stage secondwindow = new Stage();
        BorderPane bd = new BorderPane();

        TextField textField = new TextField();
        Button confirmbutton = new Button();
        confirmbutton.setText("确认");
        textField.setEditable(true);
        textField.setText("请输入公司名");

        bd.setLeft(textField);
        bd.setRight(confirmbutton);

        Scene scene = new Scene(bd, 300, 30);
        secondwindow.setScene(scene);

        secondwindow.setTitle("添加公司");
        secondwindow.show();

        confirmbutton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String str = textField.getText();
                companylist.add(str);
                event.consume();
                secondwindow.close();
            }
        });
    }

    @FXML
    public void onDeleteCompanyBtnClicked() {
        int index = companylistview.getFocusModel().getFocusedIndex();
        companylist.remove(index);
        companylistview.refresh();
    }

    @FXML
    public void onSaveCompanyBtnClicked() {
        helper.writeCompany(this.companylist);
    }

    @FXML
    public void onSaveTableBtnClicked() throws Exception {
        String uuid = helper.getScheduleUID();
        helper.writeSchedule(this.timeblocklist, uuid);
        String l = levelcbox.getValue() == null ? "" : levelcbox.getValue().toString();
        String year = yearField.getText();
        String month = monthField.getText();
        helper.uploadSchedule(session.getUsername(), titleField.getText(), helper.computeLevel(session.getLevel(), l == null ? "" : l), uuid, helper.getId(),year,month);
        FileUploadUtil ftp = new FileUploadUtil();
        File file = new File(helper.envpath + File.separator + helper.getScheduleUID() + ".dat");
        ftp.uploadFile(file, "/usr/local/testdat", helper.getScheduleUID() + ".dat");
        file.delete();
    }

    @FXML
    public void onDeleteTableBtnClicked() {
        helper.removeSchedule(helper.getId());
        ApplicationContext applicationContext = ApplicationContextUtils.getApplicationContext();
        SceneHandler sceneHandler = (SceneHandler) applicationContext.getBean("SceneHandler");
        sceneHandler.updatePane_checkSchedule(sceneHandler.getPane());
    }

    @FXML
    public void onUpdateBtnClicked(){
        String yearstr = yearField.getText();
        String monthstr = monthField.getText();
        if(!Pattern.matches("[1-9][0-9]{3}",yearstr)){return;}
        if(!Pattern.matches("[1-9]||(1)[0-2]",monthstr)){return;}
        int year = Integer.parseInt(yearField.getText());
        int month = Integer.parseInt(monthField.getText());
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.CHINA);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.MONTH,month-1);
        for(int i = 1; i <= 31; i++){
            cal.set(Calendar.DAY_OF_MONTH, i);
            String weekday = sdf.format(cal.getTime());
            weekdate[i-1] = weekday;
            scheduletable.getColumns().get(i-1).setText(i+" "+weekday);
        }
    }

    public void setScreenshot() {
        screenshotBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                try {

                    SnapshotParameters param = new SnapshotParameters();
                    param.setDepthBuffer(true);
                    param.setFill(Color.CORNSILK);

                    AnchorPane pane = new AnchorPane();
                    TableView<Timeblock> tb = new TableView<>();
                    TableColumn<Timeblock,String> tc = new TableColumn<>();
                    tc.setCellValueFactory(cellData -> cellData.getValue().timenameProperty());
                    tb.getColumns().add(tc);

                    TableView<Timeblock> tb2 = new TableView<>();

                    TableColumn<Timeblock,String> tc2 = new TableColumn<>();
                    tc2.setCellValueFactory(cellData -> cellData.getValue().timenameProperty());
                    tb2.getColumns().add(tc2);

                    for (int i = 1; i <= 16; i++) {
                        TableColumn<Timeblock, String> temp = new TableColumn(Integer.toString(i)+" "+weekdate[i-1]);

                        temp.setPrefWidth(69);
                        int index = i - 1;
                        temp.setCellValueFactory(cellData -> cellData.getValue().getLocationElement(index));
                        temp.setCellFactory(col -> {
                            TableCell<Timeblock, String> cell = new TableCell<Timeblock, String>() {
                                @Override
                                public void updateItem(String item, boolean empty) {
                                    super.updateItem(item, empty);
                                    if (item != null) {
                                        StringBuilder sb = new StringBuilder();
                                        String[] arr = item.split("(?<=\\G.....)");
                                        for (int i = 0; i < arr.length - 1; i++) {
                                            sb.append(arr[i] + "\n");
                                        }
                                        sb.append(arr[arr.length - 1]);
                                        this.setText(sb.toString());

                                    }
                                }
                            };
                            cell.setPrefHeight(50);
                            return cell;
                        });
                        tb.getColumns().add(temp);
                    }

                    for (int i = 17; i <= 31; i++) {
                        TableColumn<Timeblock, String> temp = new TableColumn(Integer.toString(i)+" "+weekdate[i-1]);
                        temp.setPrefWidth(69);
                        int index = i - 1;
                        temp.setCellValueFactory(cellData -> cellData.getValue().getLocationElement(index));
                        temp.setCellFactory(col -> {
                            TableCell<Timeblock, String> cell = new TableCell<Timeblock, String>() {
                                @Override
                                public void updateItem(String item, boolean empty) {
                                    super.updateItem(item, empty);
                                    if (item != null) {
                                        StringBuilder sb = new StringBuilder();
                                        String[] arr = item.split("(?<=\\G.....)");
                                        for (int i = 0; i < arr.length - 1; i++) {
                                            sb.append(arr[i] + "\n");
                                        }
                                        sb.append(arr[arr.length - 1]);
                                        this.setText(sb.toString());

                                    }
                                }
                            };
                            cell.setPrefHeight(50);
                            return cell;
                        });
                        tb2.getColumns().add(temp);
                    }



                    tb.setItems(timeblocklist);
                    tb.setStyle("-fx-font-size :13");

                    tb2.setItems(timeblocklist);
                    tb.setStyle("-fx-font-size :13");

                    pane.getChildren().add(tb);
                    pane.setPrefWidth(1050);
                    pane.setPrefHeight(700);
                    tb.setPrefHeight(700);

                    pane.setSnapToPixel(true);
                    pane.setStyle("-fx-background-color: DARKGREY; -fx-padding: 1;"
                            +"-fx-hgap: 1; -fx-vgap: 1;");
                    Scene scene = new Scene(pane);
                    WritableImage snapshot = pane.snapshot(new SnapshotParameters(), null);

                    BufferedImage tempImg = SwingFXUtils.fromFXImage(snapshot, null);

                    File outputfile = new File(helper.getEnvpath() + File.separator + "First.png");
                    ImageIO.write(tempImg,"png" , outputfile);

                    pane.getChildren().remove(tb);

                    pane.getChildren().add(tb2);
                    tb2.setPrefHeight(700);

                    WritableImage snapshot2 = pane.snapshot(new SnapshotParameters(), null);

                    BufferedImage tempImg2 = SwingFXUtils.fromFXImage(snapshot2, null);

                    File outputfile2 = new File(helper.getEnvpath() + File.separator + "Second.png");
                    ImageIO.write(tempImg2,"png" , outputfile2);


                } catch (IOException e) {
                    e.printStackTrace();
                }
                event.consume();
            }
        });
    }

}



