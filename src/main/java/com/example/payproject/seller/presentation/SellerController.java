package com.example.payproject.seller.presentation;

import com.example.payproject.common.ResponseEntity;
import com.example.payproject.product.application.dto.ProductInfo;
import com.example.payproject.product.presentation.dto.ProductRequest;
import com.example.payproject.seller.application.SellerService;
import com.example.payproject.seller.application.dto.SellerInfo;
import com.example.payproject.seller.presentation.dto.SellerRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.v1}/sellers")
public class SellerController {

    private final SellerService sellerService;

    @Operation(summary = "판매자 목록 조회", description = "판매자를 페이지 단위로 조회한다.")
    @GetMapping
    public ResponseEntity<List<SellerInfo>> findAll(Pageable pageable) {
        return sellerService.findAll(pageable);
    }

    @Operation(summary = "판매자 등록", description = "판매자를 등록한다.")
    @PostMapping
    public ResponseEntity<SellerInfo> create(@RequestBody SellerRequest request) {
        return sellerService.create(request.toCommand());
    }

    @Operation(summary = "판매자 정보 수정", description = "판매자 정보를 수정한다.")
    @PutMapping("{id}")
    public ResponseEntity<SellerInfo> update(@PathVariable("id") UUID id,
                                              @RequestBody SellerRequest request) {
        return sellerService.update(id, request.toCommand());
    }

    @Operation(summary = "판매자 삭제", description = "판매자를 삭제한다.")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        return sellerService.delete(id);
    }

}
