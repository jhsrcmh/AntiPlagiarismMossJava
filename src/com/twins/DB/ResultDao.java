package com.twins.DB;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import com.twins.entity.*;
public class ResultDao {
	private BaseDao bd = new BaseDao();
	
	public boolean addResultWithBatch(ArrayList<Result> result) {
		try {
			java.sql.PreparedStatement sql = this.bd.getConnction().prepareStatement("insert into result(fileA, fileB, score, sameLines) values(?,?,?,?)");
			for(Result rb :result) {
				sql.setString(1, rb.getFileA());
				sql.setString(2, rb.getFileB());
				sql.setFloat(3, rb.getScore());
				sql.setInt(4, rb.getSameLines());
				sql.addBatch();
			}
			sql.executeBatch();
			System.out.println("successfully insert the result");
			return true;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}	
		return false;
	}
	
	public int getCount() {
		try {
			java.sql.Statement sql = this.bd.getConnction().createStatement();
			String str = "select count(*) from result";
			java.sql.ResultSet rs = sql.executeQuery(str);
			while( rs.next() ) {
				return rs.getInt(1);
			}
		} catch(SQLException ex) {
			System.out.println(ex);
		}
		return -1;
	}
	public int getErrorCount(float percetange) {
		try {
			java.sql.Statement sql = this.bd.getConnction().createStatement();
			String str = "select count(*) from result where score > "+percetange;
			java.sql.ResultSet rs = sql.executeQuery(str);
			while( rs.next() ) {
				return rs.getInt(1); 
			}
		} catch(SQLException ex) {
			System.out.println(ex);
		}
		return 0;
	}
	public String getPercentage(float percentage)
	{
		int temp = getCount();
		int error = getErrorCount(percentage);
		if(temp!=0) {
			double a = (double)error/temp;
			return (new DecimalFormat("00.0000").format(a*100)) +"%";
		}
		return "Not record or error encountered";
	}
}
