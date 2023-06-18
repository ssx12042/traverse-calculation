package com.jeesite.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.SetUtils;
import com.jeesite.common.lang.ObjectUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.reflect.ReflectUtils;
import com.jeesite.common.utils.excel.ExcelException;
import com.jeesite.common.utils.excel.annotation.ExcelField;
import com.jeesite.common.utils.excel.annotation.ExcelFields;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Author: ssx
 * @Date: 2022/4/25
 * @Description:
 */
public class ExcelUtils {

    public static class MyExcelExport implements Closeable {

        private static Logger log = LoggerFactory.getLogger(MyExcelExport.class);

        /**
         * 工作薄对象
         */
        private Workbook wb;

        /**
         * 工作表对象
         */
        private Sheet sheet;

        /**
         * 样式列表
         */
        private Map<String, CellStyle> styles;

        /**
         * 当前行号
         */
        private int rownum;

        /**
         * 注解列表（Object[]{ ExcelField, Field/Method }）
         */
        private List<Object[]> annotationList;

        /**
         * 用于清理缓存
         */
        private Set<Class<?>> fieldTypes = SetUtils.newHashSet();

        /**
         * 当前操作的表明
         */
        private String tableName;

        /**
         * 合并单元格的开始列
         */
        private int colunm_num;

        /**
         * 当前行的仪器高的数值
         */
        private double heightInstrumentFore_currentRow;
        private double heightInstrumentBack_currentRow;

        /**
         * 构造函数
         * @param title 表格标题，传“空值”，表示无标题
         * @param cls 实体对象，通过annotation.ExportField获取标题
         */
        public MyExcelExport(String title, Class<?> cls){
            this(title, cls, ExcelField.Type.EXPORT);
        }

        /**
         * 构造函数
         * @param title 表格标题，传“空值”，表示无标题
         * @param cls 实体对象，通过annotation.ExportField获取标题
         * @param type 导出类型（1:导出数据；2：导出模板）
         * @param groups 导入分组
         */
        public MyExcelExport(String title, Class<?> cls, ExcelField.Type type, String... groups){
            this(null, null, title, cls, type, groups);
        }

        /**
         * 构造函数
         * @param sheetName 指定Sheet名称
         * @param title 表格标题，传“空值”，表示无标题
         * @param cls 实体对象，通过annotation.ExportField获取标题
         * @param type 导出类型（1:导出数据；2：导出模板）
         * @param groups 导入分组
         */
        public MyExcelExport(String sheetName, String title, Class<?> cls, ExcelField.Type type, String... groups){
            this(null, sheetName, title, cls, type, groups);
        }

        /**
         * 构造函数
         * @param wb 指定现有工作簿对象
         * @param sheetName 指定Sheet名称
         * @param title 表格标题，传“空值”，表示无标题
         * @param cls 实体对象，通过annotation.ExportField获取标题
         * @param type 导出类型（1:导出数据；2：导出模板）
         * @param groups 导入分组
         */
        public MyExcelExport(Workbook wb, String sheetName, String title, Class<?> cls, ExcelField.Type type, String... groups){
            if (wb != null){
                this.wb = wb;
            }else{
                this.wb = createWorkbook();
            }
            this.createSheet(sheetName, title, cls, type, groups);
        }

        /**
         * 构造函数
         * @param title 表格标题，传“空值”，表示无标题
         * @param headerList 表头数组
         */
        public MyExcelExport(String title, List<String> headerList, List<Integer> headerWidthList) {
            this(null, null, title, headerList, headerWidthList);
        }

        /**
         * 构造函数
         * @param sheetName 指定Sheet名称
         * @param title 表格标题，传“空值”，表示无标题
         * @param headerList 表头数组
         */
        public MyExcelExport(String sheetName, String title, List<String> headerList, List<Integer> headerWidthList) {
            this(null, sheetName, title, headerList, headerWidthList);
        }

        /**
         * 构造函数
         * @param wb 指定现有工作簿对象
         * @param sheetName，指定Sheet名称
         * @param title 表格标题，传“空值”，表示无标题
         * @param headerList 表头列表
         */
        public MyExcelExport(Workbook wb, String sheetName, String title, List<String> headerList, List<Integer> headerWidthList) {
            if (wb != null){
                this.wb = wb;
            }else{
                this.wb = createWorkbook();
            }
            this.createSheet(sheetName, title, headerList, headerWidthList);
        }

        /**
         * 创建一个工作簿
         */
        public Workbook createWorkbook(){
            return new SXSSFWorkbook(500);
        }

        /**
         * 获取当前工作薄
         * @author ThinkGem
         */
        public Workbook getWorkbook() {
            return wb;
        }

        /**
         * 创建工作表
         * @param sheetName，指定Sheet名称
         * @param title 表格标题，传“空值”，表示无标题
         * @param cls 实体对象，通过annotation.ExportField获取标题
         * @param type 导出类型（1:导出数据；2：导出模板）
         * @param groups 导入分组
         */
        public void createSheet(String sheetName, String title, Class<?> cls, ExcelField.Type type, String... groups){
            this.annotationList = ListUtils.newArrayList();
            // Get annotation field
            Field[] fs = cls.getDeclaredFields();
            for (Field f : fs){
                ExcelFields efs = f.getAnnotation(ExcelFields.class);
                if (efs != null && efs.value() != null){
                    for (ExcelField ef : efs.value()){
                        addAnnotation(annotationList, ef, f, type, groups);
                    }
                }
                ExcelField ef = f.getAnnotation(ExcelField.class);
                addAnnotation(annotationList, ef, f, type, groups);
            }
            // Get annotation method
            Method[] ms = cls.getDeclaredMethods();
            for (Method m : ms){
                ExcelFields efs = m.getAnnotation(ExcelFields.class);
                if (efs != null && efs.value() != null){
                    for (ExcelField ef : efs.value()){
                        addAnnotation(annotationList, ef, m, type, groups);
                    }
                }
                ExcelField ef = m.getAnnotation(ExcelField.class);
                addAnnotation(annotationList, ef, m, type, groups);
            }
            // Field sorting
            Collections.sort(annotationList, new Comparator<Object[]>() {
                @Override
                public int compare(Object[] o1, Object[] o2) {
                    return new Integer(((ExcelField)o1[0]).sort()).compareTo(
                            new Integer(((ExcelField)o2[0]).sort()));
                };
            });
            // Initialize
            List<String> headerList = ListUtils.newArrayList();
            List<Integer> headerWidthList = ListUtils.newArrayList();
            for (Object[] os : annotationList){
                ExcelField ef = (ExcelField)os[0];
                String headerTitle = ef.title();
                // 如果是导出，则去掉注释
                if (type == ExcelField.Type.EXPORT){
                    String[] ss = StringUtils.split(headerTitle, "**", 2);
                    if (ss.length == 2){
                        headerTitle = ss[0];
                    }
                }
                headerList.add(headerTitle);
                headerWidthList.add(ef.width());
            }
            // 创建工作表
            this.createSheet(sheetName, title, headerList, headerWidthList);
        }

