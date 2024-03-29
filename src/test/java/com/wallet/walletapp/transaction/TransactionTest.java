package com.wallet.walletapp.transaction;

import com.wallet.walletapp.wallet.InsufficientBalanceException;
import com.wallet.walletapp.wallet.Wallet;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class TransactionTest {
    @Test
    void shouldUpdateWalletBalanceOnProcess() throws InsufficientBalanceException {
        Wallet wallet = mock(Wallet.class);
        Transaction transaction = creditTransaction();
        transaction.setWallet(wallet);

        transaction.process();

        verify(wallet).credit(50L);
    }

    @Test
    void shouldBeMinimumAmount10() {
        Long lesserThan10 = 5L;
        Transaction transaction = new Transaction();
        transaction.setAmount(lesserThan10);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<Transaction>> violations = validator.validate(transaction);

        assertEquals(1, violations.size());
        assertEquals("Amount should not be lesser than 10", violations.iterator().next().getMessage());
    }

    private Transaction creditTransaction() {
        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(50L);
        newTransaction.setTransactionType(TransactionType.CREDIT);
        return newTransaction;
    }

}
