package com.baozi.consul.bean.kv;

import com.alibaba.fastjson2.annotation.JSONField;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class KVStore {
    @JSONField(name = "CreateIndex")
    private Integer createIndex;
    @JSONField(name = "ModifyIndex")
    private Integer modifyIndex;
    @JSONField(name = "LockIndex")
    private Integer lockIndex;
    @JSONField(name = "Key")
    private String key;
    @JSONField(name = "Flags")
    private Integer flags;
    @JSONField(name = "Value")
    private String value;
    @JSONField(name = "Session")
    private String session;

    public Integer getCreateIndex() {
        return createIndex;
    }

    public void setCreateIndex(Integer createIndex) {
        this.createIndex = createIndex;
    }

    public Integer getModifyIndex() {
        return modifyIndex;
    }

    public void setModifyIndex(Integer modifyIndex) {
        this.modifyIndex = modifyIndex;
    }

    public Integer getLockIndex() {
        return lockIndex;
    }

    public void setLockIndex(Integer lockIndex) {
        this.lockIndex = lockIndex;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getFlags() {
        return flags;
    }

    public void setFlags(Integer flags) {
        this.flags = flags;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    // 解码value
    public String decodeValue() {
        return new String(Base64.getDecoder().decode(this.value), StandardCharsets.UTF_8);
    }
}
