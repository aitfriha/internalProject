package com.wproducts.administration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain=true)
@Document
@Builder
public class PasswordResetToken {

    private static final int EXPIRATION = 60 * 24;

    @Id
    private String id;

    private String token;

    @DBRef
    private User user;

    private Date expiryDate;

    public PasswordResetToken(String token, User user) {
        this.token=token;
        this.user=user;
    }
}