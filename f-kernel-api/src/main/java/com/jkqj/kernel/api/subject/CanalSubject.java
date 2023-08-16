package com.jkqj.kernel.api.subject;

import com.jkqj.nats.Subject;

import java.io.Serializable;
import java.util.Optional;

public class CanalSubject implements Subject, Serializable {
    private final String category;

    private final String name;

    private String group;

    public CanalSubject(String name) {
        this.category = "Canal-Message";
        this.name = name;
    }

    public CanalSubject(String name, String group) {
        this.category = "Canal-Message";
        this.name = name;
        this.group = group;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Optional<String> getGroup() {
        return Optional.ofNullable(group);
    }

    @Override
    public String toString() {
        return "CanalSubject{" +
                "category='" + category + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
