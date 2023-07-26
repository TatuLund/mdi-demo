package com.example.application.views;

import org.springframework.stereotype.Component;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.UIScope;

@UIScope
@Component
@WindowContent(value ="form", title = "Form", top = "50%", left = "10%", height = "50%", width = "90%")
public class FormWindow extends FormLayout {

    DatePicker datePicker = new DatePicker("Date");
    ComboBox<String> comboBox = new ComboBox<>("Combo");
    TextField textField = new TextField("Text");

    public FormWindow() {
        comboBox.setItems("One", "Two", "Three");
        add(datePicker, comboBox, textField);
    }

}
