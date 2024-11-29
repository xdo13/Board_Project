package model2.mvcboard;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import fileupload.FileUtil;

@WebServlet("/mvcboard/download.do")
public class DownloadController extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		//매개변수 받기
		String ofile= req.getParameter("ofile"); //원본 파일명
		String sfile= req.getParameter("sfile"); // 저장된 파일명
		String idx = req.getParameter("idx"); // 게시물 일련번호
		
		//파일 다운로드
		FileUtil.download(req, res, "D:/uploads", sfile, ofile);
		
		//해당 게시물의 다운로드 수 1 증가
		
		MVCBoardDAO dao = new MVCBoardDAO();
		dao.downCountPlus(idx);
		dao.close();
	}

}
