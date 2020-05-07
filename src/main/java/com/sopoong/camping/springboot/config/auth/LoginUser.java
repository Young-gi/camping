package com.sopoong.camping.springboot.config.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) // 해당 어노테이션이 생성될 수 있는 위치를 지정함. 여기서는 메소드의 파라미터로 선언된 객체에만.
@Retention(RetentionPolicy.RUNTIME) // 어노테이션이 영향을 미치는 시점에 대해 결정
public @interface LoginUser {
}
