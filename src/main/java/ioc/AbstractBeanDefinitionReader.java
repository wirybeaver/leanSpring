package ioc;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader{
    protected final Map<String, BeanDefinition> registry = new HashMap<>();

    public Map<String, BeanDefinition> getRegistry() {
        return registry;
    }
}
