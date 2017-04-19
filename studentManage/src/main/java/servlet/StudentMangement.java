package servlet;

import dao.StudentDAO;
import entity.Student;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Created by liqi1 on 2017/4/18.
 */
@WebServlet(name = "management",urlPatterns = "/management")
public class StudentMangement extends HttpServlet {
    private static StudentDAO  studentDAO ;

    public StudentMangement(){
        super();
        studentDAO = new StudentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (StringUtils.equals(action,"listAllStudent")){
            listStudent(req,resp);
        }else if(StringUtils.equals(action,"addStudent")){
           addOrUpdateStudent(req,resp);
        }else if(StringUtils.equals(action,"updateStudent")){
            addOrUpdateStudent(req,resp);
        }else if(StringUtils.equals(action,"removeStudent")){
            removeStudent(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       doGet(req,resp);

    }

    protected void listStudent(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException{
        int page = Integer.parseInt(req.getParameter("page"));
        List<Map<String,String>> studentList = studentDAO.listStudentByMap(page);
        req.setAttribute("studentList",studentList);
        resp.sendRedirect("../index.jsp");
    }


    protected void addOrUpdateStudent(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException{
        Student student = new Student();
        student.setId(req.getParameter("id"));
        student.setName(req.getParameter("name"));
        student.setBirthday(LocalDate.parse(req.getParameter("birthday")));
        student.setAvgscore(Integer.parseInt(req.getParameter("name")));
        student.setDescription(req.getParameter("description"));
        studentDAO.addOrUpdateStudent(student);
    }

    protected void removeStudent(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String studentID = req.getParameter("id");
        studentDAO.removeStudent(studentID);
    }

}
