package aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author Xuanyi Li
 */
final public class JdkDynamicAopProxy extends AbstractAopProxy implements InvocationHandler {

    public JdkDynamicAopProxy(AdvisedSupport advisedSupport){
        super(advisedSupport);
    }

    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(getClass().getClassLoader(), advisedSupport.getTargetSource().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MethodMatcher methodMatcher = advisedSupport.getMethodMatcher();
        Object target = advisedSupport.getTargetSource().getTarget();
        if(methodMatcher.matches(method, target.getClass())){
            return advisedSupport.getMethodInterceptor().invoke(new ReflectiveMethodInvocation(method, args, target));
        }
        else{
            return method.invoke(target, args);
        }
    }
}
