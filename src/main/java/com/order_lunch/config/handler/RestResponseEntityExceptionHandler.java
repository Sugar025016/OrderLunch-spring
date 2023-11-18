package com.order_lunch.config.handler;

import javax.management.relation.RelationNotFoundException;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.server.ResponseStatusException;

import com.order_lunch.model.ValidationError;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler {
    // public class RestResponseEntityExceptionHandler implements WebMvcConfigurer{

    // @ResponseBody
    // @ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
    // protected ResponseEntity<Object> handleConflict(
    //         RuntimeException ex, WebRequest request) {
    //     String bodyOfResponse = "This should be application specific";
    //     // return handleExceptionInternal(ex, bodyOfResponse,
    //     // new HttpHeaders(), HttpStatus.CONFLICT, request);
    //     return ResponseEntity.status(HttpStatus.NOT_FOUND).body(bodyOfResponse);
    // }

    // @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex) {
        // 自定義錯誤回應，例如返回一個包含錯誤訊息的 JSON 物件
        String errorMessage = "Not found: " + ex.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ValidationError> handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        ValidationError validationError = new ValidationError();

        // 获取所有验证错误并添加到ValidationError对象中
        bindingResult.getFieldErrors().forEach(fieldError -> {
            validationError.addFieldError(fieldError.getField(), fieldError.getDefaultMessage());
        });

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(validationError);
    }

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ValidationError> handleValidationException(ResponseStatusException ex) {
        // String exceptionClassName = ex.getClass().getName();
        ValidationError validationError = new ValidationError();

        // validationError.addFieldError(exceptionClassName,ex.getReason());

        // 获取异常消息
        String errorMessage = ex.getLocalizedMessage();

        // 从异常消息中提取类名
        String className = extractClassNameFromErrorMessage(errorMessage);

        validationError.addFieldError(className, ex.getReason());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(validationError);
    }

    @ExceptionHandler(RelationNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceAccessException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // @ExceptionHandler(OtherException.class)
    // public ResponseEntity<Object> handleOtherException(OtherException ex) {
    // // 自定義其他異常的處理邏輯，返回適當的錯誤回應
    // String errorMessage = "Other exception: " + ex.getMessage();
    // return
    // ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    // }

    private String extractClassNameFromErrorMessage(String errorMessage) {
        // 在实际项目中，您可以编写逻辑来从错误消息中提取类名
        // 这里只是一个示例，您可能需要根据实际的错误消息格式来实现逻辑
        // 以下示例假设错误消息的格式是 "ClassNotFound: 类名"
        String[] parts = errorMessage.split(": ");
        if (parts.length >= 2) {
            return parts[1].trim();
        } else {
            return "UnknownClass";
        }
    }

}