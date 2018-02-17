package com.nice.incontact.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class FileServer {
	
	ConfigLoader config = new ConfigLoader();
	
	private static String getCount(String db_connect_string, String db_userid, String db_password, String timestamp){
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Connection conn = DriverManager.getConnection(db_connect_string,
					db_userid, db_password);
			System.out.println("connected");
			Statement statement = conn.createStatement();
		
			String queryString = "select count(*) from [FileServer].dbo.CloudFileEntry WHERE Created >= '"+timestamp+"'";
			ResultSet rs = statement.executeQuery(queryString);
			while (rs.next()) {
				return rs.getString(1);
			}
			return "null";
		} catch (Exception e) {
			e.printStackTrace();
			return "null";
		}
	}

	public int getEntryCount(String timestamp){
		return Integer.parseInt(FileServer.getCount("jdbc:sqlserver://"+config.fileServerHost, config.userName, config.password, timestamp));
	}
}
