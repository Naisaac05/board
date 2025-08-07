package com.himedia.board.controller;

import com.google.gson.Gson;
import com.himedia.board.dto.KakaoProfile;
import com.himedia.board.dto.MemberDto;
import com.himedia.board.dto.OAuthToken;
import com.himedia.board.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Member;
import java.net.HttpURLConnection;
import java.net.URL;

@Controller
public class MemberController {

    @Autowired
    MemberService ms;

    @GetMapping("/")
    public String index() {
        return "member/loginForm";
    }
    @PostMapping("/login")
    public String login(@ModelAttribute("dto") @Valid MemberDto memberdto, BindingResult result, HttpServletRequest request, Model model ) {
        String url = "member/loginForm";

            //비어 있는 입력에 대한 에러처리
            if(result.hasFieldErrors("userid"))
                model.addAttribute("msg",result.getFieldError("userid").getDefaultMessage());
            else if(result.hasFieldErrors("pwd"))
                model.addAttribute("msg",result.getFieldError("pwd").getDefaultMessage());
        else{
            //정상 로그인
            MemberDto mdto = ms.getMember(memberdto.getUserid());
            if(mdto == null)
                model.addAttribute("msg","아이디와 패스워드를 확인하세요");
            else if(!mdto.getPwd().equals(memberdto.getPwd()))
                model.addAttribute("msg","아이디와 패스워드를 확인하세요");
            else{
                HttpSession session = request.getSession();
                session.setAttribute("loginUser",mdto);
                url="redirect:/main";
            }
        }
        return url;
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "member/joinForm";
    }

    @GetMapping("/idcheck")
    public String idcheck(@RequestParam("userid") String userid, Model model) {
        MemberDto mdto = ms.getMember(userid);
        if(mdto != null) model.addAttribute("result","1");
        else model.addAttribute("result","-1");
        model.addAttribute("userid",userid);
        return "member/idcheck";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute("dto") @Valid MemberDto memberdto, BindingResult result,
                       @RequestParam(value="reid", required = false) String reid,
                       @RequestParam(value="pwd_check", required = false) String pwd_check,
                       Model model) {
        String url = "member/joinForm";

        model.addAttribute("reid",reid);

        if(result.hasFieldErrors("userid"))
            model.addAttribute("msg",result.getFieldError("userid").getDefaultMessage());
        else if (!memberdto.getUserid().equals(reid))
            model.addAttribute("msg", "아이디 중복검사를 진행하세요");
        else if (ms.getMember(memberdto.getUserid()) != null)
            model.addAttribute("msg", "이미 사용 중인 아이디입니다");
        else if (!pwd_check.equals(memberdto.getPwd()))
            model.addAttribute("msg", "패스워드 확인 일치하지 않습니다");
        else if(result.hasFieldErrors("pwd"))
            model.addAttribute("msg",result.getFieldError("pwd").getDefaultMessage());
        else if(result.hasFieldErrors("name"))
            model.addAttribute("msg",result.getFieldError("name").getDefaultMessage());
        else if(result.hasFieldErrors("email"))
            model.addAttribute("msg",result.getFieldError("email").getDefaultMessage());
        else if(result.hasFieldErrors("phone"))
            model.addAttribute("msg",result.getFieldError("phone").getDefaultMessage());
        else{
            ms.insert(memberdto);
            model.addAttribute("msg","회원가입이 완료되었습니다");
            url="member/loginForm";
        }

        return url;
    }

    @GetMapping("/kakaostart")
    public @ResponseBody String kakaostart() {
        String exeCode="<script type='text/javascript'>" +
                "location.href='https://kauth.kakao.com/oauth/authorize?"
                +"client_id=4b7960a694fa914e70f2d6883478757d&"
                +"redirect_uri=http://13.125.199.21:8070/kakaoLogin"
                +"&response_type=code'"
                +"</script>";

        return exeCode;
    }

    @GetMapping("/kakaoLogin")
    public String kakaoLogin(@RequestParam("code") String code, HttpServletRequest request) throws Exception {
        System.out.println("1차 수신 코드: "+code);

        //개인정보 요청을 위한 2차 토큰을 요청합니다
        //요청할 url과
        String endpoint="https://kauth.kakao.com/oauth/token";
        URL url = new URL(endpoint);

        String bodyData = "grant_type=authorization_code";
        bodyData += "&client_id=4b7960a694fa914e70f2d6883478757d";
        bodyData += "&redirect_uri=http://13.125.199.21:8070/kakaoLogin";
        bodyData += "&code="+code;
        //url 객체에 bodyData를 더해서 요청합니다

        //Stream 연결 //import-java.net.HttpURLConnection;
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //http header 값 넣기(요청 내용에 헤더 추가)
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        conn.setDoOutput(true);//송신 설정
        BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(conn.getOutputStream(), "UTF-8")
        );
        bw.write(bodyData);
        bw.flush(); //실제송신 시점

        BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), "UTF-8")
        );
        String input="";
        StringBuffer sb = new StringBuffer(); //조각난 String을 조립하기위한 객체
        while ( (input = br.readLine()) != null ) { //응답 수신
            sb.append(input); //수신 내용의 누적
            System.out.println(input); //수신 내용을 그때 그때 출력
        }

        //"access_token"
        // :"YFvd1U5ripVUZV75kiyqKElSS3g2Xjq1AAAAAQoNFZsAAAGYQMgptvoXDHwO3UaB",
        // "token_type"
        // :"bearer",
        // "refresh_token"
        // :"uStOAAPLPjT_S6bdSJXJyaxGrRrR5qcDAAAAAgoNFZsAAAGYQMgpr_oXDHwO3UaB",
        // "expires_in"
        // :21599,
        // "scope":"profile_image profile_nickname",
        // "refresh_token_expires_in":5183999}

        Gson gson = new Gson();
        //sb->oAuthToken 복사
        OAuthToken oAuthToken = gson.fromJson(sb.toString(), OAuthToken.class);
        System.out.println("oAuthToken.accessToken : "+ oAuthToken.getAccess_token());

        //2차 accessToken을 이용하여 실제 개인 정보 요청 & 수신
        System.out.println();
        endpoint="https://kapi.kakao.com/v2/user/me";
        url = new URL(endpoint);
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Authorization", "Bearer " + oAuthToken.getAccess_token());
        conn.setDoOutput(true);
        br=new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        input="";
        sb=new StringBuffer();
        while ( (input = br.readLine()) != null ) {
            sb.append(input);
            System.out.println(input); //수신할 내용을 콘솔창에 출력
        }

        //{"id":4365953845,"connected_at":"2025-07-25T08:51:56Z","properties":{"nickname":"나이삭","profile_image":"http://img1.kakaocdn.net/thumb/R640x640.q70/?fname=http://t1.kakaocdn.net/account_images/default_profile.jpeg","thumbnail_image":"http://img1.kakaocdn.net/thumb/R110x110.q70/?fname=http://t1.kakaocdn.net/account_images/default_profile.jpeg"},"kakao_account":{"profile_nickname_needs_agreement":false,"profile_image_needs_agreement":false,"profile":{"nickname":"나이삭","thumbnail_image_url":"http://img1.kakaocdn.net/thumb/R110x110.q70/?fname=http://t1.kakaocdn.net/account_images/default_profile.jpeg","profile_image_url":"http://img1.kakaocdn.net/thumb/R640x640.q70/?fname=http://t1.kakaocdn.net/account_images/default_profile.jpeg","is_default_image":true,"is_default_nickname":false}}}
        //id : 4365953845
        //nickname : 나이삭



        gson = new Gson();
        KakaoProfile kakaoProfile = gson.fromJson(sb.toString(), KakaoProfile.class);
        System.out.println("id : " +kakaoProfile.getId());
        KakaoProfile.KakaoAccount ac = kakaoProfile.getKakao_account();
        KakaoProfile.KakaoAccount.Profile pf = ac.getProfile();
        System.out.println("nickname : " +pf.getNickname());

        String id = kakaoProfile.getId();
        String nickname = pf.getNickname();

        //전송된 개인 정보를 이용해서 회원 가입 & 로그인을 진행합니다

        MemberDto mdto = ms.getMember(id);
        //mdto=null이면 회원가입 후 로그인을 진행하고 null이 아니면 로그인만 진행

        if(mdto == null){
            mdto = new MemberDto();
            mdto.setUserid(id);
            mdto.setName(nickname);
            mdto.setEmail(nickname);
            mdto.setProvider("kakao");
            ms.insert(mdto);
        }

        HttpSession session = request.getSession();
        session.setAttribute("loginUser", mdto);

        return "redirect:/main";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute("loginUser");
        return "member/loginForm";
    }

    @GetMapping("/updateMemberForm")
    public String updateMemberForm(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        session.getAttribute("loginUser");
        MemberDto mdto = (MemberDto) session.getAttribute("loginUser");
        model.addAttribute("dto", mdto);
        return "member/updateForm";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("dto") @Valid MemberDto memberdto, BindingResult result ,
                 @RequestParam(value="pwd_check", required = false)String pwd_check, HttpServletRequest request ,Model model) {
        HttpSession session = request.getSession();
        String url= "member/updateForm";
        if(result.hasFieldErrors("pwd") && memberdto.getProvider() == null)
            model.addAttribute("msg","패스워드를 입력하세요");
        else if(!memberdto.getPwd().equals(pwd_check) && memberdto.getProvider() == null)
            model.addAttribute("msg","패스워드가 일치하지 않습니다");
        else if(result.hasFieldErrors("email"))
            model.addAttribute("msg","이메일을 입력하세요");
        else if (result.hasFieldErrors("phone"))
            model.addAttribute("msg","전화번호를 입력하세요");
        else{
            ms.update(memberdto);
            url="redirect:/main";
            session.setAttribute("loginUser", memberdto);
        }
        return url;
    }

    @GetMapping("/deleteMember")
    public String delete(@RequestParam("userid") String userid, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        session.removeAttribute("loginUser");
        ms.deleteMember(userid);
        model.addAttribute("msg","회원 탈퇴가 완료되었습니다");
        return "member/loginForm";
    }

}
