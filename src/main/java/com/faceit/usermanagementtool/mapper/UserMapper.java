package com.faceit.usermanagementtool.mapper;

import com.faceit.usermanagementtool.model.User;
import com.faceit.usermanagementtool.openapi.model.FilteredUsersResponse;
import io.grpc.managementtool.UsersReadResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface UserMapper {

    //GRPC Protobuf mappers:
    @Mapping(target = "password", expression = "java(com.faceit.usermanagementtool.util.Util.cryptSensibleData(userRequest.getPassword()))")
    User toUserGrpc(io.grpc.managementtool.UserRequest userRequest);

    @Mapping(target = "password", expression = "java(com.faceit.usermanagementtool.util.Util.cryptSensibleData(userRequest.getPassword()))")
    io.grpc.managementtool.UserRequest toGrpcUserRequest(com.faceit.usermanagementtool.openapi.model.UserRequest userRequest);

    List<UsersReadResponse> toGrpcUserResponse(List<User> users);
    
    //REST mappers:
    List<FilteredUsersResponse> toFilteredUsers(List<User> users);
    
    @Mapping(target="updatedAt", expression = "java(java.time.LocalDateTime.now().toString())")
    @Mapping(target="createdAt", expression = "java(java.time.LocalDateTime.now().toString())")
    @Mapping(target = "password", expression = "java(com.faceit.usermanagementtool.util.Util.cryptSensibleData(userRequest.getPassword()))")
    User toUserRequest(com.faceit.usermanagementtool.openapi.model.UserRequest userRequest);

    @Mapping(target="updatedAt", expression = "java(java.time.LocalDateTime.now().toString())")
    @Mapping(target="createdAt", source = "creationDate")
    User toUpdateUserRequest(com.faceit.usermanagementtool.openapi.model.UserRequest updateRequest, String creationDate);
}
