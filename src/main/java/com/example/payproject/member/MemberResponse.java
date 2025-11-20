package com.example.payproject.member;

import com.example.payproject.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Schema(description = "회원 정보")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {

    @Schema(description = "유저의 UUID")
    private UUID id;

    @Schema(description = "유저의 email")
    private String email;

    @Schema(description = "유저명")
    private String name;

    @Schema(description = "핸드폰 번호")
    private String phone;

    public static MemberResponse from(Member member){
        return new MemberResponse(
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getPhone()
        );
    }

}
