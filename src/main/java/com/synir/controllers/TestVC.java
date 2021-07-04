//package com.synir.controllers;
//
//
//import com.synir.models.POJO.Company;
//import com.synir.models.POJO.Schedule;
//import com.synir.models.POJO.Timeblock;
//import com.synir.utility.Helper;
//import javafx.event.EventHandler;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.Scene;
//import javafx.scene.SnapshotParameters;
//import javafx.scene.control.*;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.ScrollPane;
//import javafx.scene.control.TextField;
//import javafx.scene.image.WritableImage;
//import javafx.scene.input.DragEvent;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.input.*;
//import javafx.scene.layout.BorderPane;
//import javafx.scene.paint.Color;
//import javafx.stage.Stage;
//
//
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//import java.net.URL;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.text.SimpleDateFormat;
//import java.time.Month;
//import java.util.*;
//
//public class TestVC implements Initializable {
//
//    @FXML
//    private TableView<Company> CompanyTable;
//
//    @FXML
//    private TableColumn<Company, String> companyname;
//
//    @FXML
//    private TableColumn<Company, String> workers;
//
//    @FXML
//    private TableView<Timeblock> EmployeeTable;
//
//    @FXML
//    private TableColumn<Timeblock, String> header1;
//
//    @FXML
//    private TableColumn<Timeblock, String> header2;
//
//    @FXML
//    private TableView<Timeblock> EmployeeTable2;
//
//    @FXML
//    private ListView<String> employeelistview;
//
//    @FXML
//    private Label AddEmployee;
//
//    @FXML
//    private ScrollPane Schedule;
//
//    @FXML
//    private ScrollPane scp;
//
//    @FXML
//    private ScrollPane employeescp;
//
//    @FXML
//    private SplitPane root;
//
//    @FXML
//    private Button addEmployeeButton;
//
//    @FXML
//    private Button screenshot;
//
//    @FXML
//    private Button saveButton;
//
//    @FXML
//    private Button deleteButton;
//
//    @FXML
//    private Button addcompany;
//
//    @FXML
//    private Button deletecompany;
//
//    @FXML
//    private Button insertcompany;
//
//    private ObservableList<Company> companylist = FXCollections.observableArrayList();
//    private ObservableList<String> employeelist = FXCollections.observableArrayList();
//    private ArrayList<Schedule> cachedemployee;
//
//
//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//
//        initializelist();
//        clickonEmployee();
//        draglocation();
//        clickonaddbutton();
//        saveTable();
//        //setScreenshot();
//        setDeleteButton();
//        setCompanyButton();
//    }
//
//    public void initializelist() {
//        ArrayList<Company> templist1;
//        Helper helper = new Helper();
//        templist1 = helper.readCompany();
//        cachedemployee = helper.readEmployee();
//
//        setEmployeeTable();
//        int Arraylistlength1 = templist1.size();
//        int Arraylistlength2 = cachedemployee.size();
//        for (int i = 0; i < Arraylistlength1; i++) {
//            companylist.add(templist1.get(i));
//        }
//        companyname.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
//        //workers.setCellValueFactory(cellData -> cellData.getValue().workerProperty());
//        CompanyTable.setItems(companylist);
//
//        for (int i = 0; i < Arraylistlength2; i++) {
//            employeelist.add(cachedemployee.get(i).getName());
//        }
//        employeelistview.setItems(employeelist);
//
//
//    }
//
//    public String getdayInCHN(int datenum){
//        String dayCHN;
//
//        Date date = new Date();
//
//        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.DATE, datenum);
//        int monthnum = date.getMonth() + 1;
//        if(monthnum >= 12){
//            monthnum = 0;
//        }
//        cal.set(Calendar.MONTH, monthnum);
//        cal.set(Calendar.YEAR, date.getYear() + 1900);
//
//        SimpleDateFormat sdf = new SimpleDateFormat("EEEE",Locale.CHINA);
//        String temp = sdf.format(cal.getTime());
//
//        if(temp.equals("星期一")){
//            dayCHN = "周一";
//        }
//        else if(temp.equals("星期二")){
//            dayCHN = "周二";
//        }
//        else if(temp.equals("星期三")){
//            dayCHN = "周三";
//        }
//        else if(temp.equals("星期四")){
//            dayCHN = "周四";
//        }
//        else if(temp.equals("星期五")){
//            dayCHN = "周五";
//        }
//        else if(temp.equals("星期六")){
//            dayCHN = "周六";
//        }
//        else {
//            dayCHN = "周日";
//        }
//
//
//        return dayCHN;
//    }
//
//    public void setEmployeeTable() {
//        for (int i = 1; i < 17; i++) {
//            TableColumn<Timeblock, String> temp = new TableColumn(Integer.toString(i)+"("+getdayInCHN(i)+")");
//            temp.setPrefWidth(69);
//
//            int index = i - 1;
//            temp.setCellValueFactory(cellData -> cellData.getValue().getLocationElement(index));
//            EmployeeTable.getColumns().add(temp);
//        }
//        header1.setCellValueFactory(cellData -> cellData.getValue().timenameProperty());
//
//        for (int i = 17; i < 32; i++) {
//            TableColumn<Timeblock, String> temp = new TableColumn(Integer.toString(i)+"("+getdayInCHN(i)+")");
//            temp.setPrefWidth(69);
//            int index = i - 1;
//            temp.setCellValueFactory(cellData -> cellData.getValue().getLocationElement(index));
//            EmployeeTable2.getColumns().add(temp);
//        }
//        header2.setCellValueFactory(cellData -> cellData.getValue().timenameProperty());
//
//        EmployeeTable.setStyle("-fx-font-size :12");
//        EmployeeTable2.setStyle("-fx-font-size :12");
//    }
//
//    public void newEmployeeClicked(Schedule employee) {
//
//        if (cachedemployee.contains(employee) == false) {
//            cachedemployee.add(employee);
//        }
//        ObservableList<Timeblock> timeblocklist = FXCollections.observableArrayList();
//        ArrayList<Timeblock> originaltimeblocklist = employee.getTimeblocks();
//
//        for (int i = 0; i < 12; i++) {
//            timeblocklist.add(employee.getSingleTimeBlock(i));
//        }
//
//        EmployeeTable.setItems(timeblocklist);
//
//        EmployeeTable2.setItems(timeblocklist);
//
//        EmployeeTable.refresh();
//
//    }
//
//    public void clickonEmployee() {
//        employeelistview.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                String string = employeelistview.getFocusModel().getFocusedItem();
//                Schedule clickedemployee = null;
//                for (Schedule e : cachedemployee) {
//                    if (e.getName().equals(string)) {
//                        clickedemployee = e;
//                        break;
//                    }
//                }
//                newEmployeeClicked(clickedemployee);
//            }
//        });
//    }
//
//    public void draglocation() {
//        CompanyTable.setOnDragDetected(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                int index = CompanyTable.getFocusModel().getFocusedIndex();
//                Company dragedcompany = companylist.get(index);
//
//                Dragboard db = CompanyTable.startDragAndDrop(TransferMode.COPY);
//
//                ClipboardContent content = new ClipboardContent();
//                content.putString(dragedcompany.getName());
//                db.setContent(content);
//
//                event.consume();
//                //System.out.println("Drag " + content.getString());
//            }
//        });
//
//        EmployeeTable.setRowFactory(tv -> {
//            TableRow<Timeblock> row = new TableRow<>();
//
//
//            row.setOnDragOver(new EventHandler<DragEvent>() {
//                @Override
//                public void handle(DragEvent event) {
//                    event.acceptTransferModes(TransferMode.COPY);
//                    //System.out.println("Drag enter ");
//                    event.consume();
//                }
//            });
//
//            row.setOnDragDropped(new EventHandler<DragEvent>() {
//                @Override
//                public void handle(DragEvent event) {
//
//                    int xpos = (int) event.getSceneX();
//                    int colindex = (xpos - 200) / 69;
//                    if (colindex != 0) {
//                        int rowindex = row.getIndex();
//                        String str = event.getDragboard().getString();
//                        //System.out.println(str);
//                        String newstr = "";
//                        if (str.length() > 5) {
//                            for (int i = 0; i < 5; i++) {
//                                newstr = newstr + str.charAt(i);
//                            }
//                            newstr = newstr + "\n";
//                            for (int i = 5; i < str.length(); i++) {
//                                newstr = newstr + str.charAt(i);
//                            }
//                        } else {
//                            newstr = str;
//                        }
//                        EmployeeTable.getItems().get(rowindex).setLocationElement(newstr, colindex - 1);
//                        EmployeeTable.refresh();
//                    }
//                    event.consume();
//                }
//            });
//
//            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    int clickcount = event.getClickCount();
//                    if (clickcount == 2) {
//                        int xpos = (int) event.getSceneX();
//                        int colindex = (xpos - 200) / 69;
//                        if (colindex != 0) {
//                            int rowindex = row.getIndex();
//                            EmployeeTable.getItems().get(rowindex).setLocationElement("/", colindex - 1);
//                            EmployeeTable.refresh();
//                        }
//                    }
//                    event.consume();
//                }
//            });
//            return row;
//        });
//
//        EmployeeTable2.setRowFactory(tv -> {
//            TableRow<Timeblock> row = new TableRow<>();
//
//
//            row.setOnDragOver(new EventHandler<DragEvent>() {
//                @Override
//                public void handle(DragEvent event) {
//                    event.acceptTransferModes(TransferMode.COPY);
//                    //System.out.println("Drag enter ");
//                    event.consume();
//                }
//            });
//
//            row.setOnDragDropped(new EventHandler<DragEvent>() {
//                @Override
//                public void handle(DragEvent event) {
//                    int xpos = (int) event.getSceneX();
//                    int colindex = (xpos - 200) / 69;
//                    if (colindex != 0) {
//                        int rowindex = row.getIndex();
//                        String str = event.getDragboard().getString();
//                        String newstr = "";
//                        if (str.length() > 5) {
//                            for (int i = 0; i < 5; i++) {
//                                newstr = newstr + str.charAt(i);
//                            }
//                            newstr = newstr + "\n";
//                            for (int i = 5; i < str.length(); i++) {
//                                newstr = newstr + str.charAt(i);
//                            }
//                        } else {
//                            newstr = str;
//                        }
//                        EmployeeTable2.getItems().get(rowindex).setLocationElement(newstr, 16 + colindex - 1);
//                        EmployeeTable2.refresh();
//                    }
//                    event.consume();
//                }
//            });
//
//            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    int clickcount = event.getClickCount();
//                    if (clickcount == 2) {
//                        int xpos = (int) event.getSceneX();
//                        int colindex = (xpos - 200) / 69;
//                        if (colindex != 0) {
//                            int rowindex = row.getIndex();
//                            EmployeeTable2.getItems().get(rowindex).setLocationElement("/", 16 + colindex - 1);
//                            EmployeeTable2.refresh();
//                        }
//                    }
//                    event.consume();
//                }
//            });
//            return row;
//        });
//
//    }
//
//    public void clickonaddbutton() {
//        addEmployeeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                addnew();
//            }
//        });
//    }
//
//    public void addnew() {
//        Stage secondwindow = new Stage();
//        BorderPane bd = new BorderPane();
//
//        TextField textField = new TextField();
//        Button confirmbutton = new Button();
//        confirmbutton.setText("确认");
//        textField.setEditable(true);
//        textField.setText("请输入员工名");
//
//        bd.setLeft(textField);
//        bd.setRight(confirmbutton);
//
//        Scene scene = new Scene(bd, 300, 30);
//        secondwindow.setScene(scene);
//
//        secondwindow.setTitle("添加员工");
//        secondwindow.show();
//
//        confirmbutton.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                Helper helper = new Helper();
//                String str = textField.getText();
//                //helper.addemployee(str);
//                Schedule employee = new Schedule(str);
//                initialEmployee(employee);
//                employeelist.add(str);
//                newEmployeeClicked(employee);
//                event.consume();
//                secondwindow.close();
//            }
//        });
//    }
//
//    public void addnewcom(){
//        Stage secondwindow = new Stage();
//        BorderPane bd = new BorderPane();
//
//        TextField textField = new TextField();
//        Button confirmbutton = new Button();
//        confirmbutton.setText("确认");
//        textField.setEditable(true);
//        textField.setText("请输入公司名");
//
//        bd.setLeft(textField);
//        bd.setRight(confirmbutton);
//
//        Scene scene = new Scene(bd, 300, 30);
//        secondwindow.setScene(scene);
//
//        secondwindow.setTitle("添加公司");
//        secondwindow.show();
//
//        confirmbutton.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                Helper helper = new Helper();
//                String str = textField.getText();
//
//                Company company = new Company(str);
//                companylist.add(company);
//                helper.writeaddcompany(company);
//
//                event.consume();
//                secondwindow.close();
//            }
//        });
//    }
//
//    public void insertnewcom(int index){
//        Stage secondwindow = new Stage();
//        BorderPane bd = new BorderPane();
//
//        TextField textField = new TextField();
//        Button confirmbutton = new Button();
//        confirmbutton.setText("确认");
//        textField.setEditable(true);
//        textField.setText("请输入公司名");
//
//        bd.setLeft(textField);
//        bd.setRight(confirmbutton);
//
//        Scene scene = new Scene(bd, 300, 30);
//        secondwindow.setScene(scene);
//
//        secondwindow.setTitle("添加公司");
//        secondwindow.show();
//
//        confirmbutton.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                Helper helper = new Helper();
//                String str = textField.getText();
//
//                Company company = new Company(str);
//                companylist.add(index+1,company);
//                helper.writedeletecompany(companylist);
//
//                event.consume();
//                secondwindow.close();
//            }
//        });
//    }
//
//    public void initialEmployee(Schedule employee) {
//        for (int i = 0; i < 12; i++) {
//            for (int j = 0; j < 31; j++) {
//                employee.getSingleTimeBlock(i).addLocationElement("/");
//            }
//        }
//    }
//
//    public void saveTable() {
//        saveButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                Helper helper = new Helper();
//                helper.writesaveddata(cachedemployee);
//                event.consume();
//            }
//        });
//    }
//
////    public void setScreenshot() {
////        screenshot.setOnMouseClicked(new EventHandler<MouseEvent>() {
////            @Override
////            public void handle(MouseEvent event) {
////
////                try {
////                    SnapshotParameters param = new SnapshotParameters();
////                    param.setDepthBuffer(true);
////                    param.setFill(Color.CORNSILK);
////                    WritableImage snapshot = EmployeeTable.snapshot(param, null);
////                    BufferedImage tempImg = SwingFXUtils.fromFXImage(snapshot, null);
////                    WritableImage snapshot2 = EmployeeTable2.snapshot(param, null);
////                    BufferedImage tempImg2 = SwingFXUtils.fromFXImage(snapshot2, null);
////
////                    String employeename = employeelistview.getFocusModel().getFocusedItem();
////
////                    Path currentRelativePath = Paths.get("");
////                    String s = currentRelativePath.toAbsolutePath().toString();
////                    //File outputfile = new File(s + "/Snapshot/"+employeename+"First.png");
////                    File outputfile = new File(s + "\\Snapshot\\"+employeename+"First.png");
////                    ImageIO.write(tempImg, "png", outputfile);
////
////                    //File outputfile2 = new File(s + "/Snapshot/"+employeename+"Second.png");
////                    File outputfile2 = new File(s + "\\Snapshot\\"+employeename+"Second.png");
////                    ImageIO.write(tempImg2, "png", outputfile2);
////
////
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////
////                event.consume();
////            }
////        });
////
////    }
//
//    public void setDeleteButton(){
//        deleteButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                String name = employeelistview.getFocusModel().getFocusedItem();
//                for(String s : employeelist){
//                    if(s.equals(name)){
//                        employeelist.remove(s);
//                        break;
//                    }
//                }
//
//                for(Schedule e: cachedemployee){
//                    if(e.getName().equals(name)){
//                        cachedemployee.remove(e);
//                        break;
//                    }
//                }
//                employeelistview.refresh();
//
//                event.consume();
//            }
//        });
//    }
//
//    public void setCompanyButton(){
//        addcompany.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                addnewcom();
//                event.consume();
//            }
//        });
//
//        deletecompany.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                Company company = CompanyTable.getFocusModel().getFocusedItem();
//                companylist.remove(company);
//                CompanyTable.refresh();
//                Helper helper = new Helper();
//                helper.writedeletecompany(companylist);
//                event.consume();
//            }
//        });
//
//        insertcompany.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                int index = CompanyTable.getFocusModel().getFocusedIndex();
//                insertnewcom(index);
//                event.consume();
//            }
//        });
//    }
//}
