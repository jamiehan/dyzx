package com.company.project.controller;

import com.company.project.service.HttpSessionService;
import io.swagger.annotations.Api;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 视图
 *
 * @author Jamie
 * @version V1.0
 * @date 2020年11月25日
 */
@Api(tags = "视图")
@Controller
@RequestMapping("/index")
public class IndexController {

    @Resource
    private HttpSessionService httpSessionService;

    @Resource
    private HttpServletResponse response;

    @GetMapping("/login")
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return "redirect:/index/home" ;
        }
        return "login" ;
    }

    @GetMapping("/selectRobot")
    public String selectRobot(Model model, String code) {
//        System.out.println("当前选择的机器人编码是：" + code);
        model.addAttribute("code",code);
        return "main";
    }

    @GetMapping("/home")
    public String home() {
        return "home" ;
    }

    @GetMapping("/index2")
    public String index2() {
        return "index2" ;
    }

    @GetMapping("/robotctl")
    public String robotCtl() {
        return "robots/robot_ctl" ;
    }

    @GetMapping("/onekeyalarm")
    public String oneKeyAlarm() {
        return "alarm/onekeyalarm" ;
    }

    @GetMapping("/facealarm")
    public String faceAlarm() {
        return "alarm/facealarm" ;
    }

    @GetMapping("/gatheringalarm")
    public String gatheringAlarm() {
        return "alarm/gatheringalarm" ;
    }

    @GetMapping("/walkongrassalarm")
    public String walkOnGrassAlarm() {
        return "alarm/walkongrassalarm" ;
    }

    @GetMapping("/users/password")
    public String updatePassword() {
        return "users/update_password" ;
    }

    @GetMapping("/users/info")
    public String userDetail(Model model) {
        model.addAttribute("flagType", "edit");
        return "users/user_edit" ;
    }

    @GetMapping("/menus")
    public String menusList() {

        return "menus/menu_list" ;
    }

    @GetMapping("/roles")
    public String roleList() {
        return "roles/role_list" ;
    }

    @GetMapping("/users")
    public String userList() {
        return "users/user_list" ;
    }

    @GetMapping("/logs")
    public String logList() {
        return "logs/log_list" ;
    }

    @GetMapping("/depts")
    public String deptList() {
        return "depts/dept_list" ;
    }

    @GetMapping("/403")
    public String error403() {
        return "error/403" ;
    }

    @GetMapping("/404")
    public String error404() {
        return "error/404" ;
    }

    @GetMapping("/500")
    public String error405() {
        return "error/500" ;
    }

    @GetMapping("/main")
    public String indexHome() {
        return "main" ;
    }

    @GetMapping("/about")
    public String about() {
        return "about" ;
    }

    @GetMapping("/build")
    public String build() {
        return "build" ;
    }

    @GetMapping("/sysContent")
    public String sysContent() {
        return "syscontent/list" ;
    }

    @GetMapping("/sysDict")
    public String sysDict() {
        return "sysdict/list" ;
    }

    @GetMapping("/sysGenerator")
    public String sysGenerator() {
        return "generator/list" ;
    }

    @GetMapping("/sysJob")
    public String sysJob() {
        return "sysjob/list";
    }

    @GetMapping("/sysJobLog")
    public String sysJobLog() {
        return "sysjoblog/list";
    }

    @GetMapping("/sysFiles")
    public String sysFiles() {
        return "sysfiles/list";
    }
}
