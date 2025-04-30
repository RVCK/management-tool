package com.faceit.usermanagementtool.service.impl;

import com.faceit.usermanagementtool.mapper.UserMapper;
import com.faceit.usermanagementtool.mapper.UserMapperImpl;
import com.faceit.usermanagementtool.model.User;
import com.faceit.usermanagementtool.openapi.model.FilteredUsersResponse;
import com.faceit.usermanagementtool.openapi.model.UserRequest;
import com.faceit.usermanagementtool.repository.UserRepository;
import com.faceit.usermanagementtool.service.ManagementToolClientFactory;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class UserServiceImpl implements ManagementToolClientFactory<UserRequest, FilteredUsersResponse> {

    @Autowired
    private UserRepository userRepository;
    
    private final UserMapper userMapper = new UserMapperImpl();

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public boolean creation(UserRequest userRequest) {
        return this.userRepository.userCreation(this.userMapper.toUserRequest(userRequest));
    }

    @Override
    public List<FilteredUsersResponse> read(Map<String, String> filters) {
        return this.userMapper.toFilteredUsers(this.userRepository.findByFilters(filters));
    }

    @Override
    @Transactional
    public boolean delete(String id) {
        return this.userRepository.removeUserById(id);
    }

    @Override
    @Transactional
    public boolean update(String id, UserRequest userUpdateRequest) {
        Boolean userExistsAndIsUpdated = Boolean.FALSE;
        List<User> userList = this.userRepository.findByFilters(Map.of("id",id));
        if(!userList.isEmpty()){
            userExistsAndIsUpdated = this.userRepository.updateUser(id, this.userMapper.toUpdateUserRequest(userUpdateRequest, userList.get(0).createdAt()));
        }
        return userExistsAndIsUpdated;
    }

    public Map<String, String> getUserFilterMap(String id, String firstName, String lastName, String nickname, String email, String country) {
        final Map<String, String> userFilters = Map.of("id", Objects.nonNull(id) ? id : Strings.EMPTY,
                "firstName", Objects.nonNull(firstName) ? firstName : Strings.EMPTY,
                "lastName", Objects.nonNull(lastName) ? lastName : Strings.EMPTY,
                "nickname", Objects.nonNull(nickname) ? nickname : Strings.EMPTY,
                "email", Objects.nonNull(email) ? email : Strings.EMPTY,
                "country", Objects.nonNull(country) ? country : Strings.EMPTY);
        return userFilters;
    }
}
