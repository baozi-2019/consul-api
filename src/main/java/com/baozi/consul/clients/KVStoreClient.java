package com.baozi.consul.clients;

import com.baozi.consul.bean.kv.KVStore;
import com.baozi.consul.exception.ConsulClientException;

import java.util.List;

public interface KVStoreClient {
    List<KVStore> readKey(String key) throws ConsulClientException;
}
