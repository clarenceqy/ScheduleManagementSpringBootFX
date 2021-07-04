package com.synir.utility;

import com.synir.models.POJO.Schedule;
import com.synir.models.POJO.ScheduleInfo;
import com.synir.models.POJO.Timeblock;
import com.synir.models.Service.ScheduleInfoService;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Component;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class Helper {
    public final String envpath;
    private boolean newSchedulestatus;
    private String schedulUID;
    private int id;
    private String title;
    private String creator;


    private ScheduleInfoService scheduleInfoService;

    public Helper() {
        newSchedulestatus = true;
        File file = new File("");
        this.envpath = file.getAbsolutePath() + File.separator + "Datafile";
        file = new File(envpath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public int computeLevel(int level, String s){
        switch (s){
            case "权限1":
                return 1;
            case "权限2":
                return 2;
            case "权限3":
                return 3;
            case "权限4":
                return 4;
            case "权限5":
                return 5;
            default:
                break;
        }
        return level - 1 > 0 ? level - 1 : 1;
    }

    public void setService(ScheduleInfoService scheduleInfoService){
        this.scheduleInfoService = scheduleInfoService;
    }
    public void setId(int id){this.id = id;}
    public int getId(){return this.id;}
    public void setTitle(String title){this.title = title;}
    public String getTitle(){return this.title;}
    public void setCreator(String creator){this.creator = creator;}
    public String getCreator(){return this.creator;}
    public String getEnvpath(){return this.envpath;}
    public void setNewSchedulestatus(boolean newSchedulestatus){this.newSchedulestatus = newSchedulestatus;}
    public boolean getNewSchedulesstate(){return this.newSchedulestatus;}

    public void writeCompany(ObservableList<String> companylist) {
        String pathstr = envpath + File.separator + "Company.dat";
        try (DataOutputStream locFile = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(pathstr)))) {
            for (String com : companylist) {
                locFile.writeUTF(com);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> readCompany() {
        String pathstr = envpath + File.separator + "Company.dat";
        File file = new File(pathstr);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<String> res = new ArrayList<>();
        try (DataInputStream locFile = new DataInputStream(new BufferedInputStream(new FileInputStream(pathstr)))) {
            boolean eof = false;
            while (!eof) {
                try {
                    String company = locFile.readUTF();
                    res.add(company);
                } catch (EOFException e) {
                    eof = true;
                }
            }
        } catch (FileNotFoundException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public void writeSchedule(ObservableList<Timeblock> ls, String fileUID) {
        String pathstr = envpath + File.separator + fileUID + ".dat";

        try (DataOutputStream locFile = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(pathstr)))) {
            for (Timeblock tb : ls) {
               for (int i = 0; i < 31;i++){
                   String s = tb.getLocationElement(i).getValue().replaceAll("\n","");
                   locFile.writeUTF(s);
               }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Schedule readSchedule(String filename) {
        Schedule schedule = new Schedule("未命名");
        FileUploadUtil ftp = new FileUploadUtil();
        try {
            ftp.downloadSchedule("/usr/local/testdat",filename+".dat");
        }catch (IOException e){

        }
        String pathstr = envpath + File.separator + filename + ".dat";
        File file = new File(pathstr);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return schedule;
        }
        try (DataInputStream locFile = new DataInputStream(new BufferedInputStream(new FileInputStream(pathstr)))) {
            boolean eof = false;
            int x = 0;
            while (!eof && x < 12) {
                try {
                    for(int i = 0; i < 31; i++){
                        schedule.getSingleTimeBlock(x).setLocationElement(locFile.readUTF(),i);
                    }
                    x++;
                } catch (EOFException e) {
                    eof = true;
                }
            }
        } catch (FileNotFoundException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }
        file.delete();
        return schedule;
    }

    public String getScheduleUID() {
        return schedulUID;
    }

    public void setScheduleUID(String schedulUID) {
        this.schedulUID = schedulUID;
    }

    public String generateUID(){
        return UUID.randomUUID().toString();
    }
    public void uploadSchedule(String creator,String title,int level,String uuid,int id){
        Date date = new Date();
        String strDateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);

        ScheduleInfo scheduleInfo = new ScheduleInfo();
        scheduleInfo.setCreator(creator);
        scheduleInfo.setTitle(title);
        scheduleInfo.setLevel(level);
        scheduleInfo.setUuid(uuid);
        scheduleInfo.setTimestamp(sdf.format(date));
        if(id != -1) {
            scheduleInfo.setId(id);
        }
        scheduleInfoService.add(scheduleInfo);
    }


    public List<ScheduleInfo> getScheduleList(String creator,int level){
        return scheduleInfoService.findByCreatorOrLevelGreaterThanEqual(creator,level);
    }
}
