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
    /*
     * @author: thoseyears
     * @methodsName:checkUser()
     * @description:分页查询所有竞赛
     * @param:String userid, String password
     * @return:String
     * @Time:2020/06/10
     * @throws:
     */
    @Override
    public String checkUser(String userid, String password)  {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        Map<String,Object> map = new HashMap<>(); //设置查询条件
        map.put("username",userid); //设置查询参数
        map.put("password",password); //设置查询参数
        queryWrapper.allEq(map);
        User user =userDAO.selectOne(queryWrapper);
        String jwt="";
        if(user!=null){
            //登录成功创建token
            try {
                jwt = JwtUtil.createJWT(Constant.JWT_ID, JwtUtil.generalSubject(user), Constant.JWT_TTL);
                Claims claims = JwtUtil.parseJWT(jwt);
                System.out.println("添加注册本地token：");
                JwtUtil.addToken(jwt);  //注册token
                JSONObject js =  JSONObject.fromObject(claims.getSubject());
                System.out.println("JWT: "+jwt);
                return jwt;
            } catch (Exception e) {
                System.out.println("注册token出错：");
                e.printStackTrace();
            }

        }
        return jwt;
    }

    @Override
    public User findlUserByid(String userid) {
        return userDAO.selectById(userid);
    }
    /*
     * @author: thoseyears
     * @methodsName:addUser()
     * @description:分页查询所有竞赛
     * @param:User user
     * @return:String
     * @Time:2020/06/10
     * @throws:
     */
    @Override
    public String addUser(User user) {

        while(userDAO.selectById(user.getUserid())!=null){
            return null;
        }
        user.setType("0");
        userDAO.insert(user);
        return user.getUserid();
    }

    @Override
    public User updateUser(String userid, User user) {
        if(user.getPassword()!=null){
            String userPassword = user.getPassword();
            user =  userDAO.selectById(userid);
            user.setPassword(userPassword);
        }else{
            User u =  userDAO.selectById(userid);
            user.setPassword( u.getPassword());
        }
        userDAO.updateById(user);
        return user;
    }

    @Override
    public boolean deleteUserByid(String userid) {
        return false;
    }
}
