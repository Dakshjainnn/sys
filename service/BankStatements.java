package banksys.sys.service;

import banksys.sys.entity.Transaction;
import banksys.sys.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@AllArgsConstructor
public class BankStatements {
    private TransactionRepository transactionRepository;

    public List<Transaction> generateStatement(String account_Number, String startDate,String endDate){
        LocalDate start =LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end =LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);
        List<Transaction> transactionList = transactionRepository.findAll().stream().filter(transaction -> transaction.getAccount_number().equals(account_Number))
                .filter(transaction -> transaction.getCreatedAt().isEqual(start)).filter(transaction -> transaction.getCreatedAt().isEqual(end)).toList();
        return transactionList;
    }
}
