package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
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

import com.company.project.entity.LocationInfoEntity;
import com.company.project.service.LocationInfoService;



/**
 * 
 *
 * @author Jamie
 * @email *****@mail.com
 * @date 2021-02-25 16:56:48
 */
@Controller
@RequestMapping("/")
public class LocationInfoController {
    @Autowired
    private LocationInfoService locationInfoService;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/locationInfo")
    public String locationInfo() {
        return "locationinfo/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("locationInfo/add")
    @RequiresPermissions("locationInfo:add")
    @ResponseBody
    public DataResult add(@RequestBody LocationInfoEntity locationInfo){
        locationInfoService.save(locationInfo);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("locationInfo/delete")
    @RequiresPermissions("locationInfo:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        locationInfoService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("locationInfo/update")
    @RequiresPermissions("locationInfo:update")
    @ResponseBody
    public DataResult update(@RequestBody LocationInfoEntity locationInfo){
        locationInfoService.updateById(locationInfo);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("locationInfo/listByPage")
    @RequiresPermissions("locationInfo:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody LocationInfoEntity locationInfo){
        Page page = new Page(locationInfo.getPage(), locationInfo.getLimit());
        LambdaQueryWrapper<LocationInfoEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(LocationInfoEntity::getId, locationInfo.getId());
        IPage<LocationInfoEntity> iPage = locationInfoService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

    @ApiOperation(value = "获取巡航位置信息")
    @GetMapping("robot/getLocationInfoList")
    @ResponseBody
    public DataResult getLocationInfoList(String robotCode){

        List<LocationInfoEntity> locationInfoList = null;
        LambdaQueryWrapper<LocationInfoEntity> queryWrapper = Wrappers.lambdaQuery();
        //根据机器人编号查询
        if(StringUtils.isNotBlank(robotCode)) {
            queryWrapper.eq(LocationInfoEntity::getRobotCode, robotCode);
            locationInfoList = locationInfoService.list(queryWrapper);

        }

        return DataResult.success(locationInfoList);
    }

}
