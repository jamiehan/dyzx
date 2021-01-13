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

import com.company.project.entity.RobotEntity;
import com.company.project.service.RobotService;



/**
 * 
 *
 * @author Jamie
 * @email *****@mail.com
 * @date 2021-01-12 10:42:11
 */
@Controller
@RequestMapping("/")
public class RobotController {
    @Autowired
    private RobotService robotService;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/robot")
    public String robot() {
        return "robot/list";
    }

    @ApiOperation(value = "新增")
    @PostMapping("robot/add")
    @RequiresPermissions("robot:add")
    @ResponseBody
    public DataResult add(@RequestBody RobotEntity robot){
        robotService.save(robot);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("robot/delete")
    @RequiresPermissions("robot:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        robotService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("robot/update")
    @RequiresPermissions("robot:update")
    @ResponseBody
    public DataResult update(@RequestBody RobotEntity robot){
        robotService.updateById(robot);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("robot/listByPage")
    @RequiresPermissions("robot:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody RobotEntity robot){
        Page page = new Page(robot.getPage(), robot.getLimit());
        LambdaQueryWrapper<RobotEntity> queryWrapper = Wrappers.lambdaQuery();
        //根据机器人编号模糊查询
        if(StringUtils.isNotBlank(robot.getBianhao())) {
            queryWrapper.like(RobotEntity::getBianhao, robot.getBianhao());
        }
        IPage<RobotEntity> iPage = robotService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

}
