package aop;

/**
 * @author Xuanyi Li
 */
public class TargetSource {

    private Class<?> targetClazz;

    private Class<?>[] interfaces;

    private Object target;

    public TargetSource(Object target, Class<?> targetClazz, Class<?>... interfaces) {
        this.targetClazz = targetClazz;
        this.interfaces = interfaces;
        this.target = target;
    }

    public Class<?> getTargetClazz() {
        return targetClazz;
    }

    public Class<?>[] getInterfaces() {
        return interfaces;
    }

    public Object getTarget() {
        return target;
    }
}
