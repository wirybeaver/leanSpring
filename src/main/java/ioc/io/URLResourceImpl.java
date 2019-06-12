package ioc.io;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class URLResourceImpl implements Resource{
    private final URL url;

    public URLResourceImpl(URL url) {
        this.url = url;
    }

    @Override
    public InputStream getInputStream() throws Exception {
        URLConnection urlConnection = url.openConnection();
        urlConnection.connect();
        return urlConnection.getInputStream();
    }
}
