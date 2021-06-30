package com.lt.config;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author liangtao
 * @description
 * @date 2020年11月24 09:50
 **/
public class MethodCostTimeImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[]{
                MethodCostTimeProxyBeanPostProcessor.class.getName()
        };

    }
}
