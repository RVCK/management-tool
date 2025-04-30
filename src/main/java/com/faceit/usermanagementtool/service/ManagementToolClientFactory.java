package com.faceit.usermanagementtool.service;

import java.util.List;
import java.util.Map;

public interface ManagementToolClientFactory<T, R> {
    
    boolean creation(T t);

    List<R> read(Map<String, String> filters);

    boolean delete(String id);

    boolean update(String id, T t);
}
