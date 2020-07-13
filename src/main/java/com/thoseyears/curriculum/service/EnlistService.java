package com.thoseyears.curriculum.service;

import com.thoseyears.curriculum.entity.Competition;
import com.thoseyears.curriculum.entity.Enlist;

import java.util.List;

public interface EnlistService {
    public List<Enlist> getEnlist(String userid);
    public List<Enlist> getAllUserEnlist(String userid);
    public void delEnlistsByCid  (String competition);
    public List<Enlist> getAllEnlistBycid(String competition);
    public Enlist judgeUserEnlist(String userid,String competitionId);
    public Enlist findEnlist(String userid,String cid,String team);
    public Enlist insertEnlist(Enlist enlist, String TOKEN);
    public Enlist updateEnlist(Enlist enlist, String userid,String TOKEN);
}
