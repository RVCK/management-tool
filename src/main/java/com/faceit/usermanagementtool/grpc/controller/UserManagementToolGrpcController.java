package com.faceit.usermanagementtool.grpc.controller;

import com.faceit.usermanagementtool.mapper.UserMapper;
import com.faceit.usermanagementtool.mapper.UserMapperImpl;
import com.faceit.usermanagementtool.model.UserGrpcReadDTO;
import com.google.common.base.Optional;
import io.grpc.managementtool.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserManagementToolGrpcController {

    private static final Logger LOG = LoggerFactory.getLogger(UserManagementToolGrpcController.class);
    
    private UserMapper userMapper = new UserMapperImpl();
    
    @Autowired
    private UserServiceGrpc.UserServiceBlockingStub grpcClient;

    /**
     * POST /grpc/users/create - Create new User resource
     * @param body (required)
     * @return HTTP Status code
     */
    @PostMapping("/grpc/users/create")
    public ResponseEntity<Void> grpcUserCreation(@RequestBody final com.faceit.usermanagementtool.openapi.model.UserRequest body) {
        HttpStatus httpStatus;
        LOG.info("[GRPC-Creation] User creation request {}", body);     
        UserRequest userRequest = this.userMapper.toGrpcUserRequest(body);
        com.google.protobuf.Empty empty = this.grpcClient.userCreation(userRequest);
        if(empty.isInitialized()){
            LOG.info("[GRPC-Creation] User ID {} created in database.", body.getId());
            httpStatus = HttpStatus.CREATED;
        }else{
            LOG.info("[GRPC-Creation] User ID {} NOT created in database.", body.getId());
            httpStatus = HttpStatus.OK;
        }
        return new ResponseEntity<>(httpStatus);
    }

    /**
     * DELETE /grpc/users/delete/{id} - Delete existing User resource
     * @param id (required)
     * @return HTTP Status code
     */
    @DeleteMapping("/grpc/users/delete/{id}")
    public ResponseEntity<Void> grpcUserDelete(@PathVariable final String id) {
        HttpStatus httpStatus;
        LOG.info("[GRPC-Delete] User delete request {}", id);
        UserDeleteRequest userDeleteRequest = UserDeleteRequest.newBuilder().setId(id).build();
        com.google.protobuf.Empty empty = this.grpcClient.userDelete(userDeleteRequest);
        if(empty.isInitialized()){
            LOG.info("[GRPC-Delete] User ID {} deleted in database.", id);
            httpStatus = HttpStatus.OK;
        }else{
            LOG.info("[GRPC-Delete] User ID {} NOT found in database.", id);
            httpStatus = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(httpStatus);
    }

    /**
     * PATCH /grpc/users/update/{id} - Update full User resource (of an existing User)
     * @param id (required)
     * @param body (required)
     * @return HTTP Status code
     */
    @PatchMapping("/grpc/users/update/{id}")
    public ResponseEntity<Void> grpcUserUpdate(@PathVariable final String id,
                                               @RequestBody final com.faceit.usermanagementtool.openapi.model.UserRequest body){
        HttpStatus httpStatus;
        LOG.info("[GRPC-Update] User update id {} with new values {}", id, body);
        UserRequest userRequest = this.userMapper.toGrpcUserRequest(body);
        com.google.protobuf.Empty empty = this.grpcClient.userUpdate(userRequest);
        if(empty.isInitialized()){
            LOG.info("[GRPC-Update] User ID {} update in database.", id);
            httpStatus = HttpStatus.OK;
        }else{
            LOG.info("[GRPC-Delete] User ID {} NOT found in database.", id);
            httpStatus = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(httpStatus);
    }

    /**
     * GET /grpc/users/read - Get User resource
     * @param id
     * @param firstName
     * @param lastName
     * @param nickname
     * @param email
     * @param country
     * @return UserGrpcReadDTO first row in database
     */
    @GetMapping("/grpc/users/read")
    ResponseEntity<UserGrpcReadDTO> grpcUserRead(@RequestParam @Nullable final String id,
                                                 @RequestParam @Nullable final String firstName,
                                                 @RequestParam @Nullable final String lastName,
                                                 @RequestParam @Nullable final String nickname,
                                                 @RequestParam @Nullable final String email,
                                                 @RequestParam @Nullable final String country){
        HttpStatus httpStatus;
        UserGrpcReadDTO response = null;
        UserFilters userFilters = UserFilters.newBuilder()
                .setId(Optional.fromNullable(id).or(""))
                .setFirstName(Optional.fromNullable(firstName).or(""))
                .setLastName(Optional.fromNullable(lastName).or(""))
                .setNickname(Optional.fromNullable(nickname).or(""))
                .setEmail(Optional.fromNullable(email).or(""))
                .setCountry(Optional.fromNullable(country).or(""))
                .build();
        UsersReadResponse usersReadResponse = this.grpcClient.userRead(userFilters);
        if(usersReadResponse.isInitialized()){
            LOG.info("[GRPC-Read] User usersReadResponse {} is in database.", usersReadResponse);
            httpStatus = HttpStatus.OK;
            response = new UserGrpcReadDTO(usersReadResponse.getId(), usersReadResponse.getNickname(), usersReadResponse.getEmail());
        }else{
            LOG.info("[GRPC-Read] User is NOT found in database.");
            httpStatus = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(response, httpStatus);
    }
    
}
