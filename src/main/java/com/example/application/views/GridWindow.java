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
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.UIScope;

@UIScope
@Component
@WindowContent(value = "grid", title = "Two Grids", left = "50%", top = "0px", width = "50%", height = "50%")
public class GridWindow extends HorizontalLayout {

    public GridWindow() {

        List<String> items1 = IntStream.range(0, 1000)
                .mapToObj(i -> "Item " + i).collect(Collectors.toList());
        List<Integer> items2 = IntStream.range(1001, 2000).mapToObj(i -> i)
                .collect(Collectors.toList());

        Grid<String> grid1 = new Grid<>();
        grid1.addColumn(i -> i.toString());
        grid1.addColumn(LitRenderer.<String> of(
                "<span><b style='color: blue'>${item.number}</b></span>")
                .withProperty("number", i -> i.toString()));
        GridListDataView<String> dataView1 = grid1.setItems(items1);

        Grid<Integer> grid2 = new Grid<>();
        grid2.addColumn(i -> i.toString())
                .setPartNameGenerator(i -> i < 1500 ? "red" : null);
        GridListDataView<Integer> dataView2 = grid2.setItems(items2);

        add(grid1, grid2);
    }

}
