package com.baozi.consul.clients;

import com.baozi.consul.bean.service.NewService;
import com.baozi.consul.exception.ConsulClientException;

public interface ServiceClient {
    // 注册服务
    boolean registerService(NewService newService) throws ConsulClientException;
    // 取消注册服务
    boolean deregisterService(String serviceId) throws ConsulClientException;
}
