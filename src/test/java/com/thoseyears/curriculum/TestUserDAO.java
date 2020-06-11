package com.thoseyears.curriculum;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.thoseyears.curriculum.entity.*;
import com.thoseyears.curriculum.dao.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class TestUserDAO{
    @Autowired
    private  UserDAO userDAO;
    @Test
    public void testFindAll(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        List<User> users = userDAO.selectList(queryWrapper);
        users.forEach(user-> System.out.println("user = " + user));
    }
    @Test
    public void testFindOne(){
        User user = userDAO.selectById("117583010123");
        System.out.println("user = " + user);
    }
    @Test
    public void testFind(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //queryWrapper.eq("id","117583010123");//设置等值查询
        /*多条件查询
        Map<String,Object> map = new HashMap<>();
        map.put("id","");
        map.put("username","lin");
        map.put("age","21");
        queryWrapper.allEq(map);
        */

        //queryWrapper.lt("age",23);//设置小于查询
        //queryWrapper.ge("age",23);//小于等于查询 gt 大于  ge 大于等于
        List<User> users = userDAO.selectList(queryWrapper);
        users.forEach(user-> System.out.println(user));
    }
    //添加
    @Test
    public void testSave(){
        User entity = new User();
        entity.setUserid("117583010120").setUsername("李").setPassword("123456").setPhone("13286986783").setContent("广西民大").setEmail("1392178770@qq.com");
        userDAO.insert(entity);
    }
    //更新
    @Test
    public void testUpdate(){
        User user = userDAO.selectById("117583010120");
        user.setUsername("小李李");
        userDAO.updateById(user);
    }
    //删除
    @Test
    public void testDeleteById(){
        userDAO.deleteById("117583010120");
    }
}
