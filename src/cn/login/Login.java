package cn.login;

import cn.itcast.util.JDBCUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Login {
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
    public static void main(String[] args) {
        show();
    }
    private static void show(){
        while (true) {
            System.out.println("===========");
            System.out.println("1、学生登录");
            System.out.println("2、教师登录");
            System.out.println("3、注册账号");
            System.out.println("0、退出系统");
            System.out.println("===========");
            System.out.println("请输入你的选择：");
            String s = sc.nextLine();
            switch (s) {
                case "1":
                    studentLogin();
                    break;
                case "2":
                    teacherLogin();
                    break;
                case "3":
                    Enroll.enroll();
                    break;
                case "0":
                    System.out.println("谢谢使用！");
                    System.exit(0);
                default:
                    System.out.println("Error");
                    break;
            }
        }
    }
    private static void teacherLogin() {
        System.out.println("请输入账号：");
        String t_account = sc.nextLine();
        System.out.println("请输入密码：");
        String t_password = sc.nextLine();
        boolean t_flag = login("teacher", t_account, t_password);
        if (t_flag) {
            System.out.println("登录成功！");
            teacherInformation(t_account);
            while (true) {
                System.out.println("--------------");
                System.out.println("1、个人信息查询");
                System.out.println("2、个人信息修改");
                System.out.println("3、账号密码修改");
                System.out.println("4、查看我的课程");
                System.out.println("5、查看选课学生");
                System.out.println("6、修改课程信息");
                System.out.println("0、退出登录");
                System.out.println("--------------");
                System.out.println("请输入你的选择");
                String s0 = sc.nextLine();
                switch (s0) {
                    case "1":
                        Teacher.selectInformation(t_account);
                        break;
                    case "2":
                        Teacher.updateInformation(t_account);
                        break;
                    case "3":
                        Teacher.updatePassword(t_account);
                        break;
                    case "4":
                        Teacher.selectCourse(t_account);
                        break;
                    case "5":
                        Teacher.selectCourseStudent(t_account);
                        break;
                    case "6":
                        Teacher.updateCourseInformation(t_account);
                        break;
                    case "0":
                        return;
                    default:
                        System.out.println("Error");
                        break;
                }
            }
        } else {
            System.out.println("账号或密码错误");
        }
    }
    private static void studentLogin() {
        System.out.println("请输入账号：");
        String s_account = sc.nextLine();
        System.out.println("请输入密码：");
        String s_password = sc.nextLine();
        boolean s_flag = login("student", s_account, s_password);
        if (s_flag) {
            System.out.println("登录成功！");
            studentInformation(s_account);
            while (true) {
                System.out.println("-------------");
                System.out.println("1、个人信息查询");
                System.out.println("2、个人信息修改");
                System.out.println("3、账号密码修改");
                System.out.println("4、查看选课情况");
                System.out.println("5、选课");
                System.out.println("6、退课");
                System.out.println("0、退出登录");
                System.out.println("-------------");
                System.out.println("请输入你的选择：");
                String s0 = sc.nextLine();
                switch (s0) {
                    case "1":
                        Student.selectInformation(s_account);
                        break;
                    case "2":
                        Student.updateInformation(s_account);
                        break;
                    case "3":
                        Student.updatePassword(s_account);
                        break;
                    case "4":
                        Student.selectCourse(s_account);
                        break;
                    case "5":
                        Student.chooseCourse(s_account);
                        break;
                    case "6":
                        Student.deleteCourse(s_account);
                        break;
                    case "0":
                        System.out.println("谢谢使用");
                        return;
                    default:
                        System.out.println("Error");
                        break;
                }
            }
        } else {
            System.out.println("账号或密码错误");
        }
    }
    private static void studentInformation(String s_account) {
        try {
            PreparedStatement pstat = conn.prepareStatement("select * from student where account = ? and (name is null or classes is null)");
            pstat.setString(1, s_account);
            ResultSet rs = pstat.executeQuery();
            if (rs.next()) {
                System.out.println("请完善信息");
                System.out.println("请输入姓名：");
                String name = sc.nextLine();
                template.update("update student set name = ? where account = ?", name, s_account);
                System.out.println("请填写性别'男'或'女'");
                while (true) {
                    String sex = sc.nextLine();
                    if (sex.equals("男") || sex.equals("女")) {
                        template.update("update student set sex = ? where account = ?", sex, s_account);
                        break;
                    } else {
                        System.out.println("输入错误,请重新输入");
                    }
                }
                System.out.println("请填写班级：'1'或'2'");
                while (true) {
                    String classes = sc.nextLine();
                    if (classes.equals("1") || classes.equals("2")) {
                        template.update("update student set classes = ? where account = ?", classes, s_account);
                        System.out.println("完善信息成功");
                        break;
                    } else {
                        System.out.println("输入错误,请重新输入");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void teacherInformation(String t_account) {
        PreparedStatement pstat = null;
        ResultSet rs = null;
        try {
            pstat = conn.prepareStatement("select * from teacher where account = ? and (name is null or phone is null)");
            pstat.setString(1, t_account);
            rs = pstat.executeQuery();
            if (rs.next()) {
                System.out.println("请完善信息");
                System.out.println("请输入姓名:");
                String name = sc.nextLine();
                template.update("update teacher set name = ? where account = ?", name, t_account);
                System.out.println("请填写手机号:");
                while(true) {
                    String phoneNumber = sc.nextLine();
                    if (phoneNumber.length() == 11) {
                        byte[] bys = phoneNumber.getBytes();
                        int i = 0;
                        for (i = 0; i < 11; i++) {
                            if (bys[i] >= 48 && bys[i] <= 57) {
                            } else {
                                System.out.println("Error");
                                break;
                            }
                        }
                        if (i == 11) {
                            template.update("update teacher set phone = ? where account = ?", phoneNumber, t_account);
                            System.out.println("完善信息成功");
                            break;
                        } else {
                            System.out.println("Error");
                        }
                    } else {
                        System.out.println("Error");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static boolean login(String person, String account, String password) {
        if (account == null || password == null) {
            return false;
        }
        PreparedStatement pstat = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "select * from " + person + " where account = ? and password = ?";
            pstat = conn.prepareStatement(sql);
            pstat.setString(1, account);
            pstat.setString(2, password);
            rs = pstat.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
