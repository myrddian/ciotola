package ciotola.core.annotation;

import ciotola.core.InjectionType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ServiceInjection {
    String name() default "";
    InjectionType injectType() default InjectionType.MATCH_TYPE;
}
