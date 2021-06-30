package com.lt.config;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author liangtao
 * @description 这里可以通过 {@link org.springframework.context.annotation.Import}
 * 导入该实现类，的方式进行配置，我们直接通过导入 {@link MethodCostTimeProxyBeanPostProcessor}也可以
 * ，具体见Import注解文档说明
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
