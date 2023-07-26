package com.example.application.views;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import com.example.application.components.window.Window;
import com.vaadin.flow.spring.annotation.UIScope;

@UIScope
@Component
public class WindowFactory {

    Map<String, Window> windows = new HashMap<>();

    public WindowFactory(ApplicationContext applicationContext) {
        // Window win1 = new Window("Window 1", "10%", "0px", "40%", "95%");
        // Window win2 = new Window("Window 2: With a long name", "50%", "0px",
        // "50%", "50%");
        // Window win3 = new Window("Window 3", "50%", "50%", "50%", "45%");
        // Window win4 = new Window("Window 4", "0px%", "0px", "100%", "100%");
        //
        // windows.put("win1", win1);
        // windows.put("win2", win2);
        // windows.put("win3", win3);
        // windows.put("win4", win4);

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
