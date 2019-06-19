package context;

import ioc.XMLBeanDefinitionReader;
import ioc.factory.AbstractBeanFactory;
import ioc.factory.AutoWireBeanFactory;

/**
 * @author Xuanyi Li
 */
public class ClassPathXMLApplicationContext extends AbstractApplicationContext {

    private String location;

    public ClassPathXMLApplicationContext(String location) throws Exception{
        this(location, new AutoWireBeanFactory());
    }

    public ClassPathXMLApplicationContext(String location, AbstractBeanFactory beanFactory) throws Exception{
        super(beanFactory);
        this.location = location;
        refresh();
    }

    @Override
    protected void loadBeanDefinitions(AbstractBeanFactory beanFactory) throws Exception {
        XMLBeanDefinitionReader xmlBeanDefinitionReader = new XMLBeanDefinitionReader(beanFactory);
        xmlBeanDefinitionReader.loadBeanDefinitions(location);
    }

}
