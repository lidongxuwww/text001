package cn.itcast.jdbc;

import cn.itcast.util.JdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class JDBCDmeo4 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入用户名：");
        String username = sc.nextLine();
        System.out.println("请输入密码：");
        String password = sc.nextLine();

        boolean flag = new JDBCDmeo4().login(username, password);
        if(flag){
            System.out.println("登录成功！");
        }else {
            System.out.println("用户名或密码错误！");
        }
    }

    public boolean login(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        Connection conn = null;
        PreparedStatement pstat = null;
        ResultSet rs = null;

        try {
            conn = JdbcUtil.getConnection();
            String sql = "select * from user where username = ? and password = ?";
            pstat = conn.prepareStatement(sql);
            pstat.setString(1, username);
            pstat.setString(2, password);

            rs = pstat.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pstat, conn);
        }
        return false;
    }
}
