package ru.yandex.praktikum;

import java.util.List;

public class Ingredient {
    private boolean success;
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }
}
