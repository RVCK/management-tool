package com.faceit.usermanagementtool.config;

import io.grpc.ManagedChannel;
import io.grpc.managementtool.UserServiceGrpc;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcConfiguration {

    @Bean(destroyMethod = "shutdownNow")
    public ManagedChannel managedChannel() {
        return NettyChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();
    }

    @Bean
    public UserServiceGrpc.UserServiceBlockingStub userServiceGrpcStub(ManagedChannel managedChannel) {
        return UserServiceGrpc.newBlockingStub(managedChannel);
    }

 
}
