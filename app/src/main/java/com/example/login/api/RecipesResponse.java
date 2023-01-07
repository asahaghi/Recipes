package com.example.login.api;

import com.example.login.model.Menu;

import java.util.List;

public class RecipesResponse {
    private List<Menu> result;

    public List<Menu> getResult() {
        return result;
    }

    public void setResult(List<Menu> result) {
        this.result = result;
    }
}
