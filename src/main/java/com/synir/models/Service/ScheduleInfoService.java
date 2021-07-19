package com.synir.models.Service;

import com.synir.models.POJO.ScheduleInfo;

import java.util.List;

public interface ScheduleInfoService {
    public void add(ScheduleInfo schedule);
    public void remove(ScheduleInfo scheduleInfo);
    public List<ScheduleInfo> getList();
    public List<ScheduleInfo>findByCreatorOrLevelGreaterThanEqual(String creator,int level);

}
