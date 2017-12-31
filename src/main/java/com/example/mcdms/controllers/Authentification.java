package com.example.mcdms.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.mcdms.entities.Users;
import com.example.mcdms.services.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class Authentification {

    @Autowired
    private UserService userService;

    private String Parse(String token) {
        String user = Jwts.parser()
                .setSigningKey("secretkey")
                .parseClaimsJws(token.replace("Bearer ", ""))
                .getBody()
                .getSubject();
        return user;
    }

    @PostMapping("signup")
    public ModelAndView signup(Users user) {
        System.out.println("im here");
        String message = "";
        String errors = "";
        //model.addAttribute("name", name);
        if (user.getEmail() != null && user.getPassword() != null && user.getUsername() != null) {
            System.out.println(user.getEmail() + " " + userService.isUserSSOUnique(user.getEmail()));
            if (userService.isUserSSOUnique(user.getEmail()) == false && (userService.isUserUsernameUnique(user.getUsername()) == false)) {

                userService.saveUser(user);
                ModelAndView mav = new ModelAndView("login");
                return mav;
            } else {
                message = "Email or Username already exists";
            }
        } else {
            errors = "Inputs must not be empty";
        }

        ModelAndView mav = new ModelAndView("login");
        mav.addObject("message", message);
        mav.addObject("errors", errors);

        return mav;

    }

    @GetMapping("signup")
    public ModelAndView signup() {

        ModelAndView mav = new ModelAndView("login");

        return mav;

    }

    @PostMapping("fileUpload")
    public ModelAndView login(Users user) {
        String messages = "";
        //model.addAttribute("name", name);
        if (user.getEmail() != null && user.getPassword() != null) {
            if (userService.userValide(user.getEmail(), user.getPassword())) {

                String username = userService.findUsername(user.getEmail());
                String token = Jwts.builder().setSubject(username)
                        .setIssuedAt(new Date())
                        .signWith(SignatureAlgorithm.HS256, "secretkey").compact();

                ModelAndView mav = new ModelAndView("fileUpload");
                mav.addObject("token", "Bearer " + token);

                mav.addObject("user", username);
                mav.addObject("ls", getList(username));
                return mav;
            }
        }
        messages = "Invalide Credentials";

        ModelAndView mav = new ModelAndView("login");
        mav.addObject("messages", messages);
        return mav;

    }

    @GetMapping("login")
    public ModelAndView login() {

        ModelAndView mav = new ModelAndView("login");
        return mav;

    }

    @GetMapping("fileUpload")
    public ModelAndView fileUpload() {

        ModelAndView mav = new ModelAndView("checkToken");
        return mav;

    }

    @PostMapping("checkToken")
    public ModelAndView checkToken(String token) {
        if (!token.equals("null") && token.length() > 6 && token != null) {

            String user = Parse(token);
            ModelAndView mav = new ModelAndView("fileUpload");
            mav.addObject("token", token);

            mav.addObject("user", user);
            mav.addObject("ls", getList(user));

            return mav;
        }
        ModelAndView mav = new ModelAndView("login");
        return mav;

    }

    public List<String> getList(String token) {
        List<String> ls = new ArrayList<String>();
        try {

            String directory = token;
            try {
                if (!(Files.exists(Paths.get(directory)))) {
                    Files.createDirectory(Paths.get(directory));
                }
            } catch (IOException e) {
                throw new RuntimeException("Could not initialize storage", e);
            }

            File[] files = new File(directory).listFiles();
            // If this pathname does not denote a directory, then listFiles()
            // returns null.

            for (File file : files) {

                if (file.isFile()) {
                    ls.add(file.getPath());
                }
            }
            return ls;
        } catch (Exception e) {

        }

        return null;

    }

    // /error
}
