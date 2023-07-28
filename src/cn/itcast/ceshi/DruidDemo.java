package cn.itcast.ceshi;

import cn.itcast.util.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DruidDemo {
    public static void main(String[] args) {
        Connection conn = null;
        PreparedStatement pstat = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "insert into user values (null,?,?)";
            pstat = conn.prepareStatement(sql);
            pstat.setString(1,"wangwu");
            pstat.setString(2,"abc");

            int count = pstat.executeUpdate();
            System.out.println(count);

        } catch (SQLException e) {
           e.printStackTrace();
        }finally {
            JDBCUtils.close(pstat,conn);
        }
    }
}
