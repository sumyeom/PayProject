package com.example.payproject.settlement.infrastructure;

import com.example.payproject.settlement.domain.SellerSettlement;
import com.example.payproject.settlement.domain.SellerSettlementRepository;
import com.example.payproject.settlement.domain.SettlementStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SellerSettlementRepositoryAdaptor implements SellerSettlementRepository {
    private final SellerSettlementJpaRepository jpaRepository;

    @Override
    public SellerSettlement save(SellerSettlement settlement) {
        return jpaRepository.save(settlement);
    }

    @Override
    public List<SellerSettlement> findByStatus(SettlementStatus status) {
        return jpaRepository.findByStatus(status);
    }

    @Override
    public List<SellerSettlement> findByStatusAndSeller(SettlementStatus status, UUID sellerId) {
        return jpaRepository.findByStatusAndSellerId(status, sellerId);
    }

    @Override
    public void saveAll(List<SellerSettlement> settlements) {
        jpaRepository.saveAll(settlements);
    }
}
