package com.company.project.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.RobotMapper;
import com.company.project.entity.RobotEntity;
import com.company.project.service.RobotService;


@Service("robotService")
public class RobotServiceImpl extends ServiceImpl<RobotMapper, RobotEntity> implements RobotService {


}