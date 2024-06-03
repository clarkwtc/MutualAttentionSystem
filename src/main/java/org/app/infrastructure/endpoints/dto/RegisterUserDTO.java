package org.app.infrastructure.endpoints.dto;

import org.app.domain.User;

public class RegisterUserDTO {
    public String id;

    public RegisterUserDTO() {
    }

    public RegisterUserDTO(User user) {
        this.id = user.getId().toString();
    }

    public static RegisterUserDTO from(User user){
        return new RegisterUserDTO(user);
    }
}
