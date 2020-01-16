package com.cayzlh.framework.base.handler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE;

import com.cayzlh.framework.base.common.BaseResponse;
import com.cayzlh.framework.base.context.BaseContextHolder;
import com.cayzlh.framework.base.exception.BusinessException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
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
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
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
            BusinessException.class,
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
            Exception.class
    })
    public final ResponseEntity<BaseResponse<?>> handleExceptions(HttpServletRequest req,
            HttpServletResponse rsp, Exception e) {
        BaseResponse<?> response = new BaseResponse<>();
        HttpStatus status = HttpStatus.OK;
        String requestURI = req.getRequestURI();
        if (e instanceof BusinessException) {
            BusinessException be = (BusinessException) e;
            response.setCode(
                    (null == be.getCode()) ? HttpStatus.INTERNAL_SERVER_ERROR.value()
                            : be.getCode());
            response.setMsg(be.getMsg());
            response.setRequestId(BaseContextHolder.getRequestId());
            log.error("request [{}] throw a BaseException: {}", requestURI, be.getMsg());
        } else if (e instanceof NoHandlerFoundException) {
            NoHandlerFoundException nfe = (NoHandlerFoundException) e;
            response.setCode(NOT_FOUND.value());
            response.setMsg(nfe.getMessage());
            response.setRequestId(BaseContextHolder.getRequestId());
            status = NOT_FOUND;
            log.error("request [{}] throw a NoHandlerFoundException: {}", requestURI,
                    nfe.getMessage());
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            HttpRequestMethodNotSupportedException ex = (HttpRequestMethodNotSupportedException) e;
            status = METHOD_NOT_ALLOWED;
            response.setCode(METHOD_NOT_ALLOWED.value());
            response.setMsg(ex.getMessage());
            response.setRequestId(BaseContextHolder.getRequestId());
            log.error("request [{}] throw a HttpRequestMethodNotSupportedException: {}", requestURI,
                    ex.getMessage());
        } else if (e instanceof HttpMediaTypeNotSupportedException) {
            HttpMediaTypeNotSupportedException ex = (HttpMediaTypeNotSupportedException) e;
            status = UNSUPPORTED_MEDIA_TYPE;
            response.setCode(UNSUPPORTED_MEDIA_TYPE.value());
            response.setMsg(ex.getMessage());
            response.setRequestId(BaseContextHolder.getRequestId());
            log.error("request [{}] throw a HttpMediaTypeNotSupportedException: {}", requestURI,
                    ex.getMessage());
        } else if (e instanceof HttpMediaTypeNotAcceptableException) {
            status = NOT_ACCEPTABLE;
            HttpMediaTypeNotAcceptableException ex = (HttpMediaTypeNotAcceptableException) e;
            response.setCode(NOT_ACCEPTABLE.value());
            response.setMsg(ex.getMessage());
            response.setRequestId(BaseContextHolder.getRequestId());
            log.error("request [{}] throw a HttpMediaTypeNotAcceptableException: {}", requestURI,
                    ex.getMessage());
        } else if (e instanceof MissingPathVariableException) {
            status = INTERNAL_SERVER_ERROR;
            MissingPathVariableException ex = (MissingPathVariableException) e;
            response.setCode(INTERNAL_SERVER_ERROR.value());
            response.setMsg(ex.getMessage());
            response.setRequestId(BaseContextHolder.getRequestId());
            log.error("request [{}] throw a MissingPathVariableException: {}", requestURI,
                    ex.getMessage());
        } else if (e instanceof MissingServletRequestParameterException) {
            status = BAD_REQUEST;
            MissingServletRequestParameterException ex = (MissingServletRequestParameterException) e;
            response.setCode(BAD_REQUEST.value());
            response.setMsg(ex.getMessage());
            response.setRequestId(BaseContextHolder.getRequestId());
            log.error("request [{}] throw a MissingServletRequestParameterException: {}",
                    requestURI, ex.getMessage());
        } else if (e instanceof ServletRequestBindingException) {
            status = BAD_REQUEST;
            ServletRequestBindingException ex = (ServletRequestBindingException) e;
            response.setCode(BAD_REQUEST.value());
            response.setMsg(ex.getMessage());
            response.setRequestId(BaseContextHolder.getRequestId());
            log.error("request [{}] throw a ServletRequestBindingException: {}", requestURI,
                    ex.getMessage());
        } else if (e instanceof ConversionNotSupportedException) {
            status = INTERNAL_SERVER_ERROR;
            ConversionNotSupportedException ex = (ConversionNotSupportedException) e;
            response.setCode(INTERNAL_SERVER_ERROR.value());
            response.setMsg(ex.getMessage());
            response.setRequestId(BaseContextHolder.getRequestId());
            log.error("request [{}] throw a ConversionNotSupportedException: {}", requestURI,
                    ex.getMessage());
        } else if (e instanceof TypeMismatchException) {
            status = BAD_REQUEST;
            TypeMismatchException ex = (TypeMismatchException) e;
            response.setCode(BAD_REQUEST.value());
            response.setMsg(ex.getMessage());
            response.setRequestId(BaseContextHolder.getRequestId());
            log.error("request [{}] throw a TypeMismatchException: {}", requestURI,
                    ex.getMessage());
        } else if (e instanceof HttpMessageNotReadableException) {
            status = BAD_REQUEST;
            HttpMessageNotReadableException ex = (HttpMessageNotReadableException) e;
            response.setCode(BAD_REQUEST.value());
            response.setMsg(ex.getMessage());
            response.setRequestId(BaseContextHolder.getRequestId());
            log.error("request [{}] throw a HttpMessageNotReadableException: {}", requestURI,
                    ex.getMessage());
        } else if (e instanceof HttpMessageNotWritableException) {
            status = INTERNAL_SERVER_ERROR;
            HttpMessageNotWritableException ex = (HttpMessageNotWritableException) e;
            response.setCode(INTERNAL_SERVER_ERROR.value());
            response.setMsg(ex.getMessage());
            response.setRequestId(BaseContextHolder.getRequestId());
            log.error("request [{}] throw a HttpMessageNotWritableException: {}", requestURI,
                    ex.getMessage());
        } else if (e instanceof MethodArgumentNotValidException) {
            status = BAD_REQUEST;
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
            response.setCode(BAD_REQUEST.value());
            response.setMsg(ex.getMessage());
            response.setRequestId(BaseContextHolder.getRequestId());
            log.error("request [{}] throw a MethodArgumentNotValidException: {}", requestURI,
                    ex.getMessage());
        } else if (e instanceof MissingServletRequestPartException) {
            status = BAD_REQUEST;
            MissingServletRequestPartException ex = (MissingServletRequestPartException) e;
            response.setCode(BAD_REQUEST.value());
            response.setMsg(ex.getMessage());
            response.setRequestId(BaseContextHolder.getRequestId());
            log.error("request [{}] throw a MissingServletRequestPartException: {}", requestURI,
                    ex.getMessage());
        } else if (e instanceof BindException) {
            status = BAD_REQUEST;
            BindException ex = (BindException) e;
            response.setCode(BAD_REQUEST.value());
            response.setMsg(ex.getMessage());
            response.setRequestId(BaseContextHolder.getRequestId());
            log.error("request [{}] throw a BindException: {}", requestURI, ex.getMessage());
        } else if (e instanceof AsyncRequestTimeoutException) {
            status = SERVICE_UNAVAILABLE;
            AsyncRequestTimeoutException ex = (AsyncRequestTimeoutException) e;
            response.setCode(SERVICE_UNAVAILABLE.value());
            response.setMsg(ex.getMessage());
            response.setRequestId(BaseContextHolder.getRequestId());
            log.error("request [{}] throw a AsyncRequestTimeoutException: {}", requestURI,
                    ex.getMessage());
        } else if (e instanceof HttpClientErrorException) {
            HttpClientErrorException httpClientErrorException = (HttpClientErrorException) e;
            status = httpClientErrorException.getStatusCode();
            response.setCode(status.value());
            response.setMsg(httpClientErrorException.getStatusText());
            response.setRequestId(BaseContextHolder.getRequestId());
        } else {
            response.setCode(INTERNAL_SERVER_ERROR.value());
            response.setMsg(e.getMessage());
            response.setRequestId(BaseContextHolder.getRequestId());
            status = INTERNAL_SERVER_ERROR;
            log.error("request [{}] throw a Exception: {}", requestURI, e.getMessage());
        }
        return new ResponseEntity<>(response, status);
    }

}
