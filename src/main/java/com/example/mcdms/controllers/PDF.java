package com.example.mcdms.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PDF {


    /*
C:/Users/Acer/Downloads/Documents/recu_fiscal.pdf
     */
    private static final String FILE_PATH = "recu_fiscal.pdf";
    private static final String APPLICATION_PDF = "application/pdf";

    @RequestMapping(value = "/spark", method = RequestMethod.GET, produces = APPLICATION_PDF)
    public @ResponseBody
    void spark(HttpServletResponse response) throws IOException {
        File file = getFile("spark.pdf");
        InputStream in = new FileInputStream(file);

        response.setContentType(APPLICATION_PDF);

        // response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
        response.setHeader("Content-Length", String.valueOf(file.length()));
        FileCopyUtils.copy(in, response.getOutputStream());
    }

    /*   @RequestMapping(value = "/help", method = RequestMethod.GET, produces = APPLICATION_PDF)
	    public @ResponseBody void downloadA(HttpServletResponse response) throws IOException {
	        File file = getFile();
	        InputStream in = new FileInputStream(file);

	        response.setContentType(APPLICATION_PDF);
	        
	       // response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
	        response.setHeader("Content-Length", String.valueOf(file.length()));
	        FileCopyUtils.copy(in, response.getOutputStream());
	    }

	    @RequestMapping(value = "/b", method = RequestMethod.GET, produces = APPLICATION_PDF)
	    public @ResponseBody HttpEntity<byte[]> downloadB() throws IOException {
	        File file = getFile();
	        byte[] document = FileCopyUtils.copyToByteArray(file);

	        HttpHeaders header = new HttpHeaders();
	        header.setContentType(new MediaType("application", "pdf"));
	        header.set("Content-Disposition", "inline; filename=" + file.getName());
	        header.setContentLength(document.length);

	        return new HttpEntity<byte[]>(document, header);
	    }
	    
	    

	 /*   @RequestMapping(value = "/c", method = RequestMethod.GET, produces = APPLICATION_PDF)
	    public @ResponseBody Resource downloadC(HttpServletResponse response) throws FileNotFoundException {
	        File file = getFile();
	        response.setContentType(APPLICATION_PDF);
	        response.setHeader("Content-Disposition", "inline; filename=" + file.getName());
	        response.setHeader("Content-Length", String.valueOf(file.length()));
	        return new FileSystemResource(file);
	    }*/
    private File getFile(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("file with path: " + filePath + " was not found.");
        }
        return file;
    }

}
