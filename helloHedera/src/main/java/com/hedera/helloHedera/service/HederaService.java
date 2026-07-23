package com.hedera.helloHedera.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hedera.hashgraph.sdk.AccountBalance;
import com.hedera.hashgraph.sdk.AccountBalanceQuery;
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.AccountInfo;
import com.hedera.hashgraph.sdk.AccountInfoQuery;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TransactionReceipt;
import com.hedera.hashgraph.sdk.TransactionResponse;
import com.hedera.hashgraph.sdk.TransferTransaction;


@Service
public class HederaService {
  @Value("${hedera.account-id}")
  private String accountId;

  @Value("${hedera.private-key}")
  private String privateKey;

  private PrivateKey operatorKey() {
    return PrivateKey.fromStringECDSA(
        privateKey.substring(2)
    );
  }

  public String getBalance() throws Exception {
    Client client = Client.forTestnet();
    client.setOperator(AccountId.fromString(accountId), operatorKey());

    AccountBalance balance = new AccountBalanceQuery().setAccountId(AccountId.fromString(accountId)).execute(client);

    return balance.hbars.toString();
  }

  public TransactionReceipt sendHbarService() throws Exception{
    Client client = Client.forTestnet();
    client.setOperator(AccountId.fromString(accountId), operatorKey());

    TransferTransaction transaction = new TransferTransaction();

    transaction.addHbarTransfer(AccountId.fromString(accountId),
    Hbar.from(-1)).addHbarTransfer(AccountId.fromString("0.0.6914158"),
    Hbar.from(1));

    TransactionResponse response = transaction.execute(client);
    TransactionReceipt receipt = response.getReceipt(client);

    return receipt;
  }


  //basic information about your Hedera account.
  public AccountInfo getAccountInfoService() throws Exception{
    Client client = Client.forTestnet();
    client.setOperator(AccountId.fromString(accountId), operatorKey());

    AccountInfo info = new AccountInfoQuery().setAccountId(AccountId.fromString(accountId)).execute(client);
    return info;
  }



}
