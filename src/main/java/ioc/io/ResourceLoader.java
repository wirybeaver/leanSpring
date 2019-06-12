package ioc.io;

import java.net.URL;

public class ResourceLoader {

    public Resource getResource(String location){
        // TODO: 有时间再研究
        URL resource = this.getClass().getClassLoader().getResource(location);
        return new URLResourceImpl(resource);
    }
}
