package simple;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.function.Consumer;

interface Fruit {
    void print();
}

class Apple implements Fruit{
    private String color;
    Apple(String color){
        this.color = color;
    }
    public void print(){
        System.out.println(color);
    }
}

interface Advice extends InvocationHandler{}

class BeforeAdvice implements Advice{

    private Object bean;
    private Consumer<Object> consumer;

    public BeforeAdvice(Object bean, Consumer<Object> consumer) {
        this.bean = bean;
        this.consumer = consumer;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        consumer.accept(bean);
        return method.invoke(bean, args);
    }
}

public class SimpleAOP {
    public static Object getProxy(Object bean, Advice advice){
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), bean.getClass().getInterfaces(), advice);
    }

    public static void main(String[] args){
        Apple apple = new Apple("blue");
        Consumer<Object> consumer = (x)-> System.out.println("method start");

        Fruit fruit = (Fruit) SimpleAOP.getProxy(apple, new BeforeAdvice(apple, consumer));

        fruit.print();
    }
}
