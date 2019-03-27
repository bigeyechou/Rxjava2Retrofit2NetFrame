package com.zhenggzh.dream.retrofitandrxjavademo.bean;

public class BaseRequestBean<T> {
    private String version = "v1";
    private T obj;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
