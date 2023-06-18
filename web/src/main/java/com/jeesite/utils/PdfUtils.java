package com.jeesite.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: ssx
 * @Date: 2022/4/5
 * @Description:
 */
public class PdfUtils {

    // 输出文件
    private static final String DEST = "./web/target/HelloWorld.pdf";
    // 字体
    private static final String FONT = "./web/src/main/resources/static/font/MiSans/MiSans-Normal.ttf";
    // 模板文件夹
    private static final String TMPFOLDER = "./web/src/main/resources/views/template/";
    // 模板文件
    private static final String HTML = "report.html";

    private static final Configuration freemarkerCfg;

    static {
        freemarkerCfg = new Configuration(Configuration.getVersion());
        // freemarker的模板目录
        try {
            freemarkerCfg.setDirectoryForTemplateLoading(new File(TMPFOLDER));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void javaToPdf() throws FileNotFoundException, DocumentException {
        // 新建pdf文档
        Document document = new Document();
        // 实例化一个PdfWriter
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(DEST));
        // 打开文档
        document.open();
        // 设置字体
        Font misans_normal = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        // 往文档写内容
        document.add(new Paragraph("hello world, 我是小苏!", misans_normal));
        // 关闭文档
        document.close();
        writer.close();
    }

    /**
     * 生成PDF文件
     * @param content 内容
     * @param dest 输出文件路径
     * @throws IOException
     * @throws DocumentException
     */
    public static void createPdf(String content, String dest) throws IOException, DocumentException {
        // 新建pdf文档
        Document document = new Document();
        // 实例化一个PdfWriter
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        // 打开文档
        document.open();

        XMLWorkerFontProvider fontImp = new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);
        fontImp.register(FONT);
        XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(content.getBytes()),
                null, StandardCharsets.UTF_8, fontImp);

        // 关闭文档
        document.close();
    }

    /**
     * freemarker渲染html
     * @param data 模板数据
     * @param htmlTmp 模板
     * @return 字符流
     */
    public static String freemarkerRender(Map<String, Object> data, String htmlTmp) {
        Writer out = new StringWriter();
        try {
            // 获取模板,并设置编码方式
            Template template = freemarkerCfg.getTemplate(htmlTmp);
            template.setEncoding("UTF-8");
            // 合并数据模型与模板
            template.process(data, out);    // 将合并后的数据和模板写入到流中，这里使用的字符流
            out.flush();
            return out.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "小苏");
        // pdf内容
        String content = freemarkerRender(data, HTML);
        // 输入到pdf
        try {
            // javaToPdf();
            createPdf(content, DEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
