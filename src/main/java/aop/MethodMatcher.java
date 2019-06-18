package aop;

import java.lang.reflect.Method;

/**
 * @author Xuanyi Li
 */
public interface MethodMatcher {

    Boolean matches(Method method, Class<?> beanClazz);
}
