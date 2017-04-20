package test;
import dao.StudentDAO;
import entity.Student;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by liqi1 on 2017/4/19.
 */
public class Test {

    StudentDAO studentDAO = new StudentDAO();

    @org.junit.Test
    public void addStudentTest(){
        LocalDate date = LocalDate.now();
        Student student;
        for (int i = 0; i <23 ; i++) {
            if(i%2==0){
                student = new Student(""+i,"user"+i,date,"gghhf",60+2*i);
            }else {
                student = new Student(""+i,"user"+i,date,"gghhf",60+2*i);
            }
            studentDAO.addOrUpdateStudent(student);
        }
    }
    @org.junit.Test
    public void listStudentTest(){
        List<Student> studentList = studentDAO.listStudentByObject(1);
        for (Student student: studentList) {
            System.out.println(student.toString());
        }
    }
    @org.junit.Test
    public void mathTest(){
        double a =26;
        double b =10;
        System.out.println(Math.ceil((a/b)));
    }
}
