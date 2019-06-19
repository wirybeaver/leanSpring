package ioc.factory;

import ioc.BeanDefinition;
import ioc.BeanDefinitionRegistry;
import ioc.BeanPostProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Xuanyi Li
 */
public abstract class AbstractBeanFactory implements BeanFactory, BeanDefinitionRegistry {

    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    private List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    private final List<String> beanDefinitionIDs = new ArrayList<>();

    @Override
    public void registerBeanDefinition(BeanDefinition beanDefinition){
        String id = beanDefinition.getID();
        beanDefinitionMap.put(id, beanDefinition);
        beanDefinitionIDs.add(id);
    }

    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor){
        beanPostProcessors.add(beanPostProcessor);
    }

    public void preInstantiateSingletons() throws Exception{
        for(String id : beanDefinitionIDs){
            getBean(id);
        }
    }


    @Override
    public Object getBean(String beanID) throws Exception {
        //
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanID);
        if(beanDefinition==null){
            throw new IllegalArgumentException("No such beanID " + beanID);
        }

        Object bean = beanDefinition.getBean();
        if(bean==null){
            bean = createBean(beanDefinition);
            bean = initializeBean(bean, beanID);
            beanDefinition.setBean(bean);
        }
        return bean;
    }

    public List<Object> getBeansForType(Class<?> clazz){
        List<Object> beans = new ArrayList<>();
        for(String ID : beanDefinitionIDs){
            BeanDefinition bd = beanDefinitionMap.get(ID);
            if(bd.getBeanClass().isAssignableFrom(clazz)){
                beans.add(bd.getBean());
            }
        }
        return beans;
    }

    private Object createBean(BeanDefinition beanDefinition) throws Exception{
        Class<?> clazz = beanDefinition.getBeanClass();
        Object bean = clazz.getConstructor().newInstance();
        if(bean instanceof BeanFactoryAware){
            ((BeanFactoryAware) bean).setBeanFactory(this);
        }
        applyPropertyValues(bean, beanDefinition);
        return bean;
    }

    private Object initializeBean(Object bean, String beanID) throws Exception{
        for(BeanPostProcessor beanPostProcessor : beanPostProcessors){
            bean = beanPostProcessor.postProcessBeforeInitialization(bean, beanID);
        }
        for(BeanPostProcessor beanPostProcessor : beanPostProcessors){
            bean = beanPostProcessor.postProcessAfterInitialization(bean, beanID);
        }
        return bean;
    }

    protected abstract void applyPropertyValues(Object bean, BeanDefinition bd) throws Exception;
}
