package org.big.service;

import java.sql.Timestamp;
import java.util.List;

import org.big.common.UUIDUtils;
import org.big.entity.Team;
import org.big.entity.UserDetail;
import org.big.entity.UserTeam;
import org.big.repository.TeamRepository;
import org.big.repository.UserTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class TeamServiceImpl implements TeamService {
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserTeamRepository userTeamRepository;

    @Override
    public List<Team> selectTeamsByUserId(String userId) {
        return this.teamRepository.selectTeamsByUserId(userId);
    }

    
    @Override
    public Team findbyID(String ID) {
        return this.teamRepository.getOne(ID);
    }

    @Override
    public void saveOne(Team thisTeam) {
        if (thisTeam.getId() == null || thisTeam.getId().equals("") || thisTeam.getId().length() <= 0) {
            thisTeam.setId(UUIDUtils.getUUID32());
            thisTeam.setAdddate(new Timestamp(System.currentTimeMillis()));
        }
        this.teamRepository.save(thisTeam);
    }

    @Override
    public void saveOneByUser(Team thisTeam) {
        thisTeam.setId(UUIDUtils.getUUID32());
    	UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        thisTeam.setLeader(thisUser.getId());
        thisTeam.setAdddate(new Timestamp(System.currentTimeMillis()));
        
        UserTeam thisUserTeam = new UserTeam();
        thisUserTeam.setUserId(thisUser.getId());
        thisUserTeam.setTeamId(thisTeam.getId());

        this.teamRepository.save(thisTeam);
        this.userTeamRepository.save(thisUserTeam);
    }

    @Override
    public Boolean removeOne(String ID) {
    	return removeOneOrMoreTeamByTid(ID);
    }

    @Override
    public Boolean removeOneByUser(String ID) {
    	return removeOneOrMoreTeamByTid(ID);
    }

    /**
     * 通过TeamId实现单个删除或批量删除
     * @param ID
     */
	private Boolean removeOneOrMoreTeamByTid(String ID) {
		String mark = this.teamRepository.findOneById(ID).getMark();
    	if (!mark.equals("Default")) {
    		this.userTeamRepository.deleteByTeamId(ID);    // 先操作中间表，删除外键关系
    		this.teamRepository.deleteById(ID);            // 再操作Team表
    		return true;
		}
    	return false;
	}

    @Override
    public Team findOneByName(String name) {
        return this.teamRepository.findOneByName(name);
    }

    @Override
    public int countMembersByTeamId(String ID) {
        return this.teamRepository.countMembersByTeamId(ID);
    }




	@Override
	public void saveForUpdate(Team thisTeam) {
		this.teamRepository.save(thisTeam);
	}

}