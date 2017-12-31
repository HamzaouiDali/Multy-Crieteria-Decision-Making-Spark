package com.example.mcdm.spark;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.storage.StorageLevel;

import com.example.mcdms.MultyCrieteriaDecisionMakingSparkApplication;

import scala.Tuple2;

public class TopsisAlgorithm implements Serializable {

    public static Vector<Double> important;
    public static Vector<Integer> maximiser;
    public static Vector<Double> aetoile;
    public static Vector<Double> amoins;
    public static List< Tuple2<Integer, Double>> sortedResut;

    public void Transform(String Output, String FileName) throws IOException {
        int linenumber = 0;
        PrintWriter writer = new PrintWriter(Output, "UTF-8");
        try (BufferedReader br = Files.newBufferedReader(Paths.get(FileName), StandardCharsets.UTF_8)) {
            for (int i = 0; i < 2; i++) {
                br.readLine();
            }
            for (String line; (line = br.readLine()) != null;) {
                String l = ++linenumber + " " + line;
                writer.println(l);
            }
            writer.close();

        }
    }

    public JavaRDD<String> etape1(String basePath) {

        // Load the input data
        JavaRDD<String> input = MultyCrieteriaDecisionMakingSparkApplication.ctx.textFile(basePath);
        // Persist the input in the memory
        input.persist(StorageLevel.MEMORY_ONLY());

        // Create Key Value pair , where the key is the column number and the value is the matrix numbers
        JavaPairRDD<Integer, Double> rdd2 = input.flatMapToPair((String s) -> {
            String[] records = s.split(" ");
            ArrayList<Tuple2<Integer, Double>> list = new ArrayList<>();

            for (int i = 1; i < records.length; i++) {
                double multi = Double.parseDouble(records[i]);
                double tmp = multi * multi;

                list.add(new Tuple2<>(i, tmp));
            }
            return list;
        });

        // sort the result by key , and taking the maximum number from each collumn
        JavaPairRDD<Integer, Double> rdd3 = rdd2.reduceByKey((x, y) -> {
            return (x + y);
        });

        // sort the result by key , and taking the maximum number from each collumn
        sortedResut = rdd3.sortByKey().collect();

        MultyCrieteriaDecisionMakingSparkApplication.ctx.broadcast(sortedResut);

        JavaRDD<String> result1 = input.map((String s) -> {

            String[] records = s.split(" ");

            for (int i = 1; i < records.length; i++) {
                double tmp = Double.parseDouble(records[i]);
                records[i] = String.valueOf(tmp / sqrt(sortedResut.get(i - 1)._2));

            }
            String toreturn = String.valueOf(records[0]);
            toreturn = toreturn + " ";

            for (int i = 1; i < records.length - 1; i++) {
                toreturn = toreturn + String.valueOf(records[i]);
                toreturn = toreturn + " ";

            }
            toreturn = toreturn + String.valueOf(records[records.length - 1]);

            return toreturn;
        });

        return result1;

    }

    private JavaRDD<String> etape2(JavaRDD<String> result1) {
        JavaRDD<String> result2 = result1.map((String s) -> {
            String[] record = s.split(" ");

            for (int i = 1; i < record.length; i++) {
                double tmp = Double.parseDouble(record[i]);
                record[i] = String.valueOf(tmp * important.elementAt(i - 1));

            }
            String toreturn = String.valueOf(record[0]);
            toreturn = toreturn + " ";

            for (int i = 1; i < record.length - 1; i++) {
                toreturn = toreturn + String.valueOf(record[i]);
                toreturn = toreturn + " ";

            }
            toreturn = toreturn + String.valueOf(record[record.length - 1]);

            return toreturn;
        });

        return result2;

    }

    private void etape3(JavaRDD<String> result2) {

        JavaPairRDD<Integer, Double> rdd = result2.flatMapToPair((String s) -> {
            String[] records = s.split(" ");
            ArrayList<Tuple2<Integer, Double>> list = new ArrayList<>();

            for (int i = 1; i < records.length; i++) {

                list.add(new Tuple2<>(i, Double.parseDouble(records[i])));
            }
            return list;
        });

        JavaPairRDD<Integer, Double> max = rdd.reduceByKey((x, y) -> Math.max(x, y));
        JavaPairRDD<Integer, Double> min = rdd.reduceByKey((x, y) -> Math.min(x, y));
        List< Tuple2<Integer, Double>> ma, mi;

        ma = max.sortByKey().collect();

        mi = min.sortByKey().collect();

        for (int i = 0; i < maximiser.size(); i++) {
            if (maximiser.elementAt(i) == 1) {
                aetoile.add(ma.get(i)._2);
                amoins.add(mi.get(i)._2);

            } else {

                aetoile.add(mi.get(i)._2);
                amoins.add(ma.get(i)._2);

            }

        }

    }

    private List<Tuple2<Double, Integer>> etape456(JavaRDD<String> result2) {

        JavaPairRDD<Double, Integer> finaldestination = result2.mapToPair((String s) -> {

            String[] records = s.split(" ");
            double setoile = 0;
            double smoins = 0;
            for (int i = 1; i < records.length; i++) {
                double tmp = Double.parseDouble(records[i]);
                double mult = tmp - aetoile.elementAt(i - 1);

                setoile += mult * mult;

                mult = tmp - amoins.elementAt(i - 1);
                smoins += mult * mult;
            }
            smoins = sqrt(smoins);
            setoile = sqrt(setoile);

            double finalresultat = (smoins / (setoile + smoins));

            return new Tuple2(finalresultat, Integer.parseInt(records[0]));
        });
        return (finaldestination.sortByKey(false).take(100));

    }

    private double sqrt(Double _2) {
        // TODO Auto-generated method stub
        double epsilon = 1e-15;    // relative error tolerance
        double t = _2;              // estimate of the square root of c

        // repeatedly apply Newton update step until desired precision is achieved
        while (Math.abs(t - _2 / t) > epsilon * t) {
            t = (_2 / t + t) / 2.0;
        }

        return t;
    }

    public List<Tuple2<Double, Integer>> ExecTopsis(String FilePath, String FileName) {
        int index = FilePath.lastIndexOf("/");
        String temp = FilePath.substring(0, index);
        String tempfinal = temp + "/0" + FileName;
        List<Tuple2<Double, Integer>> tore = new ArrayList();
        try {
            // TODO code application logic here

            important = new Vector<>();
            maximiser = new Vector<>();
            aetoile = new Vector<>();
            amoins = new Vector<>();
            int i = 0;
            try (BufferedReader br = Files.newBufferedReader(Paths.get(FilePath), StandardCharsets.UTF_8)) {
                for (String line; (line = br.readLine()) != null && i < 2;) {

                    if (i == 0) {
                        Arrays.asList(line.split(" ")).forEach((String e) -> {
                            maximiser.add(Integer.parseInt(e));
                        });
                    } else {
                        Arrays.asList(line.split(" ")).forEach((String e) -> {
                            important.add(Double.parseDouble(e));
                        });
                    }
                    i++;
                }
                Transform(tempfinal, FilePath);
                JavaRDD<String> t1 = etape1(tempfinal);
                JavaRDD<String> t2 = etape2(t1);
                etape3(t2);
                tore = etape456(t2);

                Files.delete((Paths.get(tempfinal)));

            }

        } finally {
            return tore;
        }
    }
}
