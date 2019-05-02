package io;

import algorithms.TimeTable;
import algorithms.YearGroup;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;


public abstract class AbstractXLSXGenerator {
    private XSSFWorkbook wb = new XSSFWorkbook();
    private XSSFSheet sheet = wb.createSheet(getScheduleSheetName());
    private Map<Integer, XSSFCellStyle> styles = new HashMap<>();



    public abstract void generate(String path, TimeTable tt, Map<String, YearGroup> groups) throws IOException;
    protected abstract String getScheduleSheetName();



    //Methods for work with the table
    protected void merge(final int startRow, final int endRow, final int startCell, final int endCell){
        sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, startCell, endCell));
    }

    protected void set(int r, int c, String content) {
        set(r, c, null, content);
    }

    protected void set(int r, int c, XSSFCellStyle style) {
        set(r, c, style, null);
    }

    protected void set(int r, int c, XSSFCellStyle style, String content) {
        XSSFRow row = sheet.getRow(r);
        if (row == null)
            row = sheet.createRow(r);

        XSSFCell cell = row.getCell(c);
        if (cell == null) {
            cell = row.createCell(c);
            sheet.autoSizeColumn(c);
        }

        if (style != null)
            cell.setCellStyle(style);
        if (content != null)
            cell.setCellValue(content);
    }

    protected XSSFCellStyle style(Style... style){
        int tb = 0;
        for (Style s: style)
            tb |= s.style;

        if (styles.get(tb) == null) {
            XSSFCellStyle cellStyle = wb.createCellStyle();
            for (Style s: style){
                s.apply.accept(cellStyle);
            }
            styles.put(tb, cellStyle);
        }

        return styles.get(tb);
    }

    protected void save(OutputStream file) throws IOException {
        wb.write(file);
    }


    private static int auto = 1;
    protected enum Style{
        CENTER(auto<<=1, a -> {a.setAlignment(HorizontalAlignment.CENTER);
            a.setVerticalAlignment(VerticalAlignment.CENTER);}),

        B_BOTTOM(auto<<=1, a -> a.setBorderBottom(BorderStyle.MEDIUM)),
        B_TOP(auto<<=1, a -> a.setBorderTop(BorderStyle.MEDIUM)),
        B_LEFT(auto<<=1, a -> a.setBorderLeft(BorderStyle.MEDIUM)),
        B_RIGHT(auto<<=1, a -> a.setBorderRight(BorderStyle.MEDIUM)),
        BG_GREEN(auto<<=1, a -> {a.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        a.setFillPattern(FillPatternType.SOLID_FOREGROUND);}),
        BG_BLUE(auto<<=1, a -> {a.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
            a.setFillPattern(FillPatternType.SOLID_FOREGROUND);})
        ;

        public int style;
        public Consumer<XSSFCellStyle> apply;
        Style(int style, Consumer<XSSFCellStyle> apply){
            this.style = style;
            this.apply = apply;
        }
    }
}
