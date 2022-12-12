package com.ironhack.BankSystem.utils;

import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;

@NoArgsConstructor
public class DataTimeUtil {

    public static LocalDateTime dateTimeNow() {

        return LocalDateTime.now(ZoneId.of("Europe/Madrid"));
    }
}
