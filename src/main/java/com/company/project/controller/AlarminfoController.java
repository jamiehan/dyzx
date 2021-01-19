package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.entity.BlacklistEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import com.company.project.common.utils.DataResult;

import com.company.project.entity.AlarminfoEntity;
import com.company.project.service.AlarminfoService;



/**
 * 
 *
 * @author Jamie
 * @email *****@mail.com
 * @date 2021-01-18 15:24:22
 */
@Controller
@RequestMapping("/")
public class AlarminfoController {
    @Autowired
    private AlarminfoService alarminfoService;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/alarminfo")
    public String alarminfo() {
        return "alarminfo/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("alarminfo/add")
    @RequiresPermissions("alarminfo:add")
    @ResponseBody
    public DataResult add(@RequestBody AlarminfoEntity alarminfo){
        alarminfoService.save(alarminfo);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("alarminfo/delete")
    @RequiresPermissions("alarminfo:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        alarminfoService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("alarminfo/update")
    @RequiresPermissions("alarminfo:update")
    @ResponseBody
    public DataResult update(@RequestBody AlarminfoEntity alarminfo){
        alarminfoService.updateById(alarminfo);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("alarminfo/listByPage")
    @RequiresPermissions("alarminfo:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody AlarminfoEntity alarminfo){
        Page page = new Page(alarminfo.getPage(), alarminfo.getLimit());
        LambdaQueryWrapper<AlarminfoEntity> queryWrapper = Wrappers.lambdaQuery();
        //根据黑名单姓名模糊查询
        if(StringUtils.isNotBlank(alarminfo.getBlackname())) {
            queryWrapper.like(AlarminfoEntity::getBlackname, alarminfo.getBlackname());
        }
        IPage<AlarminfoEntity> iPage = alarminfoService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

}
