package com.miko.service;

import com.miko.entity.BotTaskModel;
import com.miko.mapper.BotTaskMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BotTaskService {

    @Resource
    private BotTaskMapper botTaskMapper;

    public List<BotTaskModel> getAllActive(){
        return botTaskMapper.getAllActive();
    };
}
