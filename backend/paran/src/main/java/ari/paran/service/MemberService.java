package ari.paran.service;

import ari.paran.Util.SecurityUtil;
<<<<<<< HEAD
import ari.paran.domain.*;
=======
import ari.paran.domain.Member;
import ari.paran.domain.Repository.MemberRepository;
>>>>>>> map2
import ari.paran.dto.MemberResponseDto;
import ari.paran.dto.Response;
import ari.paran.dto.request.LoginDto;
import ari.paran.dto.request.SignupDto;
import ari.paran.dto.request.TokenRequestDto;
import ari.paran.dto.response.TokenDto;
import ari.paran.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

<<<<<<< HEAD
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Optional;
=======
>>>>>>> map2
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final SignupCodeRepository signupCodeRepository;
    private final Response response;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final RedisTemplate redisTemplate;
    private final JavaMailSender javaMailSender;

    @Transactional(readOnly = true)
    public MemberResponseDto getMemberInfo(String email){
        return memberRepository.findByEmail(email)
                .map(MemberResponseDto::of)
                .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));
    }

    @Transactional(readOnly = true)
    public MemberResponseDto getMyInfo(){
        return memberRepository.findById(SecurityUtil.getCurrentMemberEmail())
                .map(MemberResponseDto::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
    }

    public ResponseEntity<?> signupUser(SignupDto signUp) {
        if (memberRepository.existsByEmail(signUp.getEmail())) {
            return response.fail("이미 회원가입된 이메일입니다.", HttpStatus.BAD_REQUEST);
        }

        Member member = Member.builder()
                .email(signUp.getEmail())
                .password(passwordEncoder.encode(signUp.getPassword()))
                .email(signUp.getEmail())
                .nickname(signUp.getNickname())
                .gender(signUp.getGender())
                .age(signUp.getAge())
                .authority(signUp.getAuthority())
                .build();
        memberRepository.save(member);

        return response.success("회원가입에 성공했습니다.");
    }

    public ResponseEntity<?> signupOwner(SignupDto signUp) {
        if (memberRepository.existsByEmail(signUp.getEmail())) {
            return response.fail("이미 회원가입된 이메일입니다.", HttpStatus.BAD_REQUEST);
        }

        Member member = Member.builder()
                .email(signUp.getEmail())
                .password(passwordEncoder.encode(signUp.getPassword()))
                .email(signUp.getEmail())
                .nickname(signUp.getNickname())
                .gender(signUp.getGender())
                .age(signUp.getAge())
                .authority(signUp.getAuthority())
                .build();

        member.changeRole(Authority.ROLE_OWNER);
        memberRepository.save(member);

        return response.success("회원가입에 성공했습니다.");
    }

    public ResponseEntity<?> authSignupCode(String code) {
        Optional<SignupCode> signupCode = signupCodeRepository.findByCode(code);

        if (signupCode.orElse(null) == null || signupCode.get().isActivated() == true) {
            return response.fail("유효하지 않은 가입코드 입니다.", HttpStatus.BAD_REQUEST);
        }

        signupCode.get().setActivatedTrue();
        signupCodeRepository.save(signupCode.get());
        return response.success();
    }

    @Transactional
    public ResponseEntity<?> sendEmail(String email) {
        String code = SecurityUtil.generateCode();

        String subject = "Ari 회원가입을 위한 인증번호입니다.";
        String content="";
        content+= "<div style='margin:100px;'>";
        content+= "<h1> 안녕하세요 Ari입니다. </h1>";
        content+= "<br>";
        content+= "<p>아래 코드를 회원가입 창으로 돌아가 입력해주세요<p>";
        content+= "<br>";
        content+= "<p>감사합니다!<p>";
        content+= "<br>";
        content+= "<div align='center' style='border:1px solid black; font-family:verdana';>";
        content+= "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        content+= "<div style='font-size:130%'>";
        content+= "CODE : <strong>";
        content+= code+"</strong><div><br/> ";
        content+= "</div>";

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            helper.setTo(email);
            helper.setFrom("aritest0222@gmail.com");
            helper.setSubject(subject);
            helper.setText(content, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        // 코드 확인용. 나중에 삭제해야함
        log.info("인증코드: {}", code);

        redisTemplate.opsForValue()
                .set(code, email, 5*60000, TimeUnit.MILLISECONDS);

        return response.success();
    }

    public ResponseEntity<?> authEmail(String code) {
        String result = (String) redisTemplate.opsForValue().get(code);

        if (result != null) {
            //이메일 확인용. 나중에 삭제해야함
            log.info("인증코드 해당 이메일: {}", result);
            return response.success();
        }

        return response.fail("인증코드가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> login(LoginDto loginDto) {

        if (memberRepository.findByEmail(loginDto.getEmail()).orElse(null) == null) {
            return response.fail("해당하는 유저가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        //1. Login id/pw를 기반으로 Authentication 객체 생성
        //이때 authentication는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = loginDto.toAuthentication();

        //2. 실제 검증(사용자 비밀번호 체크)이 이루어지는 부분
        //authenticate 메서드가 실행될 때 CustomUserDetailService에서 만든 loadUserByUserName 메서드 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        //3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        //4. RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
        redisTemplate.opsForValue()
                .set("RT" + authentication.getName(), tokenDto.getRefreshToken(),
                        tokenDto.getRefreshTokenExpiresIn(), TimeUnit.MILLISECONDS);

        return response.success(tokenDto, "로그인에 성공했습니다.", HttpStatus.OK);
    }

    public ResponseEntity<?> reissue(TokenRequestDto reissue) {
        //1. refresh token 검증
        if (!tokenProvider.validateToken(reissue.getRefreshToken())) {
            return response.fail("Refresh Token 정보가 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        //2. Access Token에서 User email을 가져옴
        Authentication authentication = tokenProvider.getAuthentication(reissue.getAccessToken());

        //3. redis에서 user email을 기반으로 저장된 refresh token 값을 가져옴
        String refreshToken = (String) redisTemplate.opsForValue().get("RT:" + authentication.getName());

        //(추가) 로그아웃되어 redis에 refresh Token이 존재하지 않는 경우 처리
        if (ObjectUtils.isEmpty(refreshToken)) {
            return response.fail("잘못된 요청입니다", HttpStatus.BAD_REQUEST);
        }
        if (!refreshToken.equals(reissue.getRefreshToken())) {
            return response.fail("Refresh Token 정보가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        //4. 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        //5. refresh token Redis 업데이트
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), tokenDto.getRefreshToken(),
                        tokenDto.getRefreshTokenExpiresIn(), TimeUnit.MILLISECONDS);

        return response.success(tokenDto, "토큰 정보가 갱신되었습니다.", HttpStatus.OK);
    }

    public ResponseEntity<?> logout(TokenRequestDto logout) {
        //1. Access Token 검증
        if (!tokenProvider.validateToken(logout.getAccessToken())) {
            return response.fail("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        }

        //2. Access Token에서 User email을 가져옴
        Authentication authentication = tokenProvider.getAuthentication(logout.getAccessToken());

        //3. Redis에서 해당 User email로 저장된 refresh token이 있는지 여부를 확인 후, 있을 경우 삭제
        if (redisTemplate.opsForValue().get("RT:" + authentication.getName()) != null) {
            //refresh token 삭제
            redisTemplate.delete("RT:" + authentication.getName());
        }

        //4. 해당 access token 유효시간 가지고 와서 BlackList로 저장
        Long expiration = tokenProvider.getExpiration(logout.getAccessToken());
        redisTemplate.opsForValue()
                .set(logout.getAccessToken(), "logout", expiration, TimeUnit.MILLISECONDS);

        return response.success("로그아웃 되었습니다");
    }

}