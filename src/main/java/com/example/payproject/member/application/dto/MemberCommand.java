package com.example.payproject.member.application.dto;

/**
 * Use-case 계층에서 회원 생성/수정 입력값을 전달하는 명령 객체.
 * 컨트롤러 등 외부 계층은 HTTP요청을 직접 전달하지 않고 이 DTO를 변환해
 * 애플리케이션 서비스에 필요한 정보만 남긴다.
 */
public record MemberCommand(
        String email,
        String name,
        String password,
        String phone,
        String saltKey,
        String flag
) {
}