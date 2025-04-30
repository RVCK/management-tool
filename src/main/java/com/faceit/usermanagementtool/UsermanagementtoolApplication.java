package com.faceit.usermanagementtool;

import com.faceit.usermanagementtool.grpc.server.UserManagementToolServiceImpl;
import com.faceit.usermanagementtool.repository.UserRepository;
import io.grpc.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class UsermanagementtoolApplication {

	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("Starting the SPRING Server with UserManagementTool hosted in it:");
		ApplicationContext app = SpringApplication.run(UsermanagementtoolApplication.class, args);
		
		System.out.println("Starting the gRPC Server with UserManagementTool hosted in it.");
		// plaintext server 50051
		Server server = ServerBuilder.forPort(9090)
				.addService(new UserManagementToolServiceImpl(app.getBean("userRepository", UserRepository.class)))
				.intercept(new ServerInterceptor() {
					@Override
					public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers,
																				 ServerCallHandler<ReqT, RespT> next) {
						System.out.println("Incoming request: " + call.getMethodDescriptor().getFullMethodName());
						return next.startCall(call, headers);
					}
				})
				.build();
		server.start();

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("Received Shutdown Request");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			server.shutdown();
			System.out.println("Successfully stopped the server");
		}));
		server.awaitTermination();
	}
	
	

}
