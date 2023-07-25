package com.example.application.views;

import com.example.application.components.appnav.AppNav;
import com.example.application.components.appnav.AppNavItem;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;

import org.vaadin.lineawesome.LineAwesomeIcon;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout implements AfterNavigationObserver {

    private H2 viewTitle;
    private AppNav nav;
    private Anchor anchor;

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE,
                LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        H1 appName = new H1("Add-ons");
        appName.addClassNames(LumoUtility.FontSize.LARGE,
                LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private AppNav createNavigation() {
        // AppNav is not yet an official component.
        // For documentation, visit https://github.com/vaadin/vcf-nav#readme
        nav = new AppNav();

        AppNavItem win1 = new AppNavItem("Window 1", "windows/win1",
                LineAwesomeIcon.WINDOWS.create());
        nav.addItem(win1);

        AppNavItem win2 = new AppNavItem("Window 2", "windows/win2",
                LineAwesomeIcon.WINDOWS.create());
        nav.addItem(win2);

        AppNavItem win3 = new AppNavItem("Window 3", "windows/win3",
                LineAwesomeIcon.WINDOWS.create());
        nav.addItem(win3);

        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();
        return layout;
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass()
                .getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        String path = event.getLocation().getPath();
        viewTitle.setText(getCurrentPageTitle());
        nav.getChildren().forEach(comp -> {
            if (comp instanceof AppNavItem) {
                AppNavItem item = (AppNavItem) comp;
                if (path.equals(item.getPath())) {
                    item.getElement().setAttribute("active", "true");
                    String href = ComponentUtil.getData(item, String.class);
                    if (href != null) {
                        anchor.setHref(href);
                    }
                } else {
                    item.getElement().removeAttribute("active");
                }
            }
        });
    }
}
