package com.synir;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/*
Main class that uses JavaFx main class ScheduleManagement as bootstrapper.
This class is the entry point of the application.
*/
@SpringBootApplication
//@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SpringBooter {
    public static void main(String[] args) {
        //Takes JavaFx main class instance to bootstrap
        Application.launch(ScheduleManagementUIApp.class, args);
    }
}
