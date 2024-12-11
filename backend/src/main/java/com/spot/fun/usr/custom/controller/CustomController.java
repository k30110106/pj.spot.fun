package com.spot.fun.usr.custom.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spot.fun.usr.custom.dto.CustomDTO;
import com.spot.fun.usr.custom.service.CustomService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/usr/custom")  
public class CustomController {

    private final CustomService service;

    @GetMapping("/{cno}")
    public ResponseEntity<CustomDTO> get(@PathVariable("cno") Long cno) {
        CustomDTO customDTO = service.get(cno);
        return ResponseEntity.ok(customDTO);
    }

    @GetMapping("/list")
    public ResponseEntity<List<CustomDTO>> list() {
        List<CustomDTO> customList = service.list(); // 🟢 서비스 호출
        return ResponseEntity.ok(customList); // 🟢 조회된 목록을 JSON 형태로 반환
    }
    
    @PostMapping("/")
    public ResponseEntity<Long> register(@RequestBody CustomDTO customDTO) {
        Long id = service.register(customDTO);
        return ResponseEntity.ok(id);
    }

    @PutMapping("/{cno}")
    public ResponseEntity<Long> update(@PathVariable("cno") Long cno, @RequestBody CustomDTO customDTO) {
        service.update(cno, customDTO);
        return ResponseEntity.ok(cno);
    }

    @DeleteMapping("/{cno}")
    public ResponseEntity<String> delete(@PathVariable("cno") Long cno) {
        service.delete(cno);
        return ResponseEntity.ok("코스가 성공적으로 삭제되었습니다.");
    }
}
