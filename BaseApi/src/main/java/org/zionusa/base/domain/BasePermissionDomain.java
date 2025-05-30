package org.zionusa.base.domain;

public interface BasePermissionDomain<T> extends BaseDomain<T> {
    String getName();

    void setName(String name);
}
