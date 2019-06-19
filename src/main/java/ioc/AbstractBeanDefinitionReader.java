package ioc;

import aop.AbstractAopProxy;

import java.util.*;

public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader{

    protected BeanDefinitionRegistry registry;

    public AbstractBeanDefinitionReader(BeanDefinitionRegistry registry){
        this.registry = registry;
    }

}
