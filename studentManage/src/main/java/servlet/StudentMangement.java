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
@WebServlet(name = "management", urlPatterns = "/management")
public class StudentMangement extends HttpServlet {
    private static StudentDAO studentDAO;

    public StudentMangement() {
        super();
        studentDAO = new StudentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (StringUtils.equals(action, "listAllStudent")) {
            listStudent(req, resp);
        } else if (StringUtils.equals(action, "add")) {
            if(!studentDAO.isexitStudent(req.getParameter("id"))){
                addOrUpdateStudent(req, resp);
            }else {
                req.setAttribute("errorInfo","该用户已存在！");
                req.getRequestDispatcher("error.jsp").forward(req,resp);
            }
        } else if (StringUtils.equals(action, "update")) {
            addOrUpdateStudent(req, resp);
        } else if (StringUtils.equals(action, "remove")) {
            removeStudent(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);

    }

    protected void listStudent(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = Integer.parseInt(req.getParameter("page"));
        List<Map<String, String>> studentList = studentDAO.listStudentByMap(page);
        int pageCount = studentDAO.getTotalPageCount("user:key", 10d);
        req.setAttribute("studentList", studentList);
        req.setAttribute("page", page);
        req.setAttribute("pageCount", pageCount);
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }


    protected void addOrUpdateStudent(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        Student student = new Student();
        try {
            student.setId(req.getParameter("id"));
            student.setName(req.getParameter("name"));
            student.setBirthday(LocalDate.parse(req.getParameter("birthday")));
            student.setAvgscore(Integer.parseInt(req.getParameter("avgscore")));
            student.setDescription(req.getParameter("description"));
            studentDAO.addOrUpdateStudent(student);
            resp.sendRedirect(req.getContextPath() + "/management?action=listAllStudent&page=1");
        } catch (Exception e) {
            req.setAttribute("errorInfo", "您输入有误，请重新输入！");
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }

    protected void removeStudent(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String studentID = req.getParameter("id");
        studentDAO.removeStudent(studentID);
        resp.sendRedirect(req.getContextPath() + "/management?action=listAllStudent&page=1");
    }

}
