package cn.tomleung.csv;

import java.io.File;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

import com.csvreader.CsvReader;

public class ImportProjectList {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Scanner in=new Scanner(System.in);
		System.out.println("请输入CSV文件夹地址：");
		process(in.nextLine(),"MonthReport");
		in.close();
		System.out.println("导入完成");
	}
	
	private static void process(String csvFolderPath,String tableName){
		File folder = new File(csvFolderPath);
		File[] files = folder.listFiles();
		for (int i = 0; i < files.length; i++) {
			File file=files[i];
			try {
				insert(file.getAbsolutePath(),tableName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void insert(String csvFile,String tableName) throws Exception{
		Connection con=new DBConnector().connect();
		String sql="INSERT INTO "+tableName+" VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement pstmt;
		try {
			pstmt = con.prepareStatement(sql);
			CsvReader r= new CsvReader(csvFile, ',',Charset.forName("GBK"));
			while(r.readRecord()){
				for(int i=1;i<=12;i++){
					pstmt.setString(i, r.get(i));
				}
				pstmt.executeUpdate();
			}
			r.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			con.close();
		}
		
	}

}
