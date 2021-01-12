package com.company.project.controller;

import cn.hutool.core.io.LineHandler;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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

import com.company.project.entity.BlacklistEntity;
import com.company.project.service.BlacklistService;



/**
 * 
 *
 * @author Jamie
 * @email *****@mail.com
 * @date 2020-12-30 15:25:30
 */
@Controller
@RequestMapping("/")
public class BlacklistController {
    @Autowired
    private BlacklistService blacklistService;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/blacklist")
    public String blacklist() {
        return "blacklist/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("blacklist/add")
    @RequiresPermissions("blacklist:add")
    @ResponseBody
    public DataResult add(@RequestBody BlacklistEntity blacklist){
        blacklistService.save(blacklist);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("blacklist/delete")
    @RequiresPermissions("blacklist:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        blacklistService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("blacklist/update")
    @RequiresPermissions("blacklist:update")
    @ResponseBody
    public DataResult update(@RequestBody BlacklistEntity blacklist){
        blacklistService.updateById(blacklist);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("blacklist/listByPage")
    @RequiresPermissions("blacklist:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody BlacklistEntity blacklist){
        Page page = new Page(blacklist.getPage(), blacklist.getLimit());
        LambdaQueryWrapper<BlacklistEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        if(StringUtils.isNotBlank(blacklist.getXingming())) {
            queryWrapper.like(BlacklistEntity::getXingming, blacklist.getXingming());
        }
        IPage<BlacklistEntity> iPage = blacklistService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }



}
