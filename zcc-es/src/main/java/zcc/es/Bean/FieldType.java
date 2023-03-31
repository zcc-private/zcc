package zcc.es.Bean;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldType {
    String type() default "string";

    String analyzer() default "not_analyzed";

    String searchAnalyze() default "";

    boolean store() default true;

    String format() default "yyyy-MM-dd HH:mm:ss";
}
