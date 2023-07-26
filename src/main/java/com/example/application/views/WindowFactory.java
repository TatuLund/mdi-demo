package com.example.application.views;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.example.application.components.window.Window;
import com.vaadin.flow.spring.annotation.UIScope;

@UIScope
@Component
public class WindowFactory {

    Map<String, Window> windows = new HashMap<>();

    public WindowFactory(ApplicationContext applicationContext) {
        Map<String, Object> beans = applicationContext
                .getBeansWithAnnotation(WindowContent.class);

        beans.values().forEach(bean -> {
            com.vaadin.flow.component.Component content = (com.vaadin.flow.component.Component) bean;
            Class<? extends com.vaadin.flow.component.Component> contentClass = content
                    .getClass();
            WindowContent contentAnnotation = contentClass
                    .getAnnotation(WindowContent.class);
            String targetWindow = contentAnnotation.value();
            Window win = new Window(contentAnnotation.title(),
                    contentAnnotation.left(), contentAnnotation.top(),
                    contentAnnotation.width(), contentAnnotation.height());
            windows.put(targetWindow, win);
            win.add(content);
        });
    }

    public Set<String> getWindows() {
        return windows.keySet();
    }

    public Window getWindow(String name) {
        return windows.get(name);
    }
}
