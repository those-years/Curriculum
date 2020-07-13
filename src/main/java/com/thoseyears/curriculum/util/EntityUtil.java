package com.thoseyears.curriculum.util;

import com.thoseyears.curriculum.entity.Competition;
import com.thoseyears.curriculum.entity.Enlist;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EntityUtil {
    public static List<Enlist> dealWithTeamUsers(List<Enlist> lists){
        List<Enlist> list = new ArrayList<>();
        for (Enlist e:lists ) {
            e.teamuserToTeamusers();
            list.add(e);
        }
        return list;
    }
    /*
     * @author: thoseyears
     * @methodsName: selectCompetition
     * @description: 筛选竞赛
     * @param: String startCompetition, String endCompetition,List<Competition> competitions
     * @return: List<Competition>
     * @Time: 2020/06/26
     * @throws:
     */
    public static List<Competition> selectCompetition(String startCompetition, String endCompetition,List<Competition> competitions){
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Competition> lists = new ArrayList<>();
        try {
            Date startDate = ft.parse(startCompetition);
            Date endDate = ft.parse(endCompetition);
            for(Competition competition:competitions) {
                //比较日期
                if (startDate.compareTo(ft.parse(competition.getStartCompetition())) == -1 && endDate.compareTo(ft.parse(competition.getEndCompetition())) != -1) {
                    lists.add(competition);
                }
            }
        } catch (Exception e) {
            System.out.println("比较日期出错");
            e.printStackTrace();
        }
        return lists;
    }
    /*
     * @author: thoseyears
     * @methodsName: selectEnableCompetition
     * @description: 筛选未过期竞赛
     * @param: String startCompetition, String endCompetition,List<Competition> competitions
     * @return: List<Competition>
     * @Time: 2020/06/26
     * @throws:
     */
    public static List<Competition> selectEnableCompetition(List<Competition> competitions){
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date nowDate = new Date();
        String startCompetition = ft.format(nowDate);
        List<Competition> lists = new ArrayList<>();
        try {
            for(Competition competition:competitions) {
                //比较日期

                if (nowDate.compareTo(ft.parse(competition.getEnrollEndTime())) == -1) {
                    lists.add(competition);
                }
            }
        } catch (Exception e) {
            System.out.println("比较日期出错");
            e.printStackTrace();
        }
        return lists;
    }
    /*
    * @author: thoseyears
    * @methodsName: clccCompetitionTime
    * @description: 计算竞赛时长
    * @param: String startCompetition, String endCompetition
    * @return: String
    * @Time: 2020/06/26
    * @throws:
    */
    public static String clccCompetitionTime(String startCompetition, String endCompetition){
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String duration = "";
        try {
            Date startDate = ft.parse(startCompetition);
            Date endDate = ft.parse(endCompetition);
            duration = (endDate.getTime()-startDate.getTime())/3600/1000 +"";
        } catch (Exception e) {
            System.out.println("计算比赛时间出错");
            e.printStackTrace();
        }
        return duration;
    }
}
