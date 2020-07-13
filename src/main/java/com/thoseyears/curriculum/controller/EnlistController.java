package com.thoseyears.curriculum.controller;

import com.alibaba.fastjson.JSONObject;
import com.thoseyears.curriculum.entity.Competition;
import com.thoseyears.curriculum.entity.Enlist;
import com.thoseyears.curriculum.service.EnlistService;
import com.thoseyears.curriculum.service.serviceimpl.EnlistServiceImpl;
import com.thoseyears.curriculum.util.Constant;
import com.thoseyears.curriculum.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/Curriculum/Enlist")
@Api(tags = "报名服务相关接口描述")
@CrossOrigin
public class EnlistController {
    @Autowired
    private EnlistServiceImpl enlistServiceImpl;
    @ApiOperation(value = "查询当前当前用户报名信息",
            notes = "<span style='color:red;'>描述:</span>&nbsp;查询当前当前用户报名信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userid",value = "用户id",dataType = "String",defaultValue = "117583010123"),
            @ApiImplicitParam(name="page",value = "第几页",dataType = "String",defaultValue = "1"),
            @ApiImplicitParam(name="pagesize",value = "一页的数据量",dataType = "String",defaultValue = "10"),
            @ApiImplicitParam(name="TOKEN",value = "令牌",dataType ="String")
    })
    @GetMapping(value="/getEnlist/{userid}")
    public  String getEnlist(@RequestHeader(name="TOKEN")String TOKEN,@PathVariable(name="userid") String userid,@RequestParam(name="page") String page,@RequestParam(name="pagesize") String pagesize){
            List<Enlist> enlists = enlistServiceImpl.getAllUserEnlist(userid);
        //转换json格式
        JSONObject result = new JSONObject();
        result.put("msg", "ok");
        result.put("code", "0");
        result.put("count", enlists.size());
        result.put("data", enlists);
        return result.toJSONString();
    }
    @ApiOperation(value = "查询当前竞赛的所有参赛人员",
            notes = "<span style='color:red;'>描述:</span>&nbsp;查询当前竞赛的所有参赛人员")
    @ApiImplicitParams({
            @ApiImplicitParam(name="competition",value = "竞赛id",dataType = "String",defaultValue = "202006061601968"),
            @ApiImplicitParam(name="TOKEN",value = "令牌",dataType ="String")
    })
    @GetMapping(value="/getCidEnlist/{competition}")
    public  String getAllEnlistByCid(@PathVariable(name="competition") String competition,@RequestHeader(name="TOKEN")String TOKEN){
        List<Enlist> enlists = enlistServiceImpl.getAllEnlistBycid(competition);
        //转换json格式
        JSONObject result = new JSONObject();
        result.put("msg", "ok");
        result.put("code", "0");
        result.put("count", enlists.size());
        result.put("data", enlists);
        return result.toJSONString();
    }
    @ApiOperation(value = "获取当前用户比赛的队伍",
            notes = "<span style='color:red;'>描述:</span>&nbsp;获取当前用户比赛的队伍")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userid",value = "用户id",dataType = "String",defaultValue = "117583010123"),
            @ApiImplicitParam(name="competitionId",value = "竞赛id",dataType = "String",defaultValue = "202006061601968"),
            @ApiImplicitParam(name="TOKEN",value = "令牌",dataType ="String")
    })
    @GetMapping(value="/getTeam/{userid}")
    public  String getTeam(@RequestHeader(name="TOKEN")String TOKEN,@PathVariable(name="userid") String userid,@RequestParam(name="competitionId") String competitionId ){
        Enlist enlist = enlistServiceImpl.findEnlist(userid,competitionId,"1");
        //转换json格式
        JSONObject result = new JSONObject();
        result.put("msg", "ok");
        result.put("code", "0");
        result.put("count", 1);
        result.put("data", enlist);
        return result.toJSONString();
    }
    @ApiOperation(value = "报名比赛",
            notes = "<span style='color:red;'>描述:</span>&nbsp;获取当前用户比赛的队伍")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userid",value = "用户id",dataType = "String",defaultValue = "117583010123"),
            @ApiImplicitParam(name="competitionId",value = "竞赛id",dataType = "String",defaultValue = "202006061601968"),
            @ApiImplicitParam(name="teamname",value = "队伍名称",dataType = "String",defaultValue = "117583010123"),
            @ApiImplicitParam(name="type",value = "比赛类型",dataType = "String",defaultValue = "0"),
            @ApiImplicitParam(name="TOKEN",value = "令牌",dataType ="String")
    })
    //@RequestHeader(name = "TOKEN") String TOKEN
    @GetMapping(value="/addEnlist/{userid}")
    public  String addCompetition(@RequestHeader(name="TOKEN")String TOKEN,@PathVariable(name="userid") String userid, @RequestParam(name="teamname") String teamname,@RequestParam(name="competitionId") String competitionId,@RequestParam(name="type") String type){
        Enlist enlist = new Enlist();
        if(type.equals("0")){
            enlist.setCid(competitionId).setUserid(userid).setTeamuser("0");
        }else{
            //初始化时队伍成员为0（不包括队长）
            enlist.setCid(competitionId).setTeamname(teamname).setUserid(userid).setTeamuser("0");
        }
        //转换json格式
        List<Object> lists = new ArrayList<>();
        List<Enlist> enlists = new ArrayList<>();
        enlists.add(enlistServiceImpl.insertEnlist(enlist,TOKEN));
        lists.add(enlists);
        String msg = "ok";
        return ResponseUtil.ResponseDell(msg, lists, 0).toString();
    }

    @ApiOperation(value = "加入一个队伍",
            notes = "<span style='color:red;'>描述:</span>&nbsp;当前用户加入一个队伍")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userid",value = "用户id",dataType = "String",defaultValue = "117583010123"),
            @ApiImplicitParam(name="enlistid",value = "队伍id",dataType = "String",defaultValue = "202006061601968"),
            @ApiImplicitParam(name="TOKEN",value = "令牌",dataType ="String")
    })
    //@RequestHeader(name = "TOKEN") String TOKEN
    @GetMapping(value="/joinEnlist/{enlistid}")
    public  String joinCompetition(@RequestHeader(name="TOKEN")String TOKEN,@PathVariable(name="enlistid") String enlistid, @RequestParam(name="userid") String userid){
        Enlist enlist = new Enlist();
        enlist.setEnlistid(enlistid);
        //转换json格式
        List<Object> lists = new ArrayList<>();
        List<Enlist> enlists = new ArrayList<>();
        enlist = enlistServiceImpl.updateEnlist(enlist,userid,TOKEN);
        enlists.add(enlist);
        lists.add(enlists);
        String msg = "ok";
        if(enlist==null){
            return ResponseUtil.ResponseDell(msg, lists, -1).toString();
        }else if(enlist.getTeamuser().equals("队伍已满")){
            return ResponseUtil.ResponseDell(msg, lists, -2).toString();
        }
        return ResponseUtil.ResponseDell(msg, lists, 0).toString();
    }
}
