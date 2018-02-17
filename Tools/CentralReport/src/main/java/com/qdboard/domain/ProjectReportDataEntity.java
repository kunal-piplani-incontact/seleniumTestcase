package com.qdboard.domain;



import java.sql.Date;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ProjectReportDataEntity{
	String testingType;
	Double release;
	
	@NotNull(message = "The mandatory field total cannot be null.")
	@Min(value = 1, message = "The value of the field total must be greater than 0.")
	int total;
	
	@NotNull(message = "The mandatory field pass cannot be null.")
	@Min(value = 0, message = "The value of the field total must be equal to or greater than 0.")
	int pass;
	
	@NotNull(message = "The mandatory field fail cannot be null.")
	@Min(value = 0, message = "The value of the field total must be equal to or greater than 0.")
	int fail;
	
	int skip;
	String browser;
	@NotNull(message = "The mandatory field Execution Duration cannot be null.")
	float   executionDuration;
	//@NotNull(message = "The mandatory field Execution Date cannot be null.")
	Date executionDate;
	
	@NotNull(message = "The mandatory field environment cannot be null.")
	String environment;
	@Valid
	@NotNull(message = "The mandatory field projectID cannot be null.")
	String projectId;
	
	@NotNull(message = "The mandatory field projectName cannot be null.")
	String projectName;
	
	String owner;
	


	public String getTestingType() {
		return testingType;
	}

	public Double getRelease() {
		return release;
	}

	public int getTotal() {
		return total;
	}

	public int getPass() {
		return pass;
	}

	public int getFail() {
		return fail;
	}

	public int getSkip() {
		return skip;
	}

	public String getBrowser() {
		return browser;
	}

	public float getExecutionDuration() {
		return executionDuration;
	}

	public Date getExecutionDate() {
		return executionDate;
	}

	public String getEnvironment() {
		return environment;
	}

	public String getProjectId() {
		return projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public String getOwner() {
		return owner;
	}

	public void setTestingType(String testingType) {
		this.testingType = testingType;
	}

	public void setRelease(double d) {
		this.release = d;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public void setPass(int pass) {
		this.pass = pass;
	}

	public void setFail(int fail) {
		this.fail = fail;
	}

	public void setSkip(int skip) {
		this.skip = skip;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public void setExecutionDuration(float executionDuration) {
		this.executionDuration = executionDuration;
	}

	public void setExecutionDate(Date executionDate) {
		this.executionDate = executionDate;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public ProjectReportDataEntity getTestRunDetail() {
		return null;
	}

	public ProjectRegistrationEntity getRegisteredData() {
		return null;
	}
}
