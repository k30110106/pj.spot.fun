package com.spot.fun.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

@Log4j2
@Component
@RequiredArgsConstructor
public class FileUploadUtil {

  @Value("${file.upload.default.path}")
  private String FILE_DEFAULT_PATH;
  private String FILE_SEPARATOR = File.separator;

  /***
   * 첨부파일 등록
   *
   * @param files
   * @param menuName
   * @return
   * @throws RuntimeException
   */
  public List<Map<String, Object>> saveFiles(List<MultipartFile> files, String menuName) throws RuntimeException {
    if(files.isEmpty() || StringUtils.isBlank(menuName)) {
      return List.of();
    }

//    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//    Calendar c1 = Calendar.getInstance();
//    String strToday = sdf.format(c1.getTime());
//    String uploadPath = FILE_DEFAULT_PATH + FILE_SEPARATOR + menuName + FILE_SEPARATOR + strToday;

    String uploadPath = FILE_DEFAULT_PATH + FILE_SEPARATOR + menuName;
    File folderPath = new File(uploadPath);
    if(!folderPath.exists()) {
      folderPath.mkdirs();
    }

    List<Map<String, Object>> results = new ArrayList<>();
    for(MultipartFile multipartFile : files) {
      String contentType = multipartFile.getContentType();
      if(!StringUtils.isBlank(contentType) && !contentType.startsWith("image")) { // 이미지 타입만 업로드가능
        log.info("[File Type Check] not image type! .. contentType : {}", contentType);
        continue;
      }

      String originName = multipartFile.getOriginalFilename();
      String uploadName = UUID.randomUUID().toString() + originName.substring(originName.lastIndexOf("."));

      Path savePath = Paths.get(uploadPath, uploadName);

      try {
        Files.copy(multipartFile.getInputStream(), savePath);

        // 썸네일이미지 처리
        Path thumbnailPath = Paths.get(uploadPath, "s_"+uploadName);
        Thumbnails.of(savePath.toFile())
                .size(200, 200)
                .toFile(thumbnailPath.toFile());

        Map<String, Object> fileMap = new HashMap<>();
        //fileMap.put("filePath", uploadPath);
        fileMap.put("uploadName", uploadName);
        fileMap.put("originName", originName);

        results.add(fileMap);
      } catch(IOException e) {
        log.info("<--- saveFiles method failed --->");
        throw new RuntimeException(e.getMessage());
      }
    }
    return results;
  }

  /***
   * 이미지 출력
   * @param menuType
   * @param uploadName
   * @return
   */
  public ResponseEntity<Resource> outputImage(String menuType, String uploadName) {
    Resource resource = new FileSystemResource(FILE_DEFAULT_PATH + FILE_SEPARATOR + menuType + FILE_SEPARATOR + uploadName);

    if(!resource.exists()) {
      String archivePath = FILE_DEFAULT_PATH + FILE_SEPARATOR + "archive";
      File folderPath = new File(archivePath);
      if(!folderPath.exists()) {
        folderPath.mkdirs();
      }

      resource = new FileSystemResource(archivePath + FILE_SEPARATOR + "no_image.jpg");
    }

    HttpHeaders headers = new HttpHeaders();
    try {
      headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
    } catch (Exception e) {
      log.info("<--- outputImage method failed --->");
      return ResponseEntity.internalServerError().build();
    }

    return ResponseEntity.ok().headers(headers).body(resource);
  }

}
