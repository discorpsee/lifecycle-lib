package com.discorpsee.lifecycle.guard;

import com.discorpsee.lifecycle.model.Guard;
import com.discorpsee.lifecycle.data.Request;

import java.util.Map;

public class AlwaysFailGuard implements Guard<Request> {

    @Override
    public boolean evaluate(Request object, Map<String, Object> context) {
        return false;
    }
}
