/*
 * Copyright (c) 2020.  All rights reserved.
 *
 * BLOG:  https://cayzlh.com
 * GITHUB:  https://github.com/cayzlh
 */

package com.cayzlh.framework.base.log.aop;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.cayzlh.framework.base.config.properties.BaseProperties;
import com.cayzlh.framework.base.context.BaseContextHolder;
import com.cayzlh.framework.base.log.annotation.Module;
import com.cayzlh.framework.base.log.annotation.OperationLog;
import com.cayzlh.framework.base.log.annotation.OperationLogIgnore;
import com.cayzlh.framework.base.log.bean.OperationInfo;
import com.cayzlh.framework.base.log.bean.RequestInfo;
import com.cayzlh.framework.common.BaseResponse;
import com.cayzlh.framework.constant.CommonsConstant;
import com.cayzlh.framework.constant.ResponseCode;
import com.cayzlh.framework.util.AnsiUtil;
import com.cayzlh.framework.util.HttpServletUtil;
import com.cayzlh.framework.util.IpUtil;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.fusesource.jansi.Ansi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Ant丶
 * @date 2020-04-25.
 */
@Slf4j
public abstract class BaseLogAop {

    /**
     * 本地线程变量，保存请求参数信息到当前线程中
     */
    protected static ThreadLocal<String> requestInfoStringThreadLocal = new ThreadLocal<>();
    protected static ThreadLocal<RequestInfo> requestInfoThreadLocal = new ThreadLocal<>();
    protected static ThreadLocal<OperationInfo> operationInfoThreadLocal = new ThreadLocal<>();

    /**
     * Aop日志配置
     */
    protected BaseProperties.Log logConfig;

    /**
     * 是否启用请求ID
     */
    protected boolean enableRequestId;

    @Autowired
    public void setBaseConfigProperties(BaseProperties baseProperties) {
        logConfig = baseProperties.getLog();
        enableRequestId = logConfig.isEnableRequestId();
        log.debug("logConfig = {}", logConfig);
        log.debug("enableRequestId = {}", enableRequestId);
    }

    /**
     * 环绕通知 方法执行前打印请求参数信息 方法执行后打印响应结果信息
     */
    public abstract Object doAround(ProceedingJoinPoint joinPoint) throws Throwable;

    /**
     * 异常通知方法
     */
    public abstract void afterThrowing(JoinPoint joinPoint, Exception exception);

    /**
     * 设置请求ID
     */
    protected abstract void setRequestId(RequestInfo requestInfo);

    /**
     * 获取请求信息对象
     */
    protected abstract void getRequestInfo(RequestInfo requestInfo);

    /**
     * 获取响应结果对象
     */
    protected abstract void getResponseResult(Object result);

    /**
     * 请求响应处理完成之后的回调方法
     */
    protected abstract void finish(RequestInfo requestInfo, OperationInfo operationInfo,
            Object result, Exception exception);

