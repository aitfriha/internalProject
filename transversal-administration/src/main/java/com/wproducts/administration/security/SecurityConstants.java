package com.wproducts.administration.security;

public interface SecurityConstants {
    String SECRET = "SecretKeyToGenJWTs";
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
    long EXPIRATION_TIME = 864_000_00; // 24H
}
