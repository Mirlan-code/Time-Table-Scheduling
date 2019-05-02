package io;

import algorithms.TimeTable;
import algorithms.YearGroup;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;


public abstract class AbstractXLSXGenerator {
    private XSSFWorkbook wb = new XSSFWorkbook();
    private XSSFSheet sheet = wb.createSheet(getScheduleSheetName());


    protected XSSFCellStyle centred;

    protected AbstractXLSXGenerator() {
        setUpStyles();
    }

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
        if (cell == null)
            cell = row.createCell(c);

        if (style != null)
            cell.setCellStyle(style);
        if (content != null)
            cell.setCellValue(content);
    }

    protected void setUpStyles() {
        centred = wb.createCellStyle();
        centred.setAlignment(HorizontalAlignment.CENTER);
    }


    protected void save(OutputStream file) throws IOException {
        wb.write(file);
    }
}
