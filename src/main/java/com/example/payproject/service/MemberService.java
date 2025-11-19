package com.example.payproject.service;

import com.example.payproject.common.ResponseEntity;
import com.example.payproject.member.Member;
import com.example.payproject.member.MemberRepository;
import com.example.payproject.member.MemberRequest;
import com.example.payproject.member.MemberResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public ResponseEntity<List<MemberResponse>> findAllMember() {
        List<Member> members = memberRepository.findAll();
        List<MemberResponse> responses = members.stream()
                .map(MemberResponse::from)
                .toList();
        return new ResponseEntity<>(HttpStatus.OK.value(), responses, memberRepository.count());
    }


    @Transactional
    public ResponseEntity<MemberResponse> create(MemberRequest request){
        Member member = new Member(
                UUID.randomUUID(),
                request.email(),
                request.name(),
                request.password(),
                request.phone(),
                request.saltKey(),
                request.flag()
        );
        Member member1 = memberRepository.save(member);
        int cnt = 0;
        if(member1 instanceof  List){
            cnt = ((List<?>) member1).size();

        }else{
            cnt=1;
        }
        MemberResponse response = MemberResponse.from(member1);
        return new ResponseEntity<>(HttpStatus.OK.value(), response, cnt);
    }

    @Transactional
    public ResponseEntity<MemberResponse> update(MemberRequest request, String id){
        Member findMember = memberRepository.findById(UUID.fromString(id)).
                orElseThrow(()->new IllegalArgumentException("회원이 존재하지 않습니다."));

        findMember.updateInfo(request.email(), request.name(), request.password(), request.phone(), request.saltKey(),request.flag());
        MemberResponse response = MemberResponse.from(findMember);
        return new ResponseEntity<>(HttpStatus.OK.value(), response, 1);
    }

    @Transactional
    public ResponseEntity<Void> delete(String id){
        memberRepository.deleteById(UUID.fromString(id));
        return new ResponseEntity<>(HttpStatus.OK.value(), null, 0);
    }
}
