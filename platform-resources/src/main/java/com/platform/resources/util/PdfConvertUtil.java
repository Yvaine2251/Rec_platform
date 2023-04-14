package com.platform.resources.util;


import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.hslf.usermodel.*;
import org.apache.poi.xslf.usermodel.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

/**
 * @author ErrorRua
 * @date 2022/11/17
 * @description: 转换PPT为PDF工具类
 */
public final class PdfConvertUtil {


    /**
     * @description: pptxToPdf
     * @param pptIs:
     * @return java.io.InputStream
     * @author ErrorRua
     * @date 2022/11/21
     */
    public static InputStream pptToPdf(InputStream pptIs) {


        Document document = null;
        PdfWriter pdfWriter = null;


        try (HSLFSlideShow hslfSlideShow = new HSLFSlideShow(pptIs);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()
            ) {

            // 获取ppt文件页面
            Dimension dimension = hslfSlideShow.getPageSize();

            document = new Document();

//             pdfWriter实例
            pdfWriter = PdfWriter.getInstance(document, byteArrayOutputStream);

            document.open();

            PdfPTable pdfPTable = new PdfPTable(1);

            List<HSLFSlide> hslfSlideList = hslfSlideShow.getSlides();

            for (HSLFSlide hslfSlide : hslfSlideList) {
                // 设置字体, 解决中文乱码
                for (HSLFShape shape : hslfSlide.getShapes()) {
                    HSLFTextShape textShape = (HSLFTextShape) shape;

                    for (HSLFTextParagraph textParagraph : textShape.getTextParagraphs()) {
                        for (HSLFTextRun textRun : textParagraph.getTextRuns()) {
                            textRun.setFontFamily("宋体");
                        }
                    }
                }
                BufferedImage bufferedImage = new BufferedImage((int) dimension.getWidth(), (int) dimension.getHeight(), BufferedImage.TYPE_INT_RGB);

                Graphics2D graphics2d = bufferedImage.createGraphics();

                graphics2d.setPaint(Color.white);
                graphics2d.setFont(new Font("宋体", Font.PLAIN, 12));

                hslfSlide.draw(graphics2d);

                graphics2d.dispose();

                Image image = Image.getInstance(bufferedImage, null);
                image.scalePercent(50f);

                // 写入单元格
                pdfPTable.addCell(new PdfPCell(image, true));
                document.add(image);
            }

            return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (document != null) {
                document.close();
            }
            if (pdfWriter != null) {
                pdfWriter.close();
            }
        }
    }

    /**
     * @description: pptxToPdf
     * @param pptIs:
     * @return java.io.InputStream
     * @author ErrorRua
     * @date 2022/11/21
     */
    public static InputStream pptxToPdf(InputStream pptIs) {


        Document document = null;

        PdfWriter pdfWriter = null;


        try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
              XMLSlideShow slideShow = new XMLSlideShow(pptIs);) {


            Dimension dimension = slideShow.getPageSize();

            document = new Document();

            pdfWriter = PdfWriter.getInstance(document, byteArrayOutputStream);

            document.open();

            PdfPTable pdfPTable = new PdfPTable(1);

            List<XSLFSlide> slideList = slideShow.getSlides();


            for (XSLFSlide slide : slideList) {

                // 设置字体, 解决中文乱码
                for (XSLFShape shape : slide.getShapes()) {
                    XSLFTextShape textShape = (XSLFTextShape) shape;

                    for (XSLFTextParagraph textParagraph : textShape.getTextParagraphs()) {
                        for (XSLFTextRun textRun : textParagraph.getTextRuns()) {
                            textRun.setFontFamily("宋体");
                        }
                    }
                }

                BufferedImage bufferedImage = new BufferedImage((int) dimension.getWidth(), (int) dimension.getHeight(), BufferedImage.TYPE_INT_RGB);

                Graphics2D graphics2d = bufferedImage.createGraphics();

                graphics2d.setPaint(Color.white);
                graphics2d.setFont(new Font("宋体", Font.PLAIN, 12));

                slide.draw(graphics2d);

                graphics2d.dispose();

                Image image = Image.getInstance(bufferedImage, null);
                image.scalePercent(50f);

                // 写入单元格
                pdfPTable.addCell(new PdfPCell(image, true));
                document.add(image);
            }

            return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (document != null) {
                document.close();
            }
            if (pdfWriter != null) {
                pdfWriter.close();
            }
        }
    }

}
