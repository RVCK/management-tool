package com.faceit.usermanagementtool.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "management-tool")
public record User(@Id String id, String firstName, String lastName, String nickname, String password, @Indexed(unique = true) String email, String country, String createdAt, String updatedAt) {
    
    public User(String id, String firstName, String lastName, String nickname, String password, String email, String country) {
        this(id, firstName, lastName, nickname, password, email, country, LocalDateTime.now().toString(), LocalDateTime.now().toString());
    }

}
