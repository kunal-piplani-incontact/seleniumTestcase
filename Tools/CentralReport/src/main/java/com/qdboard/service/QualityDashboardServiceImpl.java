package com.qdboard.service;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qdboard.BaseClass;
import com.qdboard.db.SQLiteDBInitializationService;
import com.qdboard.domain.EntityMapper;
import com.qdboard.domain.ProjectRegistrationEntity;
import com.qdboard.domain.ProjectReportDataEntity;
import com.qdboard.exception.GenericException;
import com.qdboard.repository.ProjectDataRepository;


@Component
public class QualityDashboardServiceImpl extends BaseClass {

	 @Autowired
	  public ProjectDataRepository repository;
	 
	 @Autowired
	 public EntityMapper mapper;
	 


	public ProjectRegistrationEntity entity = new ProjectRegistrationEntity();
	public ProjectReportDataEntity dataEntity = new ProjectReportDataEntity();
	
	public boolean addProjectRegistration(String owner, String projectName) throws GenericException, InterruptedException, SQLException{
		
		if (projectName != null) {
			if((sqliteDBInitializationService.selectByProjectName(projectName))){
				throw new GenericException("Project is already registered");
			}else{
	        return sqliteDBInitializationService.prepareAndGetProjectRegistrationEntity(projectName, owner);
			}
		}
		return false;     

	}

	public void addProjectReportData(ProjectReportDataEntity config, String projectName) throws Exception {
		String configId = null;
		if (projectName != null) {
			configId = config.getProjectId();
		}
	
	//	if (sqliteDBInitializationService.selectByProjectId(configId)){
			 sqliteDBInitializationService.prepareAndGetProjectReportDataEntity(config);
	//	}
		
			
		}
		
	}


