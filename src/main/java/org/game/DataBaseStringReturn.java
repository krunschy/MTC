package org.game;

import org.game.httpserver.http.HttpStatus;

public class DataBaseStringReturn {
    private HttpStatus http;
    private String[] value;

    public HttpStatus getHttp() {
        return http;
    }

    public void setHttp(HttpStatus http) {
        this.http = http;
    }

    public String[] getValue() {
        return value;
    }

    public void setValue(String[] value) {
        this.value = value;
    }
}
