package aop;

import ioc.BeanPostProcessor;
import ioc.factory.AbstractBeanFactory;
import ioc.factory.BeanFactory;
import ioc.factory.BeanFactoryAware;
import org.aopalliance.intercept.MethodInterceptor;

import java.util.List;

/**
 * @author Xuanyi Li
 */
public class AwareBeanPostProcessor implements BeanFactoryAware, BeanPostProcessor {

    private AbstractBeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        // TODO 不加MethodInterceptor为什么会死循环
        if(bean instanceof AspectJExpressionPointcutAdvisor || bean instanceof MethodInterceptor){
            return bean;
        }

        List<Object> aspects = beanFactory.getBeansForType(AspectJExpressionPointcutAdvisor.class);
        for(Object obj : aspects){
            AspectJExpressionPointcutAdvisor aspect = (AspectJExpressionPointcutAdvisor) obj;
            if(aspect.getPointCut().getClassFilter().matches(bean.getClass())){
                ProxyFactory proxyFactory = new ProxyFactory();
                proxyFactory.setMethodMatcher(aspect.getPointCut().getMethodMatcher());
                proxyFactory.setMethodInterceptor((MethodInterceptor) aspect.getAdvice());
                proxyFactory.setTargetSource(new TargetSource(bean, bean.getClass(), bean.getClass().getInterfaces()));
                // TODO: bean = proxyFactory.getProxy()
                return proxyFactory.getProxy();
            }
        }

        return bean;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws Exception {
        this.beanFactory = (AbstractBeanFactory) beanFactory;
    }
}
