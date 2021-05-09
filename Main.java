import com.hamoid.VideoExport;
import processing.core.PFont;
import processing.core.PApplet;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;

public class Main extends PApplet{

    String[] monthNames = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};

    int frames = 0;
    int PEOPLE_COUNT;
    private int DAY_LEN;
    int TOP_VISIBLE = 15;
    float FRAMES_PER_DAY = (float) 15;
    int START_DAY = 0;
    int START_DATE = dateToDays("2020-09-09");
    int ROCKS_START_DATE = dateToDays("2021-03-29");
    BarGraph[] corners = new BarGraph[4];

    public PFont font;
    public PFont numberFont;

    final int NORTHWEST = 0;
    final int NORTHEAST = 2;
    final int SOUTHEAST = 3;
    final int SOUTHWEST = 1;
    private VideoExport videoExport;

    public static void main(String[] args){
        PApplet.main("Main", args);
    }

    public void setup(){
        try {
            DataReader.makeCSV();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        font = loadFont("Constantia-96.vlw");
        numberFont = loadFont("CambriaMath-96.vlw");
        String[] textFile = loadStrings("data\\out.csv");
        String[] parts = textFile[0].split(",");
        PEOPLE_COUNT = parts.length - 1;
        DAY_LEN = textFile.length - 2;
        Person[] people = new Person[PEOPLE_COUNT];
        for (int i = 0; i < PEOPLE_COUNT; i++){
            people[i] = new Person(this, parts[i+1], textFile[1].split(",")[i+1], TOP_VISIBLE, DAY_LEN);
        }
        for (int d = 0; d < DAY_LEN; d++){
            String[] dataParts = textFile[d+2].split(",");
            for(int p = 0; p < PEOPLE_COUNT; p++){
                float val = Float.parseFloat(dataParts[p+1]);
                people[p].values[d] = Float.parseFloat(dataParts[p+1]);
            }
        }

        ArrayList<Person> orni = new ArrayList<>();
        ArrayList<Person> fossils = new ArrayList<>();
        ArrayList<Person> stars = new ArrayList<>();
        ArrayList<Person> rocks = new ArrayList<>();

        for (int p = 0; p < people.length; p++){
            Person pe = people[p];
            if (pe.event.equals("Orni")){
                orni.add(pe);
            } else if (pe.event.equals("Fossils")){
                fossils.add(pe);
            } else if (pe.event.equals("Stars")) {
                stars.add(pe);
            } else if (pe.event.equals("Rocks")) {
                rocks.add(pe);
            }
        }


        corners[NORTHWEST] = new BarGraph(orni.toArray(new Person[0]), font, numberFont, this, 10, 560, 80, 480, START_DATE, FRAMES_PER_DAY, TOP_VISIBLE, false);
        corners[NORTHEAST] = new BarGraph(fossils.toArray(new Person[0]), font, numberFont, this, 960, 1510, 80, 480, START_DATE, FRAMES_PER_DAY, TOP_VISIBLE, false);
        corners[SOUTHWEST] = new BarGraph(stars.toArray(new Person[0]), font, numberFont, this, 10, 560, 600, 1000, START_DATE, FRAMES_PER_DAY, TOP_VISIBLE,false);
        corners[SOUTHEAST] = new BarGraph(rocks.toArray(new Person[0]), font, numberFont, this, 960, 1510, 600, 1000, ROCKS_START_DATE, FRAMES_PER_DAY, TOP_VISIBLE,false);


        for (BarGraph bg : corners){
            if (bg != null)
                bg.setSettings(false, false, false);
        }

        videoExport = new VideoExport(this, "outputtedVideo.mp4");
        videoExport.startMovie();
    }
    public void draw(){
        fill(0);
        background(0);
        textAlign(RIGHT);
        fill(255);
        for (BarGraph bg : corners){
            bg.draw(START_DATE, frames);
        }
        textFont(font, 40);
        text(daysToDate(frames/FRAMES_PER_DAY + START_DAY,true), width/2 - 110,height/2 - 10);
        videoExport.saveFrame();
        if(getDayFromFrameCount(frames + 1) >= DAY_LEN){
            videoExport.endMovie();
            exit();
        }
        frames++;
    }

    float getDayFromFrameCount(int fc){
        return fc/FRAMES_PER_DAY + START_DAY;
    }

    public void settings(){
        size(1920, 1080);
    }

    int dateToDaysShort(String s){
        return dateToDays(s) - START_DATE;
    }

    int dateToDays(String s){
        int year = Integer.parseInt(s.substring(0,4))-1900;
        int month = Integer.parseInt(s.substring(5,7))-1;
        int date = Integer.parseInt(s.substring(8,10));
        Date d1 = new Date(year, month, date, 6, 6, 6);
        int days = (int)(d1.getTime()/86400000L);
        return days;
    }

    String daysToDate(float daysF, boolean longForm){
        int days = (int)daysF+START_DATE+1;
        Date d1 = new Date();
        d1.setTime(days* 86400000L);
        int year = d1.getYear()+1900;
        int month = d1.getMonth()+1;
        int date = d1.getDate();
        if(longForm){
            return year+" "+monthNames[month-1]+" "+date;
        }else{
            return year+"-"+ PApplet.nf(month,2,0)+"-"+ PApplet.nf(date,2,0);
        }
    }
}
