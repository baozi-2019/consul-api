package com.baozi.consul.properties;

import java.time.Duration;

public class HttpClientProperties {
    private Integer maxConnTotal = 1000;
    private Integer maxConnPerRoute = 100;
    private Duration connectTimeToLive = Duration.ofSeconds(5);
    private DefaultSocketProperties defaultSocket = new DefaultSocketProperties();
    private Duration closeIdle = Duration.ofSeconds(5);
    private RequestProperties request = new RequestProperties();

    public Integer getMaxConnTotal() {
        return maxConnTotal;
    }

    public void setMaxConnTotal(Integer maxConnTotal) {
        this.maxConnTotal = maxConnTotal;
    }

    public Integer getMaxConnPerRoute() {
        return maxConnPerRoute;
    }

    public void setMaxConnPerRoute(Integer maxConnPerRoute) {
        this.maxConnPerRoute = maxConnPerRoute;
    }

    public Duration getConnectTimeToLive() {
        return connectTimeToLive;
    }

    public void setConnectTimeToLive(Duration connectTimeToLive) {
        this.connectTimeToLive = connectTimeToLive;
    }

    public DefaultSocketProperties getDefaultSocket() {
        return defaultSocket;
    }

    public void setDefaultSocket(DefaultSocketProperties defaultSocket) {
        this.defaultSocket = defaultSocket;
    }

    public Duration getCloseIdle() {
        return closeIdle;
    }

    public void setCloseIdle(Duration closeIdle) {
        this.closeIdle = closeIdle;
    }

    public RequestProperties getRequest() {
        return request;
    }

    public void setRequest(RequestProperties request) {
        this.request = request;
    }

    public static class DefaultSocketProperties {
        private Duration soTimeout = Duration.ofSeconds(5);

        public Duration getSoTimeout() {
            return soTimeout;
        }

        public void setSoTimeout(Duration soTimeout) {
            this.soTimeout = soTimeout;
        }
    }

    public static class RequestProperties {
        private Duration connectionRequestTimeout = Duration.ofSeconds(5);
        private Duration connectionKeepAlive = Duration.ofSeconds(5);

        public Duration getConnectionRequestTimeout() {
            return connectionRequestTimeout;
        }

        public void setConnectionRequestTimeout(Duration connectionRequestTimeout) {
            this.connectionRequestTimeout = connectionRequestTimeout;
        }

        public Duration getConnectionKeepAlive() {
            return connectionKeepAlive;
        }

        public void setConnectionKeepAlive(Duration connectionKeepAlive) {
            this.connectionKeepAlive = connectionKeepAlive;
        }
    }
}
