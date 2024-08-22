package com.example.backend.dto;
import java.util.List;

public class UserPageResponse {

    private List<UserDto> list;
    private long total;

    // Constructor
    public UserPageResponse() {
    }

    public UserPageResponse(List<UserDto> list, long total) {
        this.list = list;
        this.total = total;
    }

    // Getters and Setters
    public List<UserDto> getList() {
        return list;
    }

    public void setList(List<UserDto> list) {
        this.list = list;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}

