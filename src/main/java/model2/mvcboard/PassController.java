package model2.mvcboard;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.JSFunction;

import java.io.IOException;

import fileupload.FileUtil;

@WebServlet("/mvcboard/pass.do")
public class PassController extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setAttribute("mode", req.getParameter("mode"));
		req.getRequestDispatcher("../mvcboard/Pass.jsp").forward(req, res);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		String idx = req.getParameter("idx");
		String mode = req.getParameter("mode");
		String pass = req.getParameter("pass");
		
		MVCBoardDAO dao = new MVCBoardDAO();
		boolean confirmed = dao.confirmPassword(pass, idx);
		dao.close();
		
		if(confirmed) {
			if (mode.equals("edit")) {
				HttpSession session = req.getSession();
				session.setAttribute("pass", pass);
				res.sendRedirect("../mvcboard/edit.do?idx=" + idx);
			}
			else if (mode.equals("delete")){
				dao = new MVCBoardDAO();
				MVCBoardDTO dto = dao.selectView(idx);
				int result = dao.deletePost(idx);
				dao.close();
				if(result == 1) { 
					String saveFileName = dto.getSfile();
					FileUtil.deleteFile(req, "D:/upload", saveFileName);
				}
				JSFunction.alertLocation(res, "삭제되었습니다.", "../mvcboard/list.do");
			}
		}
		else {
			JSFunction.alertBack(res, "비밀번호 검증에 실패했습니다.");
		}
				
	}

}

//비밀번호 확인창이 안떠서 해결해야함

