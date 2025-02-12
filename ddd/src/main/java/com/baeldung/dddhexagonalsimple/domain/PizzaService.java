package com.baeldung.dddhexagonalsimple.domain;

import com.baeldung.dddhexagonalsimple.domain.model.PizzaOrder;
import com.baeldung.dddhexagonalsimple.domain.ports.inbound.ProcessPizzaOrderUseCase;
import com.baeldung.dddhexagonalsimple.domain.ports.outbound.SavePizzaOrderPort;
import lombok.AllArgsConstructor;
import com.baeldung.dddhexagonalsimple.domain.model.Pizza;
import com.baeldung.dddhexagonalsimple.domain.ports.inbound.CreatePizzaUseCase;
import com.baeldung.dddhexagonalsimple.domain.ports.inbound.GetPizzaUseCase;
import com.baeldung.dddhexagonalsimple.domain.ports.outbound.FindPizzaPort;
import com.baeldung.dddhexagonalsimple.domain.ports.outbound.SavePizzaPort;

import java.math.BigDecimal;
import java.util.Optional;

@AllArgsConstructor
public class PizzaService implements CreatePizzaUseCase, GetPizzaUseCase, ProcessPizzaOrderUseCase {

    private final SavePizzaPort savePizzaPort;
    private final FindPizzaPort findPizzaPort;
    private final SavePizzaOrderPort savePizzaOrderPort;

    private final PriceCalculator priceCalculator;

    @Override
    public Pizza create(String name, BigDecimal pricePerSquareInch) {
        Pizza created = Pizza.builder().name(name).pricePerSquareInch(pricePerSquareInch).build();

        return savePizzaPort.save(created);
    }

    @Override
    public Optional<Pizza> get(Long id) {
        return findPizzaPort.find(id);
    }

    @Override
    public PizzaOrder process(Pizza pizza, Integer diameterInInches) {
        BigDecimal priceOfPizza = priceCalculator.calculateRoundedPriceOfPizza(pizza, diameterInInches);
        PizzaOrder processed = PizzaOrder.builder().pizza(pizza).diameterInInches(diameterInInches).price(priceOfPizza).build();

        return savePizzaOrderPort.save(processed);
    }
}
