package aop;

import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.PointcutPrimitive;
import org.aspectj.weaver.tools.ShadowMatch;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * TODO: AspectJ
 * @author Xuanyi Li
 */
public class AspectJExpressionPointcut implements Pointcut, ClassFilter, MethodMatcher{

    private PointcutParser pointcutParser;

    private String expression;

    private PointcutExpression pointcutExpression;

    private static final Set<PointcutPrimitive> DEFAULT_SUPPORTED_PRIMITIVES = new HashSet<>();

    static {
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.EXECUTION);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.ARGS);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.REFERENCE);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.THIS);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.TARGET);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.WITHIN);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ANNOTATION);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_WITHIN);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ARGS);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_TARGET);
    }

    public AspectJExpressionPointcut() {
        this(DEFAULT_SUPPORTED_PRIMITIVES);
    }

    public AspectJExpressionPointcut(Set<PointcutPrimitive> supportedPrimitives) {
        pointcutParser = PointcutParser
                .getPointcutParserSupportingSpecifiedPrimitivesAndUsingContextClassloaderForResolution(supportedPrimitives);
    }

    /**
     *
     * @author Xuanyi Li
     * @param targetClass
     * @return java.lang.Boolean
     */
    @Override
    public Boolean matches(Class targetClass) {
        checkReadyToMatches();
        return pointcutExpression.couldMatchJoinPointsInType(targetClass);
    }

    /**
     *
     * @author Xuanyi Li
     * @param method
     * @param targetClass
     * @return java.lang.Boolean
     */
    @Override
    public Boolean matches(Method method, Class targetClass) {
        checkReadyToMatches();
        ShadowMatch shadowMatch = pointcutExpression.matchesMethodExecution(method);

        // Special handling for this, target, @this, @target, @annotation
        // in Spring - we can optimize since we know we have exactly this class,
        // and there will never be matching subclass at runtime.
        // https://github.com/spring-projects/spring-framework/blob/master/spring-aop/src/main/java/org/springframework/aop/aspectj/AspectJExpressionPointcut.java
        if (shadowMatch.alwaysMatches()) {
            return true;
        }
        else if (shadowMatch.neverMatches()) {
            return false;
        }

        return false;
    }

    private void checkReadyToMatches() {
        if (getExpression() == null) {
            throw new IllegalStateException("Must set property 'expression' before attempting to match");
        }
        if (pointcutExpression == null) {
            pointcutExpression = pointcutParser.parsePointcutExpression(expression);
        }
    }

    @Override
    public ClassFilter getClassFilter() {
        return this;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return this;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getExpression() {
        return expression;
    }
}
