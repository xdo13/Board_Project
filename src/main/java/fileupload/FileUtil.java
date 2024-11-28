package fileupload;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
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
}
