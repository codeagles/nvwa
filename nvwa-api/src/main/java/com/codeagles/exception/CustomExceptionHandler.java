package com.codeagles.exception;

import com.codeagles.utils.JSONResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * Created with IntelliJ IDEA.
 * User: Codeagles
 * Date: 2020/2/19
 * Time: 5:59 PM
 * <p>
 * Description:
 */
@RestControllerAdvice
public class CustomExceptionHandler {

    //MaxUploadSizeExceededException
    //上传文件超过500K捕获异常
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public JSONResult handlerMaxUploadFile(MaxUploadSizeExceededException ex){
        return JSONResult.errorMsg("文件上传大小不能超过500k，请修改文件大小");
    }
}
