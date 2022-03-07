package com.ableto.utilities.RetryLogic;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class AnnotationTransformer implements IAnnotationTransformer {

    public void transform(ITestAnnotation iTestAnnotation, Class testClass, Constructor constructor, Method method)
    {
        iTestAnnotation.setRetryAnalyzer(RetryAnalyzer.class);
    }
}
