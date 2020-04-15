package com.discorpsee.lifecycle.data;

import com.discorpsee.lifecycle.HasState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request implements HasState<MyState> {
    private MyState state;

    public static Request from(MyState status) {
        return new Request(status);
    }
}
