package dao;

import entity.Student;
import redis.clients.jedis.Jedis;
import util.RedisUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by liqi1 on 2017/4/18.
 */
public class StudentDAO {
    private static final int PAGE_SIZE = 10;
    public Jedis jedis;

    public void addOrUpdateStudent(Student student){
        jedis = RedisUtil.getJedis();
        Map<String,String > studentInfo = new HashMap();
        studentInfo.put("id",student.getId());
        studentInfo.put("name",student.getName());
        studentInfo.put("birthday",student.getBirthday().toString());
        studentInfo.put("description",student.getDescription());
        studentInfo.put("avgscore",String.valueOf(student.getAvgscore()));
        jedis.hmset("user:"+student.getId(),studentInfo);
        jedis.zadd("user:key", student.getAvgscore(),student.getId());
        jedis.resetState();
    }

    public void removeStudent(String id){
        jedis = RedisUtil.getJedis();
        jedis.del("user:"+id);
        jedis.zrem("user:key",id);
        jedis.resetState();
    }

    public List<Student> listStudentByObject(int page) {
        jedis = RedisUtil.getJedis();
        Set<String> studentKey = jedis.zrevrange("user:key",(page-1)*PAGE_SIZE,page*PAGE_SIZE-1);
        List<Student> studentList = new ArrayList<>();
        for (String students: studentKey) {
            Student student = new Student();
            Map<String,String>  studentInfo = jedis.hgetAll("user:"+students);
                student.setId(studentInfo.get("id"));
                student.setName(studentInfo.get("name"));
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = LocalDate.parse(studentInfo.get("birthday"),formatter);
                student.setBirthday(date);
                student.setDescription(studentInfo.get("description"));
                student.setAvgscore(Integer.parseInt(studentInfo.get("avgscore")));
                studentList.add(student);
        }
       return studentList;
    }

    public List<Map<String,String>> listStudentByMap(int page) {
        jedis = RedisUtil.getJedis();
        Set<String> studentKey = jedis.zrange("user:key",(page-1)*PAGE_SIZE,page*PAGE_SIZE-1);
        List<Map<String,String>> studentList = new ArrayList<>();
        Map<String,String> studentInfo;
        for (String students: studentKey) {
              studentInfo = jedis.hgetAll("user:"+students);
            studentList.add(studentInfo);
        }
        return studentList;
    }
}
