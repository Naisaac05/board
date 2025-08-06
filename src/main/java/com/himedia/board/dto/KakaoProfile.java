package com.himedia.board.dto;

import lombok.Getter;
import lombok.Setter;


    @Getter @Setter
    public class KakaoProfile {
        private String id;
        private KakaoAccount kakao_account;

        @Getter @Setter
        public class KakaoAccount {
            private Profile profile;
            private String email;
            private Boolean has_email;

            @Getter @Setter
            public class Profile {
                private String nickname;
                private String profile_image_url;
            }
        }
    }
