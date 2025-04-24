package com.faceit.usermanagementtool.model;


public record User(String id, String firstName, String lastName, String nickname, String password, String email, String country) {
    
}
