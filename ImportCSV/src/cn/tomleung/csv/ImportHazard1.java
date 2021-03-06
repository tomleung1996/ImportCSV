package cn.tomleung.csv;

import java.io.File;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

import com.csvreader.CsvReader;

public class ImportHazard1 {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Scanner in = new Scanner(System.in);
		System.out.println("请输入CSV文件夹地址：");
		process(in.nextLine(), "Project_Hazard1_RAW");
		in.close();
		System.out.println("导入完成");
	}

	private static void process(String csvFolderPath, String tableName) {
		File folder = new File(csvFolderPath);
		File[] files = folder.listFiles();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			try {
				insert(file.getAbsolutePath(), tableName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void insert(String csvFile, String tableName) throws Exception {
		Connection con = new DBConnector().connect();
		String sql = "INSERT INTO " + tableName + " VALUES(?,?,?,?,?,?,?,?,?)";
		PreparedStatement pstmt;
		try {
			pstmt = con.prepareStatement(sql);
			CsvReader r = new CsvReader(csvFile, ',', Charset.forName("GBK"));
			while (r.readRecord()) {
				String pname=r.get(1);
				String situation=r.get(17);
				String cname=r.get(18);
				String date=r.get(19);
				pstmt.setString(1, pname);
				pstmt.setString(7, situation);
				pstmt.setString(8, cname);
				pstmt.setString(9, date);
				for(int i=2;i<=12;i+=5){
					if("√".equals(r.get(i))){
						pstmt.setString(2, r.get(i+1));
						pstmt.setString(3, r.get(i+2));
						pstmt.setString(4, i==2?"深基坑":i==7?"高支模":"脚手架");
						pstmt.setString(5, r.get(i+3));
						pstmt.setString(6, r.get(i+4));
					}else{
						continue;
					}
					pstmt.executeUpdate();
				}
//				System.out.println(situation);
//				if("√".equals(r.get(2))){
//					pstmt.setString(2, r.get(3));
//					pstmt.setString(3, r.get(4));
//					pstmt.setString(4, "深基坑");
//					pstmt.setString(5, r.get(5));
//					pstmt.setString(6, r.get(6));
//				}else{
//					continue;
//				}

//				for (int i = 1; i <= 12; i++) {
//					pstmt.setString(i, r.get(i));
//				}
//				pstmt.executeUpdate();
			}
			r.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			con.close();
		}

	}

}
