package aop;

import org.aopalliance.aop.Advice;

/**
 * @author Xuanyi Li
 */
public class AspectJExpressionPointcutAdvisor implements PointcutAdvisor{

    private AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();

    private Advice advice;

    private void setExpression(String expression){
        pointcut.setExpression(expression);
    }

    public void setAdvice(Advice advice) {
        this.advice = advice;
    }

    @Override
    public Pointcut getPointCut() {
        return pointcut;
    }

    @Override
    public Advice getAdvice() {
        return advice;
    }
}
