package cn.tomleung.csv;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnector {
	String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	String dbURL = "jdbc:sqlserver://localhost:1433;DatabaseName=Project1";
	String userName = "jg";
	String userPwd = "jg002";
	Connection dbConn;
	public Connection connect(){
		try{
			Class.forName(driverName);
			dbConn=DriverManager.getConnection(dbURL,userName,userPwd);
		}catch(Exception e){
			e.printStackTrace();
		}
		return dbConn;
	}
}
