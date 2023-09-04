package com.tot.team4.util;

public class TokenDetailImpl implements TokenDetail {

    private final String username;

    private final Long userId;

    public TokenDetailImpl(Long userId,String username) {
        this.username = username;
        this.userId = userId;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public Long getUserId() {
        return this.userId;
    }
}
