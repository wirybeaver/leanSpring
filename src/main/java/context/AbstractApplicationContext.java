package context;

import ioc.BeanPostProcessor;
import ioc.factory.AbstractBeanFactory;

/**
 * @author Xuanyi Li
 */
public abstract class AbstractApplicationContext implements ApplicationContext {

    private AbstractBeanFactory beanFactory;

    public AbstractApplicationContext(AbstractBeanFactory beanFactory){
        this.beanFactory = beanFactory;
    }

    public void refresh() throws Exception{
        loadBeanDefinitions(beanFactory);
        registerBeanPostProcessor();
        onRefresh();
    }

    public void registerBeanPostProcessor(){
        for(Object obj : beanFactory.getBeansForType(BeanPostProcessor.class)){
            BeanPostProcessor beanPostProcessor = (BeanPostProcessor) obj;
            beanFactory.addBeanPostProcessor(beanPostProcessor);
        }
    }

    protected abstract void loadBeanDefinitions(AbstractBeanFactory beanFactory) throws Exception;

    // 默认的一个hook方法
    protected void onRefresh() throws Exception{
        beanFactory.preInstantiateSingletons();
    }

    @Override
    public Object getBean(String beanID) throws Exception {
        return beanFactory.getBean(beanID);
    }
}