    /**
     * 处理
     */
    public Object handle(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取请求相关信息
        try {
            // 获取当前的HttpServletRequest对象
            HttpServletRequest request = HttpServletUtil.getRequest();

            // HTTP请求信息对象
            RequestInfo requestInfo = new RequestInfo();

            // 请求路径 /api/foobar/add
            String path = request.getRequestURI();
            requestInfo.setPath(path);

            // 排除路径
            Set<String> excludePaths = logConfig.getExcludePaths();
            // 请求路径
            if (handleExcludePaths(excludePaths, path)) {
                return joinPoint.proceed();
            }

            // 获取请求类名和方法名称
            Signature signature = joinPoint.getSignature();

            // 获取真实的方法对象
            MethodSignature methodSignature = (MethodSignature) signature;
            Method method = methodSignature.getMethod();

            // 处理操作日志信息
            handleOperationLogInfo(method);

            // IP地址
            String ip = IpUtil.getRequestIp();
            requestInfo.setIp(ip);

            // 获取请求方式
            String requestMethod = request.getMethod();
            requestInfo.setRequestMethod(requestMethod);

            // 获取请求内容类型
            String contentType = request.getContentType();
            requestInfo.setContentType(contentType);

            // 判断控制器方法参数中是否有RequestBody注解
            Annotation[][] annotations = method.getParameterAnnotations();
            boolean isRequestBody = isRequestBody(annotations);
            requestInfo.setRequestBody(isRequestBody);

            // 获取Shiro注解值，并记录到map中
            handleShiroAnnotationValue(requestInfo, method);

            // 设置请求参数
            Object requestParamObject = getRequestParamObject(joinPoint, request, isRequestBody);
            requestInfo.setParam(requestParamObject);
            requestInfo.setTime(DateUtil.formatDateTime(new Date()));

            // 获取请求头token
            String token = request.getHeader(CommonsConstant.JWT_TOKEN_NAME);
            requestInfo.setToken(token);

            // 用户浏览器代理字符串
            requestInfo.setUserAgent(request.getHeader(CommonsConstant.USER_AGENT));

            // 记录请求ID
            setRequestId(requestInfo);

            // 调用子类重写方法，控制请求信息日志处理
            getRequestInfo(requestInfo);
        } catch (Exception e) {
            log.error("请求日志AOP处理异常", e);
        }

        // 执行目标方法,获得返回值
        // 方法异常时，会调用子类的@AfterThrowing注解的方法，不会调用下面的代码，异常单独处理
        Object result = joinPoint.proceed();
        try {
            // 调用子类重写方法，控制响应结果日志处理
            getResponseResult(result);
        } catch (Exception e) {
            log.error("处理响应结果异常", e);
        } finally {
            handleAfterReturn(result, null);
        }
        return result;
    }

    private void handleOperationLogInfo(Method method) {
        // 设置控制器类名称和方法名称
        OperationInfo operationLogInfo = new OperationInfo()
                .setControllerClassName(method.getDeclaringClass().getName())
                .setControllerMethodName(method.getName());

        // 获取Module类注解
        Class<?> controllerClass = method.getDeclaringClass();
        Module module = controllerClass.getAnnotation(Module.class);
        if (module != null) {
            String moduleName = module.name();
            String moduleValue = module.value();
            if (StringUtils.isNotBlank(moduleValue)) {
                operationLogInfo.setModule(moduleValue);
            }
            if (StringUtils.isNotBlank(moduleName)) {
                operationLogInfo.setModule(moduleName);
            }
        }
        // 获取OperationLogIgnore注解
        OperationLogIgnore classOperationLogIgnore = controllerClass
                .getAnnotation(OperationLogIgnore.class);
        if (classOperationLogIgnore != null) {
            // 不记录日志
            operationLogInfo.setIgnore(true);
        }
        // 判断方法是否要过滤
        OperationLogIgnore operationLogIgnore = method.getAnnotation(OperationLogIgnore.class);
        if (operationLogIgnore != null) {
            operationLogInfo.setIgnore(true);
        }
        // 从方法上获取OperationLog注解
        OperationLog operationLog = method.getAnnotation(OperationLog.class);
        if (operationLog != null) {
            String operationLogName = operationLog.name();
            String operationLogValue = operationLog.value();
            if (StringUtils.isNotBlank(operationLogValue)) {
                operationLogInfo.setName(operationLogValue);
            }
            if (StringUtils.isNotBlank(operationLogName)) {
                operationLogInfo.setName(operationLogName);
            }
            operationLogInfo.setRemark(operationLog.remark());
        }
        operationInfoThreadLocal.set(operationLogInfo);
    }

    protected boolean handleExcludePaths(Set<String> excludePaths, String realPath) {
        if (CollectionUtils.isEmpty(excludePaths) || StringUtils.isBlank(realPath)) {
            return false;
        }
        // 如果是排除路径，则跳过
        return excludePaths.contains(realPath);
    }

    /**
     * 判断控制器方法参数中是否有RequestBody注解
     */
    protected boolean isRequestBody(Annotation[][] annotations) {
        boolean isRequestBody = false;
        for (Annotation[] annotationArray : annotations) {
            for (Annotation annotation : annotationArray) {
                if (annotation instanceof RequestBody) {
                    isRequestBody = true;
                    break;
                }
            }
        }
        return isRequestBody;
    }

