package com.company.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.entity.BlacklistEntity;

/**
 * 
 *
 * @author Jamie
 * @email *****@mail.com
 * @date 2020-12-30 15:25:30
 */
public interface BlacklistService extends IService<BlacklistEntity> {

    BlacklistEntity getBlackListByPersonId(String personId);
}

