package cn.itcast.jdbc;

import cn.itcast.util.JdbcUtil;

import java.sql.*;
import java.util.Scanner;

public class JDBCDemo3 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入用户名：");
        String username = sc.nextLine();
        System.out.println("请输入密码：");
        String password = sc.nextLine();

        boolean flag = new JDBCDemo3().login(username, password);
        if (flag) {
            System.out.println("登录成功");
        } else {
            System.out.println("用户名或密码错误");
        }
    }

    public boolean login(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtil.getConnection();
            String sql = "select * from user where username = '"+ username +"' and password = '" + password + "'";
            stat = conn.createStatement();
            rs = stat.executeQuery(sql);
            return rs.next();


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, stat, conn);
        }

        return false;
    }
}
