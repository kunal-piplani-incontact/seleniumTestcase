package com.qdboard;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;

import com.qdboard.db.DButils;
import com.qdboard.db.SQLiteDBInitializationService;

public class BaseClass {
	
	@Autowired
	public SQLiteDBInitializationService sqliteDBInitializationService;
	
	@Autowired
	public DataSource dataSource;
	
	@Autowired
	protected
	DButils sqLiteDataBase;

}
