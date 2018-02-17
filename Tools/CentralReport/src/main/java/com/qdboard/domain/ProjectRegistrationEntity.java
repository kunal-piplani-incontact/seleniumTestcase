package com.qdboard.domain;



import java.sql.SQLException;
import java.util.Random;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;

import com.qdboard.db.SQLiteDBInitializationService;

@Entity
@Table(name="Project")
public class ProjectRegistrationEntity  {
	

	 @Id
	 @Column(name = "projectId")
	 private String id;
	 
	 @NotNull(message = "The mandatory field projectName cannot be null.")
	 @Column(unique=true)
	 private String projectName;
	 
	 @NotNull(message = "The mandatory field registered Owner cannot be null.")
	 @Column(name = "regOwner")
	 private String owner;
	 
	 @Column(name = "RegistrationDate")
	 private long registrationDate;
	 
	 public ProjectRegistrationEntity() {
		 	
			super();
		}
	 
	 public ProjectRegistrationEntity(String projectName, String owner) throws InterruptedException, SQLException {
			super();
			setId("IC");
			this.owner = owner;
			this.projectName = projectName;
			this.registrationDate = System.currentTimeMillis();
			
			
		}
	 
	public Boolean findByName(String projectName){
		 
		return ( this.getProjectName().equalsIgnoreCase(projectName));
	
		
	 }

	public String getId() {
		return id;
	}

	public String getProjectName() {
		return projectName;
	}

	public String getOwner() {
		return owner;
	}

	public long getRegistrationDate() {
		return registrationDate;
	}

	public void setId(String string ) throws InterruptedException, SQLException {
		this.id = randomStringGen(string) ;
	
		
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public void setRegistrationDate(long l) {
	
		this.registrationDate = l ;
	}

	public String randomStringGen(String input) throws InterruptedException {
		StringBuffer stringName = new StringBuffer(input);
		Random random = new Random();
		for (int i = 0; i < 3; i++) { // attach upto 4 random numbers
			stringName.append(random.nextInt(10));
		}
		return stringName.toString();
	}


}
