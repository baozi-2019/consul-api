package com.baozi.consul.clients;

import com.baozi.consul.bean.kv.KVStore;
import com.baozi.consul.exception.ConsulClientException;

public interface KVStoreClient {
    KVStore readKey(String key) throws ConsulClientException;
}
