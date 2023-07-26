package com.example.application.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.router.WildcardParameter;

@Route(value = "windows", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class WindowsView extends Div
        implements HasUrlParameter<String>, AfterNavigationObserver {

    private String param;
    private WindowFactory windows;

    public WindowsView(WindowFactory windows) {
        this.windows = windows;
    }

    @Override
    public void setParameter(BeforeEvent event,
            @WildcardParameter String parameter) {
        param = parameter;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        String[] wins = param.split("/");
        for (String win : wins) {
            if (windows.getWindow(win) != null) {
                windows.getWindow(win).open();
            }
        }
    }
}
