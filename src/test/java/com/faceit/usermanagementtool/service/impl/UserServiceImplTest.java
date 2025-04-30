package com.faceit.usermanagementtool.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.faceit.usermanagementtool.mapper.UserMapper;
import com.faceit.usermanagementtool.model.User;
import com.faceit.usermanagementtool.openapi.model.FilteredUsersResponse;
import com.faceit.usermanagementtool.openapi.model.UserRequest;
import com.faceit.usermanagementtool.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

//Unit testing (happy path CRUD)

@SpringBootTest
public class UserServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserMapper userMapper;
  
  @InjectMocks
  private UserServiceImpl restUserService;

  @BeforeEach
  public void init() {
    openMocks(this);
    this.restUserService = Mockito.spy(new UserServiceImpl(userRepository));
  }
  
  private UserRequest userRequestCreation(){
    UserRequest userRequest = new UserRequest();
    userRequest.setId("1");
    userRequest.setFirstName("firstName");
    userRequest.setLastName("lastName");
    userRequest.setNickname("nickname");
    userRequest.setCountry("CU");
    userRequest.setEmail("email");
    userRequest.setPassword("pass");
    return userRequest;
  }
  
  @Test
  public void creationUserTest(){
    //Ini
    UserRequest userRequest = userRequestCreation();
    when(this.userRepository.userCreation(any())).thenReturn(true);
    
    //Main
    boolean isCreated = this.restUserService.creation(userRequest);

    //Verify
    Assertions.assertTrue(isCreated);
  }

  @Test
  public void creationUserKOTest(){
    //Ini
    UserRequest userRequest = userRequestCreation();
    when(this.userRepository.userCreation(any())).thenReturn(false);

    //Main
    boolean isCreated = this.restUserService.creation(userRequest);

    //Verify
    Assertions.assertFalse(isCreated);
  }

  @Test
  public void updateUserTest(){
    //Ini
    UserRequest userRequest = userRequestCreation();
    when(this.userRepository.updateUser(any(), any())).thenReturn(true);
    List<User> users = new ArrayList<>();
    User user = new User("1", "firstName", "lastName", "nickname", "pass", "email", "country", "time", "time" ); 
    users.add(user);
    when(this.userRepository.findByFilters(any())).thenReturn(users);

    //Main
    boolean isUpdated = this.restUserService.update("1", userRequest);

    //Verify
    Assertions.assertTrue(isUpdated);
  }

  @Test
  public void deleteUserTest(){
    //Ini
    when(this.userRepository.removeUserById(anyString())).thenReturn(true);

    //Main
    boolean isDeleted = this.restUserService.delete("1");

    //Verify
    Assertions.assertTrue(isDeleted);
  }

  @Test
  public void readUserTest(){
    //Ini
    List<User> filteredUsers = 
        List.of(new User("1", "firstName", "lastName", "nick",
            "pass", "email", "UK"));
    when(this.userRepository.findByFilters(any())).thenReturn(filteredUsers);
    Map<String, String> filterMap = Map.of("id","1", "nickname", "nick");

    //Main
    List<FilteredUsersResponse> response = this.restUserService.read(filterMap);

    //Verify
    Assertions.assertNotNull(response);
    Assertions.assertFalse(response.isEmpty());

  }
}