    /**
     * 获取Shiro注解值，并记录到map中
     */
    protected void handleShiroAnnotationValue(RequestInfo requestInfo, Method method) {
        RequiresRoles requiresRoles = method.getAnnotation(RequiresRoles.class);
        if (requiresRoles != null) {
            String[] requiresRolesValues = requiresRoles.value();
            if (ArrayUtils.isNotEmpty(requiresRolesValues)) {
                String requiresRolesString = Arrays.toString(requiresRolesValues);
                requestInfo.setRequiresRoles(requiresRolesString);
            }
        }

        RequiresPermissions requiresPermissions = method.getAnnotation(RequiresPermissions.class);
        if (requiresPermissions != null) {
            String[] requiresPermissionsValues = requiresPermissions.value();
            if (ArrayUtils.isNotEmpty(requiresPermissionsValues)) {
                String requiresPermissionsString = Arrays.toString(requiresPermissionsValues);
                requestInfo.setRequiresPermissions(requiresPermissionsString);
            }
        }

        RequiresAuthentication requiresAuthentication = method
                .getAnnotation(RequiresAuthentication.class);
        if (requiresAuthentication != null) {
            requestInfo.setRequiresAuthentication(true);
        }

        RequiresUser requiresUser = method.getAnnotation(RequiresUser.class);
        if (requiresUser != null) {
            requestInfo.setRequiresUser(true);
        }

        RequiresGuest requiresGuest = method.getAnnotation(RequiresGuest.class);
        if (requiresGuest != null) {
            requestInfo.setRequiresGuest(true);
        }

    }

    /**
     * 获取请求参数JSON字符串
     */
    protected Object getRequestParamObject(ProceedingJoinPoint joinPoint,
            HttpServletRequest request, boolean isRequestBody) {
        Object paramObject = null;
        if (isRequestBody) {
            // POST,application/json,RequestBody的类型,简单判断,然后序列化成JSON字符串
            Object[] args = joinPoint.getArgs();
            paramObject = getArgsObject(args);
        } else {
            // 获取getParameterMap中所有的值,处理后序列化成JSON字符串
            Map<String, String[]> paramsMap = request.getParameterMap();
            paramObject = getParamJSONObject(paramsMap);
        }
        return paramObject;
    }

