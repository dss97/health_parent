package com.dou.test;


import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class POITest {
    //使用POI进行读写文件(office都可以)
    //@Test
    public void test01() throws IOException, InvalidFormatException {
        //加载指定文件,创建一个Excel对象
        XSSFWorkbook excel = new XSSFWorkbook(new File("D:\\dou.xlsx"));
        //读取Excel文件中第一个sheet标签页
        XSSFSheet sheet = excel.getSheetAt(0);
        //遍历sheet标签页,获取每一行数据
        for (Row row : sheet) {
            //获取每行每个单元格中的数据
            for (Cell cell : row) {
                System.out.println(cell.getStringCellValue());
            }
          excel.close();
        }
    }


    //使用POI进行读写文件(office都可以)
    //@Test
    public void test02() throws IOException, InvalidFormatException {
        //加载指定文件,创建一个Excel对象
        XSSFWorkbook excel = new XSSFWorkbook(new File("D:\\dou.xlsx"));
        //获取第一个sheet表
        XSSFSheet sheet = excel.getSheetAt(0);
        //获取当前工作表最后一行的行号,行号索引从0开始,遍历时候用=等号
        int lastRowNum = sheet.getLastRowNum();
        for (int i = 0; i <= lastRowNum; i++) {
            //根据行号获取每一行对象
            XSSFRow row = sheet.getRow(i);
            //获取当前行最后一个单元格索引
            short lastCellNum = row.getLastCellNum();
            for (int j=0;j<=lastCellNum-1;j++){
                XSSFCell cell = row.getCell(j);
                System.out.println(cell.getStringCellValue());
            }
        }
        excel.close();
    }

    //使用POI向文件中传入数据,并且通过输出流将创建的数据写入本地磁盘中
    //@Test
    public void test03() throws IOException {
        //在内存中创建一个excel文件(工作簿)
        XSSFWorkbook excel = new XSSFWorkbook();
        //创建一个工作表对象
        XSSFSheet sheet = excel.createSheet("小豆");
        //在工作表中创建行对象
        XSSFRow title = sheet.createRow(0);
        //在行中创建单元格对象
        title.createCell(0).setCellValue("姓名");
        title.createCell(1).setCellValue("年龄");
        title.createCell(2).setCellValue("爱好");
        title.createCell(3).setCellValue("地址");

        XSSFRow dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue("小李");
        dataRow.createCell(1).setCellValue("20");
        dataRow.createCell(2).setCellValue("女的");
        dataRow.createCell(3).setCellValue("郑州");

        //创建一个输出流,
        FileOutputStream fos = new FileOutputStream(new File("D:\\demo.xlsx"));
        excel.write(fos);
        //最后刷新关闭
        fos.flush();
        excel.close();
        //输出流不能关闭

    }
}
