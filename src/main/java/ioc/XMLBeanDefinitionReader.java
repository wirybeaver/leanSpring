package ioc;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;

public class XMLBeanDefinitionReader extends AbstractBeanDefinitionReader {

    private static final String beanTag = "bean";
    private static final String propertyTag = "property";
    private static final String nameTag = "name";
    private static final String valueTag = "value";
    private static final String refTag = "ref";
    private static final String idTag = "id";
    private static final String classTag = "class";

    public XMLBeanDefinitionReader(BeanDefinitionRegistry beanDefinitionRegistry){
        super(beanDefinitionRegistry);
    }

    @Override
    public void loadBeanDefinitions(String location) throws Exception {
        Element root = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder().parse(location).getDocumentElement();
        parseBeanDefinitions(root);
    }

    private void parseBeanDefinitions(Element root){
        NodeList beanNodes = root.getElementsByTagName(beanTag);
        for(int i=0; i<beanNodes.getLength(); i++){
            Node iter = beanNodes.item(i);
            if(iter instanceof Element){
                parseBeanDefinition((Element) iter);
            }
        }
    }

    private void parseBeanDefinition(Element beanElement) {
        String id = beanElement.getAttribute(idTag);
        String clazz = beanElement.getAttribute(classTag);
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setId(id);
        beanDefinition.setBeanClassName(clazz);
        parseProperties(beanDefinition, beanElement);
        registry.registerBeanDefinition(beanDefinition);
    }

    private void parseProperties(BeanDefinition beanDefinition, Element beanElement){
        NodeList propertyNodes = beanElement.getElementsByTagName(propertyTag);
        PropertyValues propertyValues = new PropertyValues();
        for(int i=0; i<propertyNodes.getLength(); i++){
            Element propertyElement = (Element) propertyNodes.item(i);
            String name = propertyElement.getAttribute(nameTag);
            String value = propertyElement.getAttribute(valueTag);
            String ref = propertyElement.getAttribute(refTag);
            if(emptyString(name) || emptyString(value) && emptyString(ref) ){
                throw new IllegalArgumentException("empty name or value and ref");
            }
            if(emptyString(value)){
                propertyValues.addPropertyValue(new PropertyValue(name, value));
            }
            else{
                propertyValues.addPropertyValue(new PropertyValue(name, new BeanReference(ref)));
            }
        }
        beanDefinition.setPropertyValues(propertyValues);
    }

    private boolean emptyString(String str){
        return str==null || str.isEmpty();
    }

}
