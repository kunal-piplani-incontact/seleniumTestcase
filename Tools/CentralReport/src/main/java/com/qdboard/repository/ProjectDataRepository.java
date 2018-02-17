package com.qdboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.qdboard.domain.ProjectRegistrationEntity;


public interface ProjectDataRepository extends JpaRepository<ProjectRegistrationEntity, String> {

	

}
