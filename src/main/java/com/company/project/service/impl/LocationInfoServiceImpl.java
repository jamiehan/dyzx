package com.company.project.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.LocationInfoMapper;
import com.company.project.entity.LocationInfoEntity;
import com.company.project.service.LocationInfoService;


@Service("locationInfoService")
public class LocationInfoServiceImpl extends ServiceImpl<LocationInfoMapper, LocationInfoEntity> implements LocationInfoService {


}