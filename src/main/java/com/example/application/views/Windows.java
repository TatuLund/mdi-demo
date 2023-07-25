package com.example.application.views;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.application.components.window.Window;
import com.vaadin.flow.spring.annotation.UIScope;

@UIScope
@Component
public class Windows {

    Map<String, Window> windows = new HashMap<>();

    public Windows() {
        Window win1 = new Window("Window 1", "10%", "0px", "40%", "95%");
        Window win2 = new Window("Window 2: With a long name", "50%", "0px", "50%",
                "50%");
        Window win3 = new Window("Window 3", "50%", "50%", "50%",
                "45%");

        windows.put("win1", win1);
        windows.put("win2", win2);
        windows.put("win3", win3);
    }

    public Window getWindow(String name) {
        return windows.get(name);
    }
}
