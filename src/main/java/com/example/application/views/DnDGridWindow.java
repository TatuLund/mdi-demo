package com.example.application.views;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.UIScope;

@UIScope
@Component
@WindowContent(value = "grid", title = "Drag and drop grid", left = "50%", top = "0px", width = "50%", height = "50%")
public class DnDGridWindow extends HorizontalLayout {

    private List<String> draggedItems;

    public DnDGridWindow() {

        List<String> items1 = IntStream.range(0, 1000).mapToObj(i -> "Item "+i).collect(Collectors.toList());
        List<Integer> items2 = IntStream.range(1001,2000).mapToObj(i -> i).collect(Collectors.toList());

        Grid<String> grid1 = new Grid<>();
        grid1.setSelectionMode(Grid.SelectionMode.MULTI);
        grid1.addColumn(String::toString);
        GridListDataView<String> dataView1 =  grid1.setItems(items1);
        
        Grid<Integer> grid2 = new Grid<>();
        grid2.setSelectionMode(Grid.SelectionMode.MULTI);
        grid2.addColumn(i -> i.toString());
        GridListDataView<Integer> dataView2 =  grid2.setItems(items2);
        
        grid1.setDropMode(GridDropMode.ON_GRID);
        grid1.setRowsDraggable(true);
        grid1.addDropListener(e -> {
            dataView1.addItems(getStrings(grid2.getSelectedItems()));
            dataView2.removeItems(grid2.getSelectedItems());
        });

        grid2.setDropMode(GridDropMode.ON_GRID);
        grid2.setRowsDraggable(true);
        grid2.addDropListener(e -> {
            dataView2.addItems(getIntegers(grid1.getSelectedItems()));
            dataView1.removeItems(grid1.getSelectedItems());
        });

        TextField field = new TextField();
        field.setValueChangeMode(ValueChangeMode.ON_BLUR);
        Shortcuts.addShortcutListener(field, e -> {
            field.getElement().executeJs("this.blur()").then(result -> Notification.show("field: " + field.getValue()));
        }, Key.ENTER).listenOn(field);
        
        ContextMenu menu = new ContextMenu(field);
        menu.getElement().getThemeList().add("short");
        for (int i=1;i<101;i++) {
            menu.addItem("Item "+i).addClickListener(e -> Notification.show("Item"));
        }
        
        add(grid1, grid2, field, menu);
    }

    private List<String> getStrings(Set<Integer> integers) {
        return integers.stream().map(i -> "Item "+i).collect(Collectors.toList());
    }

    private List<Integer> getIntegers(Set<String> strings) {
        return strings.stream().map(s -> {
            String[] fragments = s.split(" ");
            return Integer.valueOf(fragments[1]);
        }).collect(Collectors.toList());
    }
}
