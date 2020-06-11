package com.thoseyears.curriculum.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
@TableName(value="user")//默认将类名作为表名
public class User {

    @TableId(value = "userid",type= IdType.INPUT)
    private String userid;
    @TableField(value = "username")
    private String username;
    @TableField(value = "password")
    private String password;
    private String phone;
    private String email;
    private String content;

}