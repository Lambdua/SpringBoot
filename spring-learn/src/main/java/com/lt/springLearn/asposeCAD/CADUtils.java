package com.lt.springLearn.asposeCAD;

import com.aspose.cad.Color;
import com.aspose.cad.Image;
import com.aspose.cad.ImageOptionsBase;
import com.aspose.cad.fileformats.cad.CadDrawTypeMode;
import com.aspose.cad.imageoptions.CadRasterizationOptions;
import com.aspose.cad.imageoptions.PdfOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @author liangtao
 * @Date 2020/7/8
 **/
public class CADUtils {

    public static void CADtoPDF(String filePath,String outPath){
        //1. Load the CAD file into an instance of Image.
        Image image=Image.load(filePath);
        //2. Create an instance of CadRasterizationOptions and set its mandatory properties such as PageWidth & PageHeight.
        CadRasterizationOptions options=new CadRasterizationOptions();
        //options属性详情：https://docs.aspose.com/display/cadjava/Converting+CAD+Drawings+to+PDF+and+Raster+Image+Formats
        options.setPageHeight(image.getHeight());
        options.setPageWidth(image.getWidth());
        options.setNoScaling(true);
        //设置布局名称 Gets or sets the layoutName.
        //默认就是Model布局
//        options.setLayouts(new String[]{"Model","Lauout1"});
        //设置背景色
        options.setBackgroundColor(Color.getWhite());
        //设置位置
        options.setPdfProductLocation("center");
        //设置自动布局缩放
        options.setAutomaticLayoutsScaling(true);
        options.setDrawType(CadDrawTypeMode.UseObjectColor);

        //3.Create an instance of ImageOptionsBase and set its VectorRasterizationOptions property to the instance of
        // CadRasterizationOptions created in the previous step.
        ImageOptionsBase pdfOptions=new PdfOptions(); //实现类
        pdfOptions.setVectorRasterizationOptions(options);
        File outputFile = new File(outPath);
        OutputStream stream;
        try {
            long start = System.currentTimeMillis();
            System.out.println("开始转换");
            stream = new FileOutputStream(outputFile);
            image.save(stream, pdfOptions);
            image.close();
            long end = System.currentTimeMillis();
            System.out.println("转换结束,用时"+ (end-start)/1000/60+"min");
        } catch (FileNotFoundException e) {

        }
//        image.save(outPath,pdfOptions);
    }
    public static void main(String[] args){
        String filePath="C:\\Users\\liangtao\\Desktop\\2019.06.14咸阳荣盛锦绣观邸35#楼资料图.dwg";
        String outputPath="C:\\Users\\liangtao\\Desktop\\output.pdf";
         CADUtils.CADtoPDF(filePath, outputPath);

    }
}
