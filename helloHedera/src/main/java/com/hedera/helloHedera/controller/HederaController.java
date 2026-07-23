package com.hedera.helloHedera.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
