package io;


import algorithms.TimeTable;
import algorithms.YearGroup;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class XSLXFullScheduleGenerator extends AbstractXLSXGenerator{

    public void generate(String path, TimeTable tt, Map<String, YearGroup> groups) throws IOException {
        try(FileOutputStream file = new FileOutputStream(path)){

            int groupRow = 0;
            int sbGroupRow = 1;

            List<String> keys = new ArrayList<>(groups.keySet());
            keys.sort(String::compareTo);

            int cs = 1;
            for (String key: keys){
                List<String> arr = new ArrayList<>(groups.get(key).getChild().keySet());
                arr.sort(String::compareTo);

                merge(0, 0, cs, cs+arr.size()-1);
                set(groupRow, cs, centred, key);

                for (int i = 0; i< arr.size(); i++)
                    set(sbGroupRow, i+cs, centred, arr.get(i));
                cs += arr.size();
            }


            save(file);
//            row.createCell(0).setCellValue("Hello there!");
//            XSSFCell cell = row.createCell(1);
//            cell.setCellValue("General Kenobi!!");
//            sheet.autoSizeColumn(cell.getColumnIndex());

        }
    }



    @Override
    protected String getScheduleSheetName() {
        return SCHEDULE_TAB_NAME;
    }

    private final static String SCHEDULE_TAB_NAME = "Schedule";

    private final static Map<Integer, String> days = new HashMap<>();
    static{
        days.put(0, "Monday");
        days.put(1, "Tuesday");
        days.put(2, "Wednesday");
        days.put(3, "Thursday");
        days.put(4, "Friday");
        days.put(5, "Saturday");
        days.put(6, "Sunday");
    }
}
