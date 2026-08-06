package com.hedera.helloHedera.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/mintft/{tokenId}/{amount}")
    public ResponseEntity<TransactionReceipt> mintft(@PathVariable String tokenId, @PathVariable long amount) throws Exception{
      return ResponseEntity.ok(hederaService.mintMoreFungibleTokens(TokenId.fromString(tokenId), amount));
    }

    @PostMapping("/burnft/{tokenId}/{amount}")
    public ResponseEntity<TransactionReceipt> burnft(@PathVariable String tokenId, @PathVariable long amount) throws Exception{
      return ResponseEntity.ok(hederaService.burnFungibleTokens(TokenId.fromString(tokenId), amount));
    }

    @PostMapping("/associate/{accountId}/{tokenId}")
    public ResponseEntity<TransactionReceipt> associate(@PathVariable String accountId, @PathVariable String tokenId) throws Exception{
      return ResponseEntity.ok(hederaService.associateAccountWithToken(AccountId.fromString(accountId), TokenId.fromString(tokenId)));
    }

    @PostMapping("/transferft/{accountId}/{tokenId}/{amount}")
    public ResponseEntity<TransactionReceipt> transferft(@PathVariable String accountId, @PathVariable String tokenId, @PathVariable long amount) throws Exception{
      return ResponseEntity.ok(hederaService.transferFungibleToken(AccountId.fromString(accountId), TokenId.fromString(tokenId), amount));
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

    @PostMapping("/freeze")
    public ResponseEntity<TransactionReceipt> freeze() throws Exception{
      return ResponseEntity.ok(hederaService.createFreezeToken());
    }

    @PostMapping("/freeze/{accountId}/{tokenId}")
    public ResponseEntity<TransactionReceipt> freeze(@PathVariable String accountId, @PathVariable String tokenId) throws Exception{
      return ResponseEntity.ok(hederaService.freezeAccountForToken(AccountId.fromString(accountId), TokenId.fromString(tokenId)));
    }

    @GetMapping("/checkfreeze/{accountId}/{tokenId}")
    public ResponseEntity<TokenRelationship> checkfreeze(@PathVariable String accountId, @PathVariable String tokenId) throws Exception{
      return ResponseEntity.ok(hederaService.checkFreezeStatus(AccountId.fromString(accountId), TokenId.fromString(tokenId)));
    }

    @PostMapping("/unfreeze/{accountId}/{tokenId}")
    public ResponseEntity<TransactionReceipt> unfreeze(@PathVariable String accountId, @PathVariable String tokenId) throws Exception{
      return ResponseEntity.ok(hederaService.unfreezeAccountForToken(AccountId.fromString(accountId), TokenId.fromString(tokenId)));
    }

    @PostMapping("/pause")
    public ResponseEntity<TransactionReceipt> pause() throws Exception{
      return ResponseEntity.ok(hederaService.createPauseToken());
    }

    @PostMapping("/pause/{tokenId}")
    public ResponseEntity<TransactionReceipt> pause(@PathVariable String tokenId) throws Exception{
      return ResponseEntity.ok(hederaService.pauseToken(TokenId.fromString(tokenId)));  
    }

    @GetMapping("/checkpause/{tokenId}")
    public ResponseEntity<TokenInfo> checkpause(@PathVariable String tokenId) throws Exception{
      return ResponseEntity.ok(hederaService.checkPauseStatus(TokenId.fromString(tokenId)));
    }

    @PostMapping("/unpause/{tokenId}")
    public ResponseEntity<TransactionReceipt> unpause(@PathVariable String tokenId) throws Exception{
      return ResponseEntity.ok(hederaService.unpauseToken(TokenId.fromString(tokenId)));
    }

    @PostMapping("/wipetoken")
    public ResponseEntity<TransactionReceipt> wipetoken() throws Exception{
      return ResponseEntity.ok(hederaService.createWipeToken());
    }

    @PostMapping("/wipetoken/{accountId}/{tokenId}/{amount}")
    public ResponseEntity<TransactionReceipt> wipetoken(@PathVariable String accountId, @PathVariable String tokenId, @PathVariable long amount) throws Exception{
      return ResponseEntity.ok(hederaService.wipeAccountForToken(AccountId.fromString(accountId), TokenId.fromString(tokenId), amount));
    }

    @PostMapping("deletetoken")
    public ResponseEntity<TransactionReceipt> deletetoken() throws Exception{
      return ResponseEntity.ok(hederaService.createDeleteToken());
    }

    @PostMapping("/deletetoken/{tokenId}")
    public ResponseEntity<TransactionReceipt> deletetoken(@PathVariable String tokenId) throws Exception{
      return ResponseEntity.ok(hederaService.deleteToken(TokenId.fromString(tokenId)));
    }

    @PostMapping("/feetoken")
    public ResponseEntity<TransactionReceipt> feetoken() throws Exception{
      return ResponseEntity.ok(hederaService.createTokenWithTransferFeeSchedule());
    }

    @PostMapping("/updatetoken/{tokenId}")
    public ResponseEntity<TransactionReceipt> updatetoken(@PathVariable String tokenId) throws Exception{
      return ResponseEntity.ok(hederaService.updateTokenWithTransferFeeSchedule(TokenId.fromString(tokenId)));
    }

    @PostMapping("/fracfeetoken")
    public ResponseEntity<TransactionReceipt> fracfeetoken() throws Exception{
      return ResponseEntity.ok(hederaService.createTokenWithFractionalFeeSchedule());
    }
}
