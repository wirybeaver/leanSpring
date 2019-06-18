package aop;


/**
 * @author Xuanyi Li
 */
public interface Pointcut {
    MethodMatcher getMethodMatcher();

    ClassFilter getClassFilter();
}
