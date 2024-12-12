package com.spot.fun.usr.board.controller;

import com.spot.fun.usr.board.entity.BoardEntity;
import com.spot.fun.usr.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

  @Autowired
  private BoardService boardService;

  @GetMapping
  public List<BoardEntity> getAllBoards() {
    return boardService.getAllBoards();
  }

  @GetMapping("/{id}")
  public ResponseEntity<BoardEntity> getBoardById(@PathVariable Long id) {
    BoardEntity board = boardService.getBoardById(id);
    if (board != null) {
      return ResponseEntity.ok(board);
    }
    return ResponseEntity.notFound().build();
  }

  @PostMapping
  public BoardEntity createBoard(@RequestBody BoardEntity board) {
    return boardService.createBoard(board);
  }

  @PutMapping("/{id}")
  public ResponseEntity<BoardEntity> updateBoard(@PathVariable Long id, @RequestBody BoardEntity updatedBoard) {
    BoardEntity board = boardService.updateBoard(id, updatedBoard);
    if (board != null) {
      return ResponseEntity.ok(board);
    }
    return ResponseEntity.notFound().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
    boardService.deleteBoard(id);
    return ResponseEntity.noContent().build();
  }
}
