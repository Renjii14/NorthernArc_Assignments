package org.northernarc.librarymanagement.service;

import org.northernarc.librarymanagement.dto.MemberRequestDTO;
import org.northernarc.librarymanagement.dto.MemberResponseDTO;
import org.northernarc.librarymanagement.dto.MemberUpdateDTO;
import org.northernarc.librarymanagement.exception.MemberNotFound;
import org.northernarc.librarymanagement.model.Member;
import org.northernarc.librarymanagement.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public MemberResponseDTO addMember(
            MemberRequestDTO memberRequestDTO) {

        Member member = mapToEntity(memberRequestDTO);

        Member saved =
                memberRepository.save(member);

        return mapToResponseDTO(saved);
    }

    @Override
    public MemberResponseDTO updateMember(
            Long memberId,
            MemberUpdateDTO memberUpdateDTO) {

        Member member =
                memberRepository.findById(memberId)
                        .orElseThrow(() ->
                                new MemberNotFound(
                                        "Member not found with id : "
                                                + memberId));

        member.setName(
                memberUpdateDTO.getName());

        member.setEmail(
                memberUpdateDTO.getEmail());

        member.setPhoneNumber(
                memberUpdateDTO.getPhoneNumber());

        Member updated =
                memberRepository.save(member);

        return mapToResponseDTO(updated);
    }

    @Override
    public MemberResponseDTO getMemberById(
            Long memberId) {

        Member member =
                memberRepository.findById(memberId)
                        .orElseThrow(() ->
                                new MemberNotFound(
                                        "Member not found with id : "
                                                + memberId));

        return mapToResponseDTO(member);
    }

    @Override
    public List<MemberResponseDTO> getAllMembers() {

        return memberRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public void deleteMember(Long memberId) {

        Member member =
                memberRepository.findById(memberId)
                        .orElseThrow(() ->
                                new MemberNotFound(
                                        "Member not found with id : "
                                                + memberId));

        memberRepository.delete(member);
    }

    private Member mapToEntity(
            MemberRequestDTO dto) {

        Member member = new Member();

        member.setName(dto.getName());
        member.setEmail(dto.getEmail());
        member.setPhoneNumber(
                dto.getPhoneNumber());

        return member;
    }

    private MemberResponseDTO mapToResponseDTO(
            Member member) {

        return new MemberResponseDTO(
                member.getMemberId(),
                member.getName()
        );
    }
}