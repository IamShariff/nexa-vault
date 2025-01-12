package com.nexavault.service.impl;

import org.bitcoinj.core.Base58;
import org.p2p.solanaj.core.Account;
import org.p2p.solanaj.core.PublicKey;
import org.p2p.solanaj.rpc.RpcClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nexavault.service.SolanaService;

@Service
public class SolanaServiceImpl implements SolanaService {

	@Value("${solana.rpc.url}")
    private String solanaRpcUrl;

    @Value("${wallet.secret.key}")
    private String walletSecretKey;
    

    @Override
    public String mintNFT(String ipfsHash) throws Exception {
        RpcClient client = new RpcClient(solanaRpcUrl);

        // Decode wallet secret key
        Account wallet = new Account(Base58.decode(walletSecretKey));

        // Use wallet's public key directly (No need for new PublicKey())
        PublicKey mint = wallet.getPublicKey();

        // Request airdrop (1,000,000 lamports)
        return client.getApi().requestAirdrop(mint, 1000000);
    }
}
