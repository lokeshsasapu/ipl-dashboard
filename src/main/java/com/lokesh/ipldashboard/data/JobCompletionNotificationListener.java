package com.lokesh.ipldashboard.data;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

  private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

  private final EntityManager em;

  @Autowired
  public JobCompletionNotificationListener(EntityManager em) {
    this.em = em;
  }

  @Override
  @Transactional
  public void afterJob(JobExecution jobExecution) {
    if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
      log.info("!!! JOB FINISHED! Time to verify the results");

    }

    Map<String, Team> teamData = new HashMap<>();
    
    em.createQuery("select team1, count(*) from Match group by team1", Object[].class)
    .getResultList()
    .stream()
    .map(m -> new Team((String) m[0], (long) m[1]))
    .forEach(team -> teamData.put(team.getTeamName(), team));
    
    em.createQuery("select team2, count(*) from Match group by team2", Object[].class)
    .getResultList()
    .stream()
    .forEach(m -> {
		Team team = teamData.get((String) m[0]);
		team.setTotalMatches(team.getTotalMatches()+(long)m[1]);
	});
    
    em.createQuery("select matchWinner, count(*) from Match group by matchWinner", Object[].class)
    .getResultList()
    .stream()
    .forEach(mw -> {
    	Team team = teamData.get((String)mw[0]);
    	if(team != null) team.setTotalWins((long)mw[1]);
    });
    teamData.values().forEach(t -> em.persist(t));
    teamData.values().forEach(t -> System.out.println(t));
    
    
    
  }
}