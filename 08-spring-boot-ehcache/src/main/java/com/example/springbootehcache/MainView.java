/*
 * Copyright (c) 2023 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */
package com.example.springbootehcache;

import com.example.springbootehcache.events.CachingEvent;
import com.example.springbootehcache.events.MethodCallEvent;
import com.example.springbootehcache.service.ExchangeService;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import java.io.Serial;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Stream;

@Route
@PreserveOnRefresh
@SpringComponent
public class MainView extends VerticalLayout {
  @Serial
  private static final long serialVersionUID = 6004653986145050428L;

  @Autowired
  public ExchangeService exchangeService;

  private final VerticalLayout logs = new VerticalLayout();
  private final ComboBox<String> fromCurrency = new ComboBox<>();
  private final ComboBox<String> toCurrency = new ComboBox<>();
  private final TextField fromAmount = new TextField();
  private final TextField toAmount = new TextField();

  @PostConstruct
  public void init() {
    final Collection<String> codes = exchangeService.getCodes();

    toAmount.setReadOnly(true);

    Stream.of(fromAmount, toAmount).forEach(textField -> {
      textField.setRequired(true);
      textField.setPlaceholder("Amount");
    });

    Stream.of(fromCurrency, toCurrency)
        .forEach(comboBox -> comboBox.setItems(codes));

    add(new HorizontalLayout(fromAmount, fromCurrency, new NativeLabel(" <=> "), toAmount, toCurrency));
    add(logs);

    fromAmount.setValue("1");
    fromCurrency.setValue("USD");
    toCurrency.setValue("EUR");
    convert(null);

    Stream.of(fromAmount, fromCurrency, toCurrency)
        .forEach(component -> component.addValueChangeListener(this::convert));
  }

  private void convert(HasValue.ValueChangeEvent<?> event) {
    final double fromAmount;
    try {
      fromAmount = Double.parseDouble(this.fromAmount.getValue());
    } catch (NumberFormatException e) {
      return;
    }

    final var fromCurrency = this.fromCurrency.getValue();
    final var toCurrency = this.toCurrency.getValue();

    toAmount.setValue(BigDecimal.valueOf(exchangeService.convert(fromAmount, fromCurrency, toCurrency)).round(new MathContext(4, RoundingMode.HALF_UP)).toPlainString());
  }

  @EventListener
  @Async
  public void onEvent(MethodCallEvent event) {
    getUI().ifPresent(ui -> ui.access(() -> logs.add(new NativeLabel(new Date() + " : " + event))));
  }

  @EventListener
  @Async
  public void onEvent(CachingEvent event) {
    getUI().ifPresent(ui -> ui.access(() -> logs.add(new NativeLabel(new Date() + " : " + event))));
  }
}
