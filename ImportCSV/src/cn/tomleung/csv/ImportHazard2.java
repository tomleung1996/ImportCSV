package cn.tomleung.csv;

import java.io.File;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

import com.csvreader.CsvReader;

public class ImportHazard2 {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Scanner in = new Scanner(System.in);
		System.out.println("请输入CSV文件夹地址：");
		process(in.nextLine(), "Project_Hazard2_RAW");
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
				String situation=r.get(18);
				String cname=r.get(19);
				String date=r.get(20);
				pstmt.setString(1, pname);
				pstmt.setString(7, situation);
				pstmt.setString(8, cname);
				pstmt.setString(9, date);
				int i=2;
				while(i<=13){
					if("√".equals(r.get(i))){
						pstmt.setString(2, r.get(i+1));
						pstmt.setString(3, r.get(i+2));
						pstmt.setString(4, i==2?"起重吊装及安装拆卸":i==8?"人工挖孔桩、地下暗桩、顶管及其它":"拆除、爆破");
						if(i==2){
							pstmt.setString(5, r.get(i+3)+"//"+r.get(i+4));
							pstmt.setString(6, r.get(i+5));
						}else{
							pstmt.setString(5, r.get(i+3));
							pstmt.setString(6, r.get(i+4));
						}
					}else{
						if(i==2)
							i+=6;
						else
							i+=5;
						continue;
					}
					pstmt.executeUpdate();
					if(i==2)
						i+=6;
					else
						i+=5;
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
