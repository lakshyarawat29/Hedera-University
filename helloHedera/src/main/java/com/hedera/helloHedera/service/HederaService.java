package com.hedera.helloHedera.service;

import java.io.Serial;
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
import com.hedera.hashgraph.sdk.NftId;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TokenAssociateTransaction;
import com.hedera.hashgraph.sdk.TokenBurnTransaction;
import com.hedera.hashgraph.sdk.TokenCreateTransaction;
import com.hedera.hashgraph.sdk.TokenFreezeTransaction;
import com.hedera.hashgraph.sdk.TokenGrantKycTransaction;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TokenInfo;
import com.hedera.hashgraph.sdk.TokenInfoQuery;
import com.hedera.hashgraph.sdk.TokenMintTransaction;
import com.hedera.hashgraph.sdk.TokenPauseTransaction;
import com.hedera.hashgraph.sdk.TokenRelationship;
import com.hedera.hashgraph.sdk.TokenRevokeKycTransaction;
import com.hedera.hashgraph.sdk.TokenType;
import com.hedera.hashgraph.sdk.TokenUnfreezeTransaction;
import com.hedera.hashgraph.sdk.TokenUnpauseTransaction;
import com.hedera.hashgraph.sdk.TokenUpdateTransaction;
import com.hedera.hashgraph.sdk.TransactionReceipt;
import com.hedera.hashgraph.sdk.TransactionResponse;
import com.hedera.hashgraph.sdk.TransferTransaction;
import com.hedera.hashgraph.sdk.proto.TokenKycStatus;

