package com.sopoong.camping.springboot.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsUpdateRequestDto {
    private String title;
    private String content;
    // UPDATE 시 작성자 명, id 는 변경하지 않으므로 아래와 같이 Entity 필드 중 일부만 받아 처리함.
    @Builder
    public PostsUpdateRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
