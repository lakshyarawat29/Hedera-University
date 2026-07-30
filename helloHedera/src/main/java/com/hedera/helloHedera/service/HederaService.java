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
import com.hedera.hashgraph.sdk.TokenBurnTransaction;
import com.hedera.hashgraph.sdk.TokenCreateTransaction;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TokenInfo;
import com.hedera.hashgraph.sdk.TokenInfoQuery;
import com.hedera.hashgraph.sdk.TokenMintTransaction;
import com.hedera.hashgraph.sdk.TokenUpdateTransaction;
import com.hedera.hashgraph.sdk.TransactionReceipt;
import com.hedera.hashgraph.sdk.TransactionResponse;
import com.hedera.hashgraph.sdk.TransferTransaction;

import ch.qos.logback.core.subst.Token;


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


  //creating a fungible token on Hedera, which can be used for various purposes such as representing assets, currencies, or other digital items.
  public TransactionReceipt createFungibleTokenService() throws Exception{
    Client client = Client.forTestnet();
    client.setOperator(AccountId.fromString(accountId), operatorKey());

    TokenCreateTransaction token = new TokenCreateTransaction().setTokenName("LakshyaCoin").setTokenSymbol("LKC").setInitialSupply(1000000).setDecimals(1).setTreasuryAccountId(AccountId.fromString(accountId));

    TransactionResponse response = token.execute(client);
    TransactionReceipt receipt = response.getReceipt(client);

    return receipt;
  }

    //creating a fungible token on Hedera, which can be used for various purposes such as representing assets, currencies, or other digital items.
  public TransactionReceipt createFungibleTokenServiceV2() throws Exception{
    Client client = Client.forTestnet();
    client.setOperator(AccountId.fromString(accountId), operatorKey());

    TokenCreateTransaction token = new TokenCreateTransaction().setTokenName("LakshyaCoin2").setTokenSymbol("LKC2").setInitialSupply(1000000).setDecimals(1).setTreasuryAccountId(AccountId.fromString(accountId)).setAdminKey(operatorKey()).setSupplyKey(operatorKey());

    TransactionResponse response = token.execute(client);
    TransactionReceipt receipt = response.getReceipt(client);

    return receipt;
  }



  //get token information about the fungible token created on Hedera, including details such as its name, symbol, total supply, and other relevant attributes.
  public TokenInfo getTokenInfoService() throws Exception{
    Client client = Client.forTestnet();
    client.setOperator(AccountId.fromString(accountId), operatorKey());

    TokenInfo info = new TokenInfoQuery().setTokenId(TokenId.fromString("0.0.9846074")).execute(client);
    return info;
  }

  //update token information about the fungible token on Hedera, allowing for modifications to its attributes such as name, symbol, or other relevant details.
  public TransactionReceipt updateTokenInfoService() throws Exception{
    Client client = Client.forTestnet();
    client.setOperator(AccountId.fromString(accountId), operatorKey());
    TokenUpdateTransaction update = new TokenUpdateTransaction().setTokenId(TokenId.fromString("0.0.9844172")).setAdminKey(operatorKey()).setSupplyKey(operatorKey());
    TransactionResponse response = update.execute(client);
    TransactionReceipt receipt = response.getReceipt(client);
    return receipt;
  }

  //mint more fungible token on Hedera to increase the total supply of the token, allowing for additional tokens to be created and distributed.
  public TransactionReceipt mintMoreFungibleTokens() throws Exception{
    Client client = Client.forTestnet();
    client.setOperator(AccountId.fromString(accountId), operatorKey());
    //updateTokenInfoService();
    return new TokenMintTransaction().setTokenId(TokenId.fromString("0.0.9846074")).setAmount(5000).execute(client).getReceipt(client);
  }

  //token burn transaction
  public TransactionReceipt burnFungibleTokens() throws Exception{
    Client client = Client.forTestnet();
    client.setOperator(AccountId.fromString(accountId), operatorKey());
    return new TokenBurnTransaction().setTokenId(TokenId.fromString("0.0.9846074")).setAmount(2500).execute(client).getReceipt(client);
  }
}
