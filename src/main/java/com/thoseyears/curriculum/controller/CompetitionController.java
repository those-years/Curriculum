package com.thoseyears.curriculum.controller;

import com.thoseyears.curriculum.entity.Competition;
import com.thoseyears.curriculum.service.serviceimpl.CompetitionServiceImpl;
import com.thoseyears.curriculum.service.serviceimpl.EnlistServiceImpl;
import com.thoseyears.curriculum.util.*;
import io.swagger.annotations.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@RestController
@RequestMapping("/Curriculum/Competition")
@Api(tags = "竞赛服务相关接口描述")
@CrossOrigin(origins = "*",allowCredentials="true",allowedHeaders = "",methods = {})
public class CompetitionController {
    @Autowired
    private CompetitionServiceImpl competitionServiceImpl;
    @Autowired
    private EnlistServiceImpl esi;

    @ApiOperation(value = "查询某一个竞赛信息接口",
            notes = "<span style='color:red;'>描述:</span>&nbsp;用来查询某一个竞赛信息的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="competitionId",value = "竞赛Id",dataType = "String",defaultValue = "202006061601968"),
    })
    //查找竞赛
    @GetMapping(value="/findCompetition/{competitionId}")
    public String findAllUser(@PathVariable(name="competitionId") String competitionId) {
        List<Competition> competitions = competitionServiceImpl.findCompetitionById(competitionId);
        List<Object> lists = new ArrayList<>();
        lists.add(competitions);
        String msg = "ok";
        return  ResponseUtil.ResponseDell(msg, lists, Constant.RESCODE_SUCCESS_MSG).toString();
    }


    @ApiOperation(value = "查询所有竞赛信息接口",
            notes = "<span style='color:red;'>描述:</span>&nbsp;用来分页查询所有竞赛信息的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page",value = "页数",dataType = "String",defaultValue = "1"),
            @ApiImplicitParam(name="pagesize",value = "每一页数据量",dataType ="String",defaultValue = "10")
    })
    //分页请求
    @GetMapping(value="/allCompetition")
    public  String getCompetition(@RequestHeader(name="TOKEN")String TOKEN,@RequestParam(name="page") String page, @RequestParam(name="pagesize") String pagesize, @RequestParam(name="competitionName",defaultValue ="") String competitionName){
        List<Competition> competitions = competitionServiceImpl.getAllCompetition(page,pagesize,TOKEN,competitionName);
        //转换json格式
        List<Object> lists = new ArrayList<>();
        if(competitions==null){
            String msg = "ok";
            JSONObject result = new JSONObject();
            result.put("msg", "查找的竞赛不存在");
            result.put("code", 1);
            result.put("count", 0);
            result.put("data", "");
            return result.toString();
        }
        lists.add(competitions);
        String msg = "ok";
        JSONObject result = ResponseUtil.ResponseDell(msg, lists, 0);
        result.put("count",competitionServiceImpl.Competitions(null,competitionName,"1"));
        return result.toString();
    }


    @ApiOperation(value = "查询当前用户发布过的竞赛信息接口",
            notes = "<span style='color:red;'>描述:</span>&nbsp;用来分页查询当前用户发布过的竞赛信息接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userid",value = "用户",dataType = "String",defaultValue = "117583010123"),
            @ApiImplicitParam(name="page",value = "页数",dataType = "String",defaultValue = "1"),
            @ApiImplicitParam(name="pagesize",value = "每一页数据量",dataType ="String",defaultValue = "10")
    })
    //用户自己分页请求
    @GetMapping(value="/UserCompetition/{userid}")
    public  String getCompetitionByUserid(@RequestHeader(name="TOKEN")String TOKEN,@PathVariable(name="userid") String userid, @RequestParam(name="page") String page, @RequestParam(name="pagesize") String pagesize,
                                          @RequestParam(name="startCompetition",defaultValue = "") String startCompetition, @RequestParam(name="endCompetition",defaultValue = "") String endCompetition){
        List<Competition> competitions = competitionServiceImpl.getAllCompetitionByUserid(TOKEN,page,pagesize,userid,startCompetition,endCompetition);
        //转换json格式
        List<Object> lists = new ArrayList<>();
        lists.add(competitions);
        String msg = "ok";
        JSONObject result = ResponseUtil.ResponseDell(msg, lists, 0);
        if(userid.equals("1")){
            result.put("count",competitionServiceImpl.Competitions(null,"","0"));

        }else{
            result.put("count",competitionServiceImpl.Competitions(userid,"","0"));

        }
        return result.toString();
    }


    @ApiOperation(value = "查询当前用户参加过的竞赛信息接口",
            notes = "<span style='color:red;'>描述:</span>&nbsp;用来分页查询当前用户发布过的竞赛信息接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userid",value = "用户",dataType = "String",defaultValue = "117583010123"),
            @ApiImplicitParam(name="page",value = "页数",dataType = "String",defaultValue = "1"),
            @ApiImplicitParam(name="pagesize",value = "每一页数据量",dataType ="String",defaultValue = "10")
    })
    @GetMapping(value="/EnrollCompetition/{userid}")
    public  String getEnrollCompetitionByUserid(@RequestHeader(name="TOKEN")String TOKEN,@PathVariable(name="userid") String userid, @RequestParam(name="page") String page, @RequestParam(name="pagesize") String pagesize,
                                                @RequestParam(name="startCompetition",defaultValue = "") String startCompetition, @RequestParam(name="endCompetition",defaultValue = "") String endCompetition){
        List<Competition> competitions = competitionServiceImpl.getEnrollCompetitionByUserid(page,pagesize,userid,startCompetition,endCompetition);
        //转换json格式
        List<Object> lists = new ArrayList<>();
        lists.add(competitions);
        String msg = "ok";
        JSONObject result = ResponseUtil.ResponseDell(msg, lists, 0);
        result.put("count",esi.getAllUserEnlist(userid).size());
        return result.toString();
    }


    @ApiOperation(value = "新增竞赛信息接口",
            notes = "<span style='color:red;'>描述:</span>&nbsp;用来新增竞赛信息的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userid",value = "创建竞赛人id",dataType = "String",defaultValue = "117583010123"),
            @ApiImplicitParam(name="TOKEN",value = "当前登录TOKEN",dataType ="String",defaultValue = "null"),
            @ApiImplicitParam(name="competitionName",value = "竞赛名字",dataType = "String",defaultValue = "java程序竞赛"),
            @ApiImplicitParam(name="startCompetition",value = "竞赛开始时间",dataType ="String",defaultValue = "2020-6-5 19:10:00"),
            @ApiImplicitParam(name="endCompetition",value = "竞赛结束时间",dataType = "String",defaultValue = "2020-6-5 21:10:00"),
            @ApiImplicitParam(name="enrollStartTime",value = "竞赛开始报名时间",dataType ="String",defaultValue = "2020-6-4 21:10:00"),
            @ApiImplicitParam(name="enrollEndTime",value = "竞赛结束报名时间",dataType ="String",defaultValue = "2020-6-4 21:10:00"),
            @ApiImplicitParam(name="num",value = "队伍数",dataType ="String",defaultValue = "20"),
            @ApiImplicitParam(name="teamNum",value = "每个队队伍人数",dataType ="String",defaultValue = "5")

    })
    //新增竞赛
    //@RequestHeader(name = "TOKEN") String TOKEN
    @GetMapping(value="/addCompetition/{userid}")
    public  String addCompetition(@RequestHeader(name="TOKEN")String TOKEN,@PathVariable(name="userid") String userid, @RequestParam(name="type") String type, @RequestParam(name="competitionName") String competitionName,@RequestParam(name="startCompetition") String startCompetition,@RequestParam(name="endCompetition") String endCompetition, @RequestParam(name="enrollStartTime") String enrollStartTime, @RequestParam(name="enrollEndTime") String enrollEndTime,@RequestParam(name="num") String num,@RequestParam(name="teamNum") String teamNum){
        Competition competition = new Competition();
        System.out.println(userid);
        System.out.println(competitionName);
        competition.setUserid(userid).setType(type).setCompetitionName(competitionName).setStartCompetition(startCompetition).setEndCompetition(endCompetition).setEnrollStartTime(enrollStartTime).setEnrollEndTime(enrollEndTime).setNum(num).setTeamNum(teamNum);
        //转换json格式
        List<Object> lists = new ArrayList<>();
        List<Competition> competitions = new ArrayList<>();
        competitions.add(competitionServiceImpl.insertCompetition(competition,TOKEN));
        lists.add(competitions);
        String msg = "ok";
        return ResponseUtil.ResponseDell(msg, lists, Constant.RESCODE_SUCCESS_MSG).toString();
    }
    @ApiOperation(value = "更新当前竞赛信息",
            notes = "<span style='color:red;'>描述:</span>&nbsp;更新当前竞赛信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="competitionId",value = "竞赛id",dataType = "String",defaultValue = "117583010123"),
            @ApiImplicitParam(name="TOKEN",value = "当前登录TOKEN",dataType ="String",defaultValue = "null"),
            @ApiImplicitParam(name="competitionName",value = "竞赛名字",dataType = "String",defaultValue = "java程序竞赛"),
            @ApiImplicitParam(name="startCompetition",value = "竞赛开始时间",dataType ="String",defaultValue = "2020-6-5 19:10:00"),
            @ApiImplicitParam(name="endCompetition",value = "竞赛结束时间",dataType = "String",defaultValue = "2020-6-5 21:10:00"),
            @ApiImplicitParam(name="enrollStartTime",value = "竞赛开始报名时间",dataType ="String",defaultValue = "2020-6-4 21:10:00"),
            @ApiImplicitParam(name="enrollEndTime",value = "竞赛结束报名时间",dataType ="String",defaultValue = "2020-6-4 21:10:00"),
            @ApiImplicitParam(name="num",value = "队伍数",dataType ="String",defaultValue = "20"),
            @ApiImplicitParam(name="type",value = "个人/团队",dataType ="String",defaultValue = "20"),
            @ApiImplicitParam(name="teamNum",value = "每个队队伍人数",dataType ="String",defaultValue = "5")

    })
    @GetMapping(value="/updateCompetition/{competitionId}")
    public  String updateCompetition(@RequestHeader(name="TOKEN")String TOKEN,@PathVariable(name="competitionId") String competitionId,
                                     @RequestParam(name="type") String type, @RequestParam(name="competitionName") String competitionName,
                                     @RequestParam(name="startCompetition") String startCompetition,
                                     @RequestParam(name="endCompetition") String endCompetition,
                                     @RequestParam(name="enrollStartTime") String enrollStartTime,
                                     @RequestParam(name="enrollEndTime") String enrollEndTime,@RequestParam(name="num") String num,
                                     @RequestParam(name="teamNum") String teamNum){
        Competition competition = new Competition();
        competition.setCompetitionId(competitionId).setType(type).setCompetitionName(competitionName).setStartCompetition(startCompetition).setEndCompetition(endCompetition).setEnrollStartTime(enrollStartTime).setEnrollEndTime(enrollEndTime).setNum(num).setTeamNum(teamNum);
        //转换json格式
        List<Object> lists = new ArrayList<>();
        List<Competition> competitions = new ArrayList<>();
        competitions.add(competitionServiceImpl.updateCompetition(competition,TOKEN));
        lists.add(competitions);
        String msg = "ok";
        return ResponseUtil.ResponseDell(msg, lists, Constant.RESCODE_SUCCESS_MSG).toString();
    }
    @ApiOperation(value = "删除当前竞赛",
            notes = "<span style='color:red;'>描述:</span>&nbsp;删除当前竞赛")
    @ApiImplicitParams({
            @ApiImplicitParam(name="competitionId",value = "比赛id",dataType = "String",defaultValue = "117583010123"),
            @ApiImplicitParam(name="TOKEN",value = "令牌",dataType ="String")
    })
    @GetMapping(value="/delCompetition/{competitionId}")
    public  String delCompetition(@RequestHeader(name="TOKEN")String TOKEN,@PathVariable(name="competitionId") String competitionId ){
        competitionServiceImpl.delCompetition(competitionId);
        JSONObject result = new JSONObject();
        result.put("msg", "删除成功");
        result.put("code", 0);
        result.put("count", 1);
        result.put("data", "删除成功");
        return result.toString();
    }

}
