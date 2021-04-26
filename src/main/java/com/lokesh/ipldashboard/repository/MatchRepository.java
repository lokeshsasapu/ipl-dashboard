package com.lokesh.ipldashboard.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.lokesh.ipldashboard.model.Match;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface MatchRepository extends CrudRepository<Match, Long>{
	
	List<Match> getByTeam1OrTeam2OrderByDateDesc(String team1, String team2, Pageable pageable);
	
	default List<Match> findLatestMatchesByTeamName(String team1, String team2, int count){
		
		return getByTeam1OrTeam2OrderByDateDesc(team1, team2, PageRequest.of(0, count));
	}

}
