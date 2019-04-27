package database;

import java.sql.*;
import station.BusStation;

public class PushDB {

	public void pushData(BusStation[] busStation) {

		Connection conn = null;
		PreparedStatement stmt = null;
		// Statement stmt;
		ResultSet rs = null;
		String sql;

		try {

			Class.forName("org.mariadb.jdbc.Driver"); // JDBC driver�� �޸𸮿� �ε�
			conn = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306/DBName", "ID", "PASSWARD");

			System.err.println("���� ����");
			stmt = conn.prepareStatement("select * from station");
			/*
			 * ���ϴ� ������
			 */
			rs = stmt.executeQuery("DELETE FROM station");

			if (busStation != null) {
				for (int i = 1; i < busStation.length; i++) {
					sql = "insert into station values('" + busStation[i].getStationName() + "','"
							+ busStation[i].getIsBusArrived() + "')";
					rs = stmt.executeQuery(sql);
				}
			} else {
				sql = "insert into station values('" + "NoBus" + "','" + "false" + "')";
				rs = stmt.executeQuery(sql);
				System.err.println("NoBus");
			}
		} catch (SQLException e) {
			System.err.println("���� ����: " + e.getMessage());
			
		} catch (ClassNotFoundException e) {
			System.err.println("ClassNotFound: "+ e.getMessage());

		} catch (Exception e) {
			System.err.println("Exception�߻�: " + e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close(); // ���� ����
				}

				if (stmt != null) {
					stmt.close(); // ���û��������� ȣ�� ��õ
				}

				if (conn != null) {
					conn.close(); // �ʼ� ����
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
