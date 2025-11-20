package com.example.payproject.seller.infrastructure;

import com.example.payproject.seller.domain.Seller;
import com.example.payproject.seller.domain.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SellerRepositoryAdaptor implements SellerRepository {

    private final SellerJpaRepository sellerJpaRepository;

    @Override
    public Page<Seller> findAll(Pageable pageable) {
        return sellerJpaRepository.findAll(pageable);
    }

    @Override
    public Optional<Seller> findById(UUID id) {
        return sellerJpaRepository.findById(id);
    }

    @Override
    public Seller save(Seller seller) {
        return sellerJpaRepository.save(seller);
    }

    @Override
    public void deleteById(UUID id) {
        sellerJpaRepository.deleteById(id);
    }
}
