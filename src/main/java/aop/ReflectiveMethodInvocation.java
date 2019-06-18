package aop;

import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

/**
 * @author Xuanyi Li
 */
public class ReflectiveMethodInvocation implements MethodInvocation{

    private Method method;

    private Object[] arguments;

    private Object target;

    public ReflectiveMethodInvocation(Method method, Object[] arguments, Object target) {
        this.method = method;
        this.arguments = arguments;
        this.target = target;
    }

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }

    @Override
    public Object proceed() throws Throwable {
        return method.invoke(target, arguments);
    }

    @Override
    public Object getThis() {
        return target;
    }

    @Override
    public AccessibleObject getStaticPart() {
        return method;
    }
}
