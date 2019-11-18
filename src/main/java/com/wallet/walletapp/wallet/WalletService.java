package com.wallet.walletapp.wallet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletService {
    @Autowired
    WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    Wallet fetch(Long id) throws WalletNotFoundException {
        return walletRepository.findById(id).orElseThrow(WalletNotFoundException::new);
    }
}
