package banksys.sys.controller;

import banksys.sys.entity.Transaction;
import banksys.sys.service.BankStatements;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bankStatement")
@AllArgsConstructor
public class TransactionController {
    private BankStatements bankStatements;
    @GetMapping
    public List<Transaction> generateBankStatement(@RequestParam String account_number,
                                                   @RequestParam String startDate,
                                                   @RequestParam String endDate){
        return bankStatements.generateStatement(account_number,startDate,endDate);
    }
}
