package banksys.sys.service;

import banksys.sys.dto.TransactionDto;
import banksys.sys.entity.Transaction;

public interface TransactionService {
    void saveTransaction(TransactionDto transaction);

}
