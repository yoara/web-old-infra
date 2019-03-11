package org.yoara.framework.component.web.common.util;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * http控制相关工具类
 * Created by yoara on 2016/3/3.
 */
public class HttpClientUtil {
    private static Logger log = LoggerFactory.getLogger(HttpClientUtil.class);

    public static String get(String uri) {
        log.info("HttpClientUtil getting uri:" + uri);
        HttpGet httpGet = new HttpGet(uri);
        return execute(httpGet,false);
    }
    
    public static String get(String uri, String contentType) {
        log.info("HttpClientUtil getting uri:" + uri);
        HttpGet httpGet = new HttpGet(uri);
        httpGet.addHeader("Content-Type", contentType);
        return execute(httpGet,false);
    }

    public static String post(String uri, Map<String, Object> params,boolean isSSL) {
        log.info("HttpClientUtil posting uri:" + uri);
        HttpPost httppost = new HttpPost(uri);
        List<NameValuePair> nvps = new ArrayList<>();
        for (Entry<String, Object> entry : params.entrySet()) {
            Object value = entry.getValue();
            nvps.add(new BasicNameValuePair(entry.getKey(), value != null ? value.toString() : null));
        }
        httppost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
        return execute(httppost,isSSL);
    }

    public static String post(String uri, Map<String, Object> params) {
        return post(uri,params,false);

    }

    private static String execute(HttpUriRequest request,boolean isSSL) {
        String out = null;
        HttpClient client = null;
        try {
            client = isSSL?new SSLClient():new DefaultHttpClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpResponse rsp;
        int status = 0;
        try {
            client.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
            client.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
            rsp = client.execute(request);
            status = rsp.getStatusLine().getStatusCode();
            log.info("HttpClientUtil got status:" + status);
            if (status == 200) {
                HttpEntity entity = rsp.getEntity();
                out = EntityUtils.toString(entity);
                log.info("HttpClientUtil got result:" + out);
            }
        } catch (ClientProtocolException e) {
            log.error("HttpClientUtil found ClientProtocolException:" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            log.error("HttpClientUtil found IOException:" + e.getMessage());
            e.printStackTrace();
        }finally {
            client.getConnectionManager().shutdown();
        }
        return out;
    }
    
    public static byte[] getBytes(String url) throws ClientProtocolException, IOException {
    	HttpGet httpGet = new HttpGet(url);
    	
        byte[] out = null;
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        int status = 0;
        try {
        	response = client.execute(httpGet);
            status = response.getStatusLine().getStatusCode();
            if (status == 200) {
                HttpEntity entity = response.getEntity();
                out = EntityUtils.toByteArray(entity);
            }
        }finally {
            client.getConnectionManager().shutdown();
        }
        return out;
    }

    static class SSLClient extends DefaultHttpClient{
        public SSLClient() throws Exception{
            super();
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }
                @Override
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = this.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", 443, ssf));
        }
    }
}
