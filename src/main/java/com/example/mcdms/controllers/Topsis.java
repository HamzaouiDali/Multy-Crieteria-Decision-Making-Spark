package com.example.mcdms.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.mcdm.spark.TopsisAlgorithm;

import scala.Tuple2;

@RestController
public class Topsis {

    @GetMapping("/{dest}/{filenames:.+}")
    public ModelAndView serveFile(@PathVariable String dest, @PathVariable String filenames) {
        System.out.println(dest + " " + filenames);
        List<Tuple2<Double, Integer>> result = (new TopsisAlgorithm().ExecTopsis(dest + "/" + filenames, filenames));
        ModelAndView mav = new ModelAndView("result");

        mav.addObject("result", result);
        return mav;
    }

}

/* 

*/
