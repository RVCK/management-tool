package com.faceit.usermanagementtool.mapper;

import com.faceit.usermanagementtool.model.User;
import com.faceit.usermanagementtool.openapi.model.FilteredUsersResponse;
import com.faceit.usermanagementtool.openapi.model.UserRequest;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    
    User toUser(UserRequest userRequest);

    List<FilteredUsersResponse> toFilteredUsers(List<User> users);
}
