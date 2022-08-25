package Utilities.Annotations;

import java.lang.annotation.Annotation;

public class AnnotationTool {

    public static boolean findAnnotation(Annotation type, Class cl)
    {
        Annotation[] annotations = cl.getAnnotations();
        for(Annotation a: annotations){
            //if(a instanceof type)
        }
        return true;
    }
}
