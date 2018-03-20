package com.dvt.HubotService.commons.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.web.bind.annotation.Mapping;

@Target({ElementType.METHOD, ElementType.TYPE}) //表示该注解用于什么地方。
@Retention(RetentionPolicy.RUNTIME) // 定义该注解的生命周期
@Documented
@Mapping
public @interface ApiVersion {
	int value();
}
