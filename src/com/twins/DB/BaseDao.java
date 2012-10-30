package com.twins.DB;

/**
 * 这类提供基本的对于数据库的连接
 * by twins
 */
import java.sql.*;
import java.lang.String;
import org.apache.commons.logging.*;

public class BaseDao {

	private static final String user_name = "root";
	private static final String password = "123456";
	private static java.sql.Connection con = null; // java 跟数据库链接
	private PreparedStatement ps;
	public static Log log = LogFactory.getLog(BaseDao.class);

	public BaseDao() {
	}

	/**
	 * Here, to make the sql settings and get connection of the db
	 * 这里使用DriverManager进行数据库的相关操作，这种方法的实质是把有关的驱动特定信息
	 * 移植到应用程序里，增加了代码的复杂性，也增加了代码的移植性。 by twins
	 */
	public java.sql.Connection getConnction() {
		if ((con == null)) {
			try {
				Class.forName("org.gjt.mm.mysql.Driver").newInstance();
				String URL = "jdbc:mysql://localhost:3306/anti-plagiarism?useUnicode=true&characterEncoding=utf-8";
				con = DriverManager.getConnection(URL, user_name, password);
				System.out.println("successful->connection()->BaseDao()");
			} catch (Exception ex) {
				System.out.println("BaseDao->getConnection->" + ex.toString());
			}
		}
		try {
			if (con.isClosed()) {
				try {
					Class.forName("org.gjt.mm.mysql.Driver").newInstance();
					String URL = "jdbc:mysql://localhost:3306/anti-plagiarism?useUnicode=true&characterEncoding=utf-8";
					con = DriverManager.getConnection(URL, user_name, password);
				} catch (Exception ex) {
					System.out.println("BaseDao->getConnection->"
							+ ex.toString());
				}
			}

		} catch (Exception ex) {
			System.out.println("BaseDao->GetConnction");
		}

		return con;
	}

	/**
	 * close the db, to make the basic applaction
	 * 
	 * @param con
	 */
	public void close() {
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
	}

	/**
	 * run a sql statement sql + ? 占位符 by twins, for the light query!
	 * 
	 * @param sql
	 * @param obj
	 * @throws SQLException
	 */
	public void exec(final String sql, final Object[] obj) {
		if (sql != null && !sql.equals("")) {
			getConnction();
			try {

				if (con != null) {
					try {
						ps = con.prepareStatement(sql,
								ResultSet.TYPE_SCROLL_INSENSITIVE,
								ResultSet.CONCUR_READ_ONLY);
					} catch (SQLException e) {
						log.error("PreparedStatement error :" + e);
					}
					for (int i = 0; i < obj.length; i++) {
						ps.setObject(i + 1, obj[i]);
					}
					ps.execute();
				}
			} catch (SQLException ex) {
				log.error(ex);
			}
		} else {
			// to construct the exceptions
			log.error("null");
		}
	}

	/**
	 * To get resultset of the sql statement
	 * 
	 * @return
	 * @throws SQLException
	 */
	public ResultSet getResult() throws SQLException {
		return ps.getResultSet();
	}

	public static void main(final String[] args) {
		BaseDao basedao = new BaseDao();
		basedao.getConnction();
		basedao.close();
		System.out.println("successfully!");
	}
}
