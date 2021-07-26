package controllers.follows;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Follow;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class EmployeeFollowsServlet
 */
@WebServlet("/employee/follows")
public class EmployeeFollowsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeeFollowsServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        Report r = em.find(Report.class, Integer.parseInt(request.getParameter("id")));

        Follow f = new Follow();
        f.setEmployee((Employee)request.getSession().getAttribute("login_employee"));
        f.setFollow(r.getEmployee());

        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        f.setCreated_at(currentTime);
        f.setUpdated_at(currentTime);


        em.getTransaction().begin();
        em.persist(f);
        em.getTransaction().commit();

        em.close();

        request.getSession().setAttribute("flush", "フォローしました。");

        request.setAttribute("follow", f);
        request.setAttribute("report", r);

        response.sendRedirect(request.getContextPath() + "/timeline/index");
    }

}
