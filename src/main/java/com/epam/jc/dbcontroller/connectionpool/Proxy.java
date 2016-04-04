package com.epam.jc.dbcontroller.connectionpool;

@FunctionalInterface
public interface Proxy<T> {
    @Private
    T toSrc();
}
