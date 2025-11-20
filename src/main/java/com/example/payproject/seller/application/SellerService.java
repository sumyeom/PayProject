package com.example.payproject.seller.application;

import com.example.payproject.common.ResponseEntity;
import com.example.payproject.product.application.dto.ProductCommand;
import com.example.payproject.product.application.dto.ProductInfo;
import com.example.payproject.product.domain.Product;
import com.example.payproject.seller.application.dto.SellerCommand;
import com.example.payproject.seller.application.dto.SellerInfo;
import com.example.payproject.seller.domain.Seller;
import com.example.payproject.seller.domain.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SellerService {
    private final SellerRepository sellerRepository;

    public ResponseEntity<List<SellerInfo>> findAll(Pageable pageable) {
        Page<Seller> page = sellerRepository.findAll(pageable);
        List<SellerInfo> products = page.stream()
                .map(SellerInfo::from)
                .toList();
        return new ResponseEntity<>(HttpStatus.OK.value(), products, page.getTotalElements());
    }

    public ResponseEntity<SellerInfo> create(SellerCommand command) {
        Seller seller = Seller.create(
                command.companyName(),
                command.representativeName(),
                command.email(),
                command.phone(),
                command.businessNumber(),
                command.address(),
                command.status()
        );

        Seller saved = sellerRepository.save(seller);
        return new ResponseEntity<>(HttpStatus.CREATED.value(), SellerInfo.from(saved), 1);
    }

    public ResponseEntity<SellerInfo> update(UUID id, SellerCommand command) {
        Seller seller = sellerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Seller not found: " + id));

        seller.updateSeller(
                command.companyName(),
                command.representativeName(),
                command.email(),
                command.phone(),
                command.businessNumber(),
                command.address(),
                command.status()
        );
        Seller updated = sellerRepository.save(seller);
        return new ResponseEntity<>(HttpStatus.OK.value(), SellerInfo.from(updated), 1);
    }

    public ResponseEntity<Void> delete(UUID id) {
        sellerRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT.value(), null, 0);
    }
}
