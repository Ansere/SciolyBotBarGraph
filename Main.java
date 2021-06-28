import processing.core.PApplet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Array;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class DataReader {

    private static ArrayList<ArrayList<String>> modifiedPerson2D;

    public static void main(String[] args) throws FileNotFoundException {
        makeCSV();
    }

    public static void makeCSV() throws FileNotFoundException {
        ArrayList<ArrayList<String>> matrix = new ArrayList<>();
        String[] csv = PApplet.loadStrings(new File("data\\orni.csv"));
        removeCommas(csv);
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
        removeCommas(csv);
        String[] fossilDates = Arrays.stream(csv[0].split(",")).skip(2).collect(Collectors.toList()).toArray(new String[0]);
        String[] missingFossilDates = getMissingDates(fossilDates[0], LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), fossilDates);
        modifiedPerson2D = Arrays.stream(csv).map(s -> new ArrayList<>(Arrays.asList(s.split(",")))).collect(Collectors.toCollection(ArrayList::new));
        modifiedPerson2D = transposeMatrix(modifiedPerson2D);
        for (int d = 0; d < missingFossilDates.length; d++){
            ArrayList<String> date = IntStream.range(0, modifiedPerson2D.get(0).size()).mapToObj(in -> "0").collect(Collectors.toCollection(ArrayList::new));
            date.set(0, missingFossilDates[d]);
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
        removeCommas(csv);
        String[] starDates = Arrays.stream(csv[0].split(",")).skip(2).collect(Collectors.toList()).toArray(new String[0]);
        String[] missingStarDates = getMissingDates(starDates[0], LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),starDates);
        modifiedPerson2D = Arrays.stream(csv).map(s -> new ArrayList<>(Arrays.asList(s.split(",")))).collect(Collectors.toCollection(ArrayList::new));
        modifiedPerson2D = transposeMatrix(modifiedPerson2D);
        for (int d = 0; d < missingStarDates.length; d++){
            ArrayList<String> date = IntStream.range(0, modifiedPerson2D.get(0).size()).mapToObj(in -> "0").collect(Collectors.toCollection(ArrayList::new));
            date.set(0, missingStarDates[d]);
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
        removeCommas(csv);
        String[] rockDates = Arrays.stream(csv[0].split(",")).skip(2).collect(Collectors.toList()).toArray(new String[0]);
        String[] missingRockDates = getMissingDates(rockDates[0], LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), rockDates);
        modifiedPerson2D = Arrays.stream(csv).map(s -> new ArrayList<>(Arrays.asList(s.split(",")))).collect(Collectors.toCollection(ArrayList::new));
        modifiedPerson2D = transposeMatrix(modifiedPerson2D);
        for (int d = 0; d < missingRockDates.length; d++){
            ArrayList<String> date = IntStream.range(0, modifiedPerson2D.get(0).size()).mapToObj(in -> "0").collect(Collectors.toCollection(ArrayList::new));
            date.set(0, missingRockDates[d]);
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
            matrix.add(resultPerson);
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
            //System.out.println(arr);
            if (length != arr.size()) {
                /*
                System.out.println(arr.get(arr.size() - 1));
                System.out.println(arr);
                System.out.println(arr.size());
                */
                return null;
            }
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

    private static String[] removeCommas(String[] matrix){
        for (int r = 0; r < matrix.length; r++){
            matrix[r] = removeCommasInName(matrix[r]);
        }
        return matrix;
    }

    private static String removeCommasInName(String str){
        boolean inQuotes = false;

        for (int i = 0; i < str.length(); i++){
            if (str.substring(i, i+1).equals("\""))
                inQuotes = !inQuotes;
            else if (inQuotes && str.substring(i, i+1).equals(","))
                str = str.substring(0, i) + str.substring(i-- + 1);
        }

        return str;
    }

    private static String[] getMissingDates(String startDate, String endDate, String[] dates){
        return LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).datesUntil(LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))).map(d -> d.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).filter(d -> !Arrays.asList(dates).contains(d)).collect(Collectors.toList()).toArray(new String[0]);
    }


}
