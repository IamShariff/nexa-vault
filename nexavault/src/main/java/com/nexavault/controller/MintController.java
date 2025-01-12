package com.nexavault.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexavault.service.SolanaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/mint")
@PreAuthorize("hasAuthority('USER')")
public class MintController {

    private final SolanaService solanaService;

    @PostMapping
    public ResponseEntity<?> mintNFT(@RequestBody String ipfsHash) {
        try {
            String signature = solanaService.mintNFT(ipfsHash);
            return ResponseEntity.ok("{\"signature\":\"" + signature + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Minting failed");
        }
    }
}
