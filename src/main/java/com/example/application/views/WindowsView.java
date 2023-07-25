package com.example.application.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@Route(value = "windows", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class WindowsView extends Div
        implements HasUrlParameter<String>, AfterNavigationObserver {

    private String win;
    private Windows windows;

    public WindowsView(Windows windows) {
        this.windows = windows;
    }

    @Override
    public void setParameter(BeforeEvent event,
            @OptionalParameter String parameter) {
        win = parameter;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        if (windows.getWindow(win) != null) {
            windows.getWindow(win).open();
        }
    }
}
