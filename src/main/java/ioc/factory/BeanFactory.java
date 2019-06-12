package ioc.factory;

public interface BeanFactory {
    Object getBean(String beanID) throws Exception;
}
