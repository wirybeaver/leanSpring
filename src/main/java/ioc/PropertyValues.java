package ioc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by code4wt on 17/8/3.
 */
public class PropertyValues {

    private final List<PropertyValue> propertyValueList = new ArrayList<>();

    public void addPropertyValue(PropertyValue pv) {
        // TODO: 去重处理封装, 直接用List暴露给client使用就是面向过程
        this.propertyValueList.add(pv);
    }

    public List<PropertyValue> getPropertyValues() {
        return this.propertyValueList;
    }

}