    /**
     * 请求参数拼装
     */
    protected Object getArgsObject(Object[] args) {
        if (args == null) {
            return null;
        }
        // 去掉HttpServletRequest和HttpServletResponse
        List<Object> realArgs = new ArrayList<>();
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest) {
                continue;
            }
            if (arg instanceof HttpServletResponse) {
                continue;
            }
            if (arg instanceof MultipartFile) {
                continue;
            }
            if (arg instanceof ModelAndView) {
                continue;
            }
            realArgs.add(arg);
        }
        if (realArgs.size() == 1) {
            return realArgs.get(0);
        } else {
            return realArgs;
        }
    }

    /**
     * 获取参数Map的JSON字符串
     */
    protected JSONObject getParamJSONObject(Map<String, String[]> paramsMap) {
        if (MapUtils.isEmpty(paramsMap)) {
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        for (Map.Entry<String, String[]> kv : paramsMap.entrySet()) {
            String key = kv.getKey();
            String[] values = kv.getValue();
            // 没有值
            if (values == null) {
                jsonObject.put(key, null);
            } else if (values.length == 1) {
                // 一个值
                jsonObject.put(key, values[0]);
            } else {
                // 多个值
                jsonObject.put(key, values);
            }
        }
        return jsonObject;
    }

    /**
     * 正常调用返回或者异常结束后调用此方法
     */
    protected void handleAfterReturn(Object result, Exception exception) {
        // 获取RequestInfo
        RequestInfo requestInfo = requestInfoThreadLocal.get();
        // 获取OperationLogInfo
        OperationInfo operationInfo = operationInfoThreadLocal.get();
        // 调用抽象方法，是否保存日志操作，需要子类重写该方法，手动调用saveSysOperationLog
        finish(requestInfo, operationInfo, result, null);
        // 释放资源
        remove();
    }

    /**
     * 处理异常
     */
    public void handleAfterThrowing(Exception exception) {
        // 获取RequestInfo
        RequestInfo requestInfo = requestInfoThreadLocal.get();
        // 获取OperationLogInfo
        OperationInfo operationInfo = operationInfoThreadLocal.get();
        // 调用抽象方法，是否保存日志操作，需要子类重写该方法，手动调用saveSysOperationLog
        finish(requestInfo, operationInfo, null, exception);
        // 释放资源
        remove();
    }

    /**
     * 处理请求ID
     */
    protected void handleRequestId(RequestInfo requestInfo) {
        if (!enableRequestId) {
            return;
        }
        String requestId = BaseContextHolder.getRequestId();
        // 设置请求ID
        requestInfo.setRequestId(requestId);
    }

    /**
     * 处理请求参数
     */
    protected void handleRequestInfo(RequestInfo requestInfo) {
        requestInfoThreadLocal.set(requestInfo);
        // 获取请求信息
        String requestInfoString = formatRequestInfo(requestInfo);
        // 如果打印方式为顺序打印，则直接打印，否则，保存的threadLocal中
        log.info(requestInfoString);
    }

    protected String formatRequestInfo(RequestInfo requestInfo) {
        String requestInfoString = null;
        try {
            if (logConfig.isRequestLogFormat()) {
                requestInfoString = "\n" + JSONUtil.toJsonStr(JSONUtil.parse(requestInfo), 2);
            } else {
                requestInfoString = JSONUtil.toJsonStr(requestInfo);
            }
        } catch (Exception e) {
            log.error("格式化请求信息日志异常", e);
        }
        return AnsiUtil.getAnsi(Ansi.Color.GREEN, "requestInfo:" + requestInfoString);
    }

    /**
     * 处理响应结果
     */
    protected void handleResponseResult(Object result) {
        if (result instanceof BaseResponse) {
            BaseResponse<?> baseResponse = (BaseResponse<?>) result;
            int code = baseResponse.getCode();
            // 获取格式化后的响应结果
            String responseResultString = formatResponseResult(baseResponse);
            printResponseResult(code, responseResultString);
        } else if (result instanceof String) {
            BaseResponse<String> objectBaseResponse = new BaseResponse<>((String) result,
                    BaseContextHolder.getRequestId());
            String responseResultString = formatResponseResult(objectBaseResponse);
            printResponseResult(objectBaseResponse.getCode(), responseResultString);
        } else {
            BaseResponse<Object> objectBaseResponse = new BaseResponse<>(result,
                    BaseContextHolder.getRequestId());
            String responseResultString = formatResponseResult(objectBaseResponse);
            printResponseResult(objectBaseResponse.getCode(), responseResultString);
        }
    }

    /**
     * 打印响应信息
     */
    protected void printResponseResult(int code, String responseResultString) {
        if (code == ResponseCode.SUCCESS) {
            log.info(responseResultString);
        } else {
            log.error(responseResultString);
        }
    }

    protected String formatResponseResult(BaseResponse<?> baseResponse) {
        String responseResultString = "responseResult:";
        try {
            if (logConfig.isResponseLogFormat()) {
                responseResultString += "\n" + JSONUtil.toJsonStr(JSONUtil.parse(baseResponse), 2);
            } else {
                responseResultString += JSONUtil.toJsonStr(baseResponse);
            }
            int code = baseResponse.getCode();
            if (code == ResponseCode.SUCCESS) {
                return AnsiUtil.getAnsi(Ansi.Color.BLUE, responseResultString);
            } else {
                return AnsiUtil.getAnsi(Ansi.Color.RED, responseResultString);
            }
        } catch (Exception e) {
            log.error("格式化响应日志异常", e);
        }
        return responseResultString;
    }

    protected void remove() {
        requestInfoStringThreadLocal.remove();
        requestInfoThreadLocal.remove();
        operationInfoThreadLocal.remove();
    }

}
