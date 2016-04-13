package com.epam.jc.DbController.ConnectionPool;

@FunctionalInterface
public interface Proxy<T> {
    @Private
    T toSrc();
}
