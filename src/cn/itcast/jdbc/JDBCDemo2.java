package cn.itcast.jdbc;

import cn.itcast.util.JdbcUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCDemo2 {
    public static void main(String[] args) {
        Connection conn = null;
        Statement stat = null;
        try {
            conn = JdbcUtil.getConnection();

            String sql = "update employee set name = 'zhangsan',age = 70 where id = 1";
            stat = conn.createStatement();
            int count = stat.executeUpdate(sql);
            System.out.println(count);
            if (count > 0) {
                System.out.println("修改成功！");
            } else {
                System.out.println("修改失败！");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtil.close(stat,conn);
        }
    }
}
