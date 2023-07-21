package com.example.application.components.window;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class Window extends Dialog {
	boolean mini;
	String left = "0px";
	String top = "0px";
	private String zIndex;
	Button closeButton = new Button(VaadinIcon.CLOSE.create());
	Button minimizeButton = new Button(VaadinIcon.CARET_DOWN.create());
	private String minOffset;
	private String height;
	private String width;

	public Window(String title, String minOffset, String left, String top, String width, String height) {
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
		closeButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
		closeButton.addClassName(LumoUtility.Margin.Left.AUTO);
		closeButton.addClickListener(e -> {
			close();
		});
		minimizeButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
		minimizeButton.addClickListener(e -> {
			mini = !mini;
			if (mini) {
				minimize();
			} else {
				restore();
			}
		});
		H3 titleSpan = new H3();
		titleSpan.setText(title);
		titleSpan.addClassNames(LumoUtility.TextColor.PRIMARY, LumoUtility.FontSize.MEDIUM,
				LumoUtility.FontWeight.MEDIUM);
		header.add(minimizeButton, titleSpan, closeButton);
	}

	public void minimize() {
		setClassName("window-mini");
		doSetPosition(minOffset, "calc(100% - 40px)");
		minimizeButton.setIcon(VaadinIcon.CARET_UP.create());
		if (isOpened()) {
			getElement().executeJs("""
					return this._overlayElement.style.zIndex;
					        """).then(ind -> {
				zIndex = ind.asString();
				getElement().executeJs("""
						return this._overlayElement.style.zIndex=0;
						        """);
			});
		}
	}

	public void restore() {
		setClassName("window");
		minimizeButton.setIcon(VaadinIcon.CARET_DOWN.create());
		doSetPosition(left, top);
	}

	public void setPosition(String left, String top) {
		this.left = left;
		this.top = top;
		doSetPosition(left, top);
	}

	private void doSetPosition(String left, String top) {
		if (isOpened()) {
			getElement().executeJs("""
					this._overlayElement.$.overlay.style.left=$0;
					this._overlayElement.$.overlay.style.top=$1;
					        """, left, top);
		}
	}
	
	@Override
	public void open() {
		super.open();
        setPosition(left, top);
        setWidth(width);
        setHeight(height);
        if (mini) {
        	restore();
        }
	}
}
