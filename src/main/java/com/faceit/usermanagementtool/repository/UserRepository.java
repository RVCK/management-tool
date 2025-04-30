package com.faceit.usermanagementtool.repository;

import com.faceit.usermanagementtool.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserRepository {
    boolean removeUserById(String id);

    boolean userCreation(User user);

    boolean updateUser(String id, User user);

    List<User> findByFilters(Map<String, String> filters);
}
