package com.faceit.usermanagementtool.config;

import com.faceit.usermanagementtool.grpc.server.UserManagementToolServiceImpl;
import com.faceit.usermanagementtool.repository.UserRepository;
import com.faceit.usermanagementtool.repository.impl.UserRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserRepositoryConfiguration {
  @Bean
  public UserManagementToolServiceImpl userManagementToolServiceImplService() {
    return new UserManagementToolServiceImpl(userRepository());
  }

  @Bean
  public UserRepository userRepository() {
    return new UserRepositoryImpl();
  }
}
