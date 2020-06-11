package com.thoseyears.curriculum.service.serviceimpl;

import net.sf.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.thoseyears.curriculum.dao.UserDAO;
import com.thoseyears.curriculum.entity.User;
import com.thoseyears.curriculum.service.UserService;
import com.thoseyears.curriculum.util.*;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDAO userDAO;
    @Override
    public List<User> findallUser() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        List<User> users = userDAO.selectList(queryWrapper);
        return users;
    }

    @Override
    public String checkUser(String userid, String password) throws Exception {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        Map<String,Object> map = new HashMap<>(); //设置查询条件
        map.put("userid",userid); //设置查询参数
        map.put("password",password); //设置查询参数
        queryWrapper.allEq(map);
        User user =userDAO.selectOne(queryWrapper);
        String jwt="";
        if(user!=null){
            //登录成功创建token
            jwt = JwtUtil.createJWT(Constant.JWT_ID, JwtUtil.generalSubject(user), Constant.JWT_TTL);
            Claims claims = JwtUtil.parseJWT(jwt);
            System.out.println("添加注册本地token：");
            JwtUtil.addToken(jwt);  //注册token
            JSONObject js =  JSONObject.fromObject(claims.getSubject());
            return jwt;
        }
        return jwt;
    }

    @Override
    public User findlUserByid(String userid) {
        return userDAO.selectById(userid);
    }

    @Override
    public String addUser(User user) {
        String userid = RandomIdFactory.getUserId();
        while(userDAO.selectById(userid)!=null){
            user.setUserid(userid);
        }
        userid  = userDAO.insert(user)+"";
        return userid;
    }

    @Override
    public boolean deleteUserByid(String userid) {
        return false;
    }
}
