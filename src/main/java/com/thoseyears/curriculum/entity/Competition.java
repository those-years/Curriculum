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
@TableName(value="competition")//默认将类名作为表名
public class Competition {
    @TableId(value="cid",type= IdType.INPUT)
    private String competitionId;

    @TableField(value = "cname")
    private String competitionName;

    @TableField(value = "competitionimg")
    private String competitionImg;

    @TableField(value = "enrollstarttime")
    private String enrollStartTime;

    @TableField(value = "enrollendtime")
    private String enrollEndTime;

    @TableField(value = "startcompetition")
    private String startCompetition;

    @TableField(value = "endcompetition")
    private String endCompetition;

    @TableField(value = "type")
    private String type;

    @TableField(value = "cnum")
    private String num;

    @TableField(value = "teamnum")
    private String teamNum;

    @TableField(value = "duration")
    private String duration;
    @TableField(value = "userid")
    private String userid;
    @TableField(exist = false)
    private String isEnrolled;
}
