import processing.core.PApplet;

class Person {
    String name;
    String event;
    float[] values;
    int[] ranks;
    int colorType;

    public Person(Main main, String n, String c, int top_visible, int day_len) {
        values = new float[day_len];
        ranks = new int[day_len];
        name = n;
        for (int i = 0; i < day_len; i++) {
            values[i] = 0;
            if (top_visible > 10)
                ranks[i] = top_visible + 1;
            else
                ranks[i] = top_visible * 2 + 1;
        }
        event = c;
        if (c.equals("Orni")) {
            colorType = main.color(3, 169, 252);
        } else if (c.equals("Fossils")) {
            colorType = main.color(282, 186, 3);
        } else if (c.equals("Stars")) {
            colorType = main.color(62, 0, 125);
        } else if (c.equals("Rocks")) {
            colorType = main.color(100, 100, 100);
        } else {
            colorType = main.color(50, 143, 168);
        }
    }
}