import ch.qos.logback.core.subst.Token;



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
  public TransactionReceipt associateAccountWithToken(AccountId recieverAccountId, TokenId tokenId) throws Exception{
    Client client = Client.forTestnet();
    client.setOperator(recieverAccountId, recieverOperatorKey());
    // Implementation for associating account with token
    return new TokenAssociateTransaction().setAccountId(recieverAccountId).setTokenIds(new ArrayList<>(List.of(tokenId))).execute(client).getReceipt(client);
  }

  //Receiver is agreeing to hold this token. that is why reciever signs this transaction. The account that wants to hold the token must sign the transaction to associate itself with the token. This is a security measure to ensure that the account owner consents to holding the token.

  //Instead of asking: "Which account do I set as the operator?" Ask: "Who is authorizing this action?"

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

  //create an nft collection on Hedera, allowing users to create unique digital assets that can represent ownership or proof of authenticity for various items such as art, collectibles, or other digital content.
  public TransactionReceipt createNFTCollection() throws Exception{
    Client client = Client.forTestnet();
    client.setOperator(AccountId.fromString(accountId), operatorKey());
    return new TokenCreateTransaction().setTokenName("LakshyaNFT").setTokenSymbol("LKNFT").setTokenType(TokenType.NON_FUNGIBLE_UNIQUE).setTreasuryAccountId(AccountId.fromString(accountId)).setSupplyKey(operatorKey()).execute(client).getReceipt(client);
  }

  //minting within the nft collection on Hedera, allowing users to create new unique tokens within the collection and expand their digital asset offerings.
  public TransactionReceipt mintNFTCollection(TokenId tokenId) throws Exception{
    Client client = Client.forTestnet();
    client.setOperator(AccountId.fromString(accountId), operatorKey());
    return new TokenMintTransaction().setTokenId(tokenId).setMetadata(new ArrayList<>(List.of("metadata for the first Lakshya Collection NFT".getBytes()))).execute(client).getReceipt(client);
  }

  //transfering nft from collection to another account on Hedera, enabling the movement of unique digital assets between accounts for various purposes such as sales, trades, or other transactions.
  public TransactionReceipt transferNFTCollection(TokenId tokenId, long serialNumber) throws Exception{
    Client client = Client.forTestnet();
    client.setOperator(AccountId.fromString(accountId), operatorKey());

    //associate the reciever account with the tokenId before transferring the NFT

    //NFT Identity = Token ID+ Serial Number
    associateAccountWithToken(AccountId.fromString(recieverAccountId), tokenId);
    return new TransferTransaction().addNftTransfer(new NftId(tokenId, serialNumber), AccountId.fromString(accountId), AccountId.fromString(recieverAccountId)).execute(client).getReceipt(client);
  }

  //create a token with kyc enables access
  public TransactionReceipt createKycToken() throws Exception{
    Client client = Client.forTestnet();
    client.setOperator(AccountId.fromString(accountId), operatorKey());
    return new TokenCreateTransaction()
      .setTokenName("LakshyaKycToken")
      .setTokenSymbol("LKYC")
      .setInitialSupply(1000000)
      .setDecimals(1)
      .setTreasuryAccountId(AccountId.fromString(accountId))
      .setAdminKey(operatorKey())
      .setSupplyKey(operatorKey())
      .setKycKey(operatorKey())
      .execute(client)
      .getReceipt(client);
  }

  //granting KYC to an account for a specific token on Hedera, allowing the account to hold and interact with the token while ensuring compliance with regulatory requirements.

  //associating the accont and then performing the KYC grant operation for the account and token on Hedera, enabling the account to access and utilize the token while adhering to KYC regulations.
  public TransactionReceipt grantKycToAccount(AccountId rec_accountId, TokenId tokenId) throws Exception{
    Client client = Client.forTestnet();
    client.setOperator(AccountId.fromString(accountId), operatorKey());
    //associateAccountWithToken(rec_accountId, tokenId);
    return new TokenGrantKycTransaction()
      .setAccountId(rec_accountId)
      .setTokenId(tokenId)
      .execute(client)
      .getReceipt(client);
  }

  //important things to note the kyc key only set kyc key for the token, and then you can grant kyc to any account for that token
  //the account cannot set its own kyc key, only the token creator can set the kyc key for the token, and then the token creator can grant kyc to any account for that token
  //so what we were doing is we were trying to set the kyc key for the account, but that is not possible, only the token creator can set the kyc key for the token, and then the token creator can grant kyc to any account for that token
  //client.setOperator(AccountId.fromString(accountId), operatorKey());
  //errors we faced : InvalidSignature: The transaction has an invalid signature because the account does not have the KYC key for the token, so we need to set the KYC key for the token first, and then we can grant kyc to any account for that token
  //Account already associated with token: 0.0.9846074, so we need to associate the account with the token first, and then we can grant kyc to any account for that token


  //concept : So KYC is the relationship between the account and the token, 
  //check the status of the kyc for the account and the token
  public boolean checkKycStatus(AccountId rec_accountId,TokenId tokenId) throws Exception{
    Client client = Client.forTestnet();
    client.setOperator(rec_accountId, operatorKey());
    AccountInfo info = new AccountInfoQuery().setAccountId(rec_accountId).execute(client);
    return info.tokenRelationships.get(tokenId).kycStatus == null ? false : info.tokenRelationships.get(tokenId).kycStatus;
  }

  //revoke the granted KYC from an account for a specific token on Hedera, allowing the account to lose access and interaction with the token while ensuring compliance with regulatory requirements.
  public TransactionReceipt revokeKycFromAccount() throws Exception{
    Client client = Client.forTestnet();
    client.setOperator(AccountId.fromString(accountId), operatorKey());
    return new TokenRevokeKycTransaction()
      .setAccountId(AccountId.fromString(recieverAccountId))
      .setTokenId(TokenId.fromString("0.0.9873980"))
      .execute(client)
      .getReceipt(client);
  }

  //http://localhost:8080/checkrevokekyc/0.0.6914158/0.0.9873980 : Checks for both the account and the token, allowing users to verify whether the KYC has been revoked and if the account still has access to the token

  //checking the revoke status for the account and the token, allowing users to verify whether the KYC has been revoked and if the account still has access to the token
  public TokenRelationship checkRevokeKycStatus(AccountId receiverAccountId,
                                     TokenId tokenId) throws Exception {

    Client client = Client.forTestnet();
    client.setOperator(AccountId.fromString(accountId), operatorKey());

    AccountInfo info = new AccountInfoQuery()
            .setAccountId(receiverAccountId)
            .execute(client);

    return info.tokenRelationships.get(tokenId);
  }

  //lets create a token with freeze key, and then we can freeze and unfreeze the account for that token on Hedera, allowing the account to lose access and interaction with the token while ensuring compliance with regulatory requirements.
  public TransactionReceipt createFreezeToken() throws Exception{
    Client client = Client.forTestnet();
    client.setOperator(AccountId.fromString(accountId), operatorKey());
    return new TokenCreateTransaction()
      .setTokenName("LakshyaFreezeToken")
      .setTokenSymbol("LKFT")
      .setInitialSupply(1000000)
      .setDecimals(1)
      .setTreasuryAccountId(AccountId.fromString(accountId))
      .setAdminKey(operatorKey())
      .setSupplyKey(operatorKey())
      .setFreezeKey(operatorKey())
      .execute(client)
      .getReceipt(client);
  }

  //freeze the account for a specific token on Hedera, allowing the account to lose access and interaction with the token while ensuring compliance with regulatory requirements.
  public TransactionReceipt freezeAccountForToken(AccountId rec_accountId, TokenId tokenId) throws Exception{
    Client client = Client.forTestnet();
    client.setOperator(AccountId.fromString(accountId), operatorKey());
    associateAccountWithToken(rec_accountId, tokenId);
    return new TokenFreezeTransaction()
      .setAccountId(rec_accountId)
      .setTokenId(tokenId)
      .execute(client)
      .getReceipt(client);
  }

  //check the freeze status for the account and the token, allowing users to verify whether the account is frozen and if it still has access to the token
  public TokenRelationship checkFreezeStatus(AccountId receiverAccountId,TokenId tokenId) throws Exception {
    Client client = Client.forTestnet();
    client.setOperator(AccountId.fromString(accountId), operatorKey());
    AccountInfo info = new AccountInfoQuery().setAccountId(receiverAccountId).execute(client);
    return info.tokenRelationships.get(tokenId);
  }

  //revoke the freeze status for the account and the token, allowing users to verify whether the account is unfrozen and if it still has access to the token
  public TransactionReceipt unfreezeAccountForToken(AccountId rec_accountId, TokenId tokenId) throws Exception{
    Client client = Client.forTestnet();
    client.setOperator(AccountId.fromString(accountId), operatorKey());
    return new TokenUnfreezeTransaction().setAccountId(rec_accountId).setTokenId(tokenId).execute(client).getReceipt(client);
  }

  //lets create a token with pause key, and then we can pause and unpause the account for that token on Hedera, allowing the account to lose access and interaction with the token while ensuring compliance with regulatory requirements.
  public TransactionReceipt createPauseToken() throws Exception{
    Client client = Client.forTestnet();
    client.setOperator(AccountId.fromString(accountId), operatorKey());
    return new TokenCreateTransaction()
      .setTokenName("LakshyaPauseToken")
      .setTokenSymbol("LKPT")
      .setInitialSupply(1000000)
      .setDecimals(1)
      .setTreasuryAccountId(AccountId.fromString(accountId))
      .setAdminKey(operatorKey())
      .setSupplyKey(operatorKey())
      .setPauseKey(operatorKey())
      .setFreezeKey(operatorKey())
      .execute(client)
      .getReceipt(client);
  }

  //lets pause the token 
  public TransactionReceipt pauseToken(TokenId tokenId) throws Exception{
    Client client = Client.forTestnet();
    client.setOperator(AccountId.fromString(accountId), operatorKey());
    return new TokenPauseTransaction().setTokenId(tokenId).execute(client).getReceipt(client);
  }

  //check the pause status for the account and the token, allowing users to verify whether the account is paused and if it still has access to the token
  public TokenInfo checkPauseStatus(TokenId tokenId) throws Exception {
    Client client = Client.forTestnet();
    client.setOperator(AccountId.fromString(accountId), operatorKey());
    TokenInfo info = new TokenInfoQuery().setTokenId(tokenId).execute(client);
    return info;
  }

  //resume the token 
  public TransactionReceipt unpauseToken(TokenId tokenId) throws Exception{
    Client client = Client.forTestnet();
    client.setOperator(AccountId.fromString(accountId), operatorKey());
    return new TokenUnpauseTransaction().setTokenId(tokenId).execute(client).getReceipt(client);
  } 

  //lets create the wipe tokens and account for the token on Hedera, allowing the account to lose access and interaction with the token while ensuring compliance with regulatory requirements.

  public TransactionReceipt createWipeToken() throws Exception{
    Client client = Client.forTestnet();
    client.setOperator(AccountId.fromString(accountId), operatorKey());
    return new TokenCreateTransaction()
      .setTokenName("LakshyaWipeToken")
      .setTokenSymbol("LKWT")
      .setInitialSupply(1000000)
      .setDecimals(1)
      .setTreasuryAccountId(AccountId.fromString(accountId))
      .setAdminKey(operatorKey())
      .setSupplyKey(operatorKey())
      .setWipeKey(operatorKey())
      .execute(client)
      .getReceipt(client);
  }

  //creating an account that contain the token that needs to be wiped, allowing the account to lose access and interaction with the token while ensuring compliance with regulatory requirements.
}