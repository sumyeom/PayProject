package com.example.payproject.member.application;

import com.example.payproject.common.ResponseEntity;
import com.example.payproject.member.domain.Member;
import com.example.payproject.member.domain.MemberRepository;
import com.example.payproject.member.infrastructure.MemberJpaRepository;
import com.example.payproject.member.application.dto.MemberCommand;
import com.example.payproject.member.application.dto.MemberInfo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public ResponseEntity<List<MemberInfo>> findAllMember(Pageable pageable) {
        Page<Member> page = memberRepository.findAll(pageable);
        List<MemberInfo> responses = page.stream()
                .map(MemberInfo::from)
                .toList();
        return new ResponseEntity<>(HttpStatus.OK.value(), responses, page.getTotalElements());
    }


    @Transactional
    public ResponseEntity<MemberInfo> create(MemberCommand command){
        Member member = Member.create(
                command.email(),
                command.name(),
                command.password(),
                command.phone(),
                command.saltKey(),
                command.flag()
        );
        Member member1 = memberRepository.save(member);
        int cnt = 0;
        if(member1 instanceof  List){
            cnt = ((List<?>) member1).size();

        }else{
            cnt=1;
        }
        MemberInfo response = MemberInfo.from(member1);
        return new ResponseEntity<>(HttpStatus.OK.value(), response, cnt);
    }

    @Transactional
    public ResponseEntity<MemberInfo> update(MemberCommand command, String id){
        Member findMember = memberRepository.findById(UUID.fromString(id)).
                orElseThrow(()->new IllegalArgumentException("회원이 존재하지 않습니다."));

        findMember.updateInfo(command.email(), command.name(), command.password(), command.phone(), command.saltKey(),command.flag());
        MemberInfo response = MemberInfo.from(findMember);
        return new ResponseEntity<>(HttpStatus.OK.value(), response, 1);
    }

    @Transactional
    public ResponseEntity<Void> delete(String id){
        memberRepository.deleteById(UUID.fromString(id));
        return new ResponseEntity<>(HttpStatus.OK.value(), null, 0);
    }
}
