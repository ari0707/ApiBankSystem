package com.ironhack.BankSystem.utils;

import com.ironhack.BankSystem.enums.AccountStatus;
import com.ironhack.BankSystem.enums.TransactionPurpose;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EnumsUtil {

    // Status desde un string
    public static AccountStatus accountStatusFromString(String stringStatus) {
        for (AccountStatus s : AccountStatus.values()) {
            if (s.name().equalsIgnoreCase(stringStatus))
                return s;
        }
        throw new IllegalArgumentException("Status " + stringStatus + " does not exist.");
    }

    // TransactionPurpose desde un String
    public static TransactionPurpose transactionPurposeFromString(String stringTransactionPurpose) {
        for (TransactionPurpose tp : TransactionPurpose.values()) {
            if (tp.name().equalsIgnoreCase(stringTransactionPurpose))
                return tp;
        }
        throw new IllegalArgumentException("TransactionPurpose " + stringTransactionPurpose + " does not exist.");
    }

    // TransactionType desde un String
//    public static TransactionType transactionTypeFromString(String stringTransactionType) {
//        for (TransactionType tt : TransactionType.values()) {
//            if (tt.name().equalsIgnoreCase(stringTransactionType))
//                return tt;
//        }
//        throw new IllegalArgumentException("TransactionPurpose " + stringTransactionType + " does not exist.");
//    }

}
