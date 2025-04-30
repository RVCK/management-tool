package com.faceit.usermanagementtool.grpc.server;

import com.faceit.usermanagementtool.mapper.UserMapper;
import com.faceit.usermanagementtool.mapper.UserMapperImpl;
import com.faceit.usermanagementtool.model.User;
import com.faceit.usermanagementtool.repository.UserRepository;
import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.managementtool.*;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserManagementToolServiceImpl extends UserServiceGrpc.UserServiceImplBase {
    
    @Autowired
    private UserRepository userRepository;

    private UserMapper userMapper = new UserMapperImpl();

    public UserManagementToolServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void userCreation(UserRequest request, StreamObserver<Empty> responseObserver){
        try{
            if (this.userRepository.userCreation(this.userMapper.toUserGrpc(request))) {
                responseObserver.onNext(Empty.getDefaultInstance());
                responseObserver.onCompleted();
            }
        } catch (DuplicateKeyException e) {
            responseObserver.onError(Status.ALREADY_EXISTS.withDescription("Duplicated key in MongoDB.").asException());
        }
    }
    
    @Override
    public void userUpdate(UserRequest request, StreamObserver<Empty> responseObserver){
        try{
            if (this.userRepository.updateUser(request.getId(), this.userMapper.toUserGrpc(request))) {
                responseObserver.onNext(Empty.getDefaultInstance());
                responseObserver.onCompleted();
            }
        } catch (Exception e) {
            responseObserver.onError(Status.NOT_FOUND.withDescription("Not found in MongoDB.").asRuntimeException());
        }
    }
    
    @Override
    public void userDelete(UserDeleteRequest request, StreamObserver<Empty> responseObserver) {
        try{
            if (this.userRepository.removeUserById(request.getId())) {
                responseObserver.onNext(Empty.getDefaultInstance());
                responseObserver.onCompleted();
            }
        } catch (Exception e) {
            responseObserver.onError(Status.NOT_FOUND.withDescription("Not found in MongoDB.").asRuntimeException());
        }
    }
    
    @Override
    public void userRead(UserFilters request, StreamObserver<io.grpc.managementtool.UsersReadResponse> responseObserver){
        try{
            Optional<User> user = this.userRepository.findByFilters(this.getUserFilterMap(request.getId(),
                    request.getFirstName(), request.getLastName(), request.getNickname(),
                    request.getEmail(), request.getCountry())).stream().findFirst();
            if(Objects.nonNull(user.get())){
                UsersReadResponse usersReadResponse = UsersReadResponse.newBuilder()
                        .setId(user.get().id())
                        .setNickname(user.get().nickname())
                        .setEmail(user.get().email())
                        .build();
                responseObserver.onNext(usersReadResponse);
                responseObserver.onCompleted();             
            }
        } catch (Exception e) {
            responseObserver.onError(Status.NOT_FOUND.withDescription("Not found in MongoDB.").asRuntimeException());
        }
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
