package com.himedia.board.dto;

import lombok.Data;

@Data
public class OAuthToken {

    private String access_token;
    private String refresh_token;
    private String token_type;
    private String expires_in;
    private String scope;
    private String refresh_expires_in;

}
