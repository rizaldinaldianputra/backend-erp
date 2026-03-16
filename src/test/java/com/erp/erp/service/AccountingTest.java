package com.erp.erp.service;

import com.erp.erp.model.ChartOfAccount;
import com.erp.erp.model.JournalDetail;
import com.erp.erp.model.JournalHeader;
import com.erp.erp.repository.ChartOfAccountRepository;
import com.erp.erp.repository.JournalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AccountingTest {

    @Mock
    private ChartOfAccountRepository coaRepository;

    @Mock
    private JournalRepository journalRepository;

    @InjectMocks
    private ChartOfAccountService coaService;

    @InjectMocks
    private AccountingService accountingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAccount() {
        ChartOfAccount account = ChartOfAccount.builder()
                .code("1001")
                .name("Cash")
                .type(ChartOfAccount.AccountType.ASSET)
                .build();

        when(coaRepository.save(any(ChartOfAccount.class))).thenReturn(account);

        ChartOfAccount created = coaService.createAccount(account);
        assertNotNull(created);
        assertEquals("1001", created.getCode());
    }

    @Test
    void testCreateJournalBalanced() {
        JournalHeader journal = JournalHeader.builder()
                .documentNumber("JV-001")
                .description("Test Journal")
                .details(new ArrayList<>())
                .build();

        journal.getDetails().add(JournalDetail.builder()
                .debit(new BigDecimal("1000"))
                .credit(BigDecimal.ZERO)
                .build());

        journal.getDetails().add(JournalDetail.builder()
                .debit(BigDecimal.ZERO)
                .credit(new BigDecimal("1000"))
                .build());

        when(journalRepository.save(any(JournalHeader.class))).thenReturn(journal);

        JournalHeader created = accountingService.createJournal(journal);
        assertNotNull(created);
        assertEquals("JV-001", created.getDocumentNumber());
    }

    @Test
    void testCreateJournalUnbalanced() {
        JournalHeader journal = JournalHeader.builder()
                .documentNumber("JV-002")
                .description("Unbalanced Journal")
                .details(new ArrayList<>())
                .build();

        journal.getDetails().add(JournalDetail.builder()
                .debit(new BigDecimal("1000"))
                .credit(BigDecimal.ZERO)
                .build());

        journal.getDetails().add(JournalDetail.builder()
                .debit(BigDecimal.ZERO)
                .credit(new BigDecimal("500"))
                .build());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountingService.createJournal(journal);
        });

        assertTrue(exception.getMessage().contains("Journal is not balanced"));
    }
}
