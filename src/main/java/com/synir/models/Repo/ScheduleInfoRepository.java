package com.synir.models.Repo;

import java.util.List;

import com.synir.models.POJO.ScheduleInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleInfoRepository extends JpaRepository<ScheduleInfo,Integer>{
    List<ScheduleInfo>findByCreatorOrLevelGreaterThan(String creator,int level);
}
