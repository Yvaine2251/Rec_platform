package com.platform.wrapper;

import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author ErrorRua
 * @date 2022/11/18
 * @description:
 */
public class RequestWrapper extends HttpServletRequestWrapper {
    private final byte[] requestBody;

    public RequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        requestBody =  StreamUtils.copyToByteArray(request.getInputStream());
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(requestBody);
        return new ServletInputStream() {
            public boolean isFinished() {
                return false;
            }
            public boolean isReady() {
                return false;
            }
            public void setReadListener(ReadListener readListener) {}
            public int read() {
                return byteArrayInputStream.read();
            }
        };

    }
    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }


    public String getRequestBody() {
        return new String(this.requestBody);
    }
}

