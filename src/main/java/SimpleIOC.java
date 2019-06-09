import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;
import java.io.InputStream;
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
                Element ele = (Element) node;
                String id = ele.getAttribute("id");
                String clazzName = ele.getAttribute("class");
                Object bean = Class.forName(clazzName).getConstructor().newInstance();
            }
        }
    }

}
