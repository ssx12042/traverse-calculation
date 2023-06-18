package com.jeesite.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.SetUtils;
import com.jeesite.common.lang.ObjectUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.reflect.ReflectUtils;
import com.jeesite.common.utils.excel.annotation.ExcelField;
import com.jeesite.common.utils.excel.annotation.ExcelFields;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Author: ssx
 * @Date: 2022/7/9
 * @Description:
 */
public class MyExcelUtils implements Closeable {

    private static Logger log = LoggerFactory.getLogger(ExcelUtils.MyExcelExport.class);

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


    public MyExcelUtils(Workbook wb, String sheetName, String title) {
        this.wb = wb;
        this.sheet = wb.getSheet(sheetName);
        this.rownum = 0;

        // Create title
        if (StringUtils.isNotBlank(title)){
            Row titleRow = sheet.getRow(rownum++);
            Cell titleCell = titleRow.getCell(0);
            titleCell.setCellValue(title);
        }
    }

    /**
     * 创建基本信息行
     * @param baseInfoList 基本信息的List
     */
    public void createBaseInfoRow(List<String> baseInfoList) {
        Row secondRow = sheet.getRow(rownum);
        int[] columnArray = new int[]{2, 7, 12, 19, 24};
        for (int i = 0; i < baseInfoList.size(); i++) {
            Cell cell = secondRow.getCell(columnArray[i]);
            cell.setCellValue(baseInfoList.get(i));
        }
        // 定位到起始数据的数据行
        rownum = 5;
        // sheet.shiftRows(sheet.getLastRowNum() - 5, sheet.getLastRowNum(), 1);
        // sheet.getRow(sheet.getLastRowNum()).setRowStyle(rowStyle);
    }
    /**
     * 添加数据（通过annotation.ExportField添加数据）
     */
    public <E> void setDataList(Class<?> cls, List<E> list){
        setDataList(cls, list, ExcelField.Type.EXPORT);
    }

    /**
     * 添加数据（通过annotation.ExportField添加数据）
     * @return list 数据列表
     */
    public <E> MyExcelUtils setDataList(Class<?> cls, List<E> list, ExcelField.Type type, String... groups){
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

        for (E e : list){
            int colunm = 0;
            colunm_num = 0; // su
            Row row = this.addRow();
            // CellStyle rowStyle = sheet.getRow(5).getRowStyle();
            // row.setRowStyle(rowStyle);
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
                break;
            }
            log.debug("Write success: ["+row.getRowNum()+"] "+sb.toString());
        }
        // 重置styles 2022年5月4日 su添加
        // this.styles = createStyles(wb);
        return this;
    }

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
                /*style = styles.get("data_column_"+column);
                if (style == null){
                    style = wb.createCellStyle();
                    style.cloneStyleFrom(styles.get("data"+(align.value()>=1&&align.value()<=3?align.value():"")));
                    if (dataFormat != null){
                        defaultDataFormat = dataFormat;
                    }
                    style.setDataFormat(wb.createDataFormat().getFormat(defaultDataFormat));
                    styles.put("data_column_" + column, style);
                }
                cell.setCellStyle(style);*/
            } catch (Exception ex) {
                log.info("Set cell value ["+row.getRowNum()+","+column+"] error: " + ex.toString());
                cell.setCellValue(ObjectUtils.toString(val));
            }
            return cell;
        }




    /**
     * 添加一行
     * @return 行对象
     */
    public Row addRow(){
        return sheet.createRow(rownum++);
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
     * 输出数据流
     * @param os 输出数据流
     */
    public MyExcelUtils write(OutputStream os){
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
    public MyExcelUtils write(HttpServletResponse response, String fileName){
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

    @Override
    public void close() throws IOException {
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
