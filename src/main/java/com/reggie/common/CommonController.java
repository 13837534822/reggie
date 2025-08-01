package com.reggie.common;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.UUID;

@RequestMapping("/common")
@RestController
public class CommonController {

    /**
     * 获取上传路径
     * @return
     */
    private String getUploadPath() throws IOException {
        String path = this.getClass().getClassLoader().getResource("").getPath();
        // 解码路径，解决中文路径问题
        path = URLDecoder.decode(path, "UTF-8");
        if (path.startsWith("/")) {
            // Windows系统需要去掉开头的"/"
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                path = path.substring(1);
            }
        }
        return path + "upload" + File.separator;
    }

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        String oldName = file.getOriginalFilename();
        //使用uuid重新生成文件名
        String fileName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."));
        try {
            //文件转存
//            file.transferTo(new File("C:\\Users\\庞家欣\\Desktop\\瑞吉外卖项目\\资料\\图片资源\\" + fileName));

            String uploadPath = getUploadPath();
            file.transferTo(new File(uploadPath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.success(fileName); //返回图片名称
    }

    //图片回显展示
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {

        try {
            String uploadPath = getUploadPath();
            //通过输入流读文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(uploadPath + name));
//            FileInputStream fileInputStream = new FileInputStream(new File("C:\\Users\\庞家欣\\Desktop\\瑞吉外卖项目\\资料\\图片资源\\" + name));
            //输出流，写回浏览器
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            byte[] bytes = new byte[2048];
            int temp = fileInputStream.read(bytes);
            while (temp != -1) {
                outputStream.write(bytes, 0, temp);
                outputStream.flush();
                temp = fileInputStream.read(bytes);
            }
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
