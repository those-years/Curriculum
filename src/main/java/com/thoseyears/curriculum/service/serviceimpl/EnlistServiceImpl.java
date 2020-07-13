package com.thoseyears.curriculum.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.thoseyears.curriculum.dao.*;
import com.thoseyears.curriculum.entity.*;
import com.thoseyears.curriculum.service.*;
import com.thoseyears.curriculum.util.*;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class EnlistServiceImpl  implements EnlistService {
    @Autowired
    private EnlistDAO enlistDAO;
    @Autowired
    private CompetitionDAO competitionDAO;
    @Autowired
    private UserDAO userDAO;
    /*
     * @author: thoseyears
     * @methodsName:getEnlist()
     * @description:查询报名信息
     * @param:String userid
     * @return:List<Enlist>
     * @Time:2020/06/11
     * @throws:
     */
    @Override
    public List<Enlist> getEnlist(String userid) {
        QueryWrapper<Enlist> queryWrapper = new QueryWrapper<>();
        Map<String,Object> map = new HashMap<>(); //设置查询条件
        map.put("userid",userid); //设置查询参数
        queryWrapper.allEq(map);
        List<Enlist>  lists=enlistDAO.selectList(queryWrapper);
        lists = EntityUtil.dealWithTeamUsers(lists);
        return lists;
    }

    /*
     * @author: thoseyears
     * @methodsName:getAllUserEnlist()
     * @description:获取当前用户的所有参赛信息
     * @param:String userid
     * @return:List<Enlist>
     * @Time:2020/06/11
     * @throws:
     */
    @Override
    public List<Enlist> getAllUserEnlist(String userid) {
        QueryWrapper<Enlist> queryWrapper = new QueryWrapper<>();
        Map<String,Object> map = new HashMap<>(); //设置查询条件
        map.put("userid",userid); //设置查询参数
        queryWrapper.allEq(map).or().like("teamuser",userid);
        List<Enlist>  lists=enlistDAO.selectList(queryWrapper);
        lists = EntityUtil.dealWithTeamUsers(lists);
        return lists;
    }

    @Override
    public void delEnlistsByCid(String competition) {
        Map<String,Object> map = new HashMap<>();
        map.put("cid",competition);
        enlistDAO.deleteByMap(map);
    }

    @Override
    public List<Enlist> getAllEnlistBycid(String competition) {
        QueryWrapper<Enlist> queryWrapper = new QueryWrapper<>();
        Map<String,Object> map = new HashMap<>(); //设置查询条件
        map.put("cid",competition); //设置查询参数
        queryWrapper.allEq(map);
        List<Enlist>  lists=enlistDAO.selectList(queryWrapper);
        lists = EntityUtil.dealWithTeamUsers(lists);
        return lists;

    }

    /*
     * @author: thoseyears
     * @methodsName:judgeUserEnlist()
     * @description:判断用户是否报名了这个比赛
     * @param:String userid,String competitionId
     * @return:Enlist
     * @Time:2020/06/11
     * @throws:
     */
    @Override
    public Enlist judgeUserEnlist(String userid,String competitionId) {
        QueryWrapper<Enlist> queryWrapper = new QueryWrapper<>();
        Map<String,Object> map = new HashMap<>(); //设置查询条件
        map.put("cid",competitionId);
        queryWrapper.allEq(map).like("teamuser",userid);
        Enlist enlist =  enlistDAO.selectOne(queryWrapper);
        return enlist;
    }
    /*
     * @author: thoseyears
     * @methodsName:findEnlist()
     * @description:查询报名信息
     * @param:String userid,String cid
     * @return:Enlist
     * @Time:2020/06/11
     * @throws:
     */
    @Override
    public Enlist findEnlist(String userid, String cid,String team) {
        QueryWrapper<Enlist> queryWrapper = new QueryWrapper<>();
        Map<String,Object> map = new HashMap<>();
        Enlist enlist = null;
        if(team.equals("1")){
            map.put("cid",cid);
            queryWrapper.allEq(map).and(qw->qw.eq("userid", userid).or().like("teamuser",userid));
            enlist = enlistDAO.selectOne(queryWrapper);
            enlist.setUserid(userDAO.selectById(enlist.getUserid()).getUsername());
            enlist.teamuserToTeamusers();
            List<String> teamUserName = new ArrayList<>();
            if(enlist.getTeamusers()!=null){
                for (String str:enlist.getTeamusers()) {
                    User user = userDAO.selectById(str);
                    teamUserName.add(user.getUsername());
                }
            }

            enlist.setTeamusers(teamUserName);
        }else {
            map.put("userid",userid);
            map.put("cid",cid);
            queryWrapper.allEq(map);
            enlist = enlistDAO.selectOne(queryWrapper);
        }
        return enlist;
    }

    @Override
    public Enlist insertEnlist(Enlist enlist, String TOKEN) {
        String enlistid = RandomIdFactory.getcId();
        while (enlistDAO.selectById(enlistid)!=null){
            enlistid = RandomIdFactory.getcId();
        }
        enlist.setEnlistid(enlistid);
        JSONObject js = JwtUtil.getUser(TOKEN);
        Competition competition = competitionDAO.selectById(enlist.getCid());
        int cnum = Integer.parseInt(competition.getNum())+1;
        competition.setNum(cnum+"");
        competitionDAO.updateById(competition);
        enlistDAO.insert(enlist);
        return enlist;
    }

    @Override
    public Enlist updateEnlist(Enlist enlist, String userid, String TOKEN) {
        Enlist isEnlist = enlistDAO.selectById(enlist.getEnlistid());
        if(isEnlist==null){
            return null;
        }
        isEnlist.teamuserToTeamusers();
        if(isEnlist.getTeamusers()!=null&&isEnlist.getTeamusers().size()>=Integer.parseInt(competitionDAO.selectById(isEnlist.getCid()).getTeamNum())-1){
            Enlist e = new Enlist();
            e.setTeamuser("队伍已满");
            return  e;
        }
        if(isEnlist.getTeamuser().equals("0")){
            isEnlist.setTeamuser(userid);
        }else{
            isEnlist.setTeamuser(isEnlist.getTeamuser()+";"+userid);
        }
        Competition competition = competitionDAO.selectById(isEnlist.getCid());
        int cnum = Integer.parseInt(competition.getNum())+1;
        competition.setNum(cnum+"");
        competitionDAO.updateById(competition);
        enlistDAO.updateById(isEnlist);
        return isEnlist;
    }
}
