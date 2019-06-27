# leanSpring
A succinct IOC & AOP container. Mainly refer to the github project tiny-Spring. Here are two majority difference.  
1. explore the `BeanRegistry` interface to let `BeanDefinitionReader` add `BeanDefinition` into `BeanFactory`'s concurrent hash map.
2. remove the `io` package and simply the resource as a file location string.

TODD:  
[] Add UML and Sequence Diagram

Reference:
1. [tiny-Spring](https://github.com/code4craft/tiny-spring)
2. [toySpring](https://github.com/code4wt/toy-spring)
