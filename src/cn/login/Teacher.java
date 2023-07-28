package cn.login;

import cn.itcast.util.JDBCUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Teacher {
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
    public static void selectInformation(String t_account) {
        List<Map<String, Object>> maps = template.queryForList("select id,name 姓名,phone 手机号 from teacher where account = ?", t_account);
        for (Map<String, Object> map : maps) {
            for (String key : map.keySet()) {
                Object value = map.get(key);
                System.out.println(key + ": " + value);
            }
        }
    }
    public static void updateInformation(String t_account) {
        System.out.println("请输入姓名:");
        String name = sc.nextLine();
        template.update("update teacher set name = ? where account = ?", name, t_account);
        System.out.println("请填写手机号:");
        while (true) {
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
                    System.out.println("修改信息成功");
                    break;
                } else {
                    System.out.println("Error");
                }
            } else {
                System.out.println("Error");
            }
        }
    }
    public static void updatePassword(String t_account) {
        System.out.println("请输入你要修改的密码");
        String password = sc.nextLine();
        template.update("update teacher set password = ? where account = ?", password, t_account);
        System.out.println("修改密码成功");
    }
    public static void selectCourse(String t_account) {
        try {
            PreparedStatement pstat = conn.prepareStatement("select id from teacher where account = ?");
            pstat.setString(1, t_account);
            ResultSet rs = pstat.executeQuery();
            rs.next();
            int t_id = rs.getInt("id");

            PreparedStatement pstat1 = conn.prepareStatement("select * from course where teacher_id = ?");
            pstat1.setInt(1, t_id);
            ResultSet rs1 = pstat1.executeQuery();
            if (rs1.next()) {
                List<Map<String, Object>> maps = template.queryForList("select id 课程ID,coursename 课程名称 from course where teacher_id = ?", t_id);
                for (Map<String, Object> map : maps) {
                    Set<String> keys = map.keySet();
                    for (String key : keys) {
                        Object value = map.get(key);
                        System.out.println(key + ": " + value);
                    }
                }
            } else {
                System.out.println("暂无课程");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void selectCourseStudent(String t_account) {
        try {
            PreparedStatement pstat = conn.prepareStatement("select id from teacher where account = ?");
            pstat.setString(1, t_account);
            ResultSet rs = pstat.executeQuery();
            rs.next();
            int t_id = rs.getInt("id");

            PreparedStatement pstat1 = conn.prepareStatement("select id from course where teacher_id = ?");
            pstat1.setInt(1, t_id);
            ResultSet rs1 = pstat1.executeQuery();
            if (rs1.next()) {
                int c_id = rs1.getInt("id");
                PreparedStatement pstat2 = conn.prepareStatement("select student_id from linked where course_id = ?");
                pstat2.setInt(1, c_id);
                ResultSet rs2 = pstat2.executeQuery();
                if (rs2.next()) {
                    List<Map<String, Object>> maps = template.queryForList("select t3.id 学生ID,t3.name 学生姓名,t3.sex 学生性别,t3.classes 学生班级,t2.id 课程ID,t2.coursename 课程名称" +
                            " from linked t1,course t2,student t3,teacher t4 where t4.id = t2.teacher_id and t2.id = t1.course_id and t1.student_id = t3.id and  t2.teacher_id = ?", t_id);
                    for (Map<String, Object> map : maps) {
                        Set<String> keys = map.keySet();
                        for (String key : keys) {
                            Object value = map.get(key);
                            System.out.println(key + ": " + value);
                        }
                    }
                } else {
                    System.out.println("暂无学生");
                }
            } else {
                System.out.println("暂无学生");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void updateCourseInformation(String t_account) {
        try {
            PreparedStatement pstat = conn.prepareStatement("select id from teacher where account = ?");
            pstat.setString(1, t_account);
            ResultSet rs = pstat.executeQuery();
            rs.next();
            int t_id = rs.getInt("id");

            PreparedStatement pstat1 = conn.prepareStatement("select * from course where teacher_id = ?");
            pstat1.setInt(1, t_id);
            ResultSet rs1 = pstat1.executeQuery();
            if (rs1.next()) {
                List<Map<String, Object>> maps = template.queryForList("select id 课程ID,coursename 课程名称 from course where teacher_id = ?", t_id);
                for (Map<String, Object> map : maps) {
                    Set<String> keys = map.keySet();
                    for (String key : keys) {
                        Object value = map.get(key);
                        System.out.println(key + ": " + value);
                    }
                }
                System.out.println("请选择你要更改的课程ID:");
                while(true) {
                    String id = sc.nextLine();
                    try {
                        int cid = Integer.parseInt(id);
                        int index = -1;
                        PreparedStatement pstat2 = conn.prepareStatement("select id from course where teacher_id = ?");
                        pstat2.setInt(1, t_id);
                        ResultSet rs2 = pstat2.executeQuery();
                        while (rs2.next()){
                            int c_id = rs2.getInt("id");
                            if(c_id == cid){
                                System.out.println("请输入修改后的名称:");
                                String courseName = sc.nextLine();
                                template.update("update course set coursename = ? where id = ? ",courseName,c_id);
                                System.out.println("修改成功");
                                return;
                            }
                        }
                        System.out.println("输入错误");
                    } catch (NumberFormatException e) {
                        System.out.println("Error");
                    }
                }

            } else {
                System.out.println("暂无课程");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
