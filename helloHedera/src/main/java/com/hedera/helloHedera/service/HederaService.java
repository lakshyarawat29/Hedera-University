package com.hedera.helloHedera.service;

import java.util.ArrayList;
import java.util.List;

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
import com.hedera.hashgraph.sdk.TokenAssociateTransaction;
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



@Service
public class HederaService {
  @Value("${hedera.account-id}")
  private String accountId;

  @Value("${hedera.reciever.private-key}")
  private String recieverPrivateKey;

  @Value("${hedera.private-key}")
  private String privateKey;

  @Value("${hedera.reciever.account-id}")
  private String recieverAccountId;

  private PrivateKey operatorKey() {
    return PrivateKey.fromStringECDSA(
        privateKey.substring(2)
    );
  }

  private PrivateKey recieverOperatorKey() {
    return PrivateKey.fromStringECDSA(
        recieverPrivateKey.substring(2)
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
    Hbar.from(-1)).addHbarTransfer(AccountId.fromString(recieverAccountId),
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
  public TokenInfo getTokenInfoService(TokenId tokenId) throws Exception{
    Client client = Client.forTestnet();
    client.setOperator(AccountId.fromString(accountId), operatorKey());

    TokenInfo info = new TokenInfoQuery().setTokenId(tokenId).execute(client);
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

  //associate an account and a token on Hedera, allowing the account to hold and interact with the specified token. with request params from the user, you can associate any account with any token on Hedera.
  public TransactionReceipt associateAccountWithToken(AccountId recieverAccountId) throws Exception{
    Client client = Client.forTestnet();
    client.setOperator(recieverAccountId, recieverOperatorKey());
    // Implementation for associating account with token
    return new TokenAssociateTransaction().setAccountId(recieverAccountId).setTokenIds(new ArrayList<>(List.of(TokenId.fromString("0.0.9846074")))).execute(client).getReceipt(client);
  }

  //lets transfer LKC2 fungible token from one account to another on Hedera, enabling the movement of tokens between accounts for various purposes such as payments, transfers, or other transactions.

  public TransactionReceipt transferFungibleToken(long amount) throws Exception{
    Client client = Client.forTestnet();
    client.setOperator(AccountId.fromString(accountId), operatorKey());
    return new TransferTransaction().addTokenTransfer(TokenId.fromString("0.0.9846074"), AccountId.fromString(accountId), -1*amount).addTokenTransfer(TokenId.fromString("0.0.9846074"), AccountId.fromString(recieverAccountId), amount).execute(client).getReceipt(client);
  }

  //account balance of LKC2 fungible token for a specific account on Hedera, allowing users to check the balance of their tokens and track their holdings.
  public AccountBalance getFungibleTokenBalance(AccountId accountId) throws Exception{
    Client client = Client.forTestnet();
    client.setOperator(accountId, operatorKey());
    return new AccountBalanceQuery().setAccountId(accountId).execute(client);
  }
}