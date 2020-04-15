package com.discorpsee.lifecycle.action;

import com.discorpsee.lifecycle.model.Action;
import com.discorpsee.lifecycle.data.Request;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class LogAction implements Action<Request> {

    @Override
    public void execute(Request object, Map<String, Object> context) {
        log.debug("Executed on {}, context {}", object, context);
    }
}
