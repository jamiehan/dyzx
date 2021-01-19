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

import com.company.project.entity.OnekeyalarmEntity;
import com.company.project.service.OnekeyalarmService;



/**
 * 
 *
 * @author Jamie
 * @email *****@mail.com
 * @date 2021-01-18 13:02:06
 */
@Controller
@RequestMapping("/")
public class OnekeyalarmController {
    @Autowired
    private OnekeyalarmService onekeyalarmService;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/onekeyalarminfo")
    public String onekeyalarm() {
        return "onekeyalarm/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("onekeyalarm/add")
    @RequiresPermissions("onekeyalarm:add")
    @ResponseBody
    public DataResult add(@RequestBody OnekeyalarmEntity onekeyalarm){
        onekeyalarmService.save(onekeyalarm);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("onekeyalarm/delete")
    @RequiresPermissions("onekeyalarm:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        onekeyalarmService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("onekeyalarm/update")
    @RequiresPermissions("onekeyalarm:update")
    @ResponseBody
    public DataResult update(@RequestBody OnekeyalarmEntity onekeyalarm){
        onekeyalarmService.updateById(onekeyalarm);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("onekeyalarm/listByPage")
    @RequiresPermissions("onekeyalarm:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody OnekeyalarmEntity onekeyalarm){
        Page page = new Page(onekeyalarm.getPage(), onekeyalarm.getLimit());
        LambdaQueryWrapper<OnekeyalarmEntity> queryWrapper = Wrappers.lambdaQuery();
        //根据机器人编号模糊查询
        if(StringUtils.isNotBlank(onekeyalarm.getBianhao())) {
            queryWrapper.like(OnekeyalarmEntity::getBianhao, onekeyalarm.getBianhao());
        }
        IPage<OnekeyalarmEntity> iPage = onekeyalarmService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

}
