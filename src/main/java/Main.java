import algorithms.TimeTable;
import io.Parser;
import io.XSLXFullScheduleGenerator;

import java.io.File;


public class Main {
    private final static String DIR = "data";

    private static void test(){
        try {
            Parser p = new Parser();
            Parser.TableResult r = p.parseAll(new File(DIR));
            TimeTable tt = new TimeTable(r.getTimeSlots(), r.getWorkingDays(), r.getLessons());
            tt.printTimeTable();

            XSLXFullScheduleGenerator g = new XSLXFullScheduleGenerator();
            g.generate("file.xlsx", tt, r.getGroupMap());

        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void main(String[] args){
        test();
    }
}