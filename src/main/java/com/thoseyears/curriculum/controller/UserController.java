package com.thoseyears.curriculum.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.thoseyears.curriculum.dao.UserDAO;
import com.thoseyears.curriculum.entity.User;
import com.thoseyears.curriculum.service.serviceimpl.UserServiceImpl;
import com.thoseyears.curriculum.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//证明是controller层并且返回json
@RestController
@RequestMapping("/Curriculum/User")
public class UserController {
    //依赖注入
    @Autowired
    UserDAO userDAO;
    @Autowired
    UserServiceImpl usi;
    @GetMapping(value="/checkUser/{userid}")
    public String CheckUser(@PathVariable(name = "userid") String userid, @RequestParam(name = "password") String password) throws Exception {
        //验证用户登录并且返回token
        return usi.checkUser(userid,password);
    }

    /**
     * @desc 验证TOKEN接口实例
     * @param token
     * @return
     */
    @GetMapping(value="/checkToken/{token}")
    public boolean CheckToken(@PathVariable(name = "token") String token) {
        return JwtUtil.checkToken(token);
    }

    /**
     * @desc 返回json格式设置事例
     * @param userid
     * @param username
     * @param phone
     * @return
     */
    @GetMapping("/allUser/{userid}")
    public String findAllUser(@PathVariable(name = "userid") String userid, @RequestParam(name = "name") String username, @RequestParam(name = "phone") String phone) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        List<User> users = userDAO.selectList(queryWrapper);
        //转换json格式
        JSONObject result = new JSONObject();
        result.put("msg", "ok");
        result.put("code", "0");
        result.put("count", users.size());
        result.put("data", users);
        return result.toJSONString();
    }


}
