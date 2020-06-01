package com.thoseyears.curriculum;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.thoseyears.curriculum.entity.*;
import com.thoseyears.curriculum.dao.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
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
}
