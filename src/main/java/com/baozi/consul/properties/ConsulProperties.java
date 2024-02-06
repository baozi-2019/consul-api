package com.baozi.consul.properties;

public class ConsulProperties {
    private String host = "127.0.0.1";
    private int port = 8500;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
