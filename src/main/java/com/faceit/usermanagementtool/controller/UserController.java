package com.faceit.usermanagementtool.controller;

import com.faceit.usermanagementtool.openapi.api.UsersApi;
import com.faceit.usermanagementtool.openapi.model.FilteredUsersResponse;
import com.faceit.usermanagementtool.openapi.model.UserRequest;
import com.faceit.usermanagementtool.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@AllArgsConstructor
@RestController
public class UserController implements UsersApi {

    @Autowired
    private UserServiceImpl userService;
    
    @Override
    public ResponseEntity<Void> userCreation(final UserRequest userRequest){
        boolean isCreated = this.userService.creation(userRequest);
        return isCreated ? new ResponseEntity<>(HttpStatus.CREATED) : new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> userDeleted(final String id){
        boolean isDeleted = this.userService.delete(id);
        return isDeleted ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<List<FilteredUsersResponse>> userSearch(final String id,
                                                                  final String firstName,
                                                                  final String lastName,
                                                                  final String nickname,
                                                                  final String email,
                                                                  final String country){
        String hola = "hola";
        return new ResponseEntity<>(this.userService.read((HashMap<String, String>) Map.of("id", id, "firstName", firstName, "lastName", lastName,
                "nickname", nickname, "email", email,  "country", country)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> userUpdate(final String id,
            final UserRequest userRequest){
        boolean isUpdated = this.userService.update(id, userRequest);
        return isUpdated ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
}
