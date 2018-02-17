package com.qdboard.controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.qdboard.BaseClass;
import com.qdboard.domain.ProjectReportDataEntity;
import com.qdboard.exception.GenericException;
import com.qdboard.service.QualityDashboardServiceImpl;


@RestController
@RequestMapping(value="/qualityDB/project/")
@Validated
public class QualityDashboardController extends BaseClass{

	@Autowired
	public QualityDashboardServiceImpl qualityDashboardServiceImpl;
	

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/registration", consumes = {
			MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public @ResponseBody String registerProjects(@Valid @RequestParam String owner, @Valid @RequestParam String projectName)
					throws GenericException {
		try {
			if ((projectName == null || projectName.isEmpty()) && (owner == null || owner.isEmpty())){
				return "Project Name and Owner can not be NULL or Empty";
			}
			
			if (projectName == null || projectName.isEmpty()){
				return "Project Name can not be NULL or Empty";
			}
			if (owner == null || owner.isEmpty()){
				return "Owner can not be NULL or Empty";
			}
			if (qualityDashboardServiceImpl.addProjectRegistration(owner, projectName)){
				String sql = "SELECT * FROM Project WHERE projectName = ?";
				try (PreparedStatement pstmt = sqLiteDataBase.connect().prepareStatement(sql)) {
					pstmt.setString(1, projectName);
					ResultSet rs = pstmt.executeQuery();
					System.out.println(rs.getString(1));
				return "Project Details Saved, please note down your projectID "+rs.getString(1);
				}
			}
			else 
				return "Project Name already exists";
		} catch (Exception e) {
			e.printStackTrace();
			return "Couldnot Save project details, please retry";
		}
	}


	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/reportData/{projectName}", consumes = {
			MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public ResponseEntity addReportingData(@Valid @RequestBody ProjectReportDataEntity config, @PathVariable(name = "projectName", required = true) String projectName) throws Exception {
		qualityDashboardServiceImpl.addProjectReportData(config, projectName);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
