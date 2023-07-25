package com.example.application.components.window;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.dom.DomListenerRegistration;
import com.vaadin.flow.theme.lumo.LumoUtility;

import elemental.json.JsonObject;
import elemental.json.JsonValue;

public class Window extends Dialog {
    boolean mini;
    String left = "0px";
    String top = "0px";
    private String zIndex;
    Button closeButton = new Button(VaadinIcon.CLOSE.create());
    Button minimizeButton = new Button(VaadinIcon.CARET_DOWN.create());
    Button maximizeButton = new Button(VaadinIcon.EXPAND_SQUARE.create());
    private String minOffset;
    private String height;
    private String width;
    private String oldTop;
    private String oldLeft;
    private String oldWidth;
    private String oldHeight;
    private boolean max;
    private boolean wasMini;

    public Window(String title, String minOffset, String left, String top,
            String width, String height) {
        this.top = top;
        this.left = left;
        this.height = height;
        this.width = width;
        this.minOffset = minOffset;
        setClassName("window");
        setModal(false);
        setResizable(true);
        setDraggable(true);
        setCloseOnOutsideClick(false);
        DialogHeader header = getHeader();
        closeButton.addThemeVariants(ButtonVariant.LUMO_SMALL,
                ButtonVariant.LUMO_TERTIARY);
        closeButton.addClassName(LumoUtility.Margin.Left.AUTO);
        closeButton.addClickListener(e -> {
            close();
        });
        minimizeButton.addThemeVariants(ButtonVariant.LUMO_SMALL,
                ButtonVariant.LUMO_TERTIARY);
        DomListenerRegistration minReg = minimizeButton.getElement()
                .addEventListener("click", e -> {
                    if (!mini) {
                        minimize();
                    } else {
                        restore();
                    }
                });
        minReg.addEventData("event.stopPropagation()");
        maximizeButton.addThemeVariants(ButtonVariant.LUMO_SMALL,
                ButtonVariant.LUMO_TERTIARY);
        DomListenerRegistration maxReg = maximizeButton.getElement()
                .addEventListener("click", e -> {
                    maximize();
                });
        maxReg.addEventData("event.stopPropagation()");
        H3 titleSpan = new H3();
        titleSpan.setText(title);
        titleSpan.addClassNames(LumoUtility.TextColor.PRIMARY,
                LumoUtility.FontSize.MEDIUM, LumoUtility.FontWeight.MEDIUM);
        header.add(minimizeButton, titleSpan, closeButton, maximizeButton);
    }

    private void updateTop() {
        getElement().executeJs(
                """
                        const dialogs = document.getElementsByTagName('vaadin-dialog-overlay');
                        var max = 0;
                        var maxInd = 0;
                        for (let i=0;i<dialogs.length;i++) {
                            if (dialogs[i].classList.contains('window')) {
                                dialogs[i].classList.remove('top');
                                const ind = Number(dialogs[i].style.zIndex);
                                if (ind > maxInd) {
                                    maxInd = ind;
                                    max = i;
                                }
                            }
                        }
                        dialogs[max].classList.add('top');
                        """);
    }

    @Override
    public void onAttach(AttachEvent attach) {
        super.onAttach(attach);
        getElement().executeJs(
                """
                        const overlay = this._overlayElement;
                        overlay.addEventListener('mouseup', (e) => {
                            if (!overlay.getAttribute('class').includes('mini')) {
                                const dialogs = document.getElementsByTagName('vaadin-dialog-overlay');
                                var max = 0;
                                var maxInd = 0;
                                for (let i=0;i<dialogs.length;i++) {
                                    if (dialogs[i].classList.contains('window')) {
                            	        dialogs[i].classList.remove('top');
                                        const ind = Number(dialogs[i].style.zIndex);
                                        if (ind > maxInd) {
                                            maxInd = ind;
                                            max = i;
                                        }
                                    }
                                }
                                dialogs[max].classList.add('top');
                                const rect = overlay.$.overlay.getBoundingClientRect();
                                $0.$server.updatePosition(rect);
                            }
                        });
                        """,
                getElement());
    }

    @ClientCallable
    public void updatePosition(JsonValue json) {
        if (!mini) {
            JsonObject rect = (JsonObject) json;
            left = rect.getNumber("left") + "px";
            top = rect.getNumber("top") + "px";
            width = rect.getNumber("width") + "px";
            height = rect.getNumber("height") + "px";
        }
    }

    public void maximize() {
        if (wasMini) {
            minimize();
            wasMini = false;
            max = false;
            return;
        }
        if (!max) {
            maximizeButton.setIcon(VaadinIcon.COMPRESS_SQUARE.create());
            max = true;
            if (mini) {
                wasMini = true;
                restore();
            }
            oldTop = top;
            oldLeft = left;
            oldWidth = width;
            oldHeight = height;
            doSetPosition("0px", "0px");
            setWidth("99.9%");
            setHeight("99.9%");
        } else {
            wasMini = false;
            max = false;
            maximizeButton.setIcon(VaadinIcon.EXPAND_SQUARE.create());
            doSetPosition(oldLeft, oldTop);
            setWidth(oldWidth);
            setHeight(oldHeight);
        }
        updateTop();
    }

    public void minimize() {
        mini = !mini;
        setClassName("window-mini");
        setDraggable(false);
        setResizable(false);
        doSetPosition(minOffset, "calc(100% - 40px)");
        minimizeButton.setIcon(VaadinIcon.CARET_UP.create());
        if (isOpened()) {
            getElement().executeJs("""
                    return this._overlayElement.style.zIndex;
                            """).then(ind -> {
                zIndex = ind.asString();
                getElement().executeJs("""
                        return this._overlayElement.style.zIndex=1;
                                """);
            });
        }
        updateTop();
    }

    public void bringToFront() {
        getElement().executeJs("this._overlayElement.bringToFront()");
        updateTop();
    }

    public void restore() {
        setClassName("window");
        setDraggable(true);
        setResizable(true);
        minimizeButton.setIcon(VaadinIcon.CARET_DOWN.create());
        doSetPosition(left, top);
        setWidth(width);
        setHeight(height);
        updateTop();
    }

    public void setPosition(String left, String top) {
        this.left = left;
        this.top = top;
        if (isOpened()) {
            doSetPosition(left, top);
        }
    }

    private void doSetPosition(String left, String top) {
        getElement().executeJs("""
                this._overlayElement.$.overlay.style.left=$0;
                this._overlayElement.$.overlay.style.top=$1;
                        """, left, top);
    }

    @Override
    public void open() {
        super.open();
        doSetPosition(left, top);
        setWidth(width);
        setHeight(height);
        if (mini) {
            restore();
        }
        updateTop();
    }
}
