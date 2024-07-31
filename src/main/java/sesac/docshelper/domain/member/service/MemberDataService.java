package sesac.docshelper.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sesac.docshelper.domain.docs.dto.NaverField;
import sesac.docshelper.domain.docs.dto.request.NaverImage;
import sesac.docshelper.domain.docs.dto.request.NaverOcrRequst;
import sesac.docshelper.domain.docs.dto.response.NaverOcrResponse;
import sesac.docshelper.domain.docs.service.client.OcrClient;
import sesac.docshelper.domain.member.dto.request.AddInfoRequest;
import sesac.docshelper.domain.member.dto.response.IdCardFrontResponse;
import sesac.docshelper.domain.member.entity.IdentityCard;
import sesac.docshelper.domain.member.entity.Member;
import sesac.docshelper.domain.member.entity.Passport;
import sesac.docshelper.domain.member.mapper.MemberMapper;
import sesac.docshelper.domain.member.repository.IdentityCardRepository;
import sesac.docshelper.domain.member.repository.MemberRepository;
import sesac.docshelper.domain.member.repository.PassPortRepository;
import sesac.docshelper.global.exception.ErrorCode;
import sesac.docshelper.global.exception.GlobalException;
import sesac.docshelper.global.util.UserDetailsImpl;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberDataService {

    private final MemberRepository memberRepository;
    private final PassPortRepository passPortRepository;
    private final IdentityCardRepository identityCardRepository;
    private final S3MultiPartUploader s3MultiPartUploader;
    private final OcrClient ocrClient;

    public Member addInfo(UserDetailsImpl userDetails, AddInfoRequest addInfoRequest) {
        try {
            return memberRepository.findByEmail(userDetails.getEmail())
                    .map(member -> {member.setIncome(addInfoRequest.income());
                        member.setHousingType(addInfoRequest.housingType());
                        member.setPhoneNumber(addInfoRequest.phoneNumber());
                        return  member;})
                    .map(memberRepository::save).orElse(null);
        } catch (Exception e){
            log.error(e.getMessage());
            throw new GlobalException(ErrorCode.ERROR_IN_ADDINFO);
        }
    }

    public Object addOcrInfo (UserDetailsImpl userDetails, MultipartFile file, String docType) {
        try {
            // S3에 저장
           String url = s3MultiPartUploader.upload(file);
           List<NaverImage> imageList = new ArrayList<>();
           imageList.add(new NaverImage("jpg", "test", url, getTemplateId(docType)));
            NaverOcrRequst request = new NaverOcrRequst(
                    "v1",
                    UUID.randomUUID().toString(),
                    System.currentTimeMillis(),
                    "ko",
                    imageList
            );

           // 빈칸 별 값 불러오기
           NaverOcrResponse ocrResponse = ocrClient.naverOcrResponse(request);
           // docs type에 맞게 값 저장
           switch (getTemplateId(docType)){
               case 31110: {return  addIdCardInfo(ocrResponse.images().getFirst().fields(), userDetails.getMember());} // 신분증 앞
               case 31111: {return  addIdCardInfo(ocrResponse.images().getFirst().fields(), userDetails.getMember());} // 신분증 뒤
               case 31112: {return addPassPortInfo(ocrResponse.images().getFirst().fields(), userDetails.getMember());} // 여권
           }


        }catch (IOException e){
            log.info(e.getMessage());
            throw new GlobalException(ErrorCode.CANT_UPLOAD_FILE);
        }

        return null;
    }

    private Integer getTemplateId (String docType) {
        return switch (docType.toLowerCase()) {
            case "foreginerfront" -> 31110;
            case "foreginerback" -> 31111;
            default -> 31112;
        };
    }

    private IdentityCard addIdCardInfo(List<NaverField> fields, Member member) {
        if (fields == null) {
            log.warn("Fields list is null");
            return null;
        }

        IdentityCard idCard = identityCardRepository.findByMember_Email(member.getEmail()).orElseGet(() -> {
            IdentityCard newCard = new IdentityCard();
            newCard.setMember(member);
            return newCard;
        });

        addEntityInfo(fields, idCard);
        identityCardRepository.save(idCard);

        return idCard;
    }

    private Passport addPassPortInfo(List<NaverField> fields, Member member) {
        if (fields == null) {
            log.warn("Fields list is null");
            return null;
        }

        Passport passport = passPortRepository.findByMember_Email(member.getEmail()).orElseGet(() -> {
            Passport newPassport = new Passport();
            newPassport.setMember(member);
            return newPassport;
        });

        addEntityInfo(fields, passport);
        passPortRepository.save(passport);

        return passport;
    }



    private <T> T addEntityInfo(List<NaverField> fields, T entity) {

        fields.forEach(field -> {
            try {
                Field entityField = entity.getClass().getDeclaredField(field.name()); // Naver의 반환값과 일치하는 멤버 변수 찾기
                entityField.setAccessible(true); // private 해제
                setFieldValue(entityField, entity, field.inferText()); // 해당 멤버변수에 inferText에 해당하는 값 넣기
            } catch (NoSuchFieldException e) {
                log.warn("Field '{}' does not exist in entity '{}'", field.name(), entity.getClass().getSimpleName());
            }
        });

        return entity;
    }

    private <T> void setFieldValue(Field field, T entity, String value) {
        try {
            field.set(entity, value);
        } catch (IllegalAccessException e) {
            log.error("Error setting field value", e);
        }
    }
}

