package com.lt.springLearn.asposeCAD;

import com.aspose.cad.imageoptions.CadRasterizationOptions;
import com.aspose.cad.imageoptions.CadRenderResult;
import com.aspose.cad.imageoptions.RenderResult;

/**
 * @author liangtao
 * @Date 2020/7/8
 **/
public class ErrorHandler extends CadRasterizationOptions.CadRenderHandler {
    @Override
    public void invoke(CadRenderResult cadRenderResult) {
        System.out.println("Tracking results of exporting");

        if (cadRenderResult.isRenderComplete())
            return;

        System.out.println("Have some problems:");

        int idxError = 1;
        for (RenderResult rr : cadRenderResult.getFailures())
        {
            System.out.printf("{0}. {1}, {2}", idxError, rr.getRenderCode(), rr.getMessage());
            idxError++;
        }
    }
}
