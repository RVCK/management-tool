package com.faceit.usermanagementtool.repository;

import com.faceit.usermanagementtool.model.User;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository {

    List<User> findByLastName(@Param("name") String name);

    boolean removeUserById(String id);

    boolean userCreation(User user);

    boolean updateUser(String id, User user);

    List<User> findByFilters(User user);
}
