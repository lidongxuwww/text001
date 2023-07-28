package cn.login;

import cn.itcast.util.JDBCUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Enroll {
    private static JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    private static Connection conn;
    private static Scanner sc = new Scanner(System.in);

    static {
        try {
            conn = JDBCUtils.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void enroll() {
        System.out.println("请输入要注册的账号身份: 1、老师 或 2、学生");
        String s = sc.nextLine();
        if (s.equals("1")) {
            enroll_d("teacher");
        } else if (s.equals("2")) {
            enroll_d("student");
        } else {
            System.out.println("输入错误");
        }

    }
    public static void enroll_d(String person) {
        try {
            System.out.println("请输入账号：");
            String account = sc.nextLine();
            PreparedStatement pstat = conn.prepareStatement("select * from " + person + " where account = ?");
            pstat.setString(1, account);
            ResultSet rs = pstat.executeQuery();
            if (rs.next()) {
                System.out.println("账号已存在");
            } else {
                System.out.println("请设置密码：");
                String password = sc.nextLine();
                template.update("insert into " + person + " (account,password) values (?,?)", account, password);
                System.out.println("注册成功,请重新登录！");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
