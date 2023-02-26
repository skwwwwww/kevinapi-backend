package com.kevin.project.annotation;

import java.lang.annotation.*;

/**
 *
 * 权限校验
 *
 * @Projectname: kevinapi-backend
 * @Filename: AuthCheck
 * @Author: skw
 */
//在什么地方添加该注解
@Target(ElementType.METHOD)
//在什么时间使用该注解
@Retention(RetentionPolicy.RUNTIME)
//是否添加在javadoc中
//@Documented
//是否允许被子类继承
//@Inherited
//  @Inherited 元注解是一个标记注解，@Inherited 阐述了某个被标注的类型是被继承的。
//  如果一个使用了@Inherited 修饰的annotation 类型被用于一个class，
//  则这个annotation 将被用于该class 的子类。
/**
 * 权限校验
 *
 * @author kevin
 */
public @interface AuthCheck {

	/**
	 * 有任何一个角色
	 *
	 * @return
	 */
	String[] anyRole() default "";

	/**
	 * 必须有某个角色
	 *
	 * @return
	 */
	String mustRole() default "";

}
