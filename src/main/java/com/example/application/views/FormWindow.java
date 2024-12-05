package com.example.application.views;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.UIScope;

@UIScope
@Component
@WindowContent(value = "form", title = "Form", top = "50%", left = "10%", height = "50%", width = "90%")
public class FormWindow extends FormLayout {

    DatePicker datePicker = new DatePicker("Date");
    ComboBox<String> comboBox = new ComboBox<>("Combo");
    TextField textField = new TextField("Text");

    public FormWindow() {
        Binder<Bean> binder = new Binder<>();
        comboBox.setItems("One", "Two", "Three");
        binder.forField(datePicker).bind(Bean::getDate, Bean::setDate);
        binder.forField(comboBox).bind(Bean::getNumber, Bean::setNumber);
        binder.forField(textField).bind(Bean::getText, Bean::setText);
        binder.addValueChangeListener(e -> {
            Notification.show(e.getValue().toString());
        });
        add(datePicker, comboBox, textField);
    }

    public class Bean {
        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        private LocalDate date;
        private String text;
        private String number;
    }
}
