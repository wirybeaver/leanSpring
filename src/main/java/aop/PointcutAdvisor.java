package aop;

/**
 * @author Xuanyi Li
 */
public interface PointcutAdvisor extends Advisor{
    Pointcut getPointCut();
}
