package io;


import algorithms.Lesson;
import algorithms.TimeSlot;
import algorithms.TimeTable;
import algorithms.YearGroup;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.AbstractXLSXGenerator.Style.*;


public class XSLXFullScheduleGenerator extends AbstractXLSXGenerator{

    public void generate(String path, TimeTable tt, Map<String, YearGroup> groups) throws IOException {
        try(FileOutputStream file = new FileOutputStream(path)){

            int groupRow = 0;
            int sbGroupRow = 1;

            List<String> keys = new ArrayList<>(groups.keySet());
            keys.sort(String::compareTo);

            Map<String, Integer> paddings = new HashMap<>();

            int cs = 1;
            for (String key: keys){
                List<String> arr = new ArrayList<>(groups.get(key).getChild().keySet());
                arr.sort(String::compareTo);

                for (int i = 0; i < arr.size(); i++){
                    paddings.put(arr.get(i), cs+i);
                }

                merge(0, 0, cs, cs+arr.size()-1);
                set(groupRow, cs, style(CENTER, B_TOP, B_LEFT, B_RIGHT, BG_BLUE), key);
                set(0, cs+arr.size()-1, style(B_RIGHT)); //For the LibreOffice, which have issues with borders of merged cells

                for (int i = 0; i< arr.size(); i++)
                    set(sbGroupRow, i+cs, style(CENTER, B_BOTTOM, BG_BLUE), arr.get(i));
                set(sbGroupRow, cs, style(CENTER, B_BOTTOM, B_LEFT, BG_BLUE));
                set(sbGroupRow, cs+arr.size()-1, style(CENTER, B_BOTTOM, B_RIGHT, BG_BLUE));
                cs += arr.size();
            }


            int currentRow = sbGroupRow + 1;
            int dayNumber = 0;
            for (List<TimeSlot> day: tt.getTimeSlots()){
                set(currentRow, 0, style(CENTER, B_BOTTOM, B_TOP, B_RIGHT, B_LEFT, BG_GREEN), days.get(dayNumber));
                set(currentRow, cs-1, style(B_RIGHT));//For the LibreOffice, which have issues with borders of merged cells
                merge(currentRow, currentRow, 0, cs-1);

                int tsn = 1;
                for (TimeSlot t: day){
                    set(currentRow + tsn, 0, style(CENTER, B_RIGHT, BG_BLUE), String.format("%d:%d - %d:%d", t.startHour, t.startMinute, t.endHour, t.endMinute));

                    for (Lesson l: t.getLessons()){
                        if (!paddings.containsKey(l.getAssignedGroup().name)){
                            int offset = 1;
                            for (int i = 0; i < keys.size(); i++){
                                if (keys.get(i).equals(l.getAssignedGroup().name)){
                                    merge(currentRow + tsn, currentRow + tsn, offset, offset + groups.get(keys.get(i)).getChild().size()-1);
                                    set(currentRow + tsn, offset, style(CENTER),
                                            String.format("%s\n%s\n%s\n", l.getCourse().courseName, l.getAssignedTeacher().getName(), l.getClassroom()));

                                    break;
                                }
                                offset += groups.get(keys.get(i)).getChild().size();
                            }

                        }else {
                            int offset = paddings.get(l.getAssignedGroup().name);
                            set(currentRow + tsn, offset, style(CENTER),
                                    String.format("%s\n%s\n%s\n", l.getCourse().courseName, l.getAssignedTeacher().getName(), l.getClassroom()));
                        }
                    }

                    int offsett = 0;
                    for (String key: keys) {
                        offsett += groups.get(key).getChild().size();
                        set(currentRow + tsn, offsett, style(CENTER, B_RIGHT));
                    }
                    tsn++;
                }

                dayNumber++;
                currentRow += 1 + day.size();
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
