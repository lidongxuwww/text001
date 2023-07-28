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

public class Student {
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
    public static void selectInformation(String s_account){
        String sql = "select id,name 姓名,sex 性别,classes 班级 from student where account = ?";
        Map<String, Object> map = template.queryForMap(sql, s_account);
        Set<String> keySets = map.keySet();
        for (String key : keySets) {
            Object value = map.get(key);
            System.out.println(key +": " + value);
        }
    }
    public static void updateInformation(String s_account){
        System.out.println("请输入姓名：");
        String name = sc.nextLine();
        template.update("update student set name = ? where account = ?",name,s_account);
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
                System.out.println("修改信息成功");
                break;
            } else {
                System.out.println("输入错误,请重新输入");
            }
        }
    }
    public static void updatePassword(String s_account){
        System.out.println("请输入你要修改的密码");
        String password = sc.nextLine();
        template.update("update student set password = ? where account = ?",password,s_account);
        System.out.println("修改密码成功");
    }
    public static void selectCourse(String s_account){
        try {
            PreparedStatement pstat = conn.prepareStatement("select id from student where account = ?");
            pstat.setString(1,s_account);
            ResultSet rs = pstat.executeQuery();
            rs.next();
            int s_id = rs.getInt("id");
            PreparedStatement pstat1 = conn.prepareStatement("select * from linked where student_id = ?");
            pstat1.setInt(1,s_id);
            ResultSet rs1 = pstat1.executeQuery();
            if(rs1.next()){
                String sql = "select t1.course_id 课程ID,t2.coursename 课程名称,t3.name 学生姓名 " +
                        "from linked t1,course t2,student t3 " +
                        "where t1.student_id = t3.id and t1.course_id = t2.id and t3.account = ?";
                List<Map<String, Object>> maps = template.queryForList(sql, s_account);
                for (Map<String, Object> map : maps) {
                    Set<String> keys = map.keySet();
                    for (String key : keys) {
                        Object value = map.get(key);
                        System.out.println(key + ": " + value);
                    }
                }
            }else{
                System.out.println("你还没有选课呢");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void chooseCourse(String s_account){
        String sql = "select t2.id 课程ID,t2.coursename 课程名称,t4.name 教课教师 from course t2,teacher t4 where t2.teacher_id = t4.id";
        List<Map<String, Object>> maps = template.queryForList(sql);
        for (Map<String, Object> map : maps) {
            Set<String> keys = map.keySet();
            for (String key : keys) {
                Object value = map.get(key);
                System.out.println(key + ": " + value);
            }
        }

        PreparedStatement pstat = null;
        PreparedStatement pstat1 = null;
        PreparedStatement pstat2 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;

        System.out.println("请输入你要选的课程ID：");
        while (true) {
            String id = sc.nextLine();
            try {
                int c_id = Integer.parseInt(id);
                pstat2 = conn.prepareStatement("select * from course where id = ?");
                pstat2.setInt(1,c_id);
                rs2 = pstat2.executeQuery();
                if(!rs2.next()){
                    System.out.println("该课程不存在");
                    continue;
                }
                pstat = conn.prepareStatement("select id from student where account = ?");
                pstat.setString(1,s_account);
                rs = pstat.executeQuery();
                rs.next();
                int s_id = rs.getInt(1);
                pstat1 = conn.prepareStatement("select * from linked where student_id = ? and course_id = ?");
                pstat1.setInt(1,s_id);
                pstat1.setInt(2,c_id);
                rs1 = pstat1.executeQuery();
                if(rs1.next()){
                    System.out.println("该课程已选过");
                }else {
                    template.update("insert into linked (course_id,student_id) values (?,?)",c_id,s_id);
                    System.out.println("选课成功");
                }
                break;
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NumberFormatException e){
                System.out.println("Error");
            }
        }
    }
    public static void deleteCourse(String s_account){
        PreparedStatement pstat = null;
        PreparedStatement pstat1 = null;
        PreparedStatement pstat2 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        int s_id = 0;
        try {
            pstat = conn.prepareStatement("select id from student where account = ?");
            pstat.setString(1, s_account);
            rs = pstat.executeQuery();
            rs.next();
            s_id = rs.getInt(1);
            PreparedStatement pstat3 = conn.prepareStatement("select * from linked where student_id = ?");
            pstat3.setInt(1,s_id);
            ResultSet rs3 = pstat3.executeQuery();
            if(rs3.next()){
                while(true) {
                    try {
                        System.out.println("请输入你要删除的课程ID:");
                        String id = sc.nextLine();
                        int c_id = Integer.parseInt(id);
                        pstat1 = conn.prepareStatement("select * from linked where course_id = ? and student_id = ?");
                        pstat1.setInt(1, c_id);
                        pstat1.setInt(2, s_id);
                        rs1 = pstat1.executeQuery();
                        if (rs1.next()) {
                            template.update("delete from linked where course_id = ? and student_id = ?", c_id, s_id);
                            System.out.println("退课成功");
                            break;
                        } else {
                            pstat2 = conn.prepareStatement("select * from course where id = ?");
                            pstat2.setInt(1,c_id);
                            rs2 = pstat2.executeQuery();
                            if(!rs2.next()){
                                System.out.println("该课程不存在");
                                continue;
                            }
                            System.out.println("你没有选择该课程");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Error");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                System.out.println("你还没有选择课程呢");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
