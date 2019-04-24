package com.example.picar.retrofit.model.type_message;

import com.example.picar.database.entity.User;

public class MessageUserEmailApproval {
        private String message;
        private Boolean emailAlreadyExist;

    public String getMessage() {
        return message;
    }


    public Boolean getEmailAlreadyExist() {
        return emailAlreadyExist;
    }

}
