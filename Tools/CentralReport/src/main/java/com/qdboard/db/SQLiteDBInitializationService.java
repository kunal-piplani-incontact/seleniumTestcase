package com.qdboard.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qdboard.BaseClass;
import com.qdboard.domain.ProjectRegistrationEntity;
import com.qdboard.domain.ProjectReportDataEntity;
import com.qdboard.exception.GenericException;


@Component
public class SQLiteDBInitializationService extends BaseClass {

	private static final Logger logger = LoggerFactory.getLogger(SQLiteDBInitializationService.class);





	public boolean buildUpRegisterData(ProjectRegistrationEntity regEntity) {
		return sqLiteDataBase.insertProjectTable(regEntity);
		
	}

	public boolean prepareAndGetProjectRegistrationEntity(String projectname, String owner) throws InterruptedException, SQLException {
		ProjectRegistrationEntity regEntity = new ProjectRegistrationEntity(projectname, owner);
		while (selectByProjectId(regEntity.getId())){
			regEntity = new ProjectRegistrationEntity(projectname, owner);
		}
		return buildUpRegisterData(regEntity);
		//	return regEntity;
	}
	
	public void buildUpTestingDetailsData(ProjectReportDataEntity recordEntity) throws InterruptedException, SQLException, GenericException {
		if (selectByProjectId(recordEntity.getProjectId())){
			sqLiteDataBase.insertTestingDetailsTable(recordEntity);
		}else 
			throw new GenericException("Project ID does not exists");
		
		
	}

	public ProjectReportDataEntity prepareAndGetProjectReportDataEntity(ProjectReportDataEntity config) throws InterruptedException, SQLException, GenericException {
		buildUpTestingDetailsData(config);
		return config;
	}

	public boolean selectByProjectName(String projectName) throws InterruptedException {
		String sql = "SELECT * FROM Project WHERE projectName = ?";
		try (PreparedStatement pstmt = sqLiteDataBase.connect().prepareStatement(sql)) {
			pstmt.setString(1, projectName);
			ResultSet rs = pstmt.executeQuery();
			if (rs.getRow() >= 1)
				return true;
			else 
				return false;

		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return false;
		
	}
	
	public boolean selectByProjectId(String projectId) throws InterruptedException, SQLException {
		
		Connection connection = sqLiteDataBase.connect();
		String sql = "SELECT * FROM Project WHERE projectId = ?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, projectId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.getRow() > 1)
				return true;
			else 
				return false;

		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return false;
	}




}
