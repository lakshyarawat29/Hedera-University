package com.hedera.helloHedera.controller;

import java.net.http.HttpResponse.ResponseInfo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hedera.hashgraph.sdk.AccountBalance;
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.AccountInfo;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TokenInfo;
import com.hedera.hashgraph.sdk.TransactionReceipt;
import com.hedera.helloHedera.service.HederaService;

@RestController
public class HederaController {
    private final HederaService hederaService;

    public HederaController(HederaService hederaService){
      this.hederaService = hederaService;
    }

    @GetMapping("/balance")
    public String balance() throws Exception{
      return hederaService.getBalance();
    }


    @PostMapping("/transfer")
    public ResponseEntity<TransactionReceipt> transfer() throws Exception{
      //String res = hederaService.sendHbarService();
      return ResponseEntity.ok(hederaService.sendHbarService());
    }

    @GetMapping("/info")
    public ResponseEntity<AccountInfo> info() throws Exception{
      return ResponseEntity.ok(hederaService.getAccountInfoService());
    }

    @PostMapping("/ft")
    public ResponseEntity<TransactionReceipt> createFT() throws Exception{
      return ResponseEntity.ok(hederaService.createFungibleTokenService());
    }

    @PostMapping("/ft2")
    public ResponseEntity<TransactionReceipt> createFT2() throws Exception{
      return ResponseEntity.ok(hederaService.createFungibleTokenServiceV2());
    }

    @GetMapping("/ftinfo/{tokenId}")
    public ResponseEntity<TokenInfo> ftinfo(@PathVariable String tokenId) throws Exception{
      return ResponseEntity.ok(hederaService.getTokenInfoService(TokenId.fromString(tokenId)));
    }

    @PostMapping("/mintft")
    public ResponseEntity<TransactionReceipt> mintft() throws Exception{
      return ResponseEntity.ok(hederaService.mintMoreFungibleTokens());
    }

    @PostMapping("/burnft")
    public ResponseEntity<TransactionReceipt> burnft() throws Exception{
      return ResponseEntity.ok(hederaService.burnFungibleTokens());
    }

    @PostMapping("/associate/{accountId}")
    public ResponseEntity<TransactionReceipt> associate(@PathVariable String accountId) throws Exception{
      return ResponseEntity.ok(hederaService.associateAccountWithToken(AccountId.fromString(accountId)));
    }

    @PostMapping("/transferft/{amount}")
    public ResponseEntity<TransactionReceipt> transferft(@PathVariable long amount) throws Exception{
      return ResponseEntity.ok(hederaService.transferFungibleToken(amount));
    }

    @GetMapping("/ftbalance/{accountId}")
    public ResponseEntity<AccountBalance> ftbalance(@PathVariable String accountId) throws Exception{
      return ResponseEntity.ok(hederaService.getFungibleTokenBalance(AccountId.fromString(accountId)));
    }
}
