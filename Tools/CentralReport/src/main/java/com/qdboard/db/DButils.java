package com.qdboard.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.qdboard.BaseClass;
import com.qdboard.domain.ProjectRegistrationEntity;
import com.qdboard.domain.ProjectReportDataEntity;

@Configuration

public class DButils extends BaseClass {
	
	private final static Logger logger = LoggerFactory.getLogger(DButils.class);



	@Value("${spring.datasource.url}")
	private String jdbcURL;
	
	@PostConstruct
	public void initialize() {
		try {
			Connection connection = dataSource.getConnection();
			Statement statement = connection.createStatement();
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS Project (" + "projectId varchar(30) Primary key , "
					+ "projectName varchar(30) NOT NULL UNIQUE," + "regOwner varchar(30) NOT NULL ,"
					+ "registrationDate LONG )");
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS TestingDetails(" + " projectId varchar(30) NOT NULL, "
					+ "	projectName varchar(30) NOT NULL," + "	regOwner varchar(30) NOT NULL,"
					+ "	release Double NOT NULL," + "	testingType varchar(100) NOT NULL," + "	total integer NOT NULL,"
					+ "	pass integer NOT NULL," + "	fail integer NOT NULL," + "	skip integer NOT NULL,"
					+ "	browser varchar(30)," + "	executionDuration Float NOT NULL," + "	executionDate DATE,"
					+ "	environment varchar(100) NOT NULL," + " PRIMARY KEY(projectName,regOwner),"
					+ " FOREIGN KEY (projectId) REFERENCES Project(projectId))");
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public Connection connect() throws SQLException {
		return DriverManager.getConnection(jdbcURL);
	}
	
	public boolean insertProjectTable(ProjectRegistrationEntity regEntity) {
		String sql = "INSERT INTO Project (projectId, projectName, regOwner, registrationDate) VALUES(?,?,?,?)";

		try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, regEntity.getId());
			pstmt.setString(2, regEntity.getProjectName());
			pstmt.setString(3, regEntity.getOwner());
			pstmt.setLong(4, regEntity.getRegistrationDate());
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			logger.error(e.getMessage());
			return false;
		}
	}
	
	public void insertTestingDetailsTable(ProjectReportDataEntity testDetails) {
		String sql = "INSERT INTO TestingDetails (projectId, projectName, regOwner, release, testingType, total,"
			+ "pass, fail, skip, browser, executionDuration,executionDate,environment)" 
			+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
		try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, testDetails.getProjectId());
			pstmt.setString(2, testDetails.getProjectName());
			pstmt.setString(3,  testDetails.getOwner());
			pstmt.setDouble(4,  testDetails.getRelease());
			pstmt.setString(5,  testDetails.getTestingType());
			pstmt.setInt(6,  testDetails.getTotal());
			pstmt.setInt(7,  testDetails.getPass());
			pstmt.setInt(8,  testDetails.getFail());
			pstmt.setInt(9,  testDetails.getSkip());
			pstmt.setString(10,  testDetails.getBrowser());
			pstmt.setFloat(11,  testDetails.getExecutionDuration());
			pstmt.setDate(12,  testDetails.getExecutionDate());
			pstmt.setString(13,  testDetails.getEnvironment());
			pstmt.executeUpdate();
	
	
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}
}
