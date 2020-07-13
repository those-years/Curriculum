package com.thoseyears.curriculum.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.thoseyears.curriculum.dao.UserDAO;
import com.thoseyears.curriculum.entity.User;
import com.thoseyears.curriculum.service.serviceimpl.UserServiceImpl;
import com.thoseyears.curriculum.util.Constant;
import com.thoseyears.curriculum.util.JwtUtil;
import com.thoseyears.curriculum.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//证明是controller层并且返回json
@RestController
@RequestMapping("/Curriculum/User")
@Api(tags = "用户服务相关接口描述")
@CrossOrigin("http://127.0.0.1:8848/competition/login.html")
public class UserController {
    //依赖注入
    @Autowired
    UserDAO userDAO;
    @Autowired
    UserServiceImpl usi;
    @ApiOperation(value = "用户登录接口",
            notes = "<span style='color:red;'>描述:</span>&nbsp;用户登录的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userid",value = "用户id",dataType = "String",defaultValue = "117583010123"),
            @ApiImplicitParam(name="password",value = "用户登录密码",dataType ="String",defaultValue = "123456")
    })
    @GetMapping(value="/checkUser/{userid}")
    public String CheckUser(@PathVariable(name = "userid") String userid, @RequestParam(name = "password") String password) throws Exception {
        //验证用户登录并且返回token
        JSONObject result = new JSONObject();
        String token = usi.checkUser(userid,password);
        if(token.equals("")){
            result.put("msg", "用户名密码不正确");
            result.put("code", "404");
            result.put("count","0");
            result.put("data", null);
            return result.toJSONString();
        }
        net.sf.json.JSONObject s =  JwtUtil.getUser(token);
        result.put("msg", "ok");
        result.put("code", "200");
        result.put("count","1");
        result.put("type",s.get("type"));
        result.put("data", usi.checkUser(userid,password));
        result.put("userid", s.get("userid"));
        return result.toJSONString();
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


    @GetMapping("/getUser/{userid}")
    public String getUser(@RequestHeader(name="TOKEN")String TOKEN,@PathVariable(name = "userid") String userid) {
        User user = usi.findlUserByid(userid);
        //转换json格式
        JSONObject result = new JSONObject();
        result.put("msg", "ok");
        result.put("code", "0");
        result.put("count",1);
        result.put("data", user);
        return result.toJSONString();
    }
    @ApiOperation(value = "用户注册接口",
            notes = "<span style='color:red;'>描述:</span>&nbsp;用户注册的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="username",value = "用户名",dataType = "String",defaultValue = "蒙虚坤"),
            @ApiImplicitParam(name="password",value = "用户登录密码",dataType ="String",defaultValue = "123456"),
            @ApiImplicitParam(name="phone",value = "电话",dataType = "String",defaultValue = "13286983783"),
            @ApiImplicitParam(name="email",value = "邮箱",dataType ="String",defaultValue = "1392178770@qq.com"),
            @ApiImplicitParam(name="content",value = "简介",dataType = "String",defaultValue = "民大"),
            @ApiImplicitParam(name="imgurl",value = "图片",dataType ="String",defaultValue = "images/1.jpg")
    })
    @GetMapping("/registerUser/{user_phone}")
    public String registerUser(@RequestParam(name="sex")String sex,@PathVariable(name = "user_phone") String userid,
                               @RequestParam(name = "username") String username, @RequestParam(name = "password") String password,
                               @RequestParam(name = "user_phone") String phone,@RequestParam(name = "email") String email,
                               @RequestParam(name = "content",defaultValue ="") String content, @RequestParam(name = "imgurl",defaultValue = "images/1.jpg") String imgurl){
       // http://localhost:8080/Curriculum/User/registerUser/蒙虚坤?password=123456&phone=13286983783&email=1392178770@qq.com&content=民大&imgurl=images/1.jpg
        User user = new User();
        Map<String,String> map = new HashMap<>();
        List<Map> resultMap = new ArrayList<>();
        String gender = sex;

        user.setUserid(userid).setSex(gender).setEmail(email).setContent(content).setPhone(phone).setPassword(password).setUsername(username).setImgurl(imgurl);
        String msg = usi.addUser(user);//插入用户返回用户id
        JSONObject result = new JSONObject();
        if(msg==null){
            result.put("msg", "false");
            result.put("code", "0");
            result.put("count","1");
            result.put("data", resultMap);
            return  result.toJSONString();
        }
        resultMap.add(map);
        String jwt = usi.checkUser(msg,password);
        map.put("TOKEN",jwt);
        result.put("msg", "true");
        result.put("userid", msg);
        result.put("code", "0");
        result.put("count","1");
        result.put("data", resultMap);
        System.out.println(result.toJSONString());
        return  result.toJSONString();
    }


    @GetMapping("/modifyUserPwd/{userid}")
    public String modifyUserPwd(@PathVariable(name = "userid") String userid,@RequestParam(name = "password") String password){
        // http://localhost:8080/Curriculum/User/registerUser/蒙虚坤?password=123456&phone=13286983783&email=1392178770@qq.com&content=民大&imgurl=images/1.jpg
        User user = new User();
        user.setPassword(password);
        user = usi.updateUser(userid,user);
        JSONObject result = new JSONObject();
        result.put("msg", "true");
        result.put("code", "0");
        result.put("count","1");
        result.put("data", user);
        return  result.toJSONString();
    }
    @GetMapping("/findUserPwd/{username}")
    public String modifyUserPwdByUsername(@PathVariable(name = "username") String username,@RequestParam(name = "email") String email,@RequestParam(name = "password") String password){
        // http://localhost:8080/Curriculum/User/registerUser/蒙虚坤?password=123456&phone=13286983783&email=1392178770@qq.com&content=民大&imgurl=images/1.jpg
        User user = new User();
        user.setPassword(password).setEmail(email);
        user = usi.updateUserByUsername(username,user);
        JSONObject result = new JSONObject();
        result.put("msg", "true");
        result.put("code", "0");
        result.put("count","1");
        result.put("data", user);
        return  result.toJSONString();
    }

    @GetMapping("/modifyUserMsg/{userid}")
    public String modifyUserMsg(@RequestParam(name="sex")String sex,@PathVariable(name = "userid") String userid,
                               @RequestParam(name = "username") String username,
                               @RequestParam(name = "user_phone") String phone,@RequestParam(name = "email") String email,
                               @RequestParam(name = "content",defaultValue ="") String content, @RequestParam(name = "imgurl",defaultValue = "images/1.jpg") String imgurl){
        // http://localhost:8080/Curriculum/User/registerUser/蒙虚坤?password=123456&phone=13286983783&email=1392178770@qq.com&content=民大&imgurl=images/1.jpg
        User user = new User();
        user.setUserid(userid).setSex(sex).setType("0").setUsername(username).setPhone(phone).setImgurl(imgurl).setContent(content).setEmail(email);
        System.out.println(user);
        user = usi.updateUser(userid,user);
        JSONObject result = new JSONObject();
        result.put("msg", "true");
        result.put("code", "0");
        result.put("count","1");
        result.put("data", user);
        return  result.toJSONString();
    }


}
