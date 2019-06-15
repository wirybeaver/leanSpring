package ioc.factory;

import ioc.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLBeanFactory implements BeanFactory {

    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
    private List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    // 增加beanDefinitionMap的并发性
    private List<String> beanDefinitionIDs = new ArrayList<>();

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

    private Object initializeBean(Object bean, String beanID) throws Exception{
        for(BeanPostProcessor beanPostProcessor : beanPostProcessors){
            beanPostProcessor.postProcessBeforeInitialization(bean, beanID);
        }
        for(BeanPostProcessor beanPostProcessor : beanPostProcessors){
            beanPostProcessor.postProcessAfterInitialization(bean, beanID);
        }
        return bean;
    }

    public XMLBeanFactory(String location) throws Exception{
        XMLBeanDefinitionReader beanDefinitionReader = new XMLBeanDefinitionReader();
        beanDefinitionReader.loadBeanDefinitions(location);
        for(BeanDefinition bd : beanDefinitionReader.getRegistry()){
            beanDefinitionIDs.add(bd.getID());
            beanDefinitionMap.put(bd.getID(), bd);
        }

        registerBeanPostProcessors();
    }

    private void registerBeanPostProcessors() throws Exception{
        // 保证顺序, 所以不能用keySet()
        for(String ID : beanDefinitionIDs){
            BeanDefinition bd = beanDefinitionMap.get(ID);
            if(bd.getBeanClass().isAssignableFrom(BeanPostProcessor.class)){
                // 一开始我感到疑惑, 使用getBean()在后处理器对象, getBean()里有initializeBean()
                // initializeBean()会涉及后处理器对象列表, 但是现在还没有形成一个完整的列表啊
                // 猜测: 某个后处理对象也有可能需要另外一个后处理对象做加工
                // 所以XMLBeanDefinition Reader应该返回一个列表, 而不是一个HashMap
                beanPostProcessors.add((BeanPostProcessor) getBean(ID));
            }
        }
    }


    private Object createBean(BeanDefinition beanDefinition) throws Exception{
        Class<?> clazz = beanDefinition.getBeanClass();
        Object bean = clazz.getConstructor().newInstance();
        applyPropertyValues(bean, beanDefinition);
        return bean;
    }

    private void applyPropertyValues(Object bean, BeanDefinition bd) throws Exception{
        if(bean instanceof BeanFactoryAware){
            ((BeanFactoryAware) bean).setBeanFactory(this);
        }
        for(PropertyValue propertyValue : bd.getPropertyValues().getPropertyValues()){
            String name = propertyValue.getName();
            Object val = propertyValue.getValue();
            if(val instanceof BeanReference){
                val = getBean(((BeanReference)val).getName());
            }
            try{
                Method method = bean.getClass().getDeclaredMethod("set"+name.substring(0,1).toUpperCase()
                        +name.substring(1), val.getClass());
                method.setAccessible(true);
                method.invoke(bean, val);
            }
            catch (NoSuchMethodException e){
                Field field = bean.getClass().getDeclaredField("name");
                field.setAccessible(true);
                field.set(bean, val);
            }
        }
    }


}
