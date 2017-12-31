package com.example.mcdms;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MultyCrieteriaDecisionMakingSparkApplication {

    public static SparkConf sparkConf;
    public static JavaSparkContext ctx;

    public static void main(String[] args) {
        sparkConf = new SparkConf().setMaster("local").setAppName("Work Count App").set("spark.driver.allowMultipleContexts", "true");

        // Create a Java version of the Spark Context from the configuration
        ctx = new JavaSparkContext(sparkConf);
        SpringApplication.run(MultyCrieteriaDecisionMakingSparkApplication.class, args);
    }

    /*@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {
	 
	   return (container -> {
	        ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/401.html");
	        ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "login.html");
	        ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500.html");
	        error404Page.
	        container.addErrorPages(error401Page, error404Page, error500Page);
	   });
	}*/
}
