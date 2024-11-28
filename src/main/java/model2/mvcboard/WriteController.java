
  package model2.mvcboard;
  
  import java.io.IOException;
  
  import fileupload.FileUtil; import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import
  jakarta.servlet.annotation.WebServlet; import
  jakarta.servlet.http.HttpServlet; import
  jakarta.servlet.http.HttpServletRequest; import
  jakarta.servlet.http.HttpServletResponse; import utils.JSFunction;
  
  @MultipartConfig(
		    maxFileSize = 1024 * 1024 * 1, // 업로드 파일의 최대 크기 (1MB)
		    maxRequestSize = 1024 * 1024 * 10 // 전체 요청 크기 (10MB)
		)
  
  @WebServlet("/mvcboard/write.do") public class WriteController extends
  HttpServlet {
  
  @Override
  
  
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws
  ServletException, IOException {
  req.getRequestDispatcher("/mvcboard/Write.jsp").forward(req, res);
  
  } protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException { 
		/* String saveDirectory = "D:/uploads"; */
  String saveDirectory = "D:/uploads";
  String originalFileName = "";
	
	  try {
	  
	  originalFileName = FileUtil.uploadFile(req, saveDirectory);
	  
	  } catch (Exception e) {
	  
	  
	  //JSFunction.alertLocation(res, "파일 업로드 오류입니다.", "../mvcboard/write.do");
	  return; }
	 
  
  MVCBoardDTO dto = new MVCBoardDTO(); dto.setName(req.getParameter("name"));
			  dto.setTitle(req.getParameter("title"));
			  dto.setContent(req.getParameter("content"));
			  dto.setPass(req.getParameter("pass"));
			  
  if(originalFileName != "") {
	  String savedFileName = FileUtil.renameFile(saveDirectory, originalFileName);
  
  dto.setOfile(savedFileName); dto.setSfile(savedFileName);
  }
  
  MVCBoardDAO dao = new MVCBoardDAO();
  int result = dao.insertWrite(dto);
  dao.close();
  
  if(result == 1) {
	  res.sendRedirect("../mvcboard/list.do");
  } else {
  JSFunction.alertLocation(res, "글쓰기에 실패했습니다.", "../mvcboard/write.do");
  		}
  	}
 }
 


