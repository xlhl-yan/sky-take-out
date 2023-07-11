package com.xlhl.sky.aspect;

import com.xlhl.sky.annotation.AutoFill;
import com.xlhl.sky.constant.AutoFillConstant;
import com.xlhl.sky.context.BaseContext;
import com.xlhl.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义切面，实现自动填充字段逻辑
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    /**
     * 切入点
     */
    @Pointcut(value = "execution(* com.xlhl.sky.mapper.*.*(..)) && @annotation(com.xlhl.sky.annotation.AutoFill)")
    public void autoFillPointCut() {
    }

    /**
     * 定义前置通知，在执行SQL前为公共字段赋值
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始进行字段填充....");

        //获取到当前被拦截的数据库操作类型
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();//获取方法签名对象
        AutoFill autoFill = methodSignature.getMethod().getAnnotation(AutoFill.class);//获取方法上的注解对象
        OperationType type = autoFill.value();//获取注解中的值/操作类型

        //获取拦截方法的参数
        Object[] args = joinPoint.getArgs();
        if (args.length == 0) {
            log.info("方法参数为空");
            return;
        }

        //方法的参数集合，约定全部参数中第一个参数为需要填充的对象
        Object object = args[0];

        //公共字段取值
        LocalDateTime dateTime = LocalDateTime.now();
        Long uid = BaseContext.getCurrentId();

        //根据操作类型为属性赋值

        //insert
        if (type == OperationType.INSERT) {
            try {
                //利用反射获取方法对象
                Method setCreateTime = object.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = object.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);

                try {
                    //通过反射调用方法赋值
                    setCreateTime.invoke(object, dateTime);
                    setCreateUser.invoke(object, uid);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        //update
        try {
            //利用反射获取方法对象
            Method setUpdateTime = object.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            Method setUpdateUser = object.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

            //通过反射调用方法赋值
            setUpdateTime.invoke(object, dateTime);
            setUpdateUser.invoke(object, uid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
