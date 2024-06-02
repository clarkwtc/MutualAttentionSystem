package org.app.infrastructure.endpoints.dto;

import org.app.domain.Account;

public class RegisterAccountDTO {
    public String id;

    public RegisterAccountDTO() {
    }

    public RegisterAccountDTO(Account account) {
        this.id = account.getId().toString();
    }

    public static RegisterAccountDTO from(Account account){
        return new RegisterAccountDTO(account);
    }
}
