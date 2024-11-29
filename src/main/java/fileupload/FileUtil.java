package fileupload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

public class FileUtil {
    
    // 파일을 업로드하는 메소드
    public static String uploadFile(HttpServletRequest req, String sDirectory) 
                throws ServletException, IOException{
        
        // 파일 파트 가져오기
        Part part = req.getPart("ofile");
        
        // 파일 헤더에서 파일 이름 추출
        String partHeader = part.getHeader("content-disposition");
        String[] phArr = partHeader.split("filename=");  
        String originalFileName = phArr[1].trim().replace("\"", "");
        
        // 파일 이름이 비어있지 않으면 지정된 디렉토리에 파일을 저장
        if(!originalFileName.isEmpty()) {
            part.write(sDirectory + File.separator + originalFileName);
        }
        
        // 원본 파일 이름 반환
        return originalFileName;
    }
    
    // 파일 이름을 현재 시간으로 변경하는 메소드
    public static String renameFile(String sDirectory, String fileName) {
        
        // 파일 확장자 추출
        String ext = fileName.substring(fileName.lastIndexOf(".")); 
        
        // 현재 시간 기반의 새로운 파일 이름 생성
        String now = new SimpleDateFormat("yyyyMMdd_HmssS").format(new Date());
        String newFileName = now + ext; 
        
        // 기존 파일을 새 파일 이름으로 변경
        File oldFile = new File(sDirectory + File.separator + fileName);
        File newFile = new File(sDirectory + File.separator + newFileName);
        oldFile.renameTo(newFile);
        
        // 새 파일 이름 반환
        return newFileName;
    }
    
    // 여러 파일을 업로드하는 메소드
    public static ArrayList<String> multipleFile(HttpServletRequest req, String sDirectory)
            throws ServletException, IOException {

        // 파일 이름을 저장할 리스트 생성
        ArrayList<String> listFileName = new ArrayList<>();
    
        // 모든 파트를 가져와서 처리
        Collection<Part> parts = req.getParts();
        for(Part part : parts) {

            // 파일 파트만 처리
            if(!part.getName().equals("ofile"))
                continue;    
            
            // 파일 헤더에서 파일 이름 추출
            String partHeader = part.getHeader("content-disposition");
            String[] phArr = partHeader.split("filename=");
            String originalFileName = phArr[1].trim().replace("\"", "");
            
            // 파일 이름이 비어있지 않으면 지정된 디렉토리에 파일 저장
            if (!originalFileName.isEmpty()) {                
                part.write(sDirectory + File.separator + originalFileName);
            }
            
            // 파일 이름을 리스트에 추가
            listFileName.add(originalFileName);
        }
    
        // 파일 이름 리스트 반환
        return listFileName; 
    }    
    public static void download(HttpServletRequest req, HttpServletResponse res, String directory, String sfileName, String ofileName) {
    	String sDirectory = "D:/uploads";
  	try {
			//파일을 찾아 입력 스트림 생성
   		File file = new File(sDirectory, sfileName);
   		InputStream iStream = new FileInputStream(file);
    		
    		//한글 파일명 깨짐 방지
    		String client = req.getHeader("User-Agent");
    		if(client.indexOf("WOW64")== -1) {
    			ofileName = new String(ofileName.getBytes("UTF-8"), "ISO-8859-1");
    		}
    		else {
    			ofileName = new String(ofileName.getBytes("KSC5601"), "ISO-8859-1");
    		}
    		
    		//파일 다운로드용 응답 헤더  설정
    		res.reset();
    		res.setContentType("application/octet-stream");
    		res.setHeader("Content-Disposition", "attachment; filename=\"" + ofileName +"\"" );
    		res.setHeader("Content-Length", "" + file.length() );
    		
    		OutputStream oStream = res.getOutputStream();
    		
    		byte b[] =new byte[(int)file.length()];
    		int readBuffer = 0;
    		while((readBuffer = iStream.read(b)) > 0) {
    			oStream.write(b, 0, readBuffer);
    		}
    		iStream.close();
    		oStream.close();
		} catch (FileNotFoundException e) {
			System.out.println("파일을 찾을 수 없습니다");
			e.printStackTrace();
		}
    	catch (Exception e) {
    		System.out.println("예외가 발생하였습니다.");
    		e.printStackTrace();
			}
    }


}
