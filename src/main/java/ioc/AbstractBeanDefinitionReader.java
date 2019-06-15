package ioc;

import java.util.*;

public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader{

    protected final List<BeanDefinition> registry = new ArrayList<>();

    public List<BeanDefinition> getRegistry() {
        return registry;
    }
}
