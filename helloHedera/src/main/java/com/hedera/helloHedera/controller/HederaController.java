package com.hedera.helloHedera.controller;

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
import com.hedera.hashgraph.sdk.TokenRelationship;
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

    @PostMapping("/associate/{accountId}/{tokenId}")
    public ResponseEntity<TransactionReceipt> associate(@PathVariable String accountId, @PathVariable String tokenId) throws Exception{
      return ResponseEntity.ok(hederaService.associateAccountWithToken(AccountId.fromString(accountId), TokenId.fromString(tokenId)));
    }

    @PostMapping("/transferft/{amount}")
    public ResponseEntity<TransactionReceipt> transferft(@PathVariable long amount) throws Exception{
      return ResponseEntity.ok(hederaService.transferFungibleToken(amount));
    }

    @GetMapping("/ftbalance/{accountId}")
    public ResponseEntity<AccountBalance> ftbalance(@PathVariable String accountId) throws Exception{
      return ResponseEntity.ok(hederaService.getFungibleTokenBalance(AccountId.fromString(accountId)));
    }

    @PostMapping("/nftcollection")
    public ResponseEntity<TransactionReceipt> nftcollection() throws Exception{
      return ResponseEntity.ok(hederaService.createNFTCollection());
    }

    @PostMapping("/mintnft/{tokenId}")
    public ResponseEntity<TransactionReceipt> mintnft(@PathVariable String tokenId) throws Exception{
      return ResponseEntity.ok(hederaService.mintNFTCollection(TokenId.fromString(tokenId)));
    }

    @PostMapping("/transfernft/{tokenId}/{serialNumber}")
    public ResponseEntity<TransactionReceipt> transfernft(@PathVariable String tokenId, @PathVariable long serialNumber) throws Exception{
      return ResponseEntity.ok(hederaService.transferNFTCollection(TokenId.fromString(tokenId), serialNumber));
    } 

    @PostMapping("/kyctoken")
    public ResponseEntity<TransactionReceipt> kyctoken() throws Exception{
      return ResponseEntity.ok(hederaService.createKycToken());
    }

    @PostMapping("/grantkyc/{accountId}/{tokenId}")
    public ResponseEntity<TransactionReceipt> grantkyc(@PathVariable String accountId, @PathVariable String tokenId) throws Exception{
      return ResponseEntity.ok(hederaService.grantKycToAccount(AccountId.fromString(accountId), TokenId.fromString(tokenId)));
    } 

    @PostMapping("/revokekyc")
    public ResponseEntity<TransactionReceipt> revokekyc() throws Exception{
      return ResponseEntity.ok(hederaService.revokeKycFromAccount());
    }

    @GetMapping("/checkkyc/{accountId}/{tokenId}")
    public ResponseEntity<Boolean> checkkyc(@PathVariable String accountId, @PathVariable String tokenId) throws Exception{
      return ResponseEntity.ok(hederaService.checkKycStatus(AccountId.fromString(accountId), TokenId.fromString(tokenId)));
    } 

    @GetMapping("/checkrevokekyc/{accountId}/{tokenId}")
    public ResponseEntity<TokenRelationship> checkrevokekyc(@PathVariable String accountId, @PathVariable String tokenId) throws Exception{
      return ResponseEntity.ok(hederaService.checkRevokeKycStatus(AccountId.fromString(accountId), TokenId.fromString(tokenId)));
    }
}