        /**
         * 添加到 annotationList
         */
        private void addAnnotation(List<Object[]> annotationList, ExcelField ef, Object fOrM, ExcelField.Type type, String... groups){
//		if (ef != null && (ef.type()==0 || ef.type()==type)){
            if (ef != null && (ef.type() == ExcelField.Type.ALL || ef.type() == type)){
                if (groups != null && groups.length > 0){
                    boolean inGroup = false;
                    for (String g : groups){
                        if (inGroup){
                            break;
                        }
                        for (String efg : ef.groups()){
                            if (StringUtils.equals(g, efg)){
                                inGroup = true;
                                annotationList.add(new Object[]{ef, fOrM});
                                break;
                            }
                        }
                    }
                }else{
                    annotationList.add(new Object[]{ef, fOrM});
                }
            }
        }

        /**
         * 创建工作表
         * @param sheetName 指定Sheet名称
         * @param title 表格标题，传“空值”，表示无标题
         * @param headerList 表头字段设置
         * @param headerWidthList 表头字段宽度设置
         */
        public void createSheet(String sheetName, String title, List<String> headerList, List<Integer> headerWidthList) {
            this.sheet = wb.createSheet(StringUtils.defaultString(sheetName, StringUtils.defaultString(title, "Sheet1")));
            this.styles = createStyles(wb);
            this.rownum = 0;
            // Create title
            if (StringUtils.isNotBlank(title)){
                Row titleRow = sheet.createRow(rownum++);
                titleRow.setHeightInPoints(30);
                Cell titleCell = titleRow.createCell(0);
                titleCell.setCellStyle(styles.get("title"));
                titleCell.setCellValue(title);
                sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(),
                        titleRow.getRowNum(), titleRow.getRowNum(), headerList.size()-1));
            }
            rownum = 2;     // 留一行给基本信息
            /*// 创建起始数据行  2022年5月3日 su添加
            Row originDataRow = sheet.createRow(rownum++);
            Cell odCell = originDataRow.createCell(0);
            odCell.setCellValue("起始数据");
            sheet.addMergedRegion(new CellRangeAddress(originDataRow.getRowNum(), originDataRow.getRowNum(), 0, 1));
            // Create header
            if (headerList == null){
                throw new ExcelException("headerList not null!");
            }
            Row headerRow = sheet.createRow(rownum++);
            headerRow.setHeightInPoints(70);    // su
            for (int i = 0; i < headerList.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellStyle(styles.get("header"));
                String[] ss = StringUtils.split(headerList.get(i), "**", 2);
                if (ss.length==2){
                    cell.setCellValue(ss[0]);
                    Comment comment = this.sheet.createDrawingPatriarch().createCellComment(
                            new XSSFClientAnchor(0, 0, 0, 0, (short) 3, 3, (short) 5, 6));
                    comment.setRow(cell.getRowIndex());
                    comment.setColumn(cell.getColumnIndex());
                    comment.setString(new XSSFRichTextString(ss[1]));
                    cell.setCellComment(comment);
                }else{
                    cell.setCellValue(headerList.get(i));
                }
//			sheet.autoSizeColumn(i);
            }
            boolean isDefWidth = (headerWidthList != null && headerWidthList.size() == headerList.size());
            for (int i = 0; i < headerList.size(); i++) {
                int colWidth = -1;
                if (isDefWidth){
                    colWidth = headerWidthList.get(i);
                }
                if (colWidth == -1){
                    colWidth = sheet.getColumnWidth(i)*2;
                    colWidth = colWidth < 3000 ? 3000 : colWidth;
                }
                if (colWidth == 0){
                    sheet.setColumnHidden(i, true);
                }else{
                    sheet.setColumnWidth(i, colWidth);
                }
            }*/
            log.debug("Create sheet {} success.", sheetName);
        }

        /**
         * 创建基本信息行  2022年5月3日 su添加
         * @param baseInfoList 基本信息的List
         */
        public void createBaseInfoRow(List<String> baseInfoList) {
            Row secondRow = sheet.createRow(1);
            // 小组
            int firstColumn = 0;
            int lastColumn = 1;
            for (int i = 0; i < baseInfoList.size(); i++) {
                Cell cell = secondRow.createCell(firstColumn);
                cell.setCellValue(baseInfoList.get(i));
                sheet.addMergedRegion(new CellRangeAddress(secondRow.getRowNum(), secondRow.getRowNum(), firstColumn, lastColumn));
                firstColumn = lastColumn + 2;
                // 如果遍历到了倒数第二个，那下一个（报告时间）就需要合并五个格子
                if (i == baseInfoList.size() - 2) {
                    lastColumn = firstColumn + 4;
                } else {
                    lastColumn = firstColumn + 3;
                }
            }
        }

        /**
         * 创建表头 2022年5月3日 su添加
         * @param cls 实体对象，通过annotation.ExportField获取标题
         */
        public void createHeader(Class<?> cls, String tableName) {
            this.tableName = tableName;
            createHeader(cls, ExcelField.Type.EXPORT);
        }

        /**
         * 创建表头
         * @param cls 实体对象，通过annotation.ExportField获取标题
         * @param type 导出类型（1:导出数据；2：导出模板）
         * @param groups 导入分组
         */
        public void createHeader(Class<?> cls, ExcelField.Type type, String... groups){
            /* **************************** 获取表头 ********************************* */
            this.annotationList = ListUtils.newArrayList();
            // Get annotation field
            Field[] fs = cls.getDeclaredFields();
            for (Field f : fs){
                ExcelFields efs = f.getAnnotation(ExcelFields.class);
                if (efs != null && efs.value() != null){
                    for (ExcelField ef : efs.value()){
                        addAnnotation(annotationList, ef, f, type, groups);
                    }
                }
                ExcelField ef = f.getAnnotation(ExcelField.class);
                addAnnotation(annotationList, ef, f, type, groups);
            }
            // Get annotation method
            Method[] ms = cls.getDeclaredMethods();
            for (Method m : ms){
                ExcelFields efs = m.getAnnotation(ExcelFields.class);
                if (efs != null && efs.value() != null){
                    for (ExcelField ef : efs.value()){
                        addAnnotation(annotationList, ef, m, type, groups);
                    }
                }
                ExcelField ef = m.getAnnotation(ExcelField.class);
                addAnnotation(annotationList, ef, m, type, groups);
            }
            // Field sorting
            Collections.sort(annotationList, new Comparator<Object[]>() {
                @Override
                public int compare(Object[] o1, Object[] o2) {
                    return new Integer(((ExcelField)o1[0]).sort()).compareTo(
                            new Integer(((ExcelField)o2[0]).sort()));
                };
            });
            // Initialize
            List<String> headerList = ListUtils.newArrayList();
            List<Integer> headerWidthList = ListUtils.newArrayList();
            for (Object[] os : annotationList){
                ExcelField ef = (ExcelField)os[0];
                String headerTitle = ef.title();
                // 如果是导出，则去掉注释
                if (type == ExcelField.Type.EXPORT){
                    String[] ss = StringUtils.split(headerTitle, "**", 2);
                    if (ss.length == 2){
                        headerTitle = ss[0];
                    }
                }
                headerList.add(headerTitle);
                headerWidthList.add(ef.width());
            }

            /* **************************** 创建表头 ********************************* */
            rownum++;   // 与上一表格隔开一行
            // 创建表格名字行
            Row tableNameRow = sheet.createRow(rownum++);
            Cell odCell = tableNameRow.createCell(0);
            odCell.setCellValue(tableName);
            sheet.addMergedRegion(new CellRangeAddress(tableNameRow.getRowNum(), tableNameRow.getRowNum(), 0, 2));

            // 创建三级表头
            if (tableName.startsWith("高程")) {
                Row threeHeaderRow = sheet.createRow(rownum++);
                threeHeaderRow.setHeightInPoints(30);

                // 名字，以及要合并的列数
                Map<String, Integer> name_mergeCol_list = new LinkedHashMap<>();
                if ("高程-起始数据".equals(tableName)) {
                    name_mergeCol_list.put("起始测站数据", 3);
                    name_mergeCol_list.put("终点测站数据", 3);
                    colunm_num = 0;
                } else if ("高程-观测数据".equals(tableName)) {
                    name_mergeCol_list.put("观测前站", 8);
                    name_mergeCol_list.put("观测后站", 8);
                    colunm_num = 7;
                } else if ("高程-数据解算".equals(tableName) || "高程-间接平差".equals(tableName)) {
                    name_mergeCol_list.put("前视", 12);
                    name_mergeCol_list.put("后视", 12);
                    name_mergeCol_list.put("高程", 5);
                    if ("高程-数据解算".equals(tableName)) {
                        name_mergeCol_list.put("结果评定", 18);
                    }
                    colunm_num = 6;
                }

                for (Map.Entry<String, Integer> entry : name_mergeCol_list.entrySet()) {
                    Cell cell = threeHeaderRow.createCell(colunm_num);
                    cell.setCellStyle(styles.get("header"));
                    cell.setCellValue(entry.getKey());
                    sheet.addMergedRegion(new CellRangeAddress(threeHeaderRow.getRowNum(), threeHeaderRow.getRowNum(), colunm_num, colunm_num + entry.getValue()));
                    Cell cell_merged;
                    for (int j = 1; j <= entry.getValue(); j++) {
                        cell_merged = threeHeaderRow.createCell(colunm_num + j);
                        cell_merged.setCellStyle(styles.get("header"));
                    }
                    colunm_num += (entry.getValue() + 1);
                    if (entry.getKey().equals("前视")) {
                        if ("高程-数据解算".equals(tableName) || "高程-间接平差".equals(tableName)) {
                            colunm_num += 2;
                        }
                    }
                }
            }

            // 创建二级表头
            colunm_num = 0;
            if (tableName.startsWith("高程") && !"高程-起始数据".equals(tableName)) {
                Row twoHeaderRow = sheet.createRow(rownum++);
                twoHeaderRow.setHeightInPoints(30);
                if ("高程-数据解算".equals(tableName)) {
                    twoHeaderRow.setHeightInPoints(50);
                }

                // 名字，以及要合并的列数
                Map<String, Integer> name_mergeCol_list = new LinkedHashMap<>();
                if ("高程-观测数据".equals(tableName)) {
                    name_mergeCol_list.put("垂直角", 2);
                    name_mergeCol_list.put("垂直角 ", 2);
                    colunm_num = 11;
                } else if ("高程-数据解算".equals(tableName) || "高程-间接平差".equals(tableName)) {
                    name_mergeCol_list.put("垂直角", 4);
                    name_mergeCol_list.put("垂直角 ", 4);
                    if ("高程-数据解算".equals(tableName)) {
                        name_mergeCol_list.put("往返测高差较差(前视)(mm)", 3);
                        name_mergeCol_list.put("每千米高差全中误差(mm)", 3);
                        name_mergeCol_list.put("导线闭合差(mm)", 3);
                    }
                    colunm_num = 10;
                }

                for (Map.Entry<String, Integer> entry : name_mergeCol_list.entrySet()) {
                    Cell cell = twoHeaderRow.createCell(colunm_num);
                    cell.setCellStyle(styles.get("header"));
                    cell.setCellValue(entry.getKey());
                    sheet.addMergedRegion(new CellRangeAddress(twoHeaderRow.getRowNum(), twoHeaderRow.getRowNum(), colunm_num, colunm_num + entry.getValue()));
                    Cell cell_merged;
                    for (int j = 1; j <= entry.getValue(); j++) {
                        cell_merged = twoHeaderRow.createCell(colunm_num + j);
                        cell_merged.setCellStyle(styles.get("header"));
                    }
                    colunm_num += (entry.getValue() + 1);
                    if (entry.getKey().equals("垂直角")) {
                        if ("高程-观测数据".equals(tableName)) {
                            colunm_num = 20;
                        } else if ("高程-数据解算".equals(tableName) || "高程-间接平差".equals(tableName)) {
                            colunm_num = 25;
                        }
                    } else if (entry.getKey().equals("垂直角 ")) {
                        if ("高程-数据解算".equals(tableName)) {
                            colunm_num = 44;
                        }
                    }
                }
            }

            // Create header
            if (headerList == null){
                throw new ExcelException("headerList not null!");
            }
            Row headerRow = sheet.createRow(rownum++);
            if (tableName.startsWith("高程")) {
                headerRow.setHeightInPoints(30);
            } else {
                headerRow.setHeightInPoints(70);    // su
            }

            colunm_num = 0;
            for (int i = 0; i < headerList.size(); i++) {
                // Cell cell = headerRow.createCell(i); // su
                Cell cell = headerRow.createCell(colunm_num);
                cell.setCellStyle(styles.get("header"));
                String[] ss = StringUtils.split(headerList.get(i), "**", 2);
                if (ss.length == 2) {
                    cell.setCellValue(ss[0]);
                    Comment comment = this.sheet.createDrawingPatriarch().createCellComment(
                            new XSSFClientAnchor(0, 0, 0, 0, (short) 3, 3, (short) 5, 6));
                    comment.setRow(cell.getRowIndex());
                    comment.setColumn(cell.getColumnIndex());
                    comment.setString(new XSSFRichTextString(ss[1]));
                    cell.setCellComment(comment);
                } else {
                    cell.setCellValue(headerList.get(i));
                }
                /* --------------------------------------------------------------- */
                // 合并表头的一些单元格 2022年5月4日 su添加
                if (!tableName.startsWith("高程")) {
                    if ("起始数据".equals(tableName)) {
                        if (i == 4 || i == 5 || i == 10 || i == 11) {
                            sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(), headerRow.getRowNum(), colunm_num, colunm_num + 1));
                            colunm_num += 1;
                        }
                    } else if ("观测数据".equals(tableName)) {
                        if (i == 16 || i == 17) {
                            sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(), headerRow.getRowNum(), colunm_num, colunm_num + 1));
                            colunm_num += 1;
                        }
                    } else if ("数据解算".equals(tableName)) {
                        if (i == 3 || i == 10 || i == 11) {
                            sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(), headerRow.getRowNum(), colunm_num, colunm_num + 1));
                            colunm_num += 1;
                        }
                    } else if ("结果评定".equals(tableName)) {
                        if (i == 0 || i == 4) {
                            sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(), headerRow.getRowNum(), colunm_num, colunm_num + 1));
                            colunm_num += 1;
                        } else if (i == 6) {
                            sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(), headerRow.getRowNum(), colunm_num, colunm_num + 2));
                            colunm_num += 2;
                        }
                    } else if ("间接平差".equals(tableName)) {
                        if (i == 3 || i == 4 || i == 11 || i == 12 || i == 13
                                || i == 14 || i == 15 || i == 16 || i == 17) {
                            sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(), headerRow.getRowNum(), colunm_num, colunm_num + 1));
                            colunm_num += 1;
                        } else if (i == 18) {
                            sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(), headerRow.getRowNum(), colunm_num, colunm_num + 2));
                            colunm_num += 2;
                        }
                    }
                }else if (tableName.startsWith("高程")) {
                    Row threeHeaderRow = sheet.getRow(headerRow.getRowNum() - 2);
                    Row twoHeaderRow = sheet.getRow(headerRow.getRowNum() - 1);
                    if ("高程-起始数据".equals(tableName)) {
                        if (i == 0 || i == 1 || i == 2 || i == 3) {
                            sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(), headerRow.getRowNum(), colunm_num, colunm_num + 1));
                            Cell cell_merged = headerRow.createCell(colunm_num + 1);
                            cell_merged.setCellStyle(styles.get("header"));
                            colunm_num += 1;
                        }
                    } else if ("高程-观测数据".equals(tableName)) {
                        if (i == 0 || i == 1 || i == 2) {
                            Cell cell1 = threeHeaderRow.createCell(colunm_num);
                            cell1.setCellStyle(styles.get("header"));
                            cell1.setCellValue(headerList.get(i));
                            sheet.addMergedRegion(new CellRangeAddress(threeHeaderRow.getRowNum(), headerRow.getRowNum(), colunm_num, colunm_num + 1));
                            Cell cell_merged = headerRow.createCell(colunm_num + 1);
                            cell_merged.setCellStyle(styles.get("header"));
                            colunm_num += 1;
                        } else if (i == 3) {
                            Cell cell1 = threeHeaderRow.createCell(colunm_num);
                            cell1.setCellStyle(styles.get("header"));
                            cell1.setCellValue(headerList.get(i));
                            sheet.addMergedRegion(new CellRangeAddress(threeHeaderRow.getRowNum(), headerRow.getRowNum(), colunm_num, colunm_num));
                        } else if (i == 4 || i == 5 || i == 9 || i == 10 || i == 11 || i == 15) {
                            Cell cell1 = twoHeaderRow.createCell(colunm_num);
                            cell1.setCellStyle(styles.get("header"));
                            cell1.setCellValue(headerList.get(i));
                            sheet.addMergedRegion(new CellRangeAddress(twoHeaderRow.getRowNum(), headerRow.getRowNum(), colunm_num, colunm_num + 1));
                            colunm_num += 1;
                        }
                    } else if ("高程-数据解算".equals(tableName) || "高程-间接平差".equals(tableName)) {
                        if (i == 0 || i == 1 || i == 2 || i == 11) {
                            Cell cell1 = threeHeaderRow.createCell(colunm_num);
                            cell1.setCellStyle(styles.get("header"));
                            cell1.setCellValue(headerList.get(i));
                            sheet.addMergedRegion(new CellRangeAddress(threeHeaderRow.getRowNum(), headerRow.getRowNum(), colunm_num, colunm_num + 1));
                            Cell cell_merged = headerRow.createCell(colunm_num + 1);
                            cell_merged.setCellStyle(styles.get("header"));
                            colunm_num += 1;
                        } else if (i == 3 || i == 4 || i== 8 || i == 9 || i == 10 || i == 12 || i == 13 || i == 17
                                || i == 18 || i == 19 || i == 20 || i == 21 || i == 22) {
                            if (i != 8 && i != 17) {
                                Cell cell1 = twoHeaderRow.createCell(colunm_num);
                                cell1.setCellStyle(styles.get("header"));
                                cell1.setCellValue(headerList.get(i));
                                sheet.addMergedRegion(new CellRangeAddress(twoHeaderRow.getRowNum(), headerRow.getRowNum(), colunm_num, colunm_num + 1));
                            } else {
                                sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(), headerRow.getRowNum(), colunm_num, colunm_num + 1));
                            }
                            Cell cell_merged = headerRow.createCell(colunm_num + 1);
                            cell_merged.setCellStyle(styles.get("header"));
                            colunm_num += 1;
                        } else{
                            if ("高程-数据解算".equals(tableName)) {
                                if (i == 23) {
                                    Cell cell1 = twoHeaderRow.createCell(colunm_num);
                                    cell1.setCellStyle(styles.get("header"));
                                    cell1.setCellValue(headerList.get(i));
                                    sheet.addMergedRegion(new CellRangeAddress(twoHeaderRow.getRowNum(), headerRow.getRowNum(), colunm_num, colunm_num));
                                } else if (i == 24 || i == 31) {
                                    Cell cell1 = twoHeaderRow.createCell(colunm_num);
                                    cell1.setCellStyle(styles.get("header"));
                                    cell1.setCellValue(headerList.get(i));
                                    sheet.addMergedRegion(new CellRangeAddress(twoHeaderRow.getRowNum(), headerRow.getRowNum(), colunm_num, colunm_num + 2));
                                    colunm_num += 2;
                                } else if (i == 25 || i == 26 || i == 27 || i == 28 || i == 29 || i == 30) {
                                    sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(), headerRow.getRowNum(), colunm_num, colunm_num + 1));
                                    Cell cell_merged = headerRow.createCell(colunm_num + 1);
                                    cell_merged.setCellStyle(styles.get("header"));
                                    colunm_num += 1;
                                }
                            } else {

                            }
                        }
                    }
                }
                // 每个表通用的，最后的两个字段是时间和备注信息
                Row row1;
                if ("高程-起始数据".equals(tableName)) {
                    row1 = sheet.getRow(headerRow.getRowNum() - 1);
                } else {
                    row1 = sheet.getRow(headerRow.getRowNum() - 2);
                }
                if (i == headerList.size() - 2) {
                    if (tableName.startsWith("高程")) {
                        Cell cell1 = row1.createCell(colunm_num);
                        cell1.setCellStyle(styles.get("header"));
                        cell1.setCellValue(headerList.get(i));
                        sheet.addMergedRegion(new CellRangeAddress(row1.getRowNum(), headerRow.getRowNum(), colunm_num, colunm_num + 2));
                    } else {
                        sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(), headerRow.getRowNum(), colunm_num, colunm_num + 2));
                    }
                    colunm_num += 2;
                }
                if (i == headerList.size() - 1) {
                    // Row row1 = sheet.getRow(headerRow.getRowNum() - 1);
                    if (tableName.startsWith("高程")) {
                        Cell cell1 = row1.createCell(colunm_num);
                        cell1.setCellStyle(styles.get("header"));
                        cell1.setCellValue(headerList.get(i));
                        sheet.addMergedRegion(new CellRangeAddress(row1.getRowNum(), headerRow.getRowNum(), colunm_num, colunm_num + 4));
                    } else {
                        sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(), headerRow.getRowNum(), colunm_num, colunm_num + 4));
                    }
                }
                colunm_num++;
                /* --------------------------------------------------------------- */
