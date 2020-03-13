package com.shsxt.crm.proxy;

import com.shsxt.crm.annotions.RequirePermission;
import com.shsxt.exception.AuthFailedException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.List;

@Component
@Aspect
public class PermissionProxy {
    @Autowired
    private HttpSession session;

    @Around(value = "@annotation(com.shsxt.crm.annotions.RequirePermission)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable{
        List<String> permissions = (List<String>) session.getAttribute("permission");
        if(null==permissions||permissions.size()==0){
            throw new AuthFailedException();
        }
        Object reuslt=null;
        MethodSignature methodSignature= (MethodSignature) pjp.getSignature();
        RequirePermission requirePermission=methodSignature.getMethod().getDeclaredAnnotation(RequirePermission.class);
        if(!(permissions.contains(requirePermission.code()))){
            throw new AuthFailedException();
        }
        reuslt=pjp.proceed();
        return reuslt;

    }

}
