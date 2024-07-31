package banksys.sys.service;

import banksys.sys.dto.*;
import banksys.sys.entity.User;
import banksys.sys.repository.UserRepository;
import banksys.sys.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    EmailService emailService;
    @Autowired
    TransactionService transactionService;

    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .acc_info(null)
                    .build();
        }
        User newUser = User.builder()
                .First_Name(userRequest.getFirst_Name())
                .Last_Name(userRequest.getLast_Name())
                .Gender(userRequest.getGender())
                .email(userRequest.getEmail())
                .accountNumber(AccountUtils.generateAccNo())
                .Account_Balance(BigDecimal.ZERO)
                .Phone_Number(userRequest.getPhone_Number())
                .Status("Active!")
                .build();
        User savedUser = userRepository.save(newUser);
        EmailDetails emailDetails=EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("Account Creation")
                .messageBody(" Congrats! Your Account is Created:)\n Your Account details are:\n Account Name:" + savedUser.getFirst_Name()+" "+savedUser.getLast_Name() +"\n"+"Account Number:" + savedUser.getAccountNumber())
                .build();
        emailService.sendEmailAlert(emailDetails);
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .acc_info(AccountInfo.builder()
                        .AccountBalance(savedUser.getAccount_Balance())
                        .Account_Number(savedUser.getAccountNumber())
                        .AccountName(savedUser.getFirst_Name() + " " + savedUser.getLast_Name())
                        .Phone_Number(savedUser.getPhone_Number())
                        .email(savedUser.getEmail())
                        .gender(savedUser.getGender())
                        .build())
                .build();
    }
    @Override
    public BankResponse balanceEnquiry(EnquiryRequest request) {
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccount_Number());
        if (!isAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .acc_info(null)
                    .build();
        }
        User foundUser = userRepository.findByAccountNumber(request.getAccount_Number());
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_SUCCESS)
                .acc_info(AccountInfo.builder()
                        .AccountBalance(foundUser.getAccount_Balance())
                        .Account_Number(request.getAccount_Number())
                        .AccountName(foundUser.getFirst_Name() + " " + foundUser.getLast_Name())
                        .gender(foundUser.getGender())
                        .email(foundUser.getEmail())
                        .Phone_Number(foundUser.getPhone_Number())
                        .build())
                .build();
    }
    @Override
    public String nameEnquiry(EnquiryRequest request) {
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccount_Number());
        if (!isAccountExist){
            return AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE;
        }
        User foundUser = userRepository.findByAccountNumber(request.getAccount_Number());
        return foundUser.getFirst_Name() + " " + foundUser.getLast_Name();
    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest request) {
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccount_Number());
        if (!isAccountExist){
            throw new RuntimeException("Account doesn't Exist :(");
        }
        User userToCredit = userRepository.findByAccountNumber(request.getAccount_Number());
        userToCredit.setAccount_Balance(userToCredit.getAccount_Balance().add(request.getAmount()));
        userRepository.save(userToCredit);

        TransactionDto transactionDto= TransactionDto.builder()
                .account_number(userToCredit.getAccountNumber())
                .transactionType("Credit")
                .amount(request.getAmount())
                .build();
        transactionService.saveTransaction(transactionDto);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                .acc_info(AccountInfo.builder()
                        .AccountBalance(userToCredit.getAccount_Balance())
                        .Account_Number(request.getAccount_Number())
                        .AccountName(userToCredit.getFirst_Name() + " " + userToCredit.getLast_Name())
                        .gender(userToCredit.getGender())
                        .email(userToCredit.getEmail())
                        .Phone_Number(userToCredit.getPhone_Number())
                        .build())
                .build();


    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest request) {
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccount_Number());
        if (!isAccountExist){
            throw new RuntimeException("Account doesn't Exist :(");
        }
        User userToDebit = userRepository.findByAccountNumber(request.getAccount_Number());
        BigInteger availableBalance =userToDebit.getAccount_Balance().toBigInteger();
        BigInteger debitAmount = request.getAmount().toBigInteger();
        if ( availableBalance.intValue() < debitAmount.intValue()){
            throw new RuntimeException("Insufficient Balance :(");
        }
        userToDebit.setAccount_Balance(userToDebit.getAccount_Balance().subtract(request.getAmount()));
        userRepository.save(userToDebit);

        TransactionDto transactionDto= TransactionDto.builder()
                .account_number(userToDebit.getAccountNumber())
                .transactionType("Debit")
                .amount(request.getAmount())
                .build();
        transactionService.saveTransaction(transactionDto);


        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_DEBITED_MESSAGE)
                .acc_info(AccountInfo.builder()
                        .AccountBalance(userToDebit.getAccount_Balance())
                        .Account_Number(request.getAccount_Number())
                        .AccountName(userToDebit.getFirst_Name() + " " + userToDebit.getLast_Name())
                        .gender(userToDebit.getGender())
                        .email(userToDebit.getEmail())
                        .Phone_Number(userToDebit.getPhone_Number())
                        .build())
                .build();



    }

    @Override
    public BankResponse transfer(TransferRequest request) {
        boolean isSourceAccountExist = userRepository.existsByAccountNumber(request.getSourceAccount_Number());
        boolean isDestinationAccountExist = userRepository.existsByAccountNumber(request.getDestinationAccount_Number());
        if (!isDestinationAccountExist){
            throw new RuntimeException("Destination Account doesn't Exist :(");
        }
        if (!isSourceAccountExist){
            throw new RuntimeException("Source Account doesn't Exist :(");
        }
        User sourceAccountUser=userRepository.findByAccountNumber(request.getSourceAccount_Number());
        if (request.getAmount().compareTo(sourceAccountUser.getAccount_Balance())>0){
            throw new RuntimeException("Insufficient Balance :(");
        }
        sourceAccountUser.setAccount_Balance(sourceAccountUser.getAccount_Balance().subtract(request.getAmount()));
        userRepository.save(sourceAccountUser);
        EmailDetails debitAlert=EmailDetails.builder()
                .subject("Amount Debited Alert")
                .recipient(sourceAccountUser.getEmail())
                .messageBody("The sum of:"+ request.getAmount() + "has been deducted from your account.\n Your current Balance is:"+ sourceAccountUser.getAccount_Balance())
                .build();
        emailService.sendEmailAlert(debitAlert);

        TransactionDto transactionDto= TransactionDto.builder()
                .account_number(sourceAccountUser.getAccountNumber())
                .transactionType("Debit")
                .amount(request.getAmount())
                .build();
        transactionService.saveTransaction(transactionDto);




        User destinationAccountUser = userRepository.findByAccountNumber(request.getDestinationAccount_Number());
        destinationAccountUser.setAccount_Balance(destinationAccountUser.getAccount_Balance().add(request.getAmount()));
        userRepository.save(destinationAccountUser);
        EmailDetails creditAlert=EmailDetails.builder()
                .subject("Amount Credit Alert")
                .recipient(sourceAccountUser.getEmail())
                .messageBody("The sum of:"+ request.getAmount() + "has been credited to your account.\n Your current Balance is:"+ destinationAccountUser.getAccount_Balance())
                .build();
        emailService.sendEmailAlert(creditAlert);

        TransactionDto transactionDto1= TransactionDto.builder()
                .account_number(destinationAccountUser.getAccountNumber())
                .transactionType("Credit")
                .amount(request.getAmount())
                .build();
        transactionService.saveTransaction(transactionDto1);
        return BankResponse.builder()
                .responseCode(AccountUtils.TRANSFER_SUCCESS)
                .responseMessage(AccountUtils.TRANSFER_MESSAGE)
                .acc_info(null)
                .build();

    }


}