package dao;

import entity.Student;
import org.apache.commons.lang3.StringEscapeUtils;
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
    private Jedis jedis;

    public StudentDAO(){
        this.jedis = RedisUtil.getJedis();
    }

    public boolean isexitStudent(String id){
        boolean flag = jedis.exists("user:"+id);
        return flag;
    }

    /**
     * 增加或更新
     * @param student
     */
    public void addOrUpdateStudent(Student student) {
        Map<String, String> studentInfo = new LinkedHashMap();
        studentInfo.put("id", student.getId());
        studentInfo.put("name", student.getName());
        studentInfo.put("birthday", student.getBirthday().toString());
        studentInfo.put("description", student.getDescription());
        studentInfo.put("avgscore", String.valueOf(student.getAvgscore()));
        jedis.hmset("user:" + student.getId(), studentInfo);
        jedis.zadd("user:key", student.getAvgscore(), student.getId());
    }

    /**
     * 删除
     * @param id
     */
    public void removeStudent(String id) {
        jedis.del("user:" + id);
        jedis.zrem("user:key", id);
    }

    /**
     * 取得Student对象形式List
     * @param page
     * @return
     */
    public List<Student> listStudentByObject(int page) {
        Set<String> studentKey = jedis.zrevrange("user:key", (page - 1) * PAGE_SIZE, page * PAGE_SIZE - 1);
        List<Student> studentList = new ArrayList<>();
        for (String students : studentKey) {
            Student student = new Student();
            Map<String, String> studentInfo = jedis.hgetAll("user:" + students);
            student.setId(studentInfo.get("id"));
            student.setName(studentInfo.get("name"));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(studentInfo.get("birthday"), formatter);
            student.setBirthday(date);
            student.setDescription(studentInfo.get("description"));
            student.setAvgscore(Integer.parseInt(studentInfo.get("avgscore")));
            studentList.add(student);
        }
        return studentList;
    }

    /**
     * 取得Map类型对象集合
     * @param page
     * @return
     */
    public List<Map<String, String>> listStudentByMap(int page) {
        Set<String> studentKey = jedis.zrevrange("user:key", (page - 1) * PAGE_SIZE, page * PAGE_SIZE - 1);
        List<Map<String, String>> studentList = new ArrayList<>();
        for (String students : studentKey) {
            Map<String, String> studentInfo = jedis.hgetAll("user:" + students);
            studentList.add(studentInfo);
        }
        return studentList;
    }

    /**
     * 得到总页数
     * @param key
     * @param pageSize
     * @return
     */
    public int getTotalPageCount(String key, double pageSize) {
        long pageCountjedis = jedis.zcard(key);
        int page = (int) Math.ceil(pageCountjedis / pageSize);
        return page;
    }
}
