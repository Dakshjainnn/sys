package banksys.sys.service;

import banksys.sys.dto.TransactionDto;
import banksys.sys.entity.Transaction;
import banksys.sys.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionServiceImpl implements TransactionService{
    @Autowired
    TransactionRepository transactionrepository;
    @Override
    public void saveTransaction(TransactionDto transactionDto) {
        Transaction transaction = Transaction.builder()
                .transactionType(transactionDto.getTransactionType())
                .account_number(transactionDto.getAccount_number())
                .amount(transactionDto.getAmount())
                .status("SUCCESS")
                .build();
        transactionrepository.save(transaction);
        System.out.println("Transaction Saved successfully:)");
    }

}
