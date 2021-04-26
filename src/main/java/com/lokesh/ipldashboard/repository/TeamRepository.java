package com.lokesh.ipldashboard.repository;

import org.springframework.data.repository.CrudRepository;

import com.lokesh.ipldashboard.data.Team;

public interface TeamRepository extends CrudRepository<Team, Long>{
	
	Team findByTeamName(String teamName);

}
