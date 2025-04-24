package com.faceit.usermanagementtool.service;

import java.util.HashMap;
import java.util.List;

public interface ManagementToolClientFactory<T, R> {
    
    boolean creation(T t);

    List<R> read(HashMap<String, String> filters);

    boolean delete(String id);

    boolean update(String id, T t);
}
