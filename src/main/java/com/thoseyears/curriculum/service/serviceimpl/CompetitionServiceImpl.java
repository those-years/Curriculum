package com.thoseyears.curriculum.service.serviceimpl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.thoseyears.curriculum.util.EntityUtil;
import com.thoseyears.curriculum.util.PageUtil;
import net.sf.json.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thoseyears.curriculum.dao.CompetitionDAO;
import com.thoseyears.curriculum.entity.*;
import com.thoseyears.curriculum.service.CompetitionService;
import com.thoseyears.curriculum.util.JwtUtil;
import com.thoseyears.curriculum.util.RandomIdFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class CompetitionServiceImpl implements CompetitionService {
    @Autowired
    private CompetitionDAO competitionDAO;
    @Autowired
    private EnlistServiceImpl enlistServiceImpl;

    /*
    * @author: thoseyears
    * @methodsName:getAllCompetition()
    * @description:分页查询所有竞赛
    * @param:String page,String pagesize
    * @return:List<Competition>
    * @Time:2020/06/12
    * @throws:
    */
    @Override
    public List<Competition>  getAllCompetition(String page,String pagesize,String TOKEN,String competitionName) {

        IPage<Competition> cPage = new Page<>(Integer.parseInt(page),Integer.parseInt(pagesize));
        IPage<Competition> pages = null;
        List<Competition>  competitions ;
        if(competitionName.equals("")){
            QueryWrapper<Competition> wrapper = new QueryWrapper();
            wrapper.orderByDesc("cid");
            pages =competitionDAO.selectPage(cPage, null);
            competitions = pages.getRecords();
            competitions =competitionDAO.selectList(wrapper);
            competitions = EntityUtil.selectEnableCompetition(competitions);
            competitions = PageUtil.createPage(competitions,page, pagesize);
        }else{
            QueryWrapper<Competition> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("cname",competitionName);
            queryWrapper.orderByDesc("cid");
            pages = competitionDAO.selectPage(cPage, queryWrapper);

            competitions = pages.getRecords();
            competitions = PageUtil.createPage(competitions,page, pagesize);
        }
        String total = pages.getTotal()+"";
        JSONObject js = JwtUtil.getUser(TOKEN);
        if(competitions==null){
            return null;
        }
        for (Competition c:competitions) {
            if(enlistServiceImpl.findEnlist(js.getString("userid"),c.getCompetitionId(),"0")!=null||enlistServiceImpl.judgeUserEnlist(js.getString("userid"),c.getCompetitionId())!=null){
                c.setIsEnrolled("1");
            }else{
                c.setIsEnrolled("0");
            }
        }
        return competitions;
    }
    /*
    public void addMsg(){
        Competition competition = new Competition();
        competition.setNum("0").setTeamNum("5").setType("1").setUserid("117583010117").setEnrollStartTime("2020-07-06 00:00:00").setEnrollEndTime("2020-07-18 00:00:00")
                .setStartCompetition("2020-07-19 00:00:00").setEndCompetition("2020-07-19 04:00:00").setCompetitionImg("images/5.png");
        String name = "html设计比赛第";
        for(int i=0;i<100;i++){
            String cid = RandomIdFactory.getUserId();
            while(competitionDAO.selectById(cid)!=null){
                cid = RandomIdFactory.getUserId();
            }
            int j =i+1;
            competition.setDuration(EntityUtil.clccCompetitionTime(competition.getStartCompetition(),competition.getEndCompetition()));
            competition.setCompetitionId(cid).setCompetitionName(name+j+"届");
            competitionDAO.insert(competition);
        }
    }
    */

    /*
     * @author: thoseyears
     * @methodsName:getAllCompetition()
     * @description:分页查询当前用户发布过的相关竞赛
     * @param:String page,String pagesize,String userid
     * @return:List<Competition>
     * @Time:2020/06/22
     * @throws:
     */
    @Override
    public List<Competition> getAllCompetitionByUserid(String TOKEN,String page, String pagesize, String userid,String startCompetition,String  endCompetition) {
        QueryWrapper<Competition> queryWrapper = new QueryWrapper<>();
        IPage<Competition> cPage = new Page<>(Integer.parseInt(page),Integer.parseInt(pagesize));
        if(!userid.equals("1")){
            queryWrapper.eq("userid",userid);
        }

        IPage<Competition> pages = competitionDAO.selectPage(cPage, queryWrapper);
        //JSONObject js = JwtUtil.getUser(TOKEN);
        List<Competition>  competitions = pages.getRecords();
        if(!startCompetition.equals("")){
            competitions= EntityUtil.selectCompetition(startCompetition,endCompetition,competitions);
        }
        JSONObject js = JwtUtil.getUser(TOKEN);
        userid =js.getString("userid");
        for (Competition c:competitions) {
            if(enlistServiceImpl.findEnlist(userid,c.getCompetitionId(),"0")!=null||enlistServiceImpl.judgeUserEnlist(userid,c.getCompetitionId())!=null){
                c.setIsEnrolled("1");
            }else{
                c.setIsEnrolled("0");
            }
        }
        return competitions;
    }
    /*
     * @author: thoseyears
     * @methodsName:getAllCompetition()
     * @description:分页查询当前用户参加过的相关竞赛
     * @param:String page,String pagesize,String userid
     * @return:List<Competition>
     * @Time:2020/06/22
     * @throws:
     */
    @Override
    public List<Competition> getEnrollCompetitionByUserid(String page, String pagesize, String userid,String startCompetition,String  endCompetition) {
        QueryWrapper<Competition> queryWrapper = new QueryWrapper<>();
        //JSONObject js = JwtUtil.getUser(TOKEN);
        List<Competition>  competitions = new ArrayList<>();
        List<Enlist> enlists =  enlistServiceImpl.getAllUserEnlist(userid);
        for (Enlist enlist:enlists) {
            Competition competition = competitionDAO.selectById(enlist.getCid());
            competitions.add(competition);
        }
        if(!startCompetition.equals("")){
            competitions= EntityUtil.selectCompetition(startCompetition,endCompetition,competitions);
        }
        for (Competition c:competitions) {
            if(enlistServiceImpl.findEnlist(userid,c.getCompetitionId(),"0")!=null||enlistServiceImpl.judgeUserEnlist(userid,c.getCompetitionId())!=null){
                c.setIsEnrolled("1");
            }else{
                c.setIsEnrolled("0");
            }
        }
        return competitions;
    }

    /*
    * @author: thoseyears
    * @methodsName: Competitions
    * @description: 获取数据总数
    * @param: String userid
    * @return:
    * @Time:
    * @throws:
    */
    @Override
    public String Competitions(String userid,String competitionName,String enableEnroll) {
        QueryWrapper<Competition> queryWrapper = new QueryWrapper<>();
        List<Competition>  competitions = null;
        if(userid!=null){
            Map<String,Object> map = new HashMap<>();
            map.put("userid",userid);
            queryWrapper.allEq(map);
            competitions = competitionDAO.selectList(queryWrapper);
            if(enableEnroll.equals("1")){
                enlistServiceImpl.getAllUserEnlist(userid);
            }else{
                queryWrapper.allEq(map);

                competitions = competitionDAO.selectList(queryWrapper);
            }

        }else if(!competitionName.equals("")){
            queryWrapper.like("cname",competitionName);
            competitions = competitionDAO.selectList(queryWrapper);
        }else if(enableEnroll.equals("1")){
            competitions = competitionDAO.selectList(queryWrapper);
            competitions = EntityUtil.selectEnableCompetition(competitions);
        }else{
            competitions = competitionDAO.selectList(queryWrapper);
        }
        return competitions.size()+"";
    }

    /*
    * @author: thoseyears
    * @methodsName: insertCompetition
    * @description: 插入并且返回
    * @param: Competition competition
    * @return: Competition
    * @Time: 2020/06/12
    * @throws:
    */
    @Override
    public Competition insertCompetition(Competition competition,String TOKEN) {
        String cid = RandomIdFactory.getcId();
        while (competitionDAO.selectById(cid)!=null){
            cid = RandomIdFactory.getcId();
        }
        competition.setCompetitionId(cid);
        competition.setDuration(EntityUtil.clccCompetitionTime(competition.getStartCompetition(),competition.getEndCompetition()));
        JSONObject js = JwtUtil.getUser(TOKEN);
        competition.setCompetitionImg(js.getString("imgurl"));
        competition.setUserid(js.getString("userid"));
        competitionDAO.insert(competition);
        return competition;
    }
    /*
     * @author: thoseyears
     * @methodsName: updateCompetition
     * @description: 更新并且返回
     * @param: Competition competition
     * @return: Competition
     * @Time: 2020/06/12
     * @throws:
     */
    @Override
    public Competition updateCompetition(Competition competition,String TOKEN) {
        JSONObject js = JwtUtil.getUser(TOKEN);
        competition.setCompetitionImg(js.getString("imgurl"));
        competition.setUserid(js.getString("userid"));
        competitionDAO.updateById(competition);
        return competition;
    }
    /*
    * @author: thoseyears
    * @methodsName: findCompetitionById
    * @description: 查找竞赛
    * @param: String competitionId
    * @return: List<Competition>
    * @Time:  2020/06/12
    * @throws:
    */
    @Override
    public List<Competition> findCompetitionById(String competitionId) {
        List<Competition> competitions = new ArrayList<>();
        competitions.add(competitionDAO.selectById(competitionId));
        return competitions;
    }

    @Override
    public boolean delCompetition(String competitionId) {
        competitionDAO.deleteById(competitionId);
        enlistServiceImpl.delEnlistsByCid(competitionId);
        return false;
    }

    /*
    * @author: thoseyears
    * @methodsName:
    * @description:
    * @param:
    * @return:
    * @Time:
    * @throws:
    */
    public void del(){

    }
}
