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

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
@TableName(value="enlist")//默认将类名作为表名
public class Enlist {
    @TableId(value = "enlistid",type= IdType.INPUT)
    private String enlistid;
    private String cid;
    @TableField(value = "userid")
    private String userid;
    @TableField(value = "teamname")
    private String teamname;
    @TableField(value = "teamuser")
    private String teamuser;
    @TableField(exist = false)
    private List<String> teamusers; //存放这个团队参赛队员id
    public void teamuserToTeamusers(){
        if(!teamuser.equals("0")) {
            String[] users = this.teamuser.split(";");
            this.teamusers = new ArrayList<>();
            for (String u : users) {
                this.teamusers.add(u);
            }
        }
    }
}
