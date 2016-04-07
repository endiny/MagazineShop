package com.epam.jc.dbcontroller.ConnectionPool;

@FunctionalInterface
public interface Proxy<T> {
    @Private
    T toSrc();
}
