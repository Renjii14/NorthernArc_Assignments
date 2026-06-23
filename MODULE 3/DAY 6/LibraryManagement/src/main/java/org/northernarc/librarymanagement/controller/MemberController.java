package org.northernarc.librarymanagement.controller;

import jakarta.validation.Valid;
import org.northernarc.librarymanagement.dto.MemberRequestDTO;
import org.northernarc.librarymanagement.dto.MemberResponseDTO;
import org.northernarc.librarymanagement.dto.MemberUpdateDTO;
import org.northernarc.librarymanagement.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberResponseDTO> addMember(
            @Valid @RequestBody MemberRequestDTO memberRequestDTO) {

        return ResponseEntity.ok(
                memberService.addMember(memberRequestDTO));
    }

    @PutMapping("/{memberId}")
    public ResponseEntity<MemberResponseDTO> updateMember(
            @PathVariable Long memberId,
            @Valid @RequestBody MemberUpdateDTO memberUpdateDTO) {

        return ResponseEntity.ok(
                memberService.updateMember(
                        memberId,
                        memberUpdateDTO));
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponseDTO> getMemberById(
            @PathVariable Long memberId) {

        return ResponseEntity.ok(
                memberService.getMemberById(memberId));
    }

    @GetMapping
    public ResponseEntity<List<MemberResponseDTO>> getAllMembers() {

        return ResponseEntity.ok(
                memberService.getAllMembers());
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<String> deleteMember(
            @PathVariable Long memberId) {

        memberService.deleteMember(memberId);

        return ResponseEntity.ok(
                "Member deleted successfully");
    }
}