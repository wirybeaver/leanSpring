package simple;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class SimpleIOC {

    private Map<String, Object> beanMap = new HashMap<>();

    public SimpleIOC(String location) throws Exception {
        loadBeans(location);
    }

    private void loadBeans(String location) throws Exception{
        // 加载xml配置文件
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new FileInputStream(location));
        NodeList nodes = doc.getDocumentElement().getElementsByTagName("bean");
        for(int i=0; i<nodes.getLength(); i++){
            Node node = nodes.item(i);
            if(node instanceof Element){
                loadSingleBean((Element)node);
            }
        }
    }

    private void loadSingleBean(Element bean) throws Exception{
        String id = bean.getAttribute("id");
        String clazzName = bean.getAttribute("class");
        var clazz = Class.forName(clazzName);
        Object obj = clazz.getConstructor().newInstance();

        NodeList propertyNodes = bean.getElementsByTagName("property");
        for(int i=0; i<propertyNodes.getLength(); i++){
            Node propertyNode = propertyNodes.item(i);
            if(propertyNode instanceof Element){
                Element ele = (Element)propertyNode;
                String fieldName = ele.getAttribute("name");
                String fieldValue = ele.getAttribute("value");
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                if(fieldValue !=null && !fieldValue.isEmpty()){
                    field.set(obj, fieldValue);
                }
                else{
                    String refID = ele.getAttribute("ref");
                    if(refID==null || refID.isEmpty() ){
                        throw new IllegalArgumentException("Property has no ref");
                    }
                    field.set(obj, getBean(refID));
                }
            }
        }

        registerBean(id, obj);
    }

    private Object getBean(String id){
        if(!beanMap.containsKey(id)){
            throw new IllegalArgumentException();
        }
        return beanMap.get(id);
    }

    private void registerBean(String id, Object obj){
        beanMap.put(id, obj);
    }

}
