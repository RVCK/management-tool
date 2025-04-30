package com.faceit.usermanagementtool.controller;

import com.faceit.usermanagementtool.openapi.api.UsersApi;
import com.faceit.usermanagementtool.openapi.model.FilteredUsersResponse;
import com.faceit.usermanagementtool.openapi.model.UserRequest;
import com.faceit.usermanagementtool.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@AllArgsConstructor
@RestController
public class UserController implements UsersApi {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserServiceImpl userService;

    /**
     * POST /users - Create new User resource
     * @param userRequest (required)
     * @return HTTP Status code
     */
    @Override
    public ResponseEntity<Void> userCreation(final UserRequest userRequest){
        LOG.info("[REST-Creation] Incoming User creation request {}", userRequest);
        boolean isCreated = this.userService.creation(userRequest);
        return isCreated ? new ResponseEntity<>(HttpStatus.CREATED) : new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * DELETE /users/{id} - Delete existing User resource
     * @param id (required)
     * @return HTTP Status code
     */
    @Override
    public ResponseEntity<Void> userDeleted(final String id){
        LOG.info("[REST-Delete] Incoming User delete ID={}", id);
        boolean isDeleted = this.userService.delete(id);
        return isDeleted ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * GET /users - Get User resource
     * @param id
     * @param firstName
     * @param lastName
     * @param nickname
     * @param email
     * @param country
     * @return FilteredUsersResponse instances List
     */
    @Override
    public ResponseEntity<List<FilteredUsersResponse>> userSearch(final String id,
                                                                  final String firstName,
                                                                  final String lastName,
                                                                  final String nickname,
                                                                  final String email,
                                                                  final String country){
        LOG.info("[REST-Read] Incoming User read with filters={}",
                new ArrayList<>(Arrays.asList(id, firstName, lastName, nickname, email, country)));
        return new ResponseEntity<>(this.userService.read(this.userService.getUserFilterMap(id, firstName, lastName,
                nickname, email, country)), HttpStatus.OK);
    }

    /**
     * PATCH /users/{id} - Update full User resource (of an existing User)
     * @param id (required)
     * @param userUpdateRequest (required)
     * @return HTTP Status code
     */
    @Override
    public ResponseEntity<Void> userUpdate(final String id,
            final UserRequest userUpdateRequest){
        LOG.info("[REST-Update] Incoming User update ID={} with new values {}", id, userUpdateRequest);
        boolean isUpdated = this.userService.update(id, userUpdateRequest);
        return isUpdated ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
