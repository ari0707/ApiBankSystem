package com.ironhack.BankSystem.utils;

import com.ironhack.BankSystem.model.Money;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


import javax.money.convert.MonetaryConversions;
import java.math.BigDecimal;
import java.util.Currency;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MoneyUtil {


    public static Money newMoney(String value) {
        return new Money(new BigDecimal(value));
    }

    public static Money newMoney(String value, String currency) {
        return new Money(new BigDecimal(value), Currency.getInstance(currency));
    }

    // Métodos que verifican si es la misma divisa(currency)
    public static boolean isSameCurrency(Currency baseCurrency, Money comparativeMoney) {
        return baseCurrency.getCurrencyCode().equals(comparativeMoney.getCurrency().getCurrencyCode());
    }

    public static boolean isSameCurrency(Money baseMoney, Money comparativeMoney) {
        return baseMoney.getCurrency().getCurrencyCode().equals(comparativeMoney.getCurrency().getCurrencyCode());
    }


    // Método para convertir las divisas(currency)
    public static Money convertCurrency(Currency baseCurrency, Money convertMoney) {
        if (!isSameCurrency(baseCurrency, convertMoney)) {
            var rateProvider = MonetaryConversions.getExchangeRateProvider();
            var conversion = rateProvider.getCurrencyConversion(baseCurrency.getCurrencyCode());
            var newConvertMoney = org.javamoney.moneta.Money.of(convertMoney.getAmount(), convertMoney.getCurrency().getCurrencyCode());
            var convertedMoney = newConvertMoney.with(conversion);
            return new Money(convertedMoney.getNumberStripped(), Currency.getInstance(convertedMoney.getCurrency().getCurrencyCode()));
        }
        return convertMoney;
    }

    public static Money convertCurrency(Money baseCurrency, Money convertMoney) {
        if (!isSameCurrency(baseCurrency, convertMoney)) {
            var rateProvider = MonetaryConversions.getExchangeRateProvider();
            var conversion = rateProvider.getCurrencyConversion(baseCurrency.getCurrency().getCurrencyCode());
            var newConvertMoney = org.javamoney.moneta.Money.of(convertMoney.getAmount(), convertMoney.getCurrency().getCurrencyCode());
            var convertedMoney = newConvertMoney.with(conversion);
            return new Money(convertedMoney.getNumberStripped(), Currency.getInstance(convertedMoney.getCurrency().getCurrencyCode()));
        }
        return convertMoney;
    }

    // Método para un balance negativo
    public static Money negativeMoney(Money money) {
        return new Money(money.getAmount().negate(), money.getCurrency());
    }

    // Método para comparar el balance
    public static int compareMoney(Money money1, Money money2) {
        Money convertedMoney2 = convertCurrency(money1, money2);
        return money1.getAmount().compareTo(convertedMoney2.getAmount());
    }

    // Método para calcular el Balance (Subtraction)
    public static Money subtractMoney(Money baseMoney, Money moneyToSubtract) {
        Money convertedMoneyToSubtract = convertCurrency(baseMoney, moneyToSubtract);
        return new Money(baseMoney.getAmount().subtract(convertedMoneyToSubtract.getAmount()), baseMoney.getCurrency());
    }

    // Método para calcular el Balance (Addition)
    public static Money addMoney(Money baseMoney, Money moneyToAdd) {
        Money convertedMoneyToAdd = convertCurrency(baseMoney, moneyToAdd);
        return new Money(baseMoney.getAmount().add(convertedMoneyToAdd.getAmount()), baseMoney.getCurrency());
    }
}
