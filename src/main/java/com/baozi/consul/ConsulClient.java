package com.baozi.consul;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.baozi.consul.bean.health.HealthService;
import com.baozi.consul.bean.kv.KVStore;
import com.baozi.consul.bean.service.NewService;
import com.baozi.consul.clients.CatalogClient;
import com.baozi.consul.clients.HealthClient;
import com.baozi.consul.clients.KVStoreClient;
import com.baozi.consul.clients.ServiceClient;
import com.baozi.consul.exception.ConsulClientException;
import com.baozi.consul.properties.ConsulProperties;
import com.baozi.consul.properties.HttpClientProperties;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
import org.apache.hc.core5.pool.PoolReusePolicy;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ConsulClient implements CatalogClient, HealthClient, ServiceClient, KVStoreClient {
    private final CloseableHttpClient httpClient;
    private final HttpHost consulHost;

    public ConsulClient(HttpClientProperties httpClientProperties, ConsulProperties consulProperties)
            throws URISyntaxException {
        HttpClientProperties.DefaultSocketProperties defaultSocket = httpClientProperties.getDefaultSocket();
        HttpClientProperties.RequestProperties requestProperties = httpClientProperties.getRequest();

        PoolingHttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
                .setDefaultSocketConfig(SocketConfig.custom()
                        .setSoTimeout(Timeout.of(defaultSocket.getSoTimeout().toMillis(), TimeUnit.MILLISECONDS))
                        .build())
                .setMaxConnTotal(httpClientProperties.getMaxConnTotal())
                .setMaxConnPerRoute(httpClientProperties.getMaxConnPerRoute())
                .setPoolConcurrencyPolicy(PoolConcurrencyPolicy.LAX)
                .setConnPoolPolicy(PoolReusePolicy.LIFO)
                .setConnectionTimeToLive(TimeValue.of(httpClientProperties.getConnectTimeToLive().toMillis(), TimeUnit.MILLISECONDS))
//                .setDefaultConnectionConfig(ConnectionConfig.custom()
//                        .setSocketTimeout(Timeout.of(defaultConnection.getSocketTimeout().toMillis(), TimeUnit.MILLISECONDS))
//                        .setConnectTimeout(Timeout.of(defaultConnection.getConnectTimeout().toMillis(), TimeUnit.MILLISECONDS))
//                        .setTimeToLive(TimeValue.of(defaultConnection.getTimeToLive().toMillis(), TimeUnit.MILLISECONDS))
//                        .build())
                .build();

        connectionManager.closeIdle(TimeValue.of(httpClientProperties.getCloseIdle().toMillis(), TimeUnit.MILLISECONDS));

        this.httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectionRequestTimeout(Timeout.of(requestProperties.getConnectionRequestTimeout().toMillis(), TimeUnit.MILLISECONDS))
                        .setConnectionKeepAlive(TimeValue.of(requestProperties.getConnectionKeepAlive().toMillis(), TimeUnit.MILLISECONDS))
                        .build())
                .build();
        this.consulHost = HttpHost.create("http://" + consulProperties.getHost() + ":" + consulProperties.getPort());
    }

    @Override
    public boolean registerService(NewService newService) throws ConsulClientException {
        try {
            return this.httpClient.execute(consulHost, ClassicRequestBuilder.put("/v1/agent/service/register")
                    .setEntity(JSON.toJSONString(newService), ContentType.APPLICATION_JSON).build(),
                    response -> response.getCode() == 200);
        } catch (IOException e) {
            throw new ConsulClientException("注册服务失败", e);
        }
    }

    @Override
    public boolean deregisterService(String serviceId) throws ConsulClientException {
        try {
            return this.httpClient.execute(consulHost,
                    ClassicRequestBuilder.put("/v1/agent/service/deregister/" + serviceId).build(),
                    response -> response.getCode() == 200);
        } catch (IOException e) {
            throw new ConsulClientException("服务注销失败", e);
        }
    }

    @Override
    public List<HealthService> listServiceInstance(String serviceName, boolean onlyHealth) throws ConsulClientException {
        try {
            return this.httpClient.execute(consulHost,
                    ClassicRequestBuilder.get("/v1/health/service/" + serviceName)
                            .addParameter("passing", String.valueOf(onlyHealth)).build(),
                    response -> {
                        HttpEntity entity = response.getEntity();
                        try (InputStream inputStream = entity.getContent()) {
                            JSONArray objects = JSON.parseArray(inputStream);
                            List<HealthService> healthServiceList = new ArrayList<>(objects.size());
                            for (int i = 0; i < objects.size(); i++) {
                                healthServiceList.add(objects.getObject(i, HealthService.class));
                            }
                            return healthServiceList;
                        }
                    }
            );
        } catch (IOException e) {
            throw new ConsulClientException("获取服务实例列表失败", e);
        }
    }

    @Override
    public List<KVStore> readKey(String key) throws ConsulClientException {
        try {
            return this.httpClient.execute(consulHost,
                    ClassicRequestBuilder.get("/v1/kv/" + key).build(),
                    response -> {
                        if (response.getCode() == HttpStatus.SC_NOT_FOUND)
                            return null;
                        HttpEntity entity = response.getEntity();
                        try (InputStream inputStream = entity.getContent()) {
                            JSONArray objects = JSON.parseArray(inputStream);
                            List<KVStore> kvStoreList = new ArrayList<>(objects.size());
                            for (int i = 0; i < objects.size(); i++) {
                                kvStoreList.add(objects.getObject(i, KVStore.class));
                            }
                            return kvStoreList;
                        }
                    }
            );
        } catch (IOException e) {
            throw new ConsulClientException("读取密钥失败", e);
        }
    }
}
