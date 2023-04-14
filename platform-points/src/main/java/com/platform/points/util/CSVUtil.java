package com.platform.points.util;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Java生成CSV文件
 */
public class CSVUtil {

    /**
     * 生成为CVS文件
     *
     * @param exportData
     *            源数据List
     * @param map
     *            csv文件的列表头map
     * @param outPutPath
     *            文件路径
     * @param fileName
     *            文件名称
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static File createCSVFile(List exportData, LinkedHashMap map,
                                     String outPutPath, String fileName) {
        File csvFile = null;
        BufferedWriter csvFileOutputStream = null;
        try {
            File file = new File(outPutPath);
            if (!file.exists()) {
                file.mkdir();
            }
            // 定义文件名格式并创建
            csvFile = File.createTempFile(fileName, ".csv",
                    new File(outPutPath));
            // UTF-8使正确读取分隔符","
            csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(csvFile), "GBK"), 1024);
            // 写入文件头部
            for (Iterator propertyIterator = map.entrySet().iterator(); propertyIterator
                    .hasNext();) {
                java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator
                        .next();
                //加\t防止过长被科学计数
                csvFileOutputStream.write(" " +  (String) propertyEntry.getValue() != null ? (String) propertyEntry.getValue()+"\t" :  " ");
                if (propertyIterator.hasNext()) {
                    csvFileOutputStream.write(",");
                }
            }
            csvFileOutputStream.newLine();
            // 写入文件内容
            for (Iterator iterator = exportData.iterator(); iterator.hasNext();) {
                Object row = (Object) iterator.next();
                for (Iterator propertyIterator = map.entrySet().iterator(); propertyIterator
                        .hasNext();) {
                    java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator
                            .next();
                    //以下部分根据不同业务做出相应的更改
                    StringBuilder sbContext = new StringBuilder("");

                    //加个\t防止过长的数字字符串被excel导入为科学计数
                    sbContext.append(BeanUtils.getProperty(row,propertyEntry.getKey().toString() ) + "\t");

                    csvFileOutputStream.write(sbContext.toString());

                    if (propertyIterator.hasNext()) {
                        csvFileOutputStream.write(",");
                    }
                }
                if (iterator.hasNext()) {
                    csvFileOutputStream.newLine();
                }
            }
            csvFileOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                csvFileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return csvFile;
    }

    /**
     * 下载文件
     *
     * @param response
     * @param csvFilePath
     *            文件路径
     * @param fileName
     *            文件名称
     * @throws IOException
     */
    public static void exportFile(HttpServletRequest request,
                                  HttpServletResponse response, String csvFilePath, String fileName)
            throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/csv;charset=GBK");

        response.setHeader("Content-Disposition", "attachment; filename="
                + new String(fileName.getBytes("GB2312"), "ISO8859-1"));
        InputStream in = null;
        try {
            in = new FileInputStream(csvFilePath);
            int len = 0;
            byte[] buffer = new byte[1024];
            OutputStream out = response.getOutputStream();
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } catch (FileNotFoundException e1) {
            System.out.println(e1);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e1) {
                    throw new RuntimeException(e1);
                }
            }
        }
    }

    /**
     * 删除该目录filePath下的所有文件
     *
     * @param filePath
     *            文件目录路径
     */
    public static void deleteFiles(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    files[i].delete();
                }
            }
        }
    }

    /**
     * 删除单个文件
     *
     * @param filePath
     *            文件目录路径
     * @param fileName
     *            文件名称
     */
    public static void deleteFile(String filePath, String fileName) {
        File file = new File(filePath);
        if (file.exists()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    if (files[i].getName().equals(fileName)) {
                        files[i].delete();
                        return;
                    }
                }
            }
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void createFileTest() {
        List exportData = new ArrayList<Map>();
        Map row1 = new LinkedHashMap<String, String>();
        //每一行 key放列号，value放当前行中列中的内容
        row1.put("1", "11");
        row1.put("2", "12");
        row1.put("3", "13");
        row1.put("4", "14");
        //将当前行加入到表格中
        exportData.add(row1);


        row1 = new LinkedHashMap<String, String>();
        row1.put("1", "21");
        row1.put("2", "22");
        row1.put("3", "23");
        row1.put("4", "24");
        exportData.add(row1);

        LinkedHashMap map = new LinkedHashMap();
        map.put("1", "第一列");
        map.put("2", "第二列");
        map.put("3", "第三列");
        map.put("4", "第四列");

        String path = "d:/export";
        String fileName = "文件导出";
        File file = CSVUtil.createCSVFile(exportData, map, path, fileName);
        String fileNameNew = file.getName();
        String pathNew = file.getPath();
        System.out.println("文件名称：" + fileNameNew );
        System.out.println("文件路径：" + pathNew );
    }
}

//注：BeanUtils.getProperty(row,(String) propertyEntry.getKey()) + "	" ，只为解决数字格式超过15位后，在Excel中打开展示科学记数问题。