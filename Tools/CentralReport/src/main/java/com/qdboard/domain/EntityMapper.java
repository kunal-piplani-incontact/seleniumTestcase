package com.qdboard.domain;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class EntityMapper {

	 
	public ProjectRegistrationEntity mapToProjectRegistrationEntity(String owner, String projectName) {
		
		ProjectRegistrationEntity newEntity = null;
		
		if (owner != null && !StringUtils.isEmpty(projectName)) {
			newEntity = new ProjectRegistrationEntity();
			newEntity.setProjectName(projectName);
			newEntity.setRegistrationDate(System.currentTimeMillis());
			newEntity.setOwner(owner);
						
		    }
		
		return newEntity;
	}

}
