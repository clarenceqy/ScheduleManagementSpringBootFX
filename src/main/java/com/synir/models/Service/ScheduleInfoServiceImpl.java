package com.synir.models.Service;

import java.util.List;
import com.synir.models.POJO.ScheduleInfo;
import com.synir.models.Repo.ScheduleInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ScheduleInfoServiceImpl implements ScheduleInfoService{

    @Autowired
    private ScheduleInfoRepository repository;

    @Override
    public List<ScheduleInfo> getList() {
        Sort sort = Sort.by(Sort.Direction.DESC, "timestamp");
        return repository.findAll(PageRequest.of(0, 10, sort)).getContent();
    }

    @Override
    public void add(ScheduleInfo schedule) {
        repository.save(schedule);

    }

    @Override
    public List<ScheduleInfo> findByCreatorOrLevelGreaterThanEqual(String creator, int level) {

        return repository.findByCreatorOrLevelGreaterThan(creator,level);
    }



}
