package com.example.payproject.seller.infrastructure;

import com.example.payproject.seller.domain.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SellerJpaRepository extends JpaRepository<Seller, UUID> {
}
