package com.faceit.usermanagementtool.service.impl;

import com.faceit.usermanagementtool.mapper.UserMapper;
import com.faceit.usermanagementtool.model.User;
import com.faceit.usermanagementtool.openapi.model.FilteredUsersResponse;
import com.faceit.usermanagementtool.openapi.model.UserRequest;
import com.faceit.usermanagementtool.repository.UserRepository;
import com.faceit.usermanagementtool.service.ManagementToolClientFactory;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements ManagementToolClientFactory<UserRequest, FilteredUsersResponse> {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public boolean creation(UserRequest userRequest) {
        return this.userRepository.userCreation(this.userMapper.toUser(userRequest));
    }

    @Override
    public List<FilteredUsersResponse> read(HashMap<String, String> filters) {
        String a ="";
        return null;
                //this.userMapper.toFilteredUsers(this.userRepository.findByFilters(new User(id, firstName, lastName, nickname, Strings.EMPTY, email, country)));
    }

    @Override
    @Transactional
    public boolean delete(String id) {
        return this.userRepository.removeUserById(id);
    }

    @Override
    @Transactional
    public boolean update(String id, UserRequest userRequest) {
        return this.userRepository.updateUser(id, this.userMapper.toUser(userRequest));
    }
}
