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
		DomListenerRegistration reg = minimizeButton.getElement().addEventListener("click", e -> {
			mini = !mini;
			if (mini) {
				minimize();
			} else {
				restore();
			}
		});
		reg.addEventData("event.stopPropagation()");
		H3 titleSpan = new H3();
		titleSpan.setText(title);
		titleSpan.addClassNames(LumoUtility.TextColor.PRIMARY, LumoUtility.FontSize.MEDIUM,
				LumoUtility.FontWeight.MEDIUM);
		header.add(minimizeButton, titleSpan, closeButton);

	}

	@Override
	public void onAttach(AttachEvent attach) {
		super.onAttach(attach);
		getElement().executeJs("""
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
				""", getElement());
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
						return this._overlayElement.style.zIndex=1;
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
	}
}