//			sheet.autoSizeColumn(i);
            }

            boolean isDefWidth = (headerWidthList != null && headerWidthList.size() == headerList.size());   // su
            // boolean isDefWidth = false;
            for (int i = 0; i < headerList.size(); i++) {
                int colWidth = -1;
                if (isDefWidth){
                    colWidth = headerWidthList.get(i);
                }
                if (colWidth == -1){
                    colWidth = sheet.getColumnWidth(i)*2;
                    colWidth = colWidth < 3000 ? 3000 : colWidth;
                }
                if (colWidth == 0){
                    sheet.setColumnHidden(i, true);
                }else{
                    sheet.setColumnWidth(i, colWidth);
                }
                // 观测数据表格的列宽设置 往后延伸8个单元格 2022年5月4日 su添加
                if ("观测数据".equals(tableName) && i == headerList.size() - 1) {
                    for (int j = 1; j <= 8; j++) {
                        sheet.setColumnWidth(i + j, colWidth);
                    }
                }
                // 数据解算表格的列宽设置 往后延伸33个单元格
                else if ("高程-数据解算".equals(tableName) && i == headerList.size() - 1) {
                    for (int j = 1; j <= 33; j++) {
                        sheet.setColumnWidth(i + j, colWidth);
                    }
                }
            }
        }

        /**
         * 创建表格样式
         * @param wb 工作薄对象
         * @return 样式列表
         */
        private Map<String, CellStyle> createStyles(Workbook wb) {
            Map<String, CellStyle> styles = new HashMap<String, CellStyle>();

            CellStyle style = wb.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            Font titleFont = wb.createFont();
            titleFont.setFontName("Arial");
            titleFont.setFontHeightInPoints((short) 16);
            titleFont.setBold(true);
            style.setFont(titleFont);
            styles.put("title", style);

            style = wb.createCellStyle();
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setBorderRight(BorderStyle.THIN);
            style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
            style.setBorderLeft(BorderStyle.THIN);
            style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
            style.setBorderTop(BorderStyle.THIN);
            style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
            style.setBorderBottom(BorderStyle.THIN);
            style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
            Font dataFont = wb.createFont();
            dataFont.setFontName("Arial");
            dataFont.setFontHeightInPoints((short) 10);
            style.setFont(dataFont);
            styles.put("data", style);

            style = wb.createCellStyle();
            style.cloneStyleFrom(styles.get("data"));
            style.setAlignment(HorizontalAlignment.LEFT);
            styles.put("data1", style);

            style = wb.createCellStyle();
            style.cloneStyleFrom(styles.get("data"));
            style.setAlignment(HorizontalAlignment.CENTER);
            styles.put("data2", style);

            style = wb.createCellStyle();
            style.cloneStyleFrom(styles.get("data"));
            style.setAlignment(HorizontalAlignment.RIGHT);
            styles.put("data3", style);

            style = wb.createCellStyle();
            style.cloneStyleFrom(styles.get("data"));
		    style.setWrapText(true);    // su
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setRightBorderColor(IndexedColors.WHITE.getIndex());
            style.setLeftBorderColor(IndexedColors.WHITE.getIndex());
            style.setTopBorderColor(IndexedColors.WHITE.getIndex());
            style.setBottomBorderColor(IndexedColors.WHITE.getIndex());
            style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font headerFont = wb.createFont();
            headerFont.setFontName("Arial");
            headerFont.setFontHeightInPoints((short) 10);
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            style.setFont(headerFont);
            styles.put("header", style);

            return styles;
        }

        /**
         * 添加一行
         * @return 行对象
         */
        public Row addRow(){
            return sheet.createRow(rownum++);
        }

        /**
         * 添加一个单元格
         * @param row 添加的行
         * @param column 添加列号
         * @param val 添加值
         * @return 单元格对象
         */
        /*public Cell addCell(Row row, int column, Object val){
            return this.addCell(row, column, val, ExcelField.Align.AUTO, Class.class, null);
        }*/

        /**
         * 添加一个单元格
         * @param row 添加的行
         * @param column 添加列号
         * @param val 添加值
         * @param align 对齐方式（1：靠左；2：居中；3：靠右）
         * @param dataFormat 数值格式（例如：0.00，yyyy-MM-dd）
         * @param size_list 当前表格一行数据的长度
         * @return 单元格对象
         */
        public Cell addCell(Row row, int column, Object val, ExcelField.Align align, Class<?> fieldType, String dataFormat, int size_list){
            if (heightInstrumentFore_currentRow == 999.0 && column < 20) {
                return row.createCell(colunm_num);
            }
            if (heightInstrumentBack_currentRow == 999.0 && column < 11) {
                return row.createCell(colunm_num);
            }
            // Cell cell = row.createCell(column);  // su
            Cell cell = row.createCell(colunm_num);
            String defaultDataFormat = "@";
            CellStyle style = wb.createCellStyle();
            try {
                if(val == null){
                    cell.setCellValue("");
                }else if(fieldType != Class.class){
                    fieldTypes.add(fieldType); // 先存起来，方便完成后清理缓存
                    cell.setCellValue((String)fieldType.getMethod("setValue", Object.class).invoke(null, val));
                    try{
                        defaultDataFormat = (String)fieldType.getMethod("getDataFormat").invoke(null);
                    } catch (Exception ex) {
                        defaultDataFormat = "@";
                    }
                }else{
                    if(val instanceof String) {
                        cell.setCellValue((String) val);
                    }else if(val instanceof Integer) {
                        cell.setCellValue((Integer) val);
                        defaultDataFormat = "0";
                    }else if(val instanceof Long) {
                        cell.setCellValue((Long) val);
                        defaultDataFormat = "0";
                    }else if(val instanceof Double) {
                        cell.setCellValue((Double) val);
                        defaultDataFormat = "0.00";
                    }else if(val instanceof Float) {
                        cell.setCellValue((Float) val);
                        defaultDataFormat = "0.00";
                    }else if(val instanceof BigDecimal) {
                        cell.setCellValue(((BigDecimal)val).doubleValue());
                    }else if(val instanceof Date) {
                        cell.setCellValue((Date) val);
                        defaultDataFormat = "yyyy-MM-dd HH:mm";
                    }else {
                        // 如果没有指定 fieldType，切自行根据类型查找相应的转换类（com.jeesite.common.utils.excel.fieldtype.值的类名+Type）
                        Class<?> fieldType2 = Class.forName(this.getClass().getName().replaceAll(this.getClass().getSimpleName(),
                                "fieldtype."+val.getClass().getSimpleName()+"Type"));
                        fieldTypes.add(fieldType2); // 先存起来，方便完成后清理缓存
                        cell.setCellValue((String)fieldType2.getMethod("setValue", Object.class).invoke(null, val));
                    }
                }
//			if (val != null){
                style = styles.get("data_column_"+column);
                if (style == null){
                    style = wb.createCellStyle();
                    style.cloneStyleFrom(styles.get("data"+(align.value()>=1&&align.value()<=3?align.value():"")));
                    if (dataFormat != null){
                        defaultDataFormat = dataFormat;
                    }
                    style.setDataFormat(wb.createDataFormat().getFormat(defaultDataFormat));
                    styles.put("data_column_" + column, style);
                }
                cell.setCellStyle(style);
//			}
            } catch (Exception ex) {
                log.info("Set cell value ["+row.getRowNum()+","+column+"] error: " + ex.toString());
                cell.setCellValue(ObjectUtils.toString(val));
            }
            /* --------------------------------------------------------------- */
            // 合并数据行的一些单元格 2022年5月4日 su添加
            if (!tableName.startsWith("高程")) {
                if ("起始数据".equals(tableName)) {
                    if (column == 4 || column == 5 || column == 10 || column == 11) {
                        // 合并单元格
                        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), colunm_num, colunm_num + 1));
                        // 统一被合并单元格的样式
                        Cell cell_merged = row.createCell(colunm_num + 1);
                        cell_merged.setCellStyle(style);
                        colunm_num += 1;
                    }
                } else if ("观测数据".equals(tableName)) {
                    if (column == 16 || column == 17) {
                        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), colunm_num, colunm_num + 1));
                        Cell cell_merged = row.createCell(colunm_num + 1);
                        cell_merged.setCellStyle(style);
                        colunm_num += 1;
                    }
                } else if ("数据解算".equals(tableName)) {
                    if (column == 3 || column == 10 || column == 11) {
                        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), colunm_num, colunm_num + 1));
                        Cell cell_merged = row.createCell(colunm_num + 1);
                        cell_merged.setCellStyle(style);
                        colunm_num += 1;
                    }
                } else if ("结果评定".equals(tableName)) {
                    if (column == 0 || column == 4) {
                        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), colunm_num, colunm_num + 1));
                        Cell cell_merged = row.createCell(colunm_num + 1);
                        cell_merged.setCellStyle(style);
                        colunm_num += 1;
                    } else if (column == 6) {
                        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), colunm_num, colunm_num + 2));
                        Cell cell_merged;
                        for (int i = 1; i <= 2; i++) {
                            cell_merged = row.createCell(colunm_num + i);
                            cell_merged.setCellStyle(style);
                        }
                        colunm_num += 2;
                    }
                } else if ("间接平差".equals(tableName)) {
                    if (column == 3 || column == 4 || column == 11 || column == 12 || column == 13
                            || column == 14 || column == 15 || column == 16 || column == 17) {
                        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), colunm_num, colunm_num + 1));
                        Cell cell_merged = row.createCell(colunm_num + 1);
                        cell_merged.setCellStyle(style);
                        colunm_num += 1;
                    }
                    if (column == 18) {
                        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), colunm_num, colunm_num + 2));
                        Cell cell_merged;
                        for (int i = 1; i <= 2; i++) {
                            cell_merged = row.createCell(colunm_num + i);
                            cell_merged.setCellStyle(style);
                        }
                        colunm_num += 2;
                    }
                }
            }else if (tableName.startsWith("高程")) {
                if ("高程-起始数据".equals(tableName)) {
                    if (column == 0 || column == 1 || column == 2 || column == 3) {
                        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), colunm_num, colunm_num + 1));
                        Cell cell_merged = row.createCell(colunm_num + 1);
                        cell_merged.setCellStyle(style);
                        colunm_num += 1;
                    }
                } else if ("高程-观测数据".equals(tableName)) {
                    if (column == 0 || column == 1 || column == 2 || column == 4 || column == 5 || column == 9 || column == 10 || column == 11 || column == 15) {
                        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), colunm_num, colunm_num + 1));
                        Cell cell_merged = row.createCell(colunm_num + 1);
                        cell_merged.setCellStyle(style);
                        colunm_num += 1;
                    }
                } else if ("高程-数据解算".equals(tableName) || "高程-间接平差".equals(tableName)) {
                    if (column == 3) {
                        heightInstrumentBack_currentRow = cell.getNumericCellValue();
                    }
                    if (column == 12) {
                        heightInstrumentFore_currentRow = cell.getNumericCellValue();
                    }
                    if (column == 0 || column == 1 || column == 2 || column == 11 || column == 20 || column == 21 || column == 22) {
                        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), colunm_num, colunm_num + 1));
                        Cell cell_merged = row.createCell(colunm_num + 1);
                        cell_merged.setCellStyle(style);
                        colunm_num += 1;
                    }else if (column == 3 || column == 4 || column== 8 || column == 9 || column == 10) {
                        if (heightInstrumentBack_currentRow == 999.0 && column ==3) {
                            cell.setCellValue("无");
                            sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), colunm_num, colunm_num + 12));
                            Cell cell_merged;
                            for (int j = 1; j <= 12; j++) {
                                cell_merged = row.createCell(colunm_num + j);
                                cell_merged.setCellStyle(style);
                            }
                            colunm_num += 12;
                        } else if (heightInstrumentBack_currentRow != 999.0) {
                            sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), colunm_num, colunm_num + 1));
                            Cell cell_merged = row.createCell(colunm_num + 1);
                            cell_merged.setCellStyle(style);
                            colunm_num += 1;
                        }
                    } else if (column == 12 || column == 13 || column == 17 || column == 18 || column == 19) {
                        if (heightInstrumentFore_currentRow == 999.0 && column == 12) {
                            cell.setCellValue("无");
                            sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), colunm_num, colunm_num + 12));
                            Cell cell_merged;
                            for (int j = 1; j <= 12; j++) {
                                cell_merged = row.createCell(colunm_num + j);
                                cell_merged.setCellStyle(style);
                            }
                            colunm_num += 12;
                        } else if (heightInstrumentFore_currentRow != 999.0) {
                            sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), colunm_num, colunm_num + 1));
                            Cell cell_merged = row.createCell(colunm_num + 1);
                            cell_merged.setCellStyle(style);
                            colunm_num += 1;
                        }
                    } else {
                        if ("高程-数据解算".equals(tableName)) {
                            if (column == 25 || column == 26 || column == 27 || column == 28 || column == 29 || column == 30) {
                                sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), colunm_num, colunm_num + 1));
                                Cell cell_merged = row.createCell(colunm_num + 1);
                                cell_merged.setCellStyle(style);
                                colunm_num += 1;
                            }else if (column == 24 || column == 31) {
                                sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), colunm_num, colunm_num + 2));
                                Cell cell_merged;
                                for (int j = 1; j <= 2; j++) {
                                    cell_merged = row.createCell(colunm_num + j);
                                    cell_merged.setCellStyle(style);
                                }
                                colunm_num += 2;
                            }
                        }
                    }
                }
            }
            // 每个表通用的，最后的两个字段是时间和备注信息
            if (column == size_list - 2) {
                sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), colunm_num, colunm_num + 2));
                Cell cell_merged;
                for (int i = 1; i <= 2; i++) {
                    cell_merged = row.createCell(colunm_num + i);
                    cell_merged.setCellStyle(style);
                }
                colunm_num += 2;
            }
            if (column == size_list - 1) {
                sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), colunm_num, colunm_num + 4));
                Cell cell_merged;
                for (int i = 1; i <= 4; i++) {
                    cell_merged = row.createCell(colunm_num + i);
                    cell_merged.setCellStyle(style);
                }
            }
            colunm_num++;
            /* --------------------------------------------------------------- */
            return cell;
        }

        /**
         * 添加数据（通过annotation.ExportField添加数据）
         * @return list 数据列表
         */
        public <E> MyExcelExport setDataList(List<E> list){
            for (E e : list){
                heightInstrumentFore_currentRow = 0;
                heightInstrumentBack_currentRow = 0;
                int colunm = 0;
                colunm_num = 0; // su
                Row row = this.addRow();
                StringBuilder sb = new StringBuilder();
                for (Object[] os : annotationList){
                    ExcelField ef = (ExcelField)os[0];
                    Object val = null;
                    // Get entity value
                    try{
                        if (StringUtils.isNotBlank(ef.attrName())){
                            val = ReflectUtils.invokeGetter(e, ef.attrName());
                        }else{
                            if (os[1] instanceof Field){
                                val = ReflectUtils.invokeGetter(e, ((Field)os[1]).getName());
                            }else if (os[1] instanceof Method){
                                val = ReflectUtils.invokeMethod(e, ((Method)os[1]).getName(), new Class[] {}, new Object[] {});
                            }
                        }
                        // If is dict, get dict label
                        if (StringUtils.isNotBlank(ef.dictType())){
                            Class<?> dictUtils = Class.forName("com.jeesite.modules.sys.utils.DictUtils");
                            val = dictUtils.getMethod("getDictLabel", String.class, String.class,
                                    String.class).invoke(null, ef.dictType(), val==null?"":val.toString(), "");
                            //val = DictUtils.getDictLabel(val==null?"":val.toString(), ef.dictType(), "");
                        }
                    }catch(Exception ex) {
                        // Failure to ignore
                        log.info(ex.toString());
                        val = "";
                    }
                    String dataFormat = ef.dataFormat();
                    try {
                        // 获取Json格式化注解的格式化参数
                        JsonFormat jf = e.getClass().getMethod("get"+StringUtils.capitalize(ef.attrName())).getAnnotation(JsonFormat.class);
                        if (jf != null && jf.pattern() != null){
                            dataFormat = jf.pattern();
                        }
                    } catch (Exception e1) {
                        // 如果获取失败，则使用默认。
                    }
                    // this.addCell(row, colunm++, val, ef.align(), ef.fieldType(), dataFormat);    // su
                    this.addCell(row, colunm++, val, ef.align(), ef.fieldType(), dataFormat, annotationList.size());
                    sb.append(val + ", ");
                }
                log.debug("Write success: ["+row.getRowNum()+"] "+sb.toString());
            }
            // 重置styles 2022年5月4日 su添加
            this.styles = createStyles(wb);
            return this;
        }

        /**
         * 输出数据流
         * @param os 输出数据流
         */
        public MyExcelExport write(OutputStream os){
            try{
                wb.write(os);
            }catch(IOException ex){
                log.error(ex.getMessage(), ex);
            }
            return this;
        }

        /**
         * 输出到客户端
         * @param fileName 输出文件名
         */
        public MyExcelExport write(HttpServletResponse response, String fileName){
            response.reset();
            response.setContentType("application/octet-stream; charset=utf-8");
            response.addHeader("Content-Disposition", "attachment; filename*=utf-8'zh_cn'"+ EncodeUtils.encodeUrl(fileName));
            try {
                write(response.getOutputStream());
            } catch (IOException ex) {
                log.error(ex.getMessage(), ex);
            }
            return this;
        }

        /**
         * 输出到文件
         * @param name 输出文件名
         */
        public MyExcelExport writeFile(String name) throws FileNotFoundException, IOException{
            FileOutputStream os = new FileOutputStream(name);
            this.write(os);
            return this;
        }

//	/**
//	 * 清理临时文件
//	 * @deprecated see close()
//	 */
//	public ExcelExport dispose(){
//		try {
//			this.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return this;
//	}

        @Override
        public void close() {
            if (wb instanceof SXSSFWorkbook){
                ((SXSSFWorkbook)wb).dispose();
            }
            Iterator<Class<?>> it = fieldTypes.iterator();
            while(it.hasNext()){
                Class<?> clazz = it.next();
                try {
                    clazz.getMethod("clearCache").invoke(null);
                } catch (Exception e) {
                    // 报错忽略，有可能没实现此方法
                }
            }
        }
    }

    public static void main(String[] args) {

    }
}
