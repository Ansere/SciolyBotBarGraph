import processing.core.PApplet;
import java.util.*;
import processing.core.PFont;

public class BarGraph{

    String[] monthNames = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};

    int DAY_LEN;
    int PEOPLE_COUNT;
    Person[] people;
    int TOP_VISIBLE;
    float[] maxes;
    int[] unitChoices;
    int[] orniDayStreak;
    int[] fosDayStreak;
    float[] sums;
    float MARGIN_WINDOW = 5;
    float RANK_WINDOW = 3;
    int orniStreak = 1;
    int fosStreak = 1;
    PApplet pApplet;
    float X_MIN;
    float X_MAX;
    float Y_MIN;
    float Y_MAX;
    float X_W;
    float Y_H;
    float BAR_MIN;
    float BAR_PROPORTION = (float) 0.9;
    int START_DATE;

    float currentScale = -1;

    float currentDay = 0;
    float FRAMES_PER_DAY;
    float BAR_HEIGHT;
    PFont font;
    PFont numberFont;
    float fontSize;
    float numberFontSize;

    int[] unitPresets = {1, 2, 5, 10, 20, 50, 100, 200, 500, 1000, 2000, 5000, 10000, 20000, 50000, 100000};

    boolean doFirstPlace = true;
    boolean doDate = true;
    boolean doTitle = true;
    boolean doTotal = true;

    public BarGraph(Person[] people, PFont font, PFont numberFont, Main pApplet, int X_MIN, int X_MAX, int Y_MIN, int Y_MAX, int START_DATE, float FRAMES_PER_DAY, int TOP_VISIBLE, boolean doFirstPlace){
        this.people = people;
        DAY_LEN = people[0].values.length;
        PEOPLE_COUNT = people.length;
        this.font = font;
        this.numberFont = numberFont;
        this.pApplet = pApplet;
        this.X_MAX = X_MAX;
        this.X_MIN = X_MIN;
        this.Y_MIN = Y_MIN;
        this.Y_MAX = Y_MAX;
        X_W = X_MAX-X_MIN;
        Y_H = Y_MAX-Y_MIN;
        this.START_DATE = START_DATE;
        this.FRAMES_PER_DAY = FRAMES_PER_DAY;
        this.TOP_VISIBLE = TOP_VISIBLE;
        this.doFirstPlace = doFirstPlace;
        setup();
    }

    public void setSettings(boolean doDate, boolean doTitle, boolean doTotal){
        this.doDate = doDate;
        this.doTitle = doTitle;
        this.doTotal = doTotal;
    }

    public void setup() {
        maxes = new float[DAY_LEN];
        unitChoices = new int[DAY_LEN];
        orniDayStreak = new int[DAY_LEN];
        fosDayStreak = new int[DAY_LEN];
        sums = new float[DAY_LEN];
        for (int d = 0; d < DAY_LEN; d++){
            maxes[d] = 0;
            orniDayStreak[d] = -1;
            fosDayStreak[d] = -1;
        }
        for (int d  = 0; d < DAY_LEN; d++){
            for (Person person : people) {
                if (person.values[d] > maxes[d])
                    maxes[d] = person.values[d];
            }
        }
        getRankings();
        getUnits();
        BAR_HEIGHT = (rankToY(1) - rankToY(0)) * BAR_PROPORTION;
        fontSize = BAR_HEIGHT;
        numberFontSize = (float) (fontSize * 0.9);
        BAR_MIN = X_MIN + fontSize * 5;

    }
    int START_DAY = 0;
    public void draw(int VISUALIZATION_START_DATE, int frames) {
        currentDay = getDayFromFrameCount(frames);
        if (currentDay < START_DATE - VISUALIZATION_START_DATE)
            return;
        currentScale = getXScale(currentDay);
        drawBackground();
        drawHorizTickMarks();
        drawBars();
        if (doFirstPlace)
            drawFirstPlace();
    }
    float getDayFromFrameCount(int fc){
        return fc/FRAMES_PER_DAY + START_DAY;
    }
    void drawBackground(){
        pApplet.fill(255);
        pApplet.textFont(font, 100);
        if (doDate){
            pApplet.textAlign(pApplet.RIGHT);
            pApplet.text(daysToDate(currentDay,true), pApplet.width-40,100);
        }
        pApplet.fill(100);
        pApplet.textAlign(pApplet.CENTER);
        if (doTitle) {
            pApplet.textFont(font, 62);
            pApplet.text("Daily Specimens Identified By Event", 840, Y_MIN - 100);
        }
        if (doTotal) {
            pApplet.textFont(numberFont, 62);
            pApplet.text("Total: " + (int) linIndex(sums, currentDay), 1500, 1000);
        }
    }
    void drawFirstPlace(){
        if (currentDay < 1 || orniDayStreak[PApplet.floor(currentDay) - 1] != orniDayStreak[PApplet.floor(currentDay)]){
            orniStreak = 1;
        } else {
            orniStreak++;
        }
        if (currentDay < 1 || fosDayStreak[PApplet.floor(currentDay) - 1] != fosDayStreak[PApplet.floor(currentDay)]){
            fosStreak = 1;
        } else {
            fosStreak++;
        }
        pApplet.fill(pApplet.color(3, 169, 252));
        pApplet.textAlign(pApplet.CENTER);
        pApplet.textFont(font, 50);
        if (orniDayStreak[PApplet.floor(currentDay)] >= 0){
            pApplet.text(people[orniDayStreak[PApplet.floor(currentDay)]].name + " first for " + PApplet.ceil(orniStreak/FRAMES_PER_DAY) + " days", 1500, 800);
        }
        if (fosDayStreak[PApplet.floor(currentDay)] >= 0){
            pApplet.fill(pApplet.color(282, 186, 3));
            pApplet.textAlign(pApplet.CENTER);
            pApplet.textFont(font, 50);
            pApplet.text(people[fosDayStreak[PApplet.floor(currentDay)]].name + " first for " + PApplet.ceil(fosStreak/FRAMES_PER_DAY) + " days", 1500, 900);
        }
    }

    void drawHorizTickMarks(){
        float preferredUnit = WAIndex(unitChoices, currentDay, 3);
        float unitRem = (float) (preferredUnit % 1.0);
        if (unitRem < 0.001){
            unitRem = 0;
        } else if (unitRem >= 0.999){
            unitRem = 0;
            preferredUnit = PApplet.ceil(preferredUnit);
        }
        int thisUnit = unitPresets[(int) preferredUnit];
        int nextUnit = unitPresets[(int) preferredUnit + 1];
        drawTickMarksOfUnit(thisUnit, 255 - unitRem*255);
        if (unitRem >= 0.001){
            drawTickMarksOfUnit(nextUnit, unitRem * 255);
        }
    }
    void drawTickMarksOfUnit(int u, float alpha){
        for(int v = 0; v < currentScale * 1.4; v += u){
            float x = valueToX(v);
            pApplet.fill(100, 100, 100, alpha);
            float W = 4;
            pApplet.rect(x - W/2, Y_MIN - 20, W, Y_H+20);
            pApplet.textAlign(pApplet.CENTER);
            pApplet.textFont(font, 30);
            pApplet.text(keyify(v), x, Y_MIN - 30);
        }
    }
    void drawBars(){
        pApplet.noStroke();
        for (int p = 0; p < PEOPLE_COUNT; p++){
            Person pe = people[p];
            float val = linIndex(pe.values, currentDay);
            float x = valueToX(val);
            float rank = WAIndex(pe.ranks, currentDay, RANK_WINDOW);
            float y = rankToY(rank);
            float appX = PApplet.max(X_MIN, BAR_MIN - pApplet.textWidth(pe.name));
            if (y < Y_MAX) {
                pApplet.fill(pe.colorType);
                pApplet.rect(BAR_MIN, y, x - BAR_MIN, BAR_HEIGHT);
                pApplet.fill(255);
                pApplet.textAlign(pApplet.RIGHT);
                if (appX <= X_MIN){
                    pApplet.textAlign(pApplet.LEFT);
                } else {
                    appX += pApplet.textWidth(pe.name);
                }
                pApplet.textFont(font, fontSize);
                pApplet.text(pe.name, appX, (float) (y + fontSize * 0.8));
                float TEXT_X = PApplet.max(x + 10, X_MIN + pApplet.textWidth(pe.name) + 10);
                pApplet.textFont(numberFont, numberFontSize);
                pApplet.textAlign(pApplet.LEFT);
                pApplet.text((int) val, TEXT_X, (float) (y + numberFontSize * 0.9));
            }
        }
    }

    void getRankings(){
        for(int d = 0; d < DAY_LEN; d++){
            boolean[] taken = new boolean[PEOPLE_COUNT];
            for (int p = 0; p < PEOPLE_COUNT; p++){
                taken[p] = false;
                sums[d] += people[p].values[d];
            }
            for (int spot = 0; spot < TOP_VISIBLE; spot++){
                float record = -1;
                int holder = -1;
                for (int p = 0; p < PEOPLE_COUNT; p++){
                    if (!taken[p]){
                        float val = people[p].values[d];
                        if (val > record){
                            record = val;
                            holder = p;
                        }
                    }
                }
                if (doFirstPlace){
                    if (people[holder].event.equals("Orni") && orniDayStreak[d] < 0){
                        orniDayStreak[d] = holder;
                    } else if (people[holder].event.equals("Fossils") && fosDayStreak[d] < 0){
                        fosDayStreak[d] = holder;
                    }
                }
                if (holder < 0){
                    continue;
                }
                people[holder].ranks[d] = spot;
                taken[holder] = true;
            }
        }
    }
    float stepIndex(float[] a, float index){
        return a[(int)index];
    }
    float linIndex(float[] a, float index){
        int indexInt = (int) index;
        float indexRem = (float) (index % 1.0);
        float beforeVal = a[indexInt];
        float afterVal = a[PApplet.min(indexInt + 1, DAY_LEN - 1)];
        return PApplet.lerp(beforeVal, afterVal, indexRem);
    }
    float WAIndex(float[] a, float index, float WINDOW_WIDTH){
        int startIndex = PApplet.max(0, PApplet.ceil(index-WINDOW_WIDTH));
        int endIndex = PApplet.min(PApplet.floor(index+WINDOW_WIDTH), DAY_LEN-1);
        float counter = 0;
        float summer = 0;

        for (int d = startIndex; d <= endIndex; d++){
            float val = a[d];
            float weight = (float) (0.5 + 0.5 * PApplet.cos((d-index)/WINDOW_WIDTH * pApplet.PI));
            counter += weight;
            summer += val*weight;
        }
        return summer/counter;
    }

    float WAIndex(int[] a, float index, float WINDOW_WIDTH){
        float[] aFloat = new float[a.length];
        for (int i = 0; i < a.length; i++){
            aFloat[i] = a[i];
        }
        return WAIndex(aFloat, index, WINDOW_WIDTH);
    }


    float getXScale(float d){
        return (float) (WAIndex(maxes, d, MARGIN_WINDOW)*1.2);
    }
    float valueToX(float val){
        return BAR_MIN + X_W * val/currentScale;
    }
    float rankToY(float rank){
        float y = Y_MIN + rank * (Y_H/TOP_VISIBLE);
        return y;
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

    void getUnits(){
        for (int d = 0; d < DAY_LEN; d++){
            float Xscale = getXScale(d);
            for (int u = 0; u < unitPresets.length; u++){
                if(unitPresets[u] >= Xscale/3.0){
                    unitChoices[d] = u - 1;
                    break;
                }
            }
        }
    }
    String keyify(int n){
        if(n < 1000){
            return n+"";
        }else if(n < 1000000){
            if(n%1000 == 0){
                return (n/1000)+"K";
            }else{
                return PApplet.nf(n/1000f,0,1)+"K";
            }
        }
        if(n%1000000 == 0){
            return (n/1000000)+"M";
        }else{
            return PApplet.nf(n/1000000f,0,1)+"M";
        }
    }
}

