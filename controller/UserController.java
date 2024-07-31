package banksys.sys.controller;

import banksys.sys.dto.*;
import banksys.sys.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User Management APIs")
public class UserController {

    @Autowired
    UserService userService;
    @Operation(
            summary = "Create New Account"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 Created"
    )
    @PostMapping
    public BankResponse createAccount(@RequestBody UserRequest userRequest){
        return userService.createAccount(userRequest);
    }
    @Operation(
            summary = "Check Balance"
    )
    @ApiResponse(
            responseCode = "202",
            description = "Http Status 202 Success"
    )
    @GetMapping("/balanceEnquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest request){
        return userService.balanceEnquiry(request);
    }
    @Operation(
            summary = "Check Name"
    )
    @ApiResponse(
            responseCode = "203",
            description = "Http Status 203 Success"
    )
    @GetMapping("/nameEnquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest request){
        return userService.nameEnquiry(request);
    }
    @Operation(
            summary = "Add money in your account"
    )
    @ApiResponse(
            responseCode = "204",
            description = "Http Status 204 Success"
    )
    @PostMapping("/credit")
    public BankResponse creditAmount(@RequestBody CreditDebitRequest request){
        return userService.creditAccount(request);
    }
    @Operation(
            summary = "Withdraw money from your account"
    )
    @ApiResponse(
            responseCode = "205",
            description = "Http Status 205 Success"
    )
    @PostMapping("/debit")
    public BankResponse debitAmount(@RequestBody CreditDebitRequest request){
        return userService.debitAccount(request);
    }@Operation(
            summary = "Transfer"
    )
    @ApiResponse(
            responseCode = "206",
            description = "Http Status 206   Success"
    )
    @PostMapping("/transfer")
    public BankResponse transferAmount(@RequestBody TransferRequest request){
        return userService.transfer(request);
    }





}
