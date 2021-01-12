package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.sun.deploy.util.BlackList;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.BlacklistMapper;
import com.company.project.entity.BlacklistEntity;
import com.company.project.service.BlacklistService;

import javax.annotation.Resource;


@Service("blacklistService")
public class BlacklistServiceImpl extends ServiceImpl<BlacklistMapper, BlacklistEntity> implements BlacklistService {

    @Resource
    public BlacklistMapper blacklistMapper;

    /**
     * 根据人像识别ID获取黑名单人员对象信息
     * @param personId
     * @return
     */
    public BlacklistEntity getBlackListByPersonId(String personId){

        QueryWrapper<BlacklistEntity> queryWrapper = new QueryWrapper();
        queryWrapper.eq("person_id",personId);
        return blacklistMapper.selectOne(queryWrapper);
    }
}