package com.spot.fun.usr.board.service;

import com.spot.fun.usr.board.entity.BoardEntity;
import com.spot.fun.usr.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService  {

  @Autowired
  private BoardRepository boardRepository;

  public List<BoardEntity> getAllBoards() {
    return boardRepository.findAll();
  }

  public BoardEntity getBoardById(Long id) {
    return boardRepository.findById(id).orElse(null);
  }

  public BoardEntity createBoard(BoardEntity board) {
    return boardRepository.save(board);
  }

  public BoardEntity updateBoard(Long id, BoardEntity updatedBoard) {
    BoardEntity board = boardRepository.findById(id).orElse(null);
    if (board != null) {
      board.setTitle(updatedBoard.getTitle());
      board.setContent(updatedBoard.getContent());
      return boardRepository.save(board);
    }
    return null;
  }

  public void deleteBoard(Long id) {
    boardRepository.deleteById(id);
  }
}
