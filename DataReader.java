import processing.core.PApplet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class DataReader {

    public static void main(String[] args) throws FileNotFoundException {
        makeCSV();
    }

    public static void makeCSV() throws FileNotFoundException {
        ArrayList<ArrayList<String>> matrix = new ArrayList<>();
        String[] starDates = {"2020-09-10", "2020-09-16", "2020-09-17", "2020-09-18", "2020-09-19", "2020-09-21", "2020-09-23", "2020-09-25", "2020-09-27", "2020-12-12", "2021-02-19", "2021-03-23"};
        String[] rockDates = {"2021-04-04", "2021-04-06"};
        String[] csv = PApplet.loadStrings(new File("data\\orni.csv"));
        for (int i = 0; i < csv.length; i++) {
            String[] person = csv[i].split(",");
            if (person[1].equals("0")){
                continue;
            }
            ArrayList<String> resultPerson = new ArrayList<>();
            for (int d = 0; d < person.length; d++) {
                if (d == 1) {
                    resultPerson.add("Orni");
                } else if (d > 2 && i > 0) {
                    resultPerson.add(Integer.parseInt(resultPerson.get(d - 1)) + Integer.parseInt(person[d]) + "");
                } else {
                    resultPerson.add(person[d]);
                }
            }
            matrix.add(resultPerson);
        }
        csv = PApplet.loadStrings(new File("data\\fossils.csv"));
        for (int i = 1; i < csv.length; i++) {
            String[] person = csv[i].split(",");
            if (person[1].equals("0")){
                continue;
            }
            ArrayList<String> resultPerson = new ArrayList<>();
            for (int d = 0; d < person.length; d++) {
                if (d == 1) {
                    resultPerson.add("Fossils");
                } else if (d > 2 && i > 0) {
                    resultPerson.add(Integer.parseInt(resultPerson.get(d - 1)) + Integer.parseInt(person[d]) + "");
                } else {
                    resultPerson.add(person[d]);
                }
            }
            matrix.add(resultPerson);
        }
        csv = PApplet.loadStrings(new File("data\\stars.csv"));
        ArrayList<ArrayList<String>> modifiedPerson2D = Arrays.stream(csv).map(s -> new ArrayList<>(Arrays.asList(s.split(",")))).collect(Collectors.toCollection(ArrayList::new));
        modifiedPerson2D = transposeMatrix(modifiedPerson2D);
        for (int d = 0; d < starDates.length; d++){
            ArrayList<String> date = IntStream.range(0, modifiedPerson2D.get(0).size()).mapToObj(in -> "0").collect(Collectors.toCollection(ArrayList::new));
            date.set(0, starDates[d]);
            modifiedPerson2D.add(date);
        }
        modifiedPerson2D = modifiedPerson2D.stream().sorted(Comparator.comparing(d -> d.get(0))).collect(Collectors.toCollection(ArrayList::new)); // sorts the username and score to
        modifiedPerson2D.add(0, modifiedPerson2D.remove(modifiedPerson2D.size() - 2));
        modifiedPerson2D.add(0, modifiedPerson2D.remove(modifiedPerson2D.size() - 1));
        modifiedPerson2D = transposeMatrix(modifiedPerson2D);
        for (int i = 1; i < modifiedPerson2D.size(); i++) {
            String[] person = modifiedPerson2D.get(i).toArray(new String[0]);
            if (person[1].equals("0")){
                continue;
            }
            ArrayList<String> resultPerson = new ArrayList<>();
            for (int d = 0; d < person.length; d++) {
                if (d == 1) {
                    resultPerson.add("Stars");
                } else if (d > 2 && i > 0) {
                    resultPerson.add(Integer.parseInt(resultPerson.get(d - 1)) + Integer.parseInt(person[d]) + "");
                } else {
                    resultPerson.add(person[d]);
                }
            }
            matrix.add(resultPerson);
        }
        csv = PApplet.loadStrings(new File("data\\rocks.csv"));
        modifiedPerson2D = Arrays.stream(csv).map(s -> new ArrayList<>(Arrays.asList(s.split(",")))).collect(Collectors.toCollection(ArrayList::new));
        modifiedPerson2D = transposeMatrix(modifiedPerson2D);
        for (int d = 0; d < rockDates.length; d++){
            ArrayList<String> date = IntStream.range(0, modifiedPerson2D.get(0).size()).mapToObj(in -> "0").collect(Collectors.toCollection(ArrayList::new));
            date.set(0, rockDates[d]);
            modifiedPerson2D.add(date);
        }
        modifiedPerson2D = modifiedPerson2D.stream().sorted(Comparator.comparing(d -> d.get(0))).collect(Collectors.toCollection(ArrayList::new)); // sorts the username and score to
        modifiedPerson2D.add(0, modifiedPerson2D.remove(modifiedPerson2D.size() - 2));
        modifiedPerson2D.add(0, modifiedPerson2D.remove(modifiedPerson2D.size() - 1));
        modifiedPerson2D = transposeMatrix(modifiedPerson2D);
        for (int i = 1; i < modifiedPerson2D.size(); i++) {
            String[] person = modifiedPerson2D.get(i).toArray(new String[0]);
            if (person[1].equals("0")){
                continue;
            }
            ArrayList<String> resultPerson = new ArrayList<>();
            for (int d = 0; d < person.length; d++) {
                if (d == 1) {
                    resultPerson.add("Rocks");
                    for (int da = 0; da < 199; da++){
                        resultPerson.add("0");
                    }
                } else if (d > 2 && i > 0) {
                    resultPerson.add(Integer.parseInt(resultPerson.get(d + 198)) + Integer.parseInt(person[d]) + "");
                } else {
                    resultPerson.add(person[d]);
                }
            }
            System.out.println(resultPerson);
            matrix.add(resultPerson);
        }

        for (ArrayList<String> person : matrix){
            if (person.get(1).equals("Rocks"))
                System.out.println(person);
        }

        while (matrix.get(matrix.size() - 1).get(0).equals("0")){
            matrix.remove(matrix.size() - 1);
        }
        matrix = transposeMatrix(matrix);
        for (int i = 1; i < matrix.get(0).size(); i++){
            matrix.get(0).set(i, matrix.get(0).get(i).substring(0, matrix.get(0).get(i).length() - 5));
        }

        writeToCSV(matrix);

    }

    private static String convertToCSVRow(ArrayList<String> data){
        return data.stream().collect(Collectors.joining(","));

    }

    private static void writeToCSV(ArrayList<ArrayList<String>> matrix){
        File output = new File("data\\out.csv");
        try (PrintWriter pw = new PrintWriter(output)){
            matrix.stream().map(DataReader::convertToCSVRow).forEach(pw::println);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<ArrayList<String>> transposeMatrix(ArrayList<ArrayList<String>> matrix){
        int length = matrix.get(0).size();
        for (ArrayList<String> arr : matrix){
            if (length != arr.size())
                return null;
        }
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        for (int c = 0; c < length; c++){
            ArrayList<String> arr = new ArrayList<>();
            for (int r = 0; r < matrix.size(); r++){
                arr.add(matrix.get(r).get(c));
            }
            result.add(arr);
        }
        return result;
    }

}
