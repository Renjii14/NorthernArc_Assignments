package org.northernarc.librarymanagement.service;
import org.northernarc.librarymanagement.dto.MemberRequestDTO;
import org.northernarc.librarymanagement.dto.MemberResponseDTO;
import org.northernarc.librarymanagement.dto.MemberUpdateDTO;

import java.util.List;

public interface MemberService {

    MemberResponseDTO addMember(MemberRequestDTO memberRequestDTO);

    MemberResponseDTO updateMember(Long memberId,MemberUpdateDTO memberUpdateDTO);

    MemberResponseDTO getMemberById(Long memberId);

    List<MemberResponseDTO> getAllMembers();

    void deleteMember(Long memberId);
}
