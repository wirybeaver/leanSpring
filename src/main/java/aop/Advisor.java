package aop;

import org.aopalliance.aop.Advice;

/**
 * @author Xuanyi Li
 */
public interface Advisor {
    Advice getAdvice();
}
