package com.baozi.consul.clients;

import com.baozi.consul.bean.health.HealthService;
import com.baozi.consul.exception.ConsulClientException;

import java.util.List;

public interface HealthClient {
    // 列举服务实例
    List<HealthService> listServiceInstance(String serviceName, boolean onlyHealth) throws ConsulClientException;
}
