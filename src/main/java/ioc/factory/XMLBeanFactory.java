package ioc.factory;

import ioc.BeanDefinition;

import java.util.HashMap;
import java.util.Map;

public class XMLBeanFactory implements BeanFactory {

    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    @Override
    public Object getBean(String beanID) throws Exception {
        return null;
    }
}
