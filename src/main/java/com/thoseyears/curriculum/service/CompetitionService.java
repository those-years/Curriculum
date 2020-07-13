package com.thoseyears.curriculum.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thoseyears.curriculum.entity.Competition;

import java.util.List;

public interface CompetitionService {
    public List<Competition> getAllCompetition(String page, String pagesize,String TOKEN,String competitionName);
    public List<Competition> getEnrollCompetitionByUserid(String page, String pagesize,String userid,String startCompetition,String  endCompetition);
    public Competition updateCompetition(Competition competition,String TOKEN);
    public List<Competition> getAllCompetitionByUserid(String TOKEN,String page, String pagesize,String userid,String startCompetition,String  endCompetition);
    public String Competitions(String userid,String competitionName,String enableEnroll);
    public Competition insertCompetition(Competition competition,String TOKEN);
    public List<Competition> findCompetitionById(String competitionId);
    public boolean delCompetition(String competitionId);

}
