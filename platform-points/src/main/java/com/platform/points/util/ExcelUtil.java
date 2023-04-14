package com.platform.points.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelUtil {

    /**
     * 将excel文件转为List对象集
     * @param c
     * @param file
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> readExcel(Class<T> c, MultipartFile file) throws Exception{
        InputStream is = null;
        if (file != null) {
            is = file.getInputStream();
        }else {
            return new ArrayList<>();
        }

        Workbook wb;
        String fileName = file.getOriginalFilename();

        if (fileName == null){
            fileName = "";
        }

        if (fileName.endsWith(".xlsx")){
            wb = new XSSFWorkbook(is);
        }else if(fileName.endsWith(".xls")){
            POIFSFileSystem poifsFileSystem = new POIFSFileSystem(is);
            wb = new HSSFWorkbook(poifsFileSystem);
        }else {
            return new ArrayList<>();
        }
        Sheet sheet = wb.getSheetAt(0);
        int rowStart = sheet.getFirstRowNum();
        int rowEnd = sheet.getLastRowNum();
        Row headRow = sheet.getRow(rowStart);
        int cellStart = headRow.getFirstCellNum();
        int cellEnd = headRow.getLastCellNum();

        Field[] fields = c.getDeclaredFields();
        Map<Integer, String> map = new HashMap<>();
        for (int i = cellStart; i < cellEnd; i++){
            map.put(i, fields[i].getName());
        }

        JSONArray array = new JSONArray();
        List<T> list = new ArrayList<>();
        if (rowStart == rowEnd) {
            JSONObject obj = new JSONObject();
            for (int i : map.keySet()) {
                obj.put(map.get(i), "");
            }
            array.add(obj);
            T t = JSON.toJavaObject(obj, c);
            list.add(t);
            return list;
        }

        for (int i = rowStart + 1; i <= rowEnd; i++) {
            Row row = sheet.getRow(i);
            JSONObject obj = new JSONObject();
            StringBuilder sb = new StringBuilder();
            for (int j = cellStart; j < cellEnd; j++) {
                if (row != null) {
                    Cell cell = row.getCell(j);
                    cell.setCellType(CellType.STRING);
                    String val = cell.getStringCellValue();

                    sb.append(val);
                    obj.put(map.get(j), val);
                }
            }
            if (sb.length() > 0) {
                T t = JSON.toJavaObject(obj, c);
                array.add(obj);
                list.add(t);
            }
        }
        return list;
    }

    /**
     * 将list对象转为excel文件
     * @param c
     * @param list
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> File writeExcel(Class<T> c, List<T> list) throws Exception{
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet(c.getName());
        sheet.setDefaultRowHeight((short) (2*256));

        Field[] fields = c.getDeclaredFields();

        //表头
        Row headRow = sheet.createRow(0);
        for (int i = 0; i < fields.length; i++){
            sheet.setColumnWidth(i, fields.length * 1000);
            Cell headCell = headRow.createCell(i);
            headCell.setCellValue(fields[i].getName());
        }

        XSSFFont font = wb.createFont();
        font.setFontName("等线");
        font.setFontHeightInPoints((short) 16);

        for (int i = 0; i < list.size(); i++){
            Row row = sheet.createRow(i + 1);
            for (int j = 0; j < fields.length; j++){
                fields[j].setAccessible(true);
                String name = fields[j].getName();
                name = name.replace(name.substring(0, 1), name.substring(0, 1).toUpperCase());
                Method method = c.getMethod("get" + name);
                Cell cell = row.createCell(j);

                String type = fields[j].getGenericType().toString();
                switch (type) {
                    case "class java.lang.String":
                        cell.setCellValue((String) method.invoke(list.get(i)));
                        break;
                    case "class java.lang.Integer":
                        cell.setCellValue((Integer) method.invoke(list.get(i)));
                        break;
                    case "int":
                        cell.setCellValue((int) method.invoke(list.get(i)));
                        break;
                    case "class java.util.Date":
                        cell.setCellValue((Date) method.invoke(list.get(i)));
                        break;
                }
            }
        }

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String fileName = c.getSimpleName() + dateFormat.format(date) + ".xlsx";

        File file = new File(fileName);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        wb.write(fileOutputStream);
        fileOutputStream.close();

        return file;
    }
}
