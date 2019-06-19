package ioc.factory;

import ioc.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class AutoWireBeanFactory extends AbstractBeanFactory {

    final protected void applyPropertyValues(Object bean, BeanDefinition bd) throws Exception{

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
                Field field = bean.getClass().getDeclaredField(propertyValue.getName());
                field.setAccessible(true);
                field.set(bean, val);
            }
        }
    }

}
