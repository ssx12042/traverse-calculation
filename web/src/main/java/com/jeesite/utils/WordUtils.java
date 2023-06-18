package com.jeesite.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: ssx
 * @Date: 2022/4/11
 * @Description:
 */
public class WordUtils {

    // 输出文件
    public static final String DEST = "./web/target/report.doc";
    // 字体
    private static final String FONT = "./web/src/main/resources/static/font/MiSans/MiSans-Normal.ttf";
    // 模板文件夹
    private static final String TMPFOLDER = "./web/src/main/resources/views/template/";
    // 模板文件
    public static final String XML = "template.xml";

    private static final Configuration freemarkerCfg;

    static {
        freemarkerCfg = new Configuration(Configuration.getVersion());
        try {
            // 设置freemarker的模板加载路径
            freemarkerCfg.setDirectoryForTemplateLoading(new File(TMPFOLDER));
            // 设置默认编码
            freemarkerCfg.setDefaultEncoding("UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createWord(Map<String, Object> data, String xmlTmp, String dest) {
        try {
            // 输出文件及名称
            File outFile = new File(dest);
            // 以utf-8的编码读取模板文件
            Template template = freemarkerCfg.getTemplate(xmlTmp, "UTF-8");
            // 构造一个向目标文件写的流
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), StandardCharsets.UTF_8),10240);
            // 将数据填充到模板并写入目标文件
            template.process(data, out);
            // 写完关闭流
            out.close();
            System.out.println("生成Word成功！");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("生成Word失败！");
        }
    }

    public static void main(String[] args) {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "张三");
        data.put("age", 20);
        data.put("sex", "男");
        // 输入到word
        createWord(data, XML, DEST);
    }
}
