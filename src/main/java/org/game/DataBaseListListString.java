package org.game;
import java.util.List;
import org.game.httpserver.http.HttpStatus;

public class DataBaseListListString {
    private HttpStatus http;
    private List<List<String>> value;

    public HttpStatus getHttp() {
        return http;
    }

    public void setHttp(HttpStatus http) {
        this.http = http;
    }

    public List<List<String>> getValue() {
        return value;
    }

    public void setValue(List<List<String>> value) {
        this.value = value;
    }
}

