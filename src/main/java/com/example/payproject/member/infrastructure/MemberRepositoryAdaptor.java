package com.example.payproject.member.infrastructure;

import com.example.payproject.member.domain.Member;
import com.example.payproject.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryAdaptor implements MemberRepository {
    private final MemberJpaRepository memberJpaRepository;

    @Override
    public Page<Member> findAll(Pageable pageable){return memberJpaRepository.findAll(pageable);}

    @Override
    public Optional<Member> findById(UUID id) {return memberJpaRepository.findById(id);}

    @Override
    public Member save(Member member){return memberJpaRepository.save(member);}

    @Override
    public void deleteById(UUID id){memberJpaRepository.deleteById(id);}
}
