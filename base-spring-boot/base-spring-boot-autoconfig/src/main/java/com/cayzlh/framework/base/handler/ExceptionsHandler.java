package com.cayzlh.framework.base.handler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE;

import cn.hutool.json.JSONUtil;
import com.cayzlh.framework.common.BaseResponse;
import com.cayzlh.framework.base.context.BaseContextHolder;
import com.cayzlh.framework.exception.CommonException;
import com.cayzlh.framework.util.AnsiUtil;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Color;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * @author cayzlh
 * @link https://github.com/cayzlh
 */
@RestControllerAdvice
@Slf4j
public class ExceptionsHandler {

    @ExceptionHandler({
            CommonException.class,
            NoHandlerFoundException.class,
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            HttpMediaTypeNotAcceptableException.class,
            MissingPathVariableException.class,
            MissingServletRequestParameterException.class,
            ServletRequestBindingException.class,
            ConversionNotSupportedException.class,
            TypeMismatchException.class,
            HttpMessageNotReadableException.class,
            HttpMessageNotWritableException.class,
            MethodArgumentNotValidException.class,
            MissingServletRequestPartException.class,
            BindException.class,
            AsyncRequestTimeoutException.class,
            HttpClientErrorException.class,
            IllegalArgumentException.class,
            AuthenticationException.class,
            ConstraintViolationException.class,
            Exception.class
    })
    public final ResponseEntity<BaseResponse<?>> handleExceptions(HttpServletRequest req,
            HttpServletResponse rsp, Exception e) {
        BaseResponse<?> response = new BaseResponse<>();
        HttpStatus status = HttpStatus.OK;
        String requestURI = req.getRequestURI();
        if (e instanceof CommonException) {
            CommonException ce = (CommonException) e;
            response.setCode(
                    (null == ce.getErrorCode()) ? HttpStatus.INTERNAL_SERVER_ERROR.value()
                            : ce.getErrorCode());
            response.setMsg(ce.getMessage());
            response.setRequestId(BaseContextHolder.getRequestId());
            status = INTERNAL_SERVER_ERROR;
        } else if (e instanceof AuthenticationException) {
            AuthenticationException ae = (AuthenticationException) e;
            response.setCode(UNAUTHORIZED.value());
            response.setMsg(ae.getMessage());
            response.setRequestId(BaseContextHolder.getRequestId());
            status = UNAUTHORIZED;
        } else if (e instanceof NoHandlerFoundException) {
            NoHandlerFoundException nfe = (NoHandlerFoundException) e;
            response.setCode(NOT_FOUND.value());
            response.setMsg(nfe.getMessage());
            response.setRequestId(BaseContextHolder.getRequestId());
            status = NOT_FOUND;
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            HttpRequestMethodNotSupportedException ex = (HttpRequestMethodNotSupportedException) e;
            status = METHOD_NOT_ALLOWED;
            response.setCode(METHOD_NOT_ALLOWED.value());
            response.setMsg(ex.getMessage());
            response.setRequestId(BaseContextHolder.getRequestId());
        } else if (e instanceof HttpMediaTypeNotSupportedException) {
            HttpMediaTypeNotSupportedException ex = (HttpMediaTypeNotSupportedException) e;
            status = UNSUPPORTED_MEDIA_TYPE;
            response.setCode(UNSUPPORTED_MEDIA_TYPE.value());
            response.setMsg(ex.getMessage());
            response.setRequestId(BaseContextHolder.getRequestId());
        } else if (e instanceof HttpMediaTypeNotAcceptableException) {
            status = NOT_ACCEPTABLE;
            HttpMediaTypeNotAcceptableException ex = (HttpMediaTypeNotAcceptableException) e;
            response.setCode(NOT_ACCEPTABLE.value());
            response.setMsg(ex.getMessage());
            response.setRequestId(BaseContextHolder.getRequestId());
        } else if (e instanceof MissingPathVariableException) {
            status = INTERNAL_SERVER_ERROR;
            MissingPathVariableException ex = (MissingPathVariableException) e;
            response.setCode(INTERNAL_SERVER_ERROR.value());
            response.setMsg(ex.getMessage());
            response.setRequestId(BaseContextHolder.getRequestId());
        } else if (e instanceof MissingServletRequestParameterException) {
            status = BAD_REQUEST;
            MissingServletRequestParameterException ex = (MissingServletRequestParameterException) e;
            response.setCode(BAD_REQUEST.value());
            response.setMsg(ex.getMessage());
            response.setRequestId(BaseContextHolder.getRequestId());
        } else if (e instanceof ServletRequestBindingException) {
            status = BAD_REQUEST;
            ServletRequestBindingException ex = (ServletRequestBindingException) e;
            response.setCode(BAD_REQUEST.value());
            response.setMsg(ex.getMessage());
            response.setRequestId(BaseContextHolder.getRequestId());
        } else if (e instanceof ConversionNotSupportedException) {
            status = INTERNAL_SERVER_ERROR;
            ConversionNotSupportedException ex = (ConversionNotSupportedException) e;
            response.setCode(INTERNAL_SERVER_ERROR.value());
            response.setMsg(ex.getMessage());
            response.setRequestId(BaseContextHolder.getRequestId());
        } else if (e instanceof TypeMismatchException) {
            status = BAD_REQUEST;
            TypeMismatchException ex = (TypeMismatchException) e;
            response.setCode(BAD_REQUEST.value());
            response.setMsg(ex.getMessage());
            response.setRequestId(BaseContextHolder.getRequestId());
        } else if (e instanceof HttpMessageNotReadableException) {
            status = BAD_REQUEST;
            HttpMessageNotReadableException ex = (HttpMessageNotReadableException) e;
            response.setCode(BAD_REQUEST.value());
            response.setMsg(ex.getMessage());
            response.setRequestId(BaseContextHolder.getRequestId());
        } else if (e instanceof HttpMessageNotWritableException) {
            status = INTERNAL_SERVER_ERROR;
            HttpMessageNotWritableException ex = (HttpMessageNotWritableException) e;
            response.setCode(INTERNAL_SERVER_ERROR.value());
            response.setMsg(ex.getMessage());
            response.setRequestId(BaseContextHolder.getRequestId());
        } else if (e instanceof MethodArgumentNotValidException) {
            status = BAD_REQUEST;
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
            List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
            List<String> collect = fieldErrors.stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            response.setCode(BAD_REQUEST.value());
            response.setMsg(StringUtils.join(collect,","));
            response.setRequestId(BaseContextHolder.getRequestId());
        } else if (e instanceof MissingServletRequestPartException) {
            status = BAD_REQUEST;
            MissingServletRequestPartException ex = (MissingServletRequestPartException) e;
            response.setCode(BAD_REQUEST.value());
            response.setMsg(ex.getMessage());
            response.setRequestId(BaseContextHolder.getRequestId());
        } else if (e instanceof BindException) {
            status = BAD_REQUEST;
            BindException ex = (BindException) e;
            BindingResult bindingResult = ex.getBindingResult();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            List<String> collect = fieldErrors.stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            response.setCode(BAD_REQUEST.value());
            response.setMsg(StringUtils.join(collect, ","));
            response.setRequestId(BaseContextHolder.getRequestId());
        } else if (e instanceof ConstraintViolationException) {
            status = BAD_REQUEST;
            ConstraintViolationException ex = (ConstraintViolationException) e;
            Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
            List<String> collect = constraintViolations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            response.setCode(BAD_REQUEST.value());
            response.setMsg(StringUtils.join(collect, ","));
            response.setRequestId(BaseContextHolder.getRequestId());
        } else if (e instanceof AsyncRequestTimeoutException) {
            status = SERVICE_UNAVAILABLE;
            AsyncRequestTimeoutException ex = (AsyncRequestTimeoutException) e;
            response.setCode(SERVICE_UNAVAILABLE.value());
            response.setMsg(ex.getMessage());
            response.setRequestId(BaseContextHolder.getRequestId());
        } else if (e instanceof HttpClientErrorException) {
            HttpClientErrorException httpClientErrorException = (HttpClientErrorException) e;
            status = httpClientErrorException.getStatusCode();
            response.setCode(status.value());
            response.setMsg(httpClientErrorException.getStatusText());
            response.setRequestId(BaseContextHolder.getRequestId());
        } else if (e instanceof IllegalArgumentException) {
            response.setCode(INTERNAL_SERVER_ERROR.value());
            response.setMsg(e.getMessage());
            response.setRequestId(BaseContextHolder.getRequestId());
            status = INTERNAL_SERVER_ERROR;
        } else {
            response.setCode(INTERNAL_SERVER_ERROR.value());
            response.setMsg(e.getMessage());
            response.setRequestId(BaseContextHolder.getRequestId());
            status = INTERNAL_SERVER_ERROR;
        }
        String ansiStr = AnsiUtil.getAnsi(Color.RED,
                "request [{}] throw an exception: {}");
        log.error(ansiStr, requestURI, e.getMessage(), e);
        rsp.setIntHeader("HttpStatus", status.value());
        rsp.setStatus(status.value());
        return new ResponseEntity<>(response, status);
    }

}
