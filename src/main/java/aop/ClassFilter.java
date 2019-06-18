package aop;

/**
 * @author Xuanyi Li
 */
public interface ClassFilter {

    Boolean matches(Class<?> clazz) throws Exception;
}
