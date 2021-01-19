package com.company.project.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.AlarminfoMapper;
import com.company.project.entity.AlarminfoEntity;
import com.company.project.service.AlarminfoService;


@Service("alarminfoService")
public class AlarminfoServiceImpl extends ServiceImpl<AlarminfoMapper, AlarminfoEntity> implements AlarminfoService {


}