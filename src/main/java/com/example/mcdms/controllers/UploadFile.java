package com.example.mcdms.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import io.jsonwebtoken.Jwts;

@RestController
public class UploadFile {

    private String Parse(String token) {
        String user = Jwts.parser()
                .setSigningKey("secretkey")
                .parseClaimsJws(token.replace("Bearer ", ""))
                .getBody()
                .getSubject();
        return user;
    }

    @PostMapping("/uploadFile")
    //@ResponseBody
    public ModelAndView uploadFile(@RequestParam("textFile") MultipartFile uploadfile, @RequestParam("token") String token) {
        List<String> ls = new ArrayList();
        if (token != null && !token.equals("null") && token.length() > 4) {
            try {
                // Get the filename and build the local file path
                String filename = uploadfile.getOriginalFilename();
                String directory = Parse(token);
                try {
                    if (!(Files.exists(Paths.get(directory)))) {
                        Files.createDirectory(Paths.get(directory));
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Could not initialize storage", e);
                }

                String filepath;
                try {
                    filepath = Paths.get(directory, filename).toString();
                    if ((Files.exists(Paths.get(filepath)))) {
                        Files.delete((Paths.get(filepath)));
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Failed to read stored files", e);
                }

                if (uploadfile.isEmpty()) {
                    throw new RuntimeException("Failed to store empty file " + uploadfile.getOriginalFilename());
                }
                // Save the file locally
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
                stream.write(uploadfile.getBytes());
                stream.close();
                File[] files = new File(directory).listFiles();
                // If this pathname does not denote a directory, then listFiles()
                // returns null.

                for (File file : files) {
                    if (file.isFile()) {
                        ls.add(Parse(token) + "/" + file.getName());
                    }
                }
            } catch (Exception e) {
                ModelAndView mav = new ModelAndView("login");

                return mav;
            }

            ModelAndView mav = new ModelAndView("fileUpload");
            mav.addObject("ls", ls);
            mav.addObject("user", Parse(token));
            mav.addObject("token", token);

            return mav;
        }
        ModelAndView mav = new ModelAndView("login");

        return mav;
    }

    @GetMapping("/uploadFile")
    public ModelAndView uploadFile() {

        ModelAndView mav = new ModelAndView("checkToken");
        return mav;

    }

}
