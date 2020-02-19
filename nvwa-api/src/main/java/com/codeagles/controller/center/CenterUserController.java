package com.codeagles.controller.center;

import com.codeagles.bo.center.CenterUsersBO;
import com.codeagles.controller.BaseController;
import com.codeagles.pojo.Users;
import com.codeagles.resource.FileUpload;
import com.codeagles.service.center.CenterUserService;
import com.codeagles.utils.CookieUtils;
import com.codeagles.utils.DateUtils;
import com.codeagles.utils.JSONResult;
import com.codeagles.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Codeagles
 * Date: 2020/2/19
 * Time: 11:21 AM
 * <p>
 * Description: 用户中心，用户相关接口
 */
@Api(value = "用户信息接口", tags = {"用户信息相关接口"})
@RestController
@RequestMapping("userInfo")
public class CenterUserController extends BaseController {

    @Autowired
    private CenterUserService centerUserService;
    @Autowired
    private FileUpload fileUpload;

    @ApiOperation(value = "修改用户信息", notes = "修改用户信息", httpMethod = "POST")
    @PostMapping("update")
    public JSONResult updateUserInfo(
            @ApiParam(value = "用户id", name = "userId", required = true)
            @RequestParam String userId,
            @RequestBody @Valid CenterUsersBO centerUsersBO,
            BindingResult bindingResult,
            HttpServletRequest request, HttpServletResponse response

            ){
        //判断BindingResult是否有保存错误信息，如果有，直接return
        if(bindingResult.hasErrors()){
            Map<String, String> errors = getErrors(bindingResult);
            return JSONResult.errorMap(errors);
        }

        Users users = centerUserService.updateUserInfo(userId,centerUsersBO);
        users = setNullProperty(users);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(users), true);
        //TODO 后续会增加令牌token，整合redis，分布式会话
        return JSONResult.ok();
    }

    @ApiOperation(value = "修改用户头像", notes = "修改用户头像", httpMethod = "POST")
    @PostMapping("uploadFace")
    public JSONResult uploadFace(
            @ApiParam(value = "用户id", name = "userId", required = true)
            @RequestParam String userId,
            @ApiParam(value = "用户头像", name = "file", required = true)
                    MultipartFile file,
            HttpServletRequest request, HttpServletResponse response

    ){
        //定义头像保存地址
//        String fileSpace = IMAGE_USER_FACE_LOCATION;
        String fileSpace = fileUpload.getImageUserFaceLocation();
        // 在路径上为每个用户增加一个userid用于区分不同用户上传
        String uploadPathPrefix = File.separator + userId;

        // 开始文件上传
        if (file != null) {
            //获得文件的名称
            FileOutputStream fileOutputStream = null;
            InputStream inputStream = null;
            try {
                String fileName = file.getOriginalFilename();
                if (StringUtils.isNotBlank(fileName)) {
                    // 要求格式 face-{userid}.png
                    // 更改文件名
                    String[] split = fileName.split("\\.");
                    String suffix = split[split.length - 1];

                    if(!suffix.equalsIgnoreCase("png")&&
                            !suffix.equalsIgnoreCase("jpg")&&
                            !suffix.equalsIgnoreCase("jpeg")){
                        return JSONResult.errorMsg("图片格式不正确");
                    }
                    //文件名重组 覆盖式上传， 增量式：额外拼接当前时间
                    String newFileName = "face-"+userId+"."+suffix;

                    //上传的头像最终保存的位置
                    String finalFilePath = fileSpace + uploadPathPrefix + File.separator + newFileName;
                    //用于提供给web服务的地址
                    uploadPathPrefix  += ("/" + newFileName);
                    File outFile = new File(finalFilePath);
                    if(outFile.getParentFile() != null){
                        //创建文件夹
                        outFile.getParentFile().mkdirs();
                    }
                    //文件输出保存目录
                    fileOutputStream = new FileOutputStream(outFile);
                    inputStream = file.getInputStream();
                    IOUtils.copy(inputStream, fileOutputStream);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }else {
            return JSONResult.errorMsg("文件不能为空");
        }

        //更新用户头像到数据库
        String imageServerUrl = fileUpload.getImageServerUrl();
        //由于浏览器可能存在缓存，所以增加时间戳来保证更新后的图片及时刷新
        String finalUserFaceUrl = imageServerUrl + uploadPathPrefix + "?t="+ DateUtils.getCurrentDateString(DateUtils.DATE_PATTERN);
        Users users = centerUserService.updateUserFace(userId, finalUserFaceUrl);

        users = setNullProperty(users);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(users), true);
        //TODO 后续会增加令牌token，整合redis，分布式会话

        return JSONResult.ok();
    }





    private Map<String, String> getErrors(BindingResult bindingResult){

        Map<String, String> errorMap = new HashMap<>();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            //发生验证错误的某个属性
            String field = fieldError.getField();
            //验证错误的信息
            String defaultMessage = fieldError.getDefaultMessage();
            errorMap.put(field, defaultMessage);

        }
        return errorMap;
    }

    private Users setNullProperty(Users users) {
        users.setPassword(null);
        users.setMobile(null);
        users.setCreateTime(null);
        users.setUpdateTime(null);
        users.setBirthday(null);
        users.setEmail(null);
        return users;
    }

}
