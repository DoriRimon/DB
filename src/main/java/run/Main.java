package run;

import base.db.DDB;
import base.db.record.Record;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        DDB db = new DDB();

        ArrayList<String> names = new ArrayList<>(Arrays.asList("empno", "first_name"));
        ArrayList<Class<?>> types = new ArrayList<>(Arrays.asList(Integer.class, String.class));
        try {
            db.createTable("emp", names, types);
            db.insert("emp", new Record(Arrays.asList(323996843, "DORI")));
            System.out.println(db.select("emp", new HashMap<>()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            db.insert("emp", new Record(Arrays.asList(92385, "OTHER")));
            System.out.println(db.select("emp", new HashMap<>()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
