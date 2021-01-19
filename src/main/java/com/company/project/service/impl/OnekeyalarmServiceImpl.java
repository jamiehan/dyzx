package com.company.project.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.OnekeyalarmMapper;
import com.company.project.entity.OnekeyalarmEntity;
import com.company.project.service.OnekeyalarmService;


@Service("onekeyalarmService")
public class OnekeyalarmServiceImpl extends ServiceImpl<OnekeyalarmMapper, OnekeyalarmEntity> implements OnekeyalarmService {


}