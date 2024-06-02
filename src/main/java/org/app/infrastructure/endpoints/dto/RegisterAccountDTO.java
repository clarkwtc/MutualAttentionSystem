package org.app.infrastructure.endpoints.dto;

import org.app.domain.User;

public class RegisterAccountDTO {
    public String id;

    public RegisterAccountDTO() {
    }

    public RegisterAccountDTO(User user) {
        this.id = user.getId().toString();
    }

    public static RegisterAccountDTO from(User user){
        return new RegisterAccountDTO(user);
    }
}
