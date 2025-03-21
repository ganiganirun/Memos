package com.example.memos.controller;

import com.example.memos.dto.MemoRequestDto;
import com.example.memos.dto.MemoResponseDto;
import com.example.memos.entity.Memo;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/memos")
public class MemoController {
  // 데이터베이스에 저장이 아닌 자료구조를 활용해 임시로 데이터 저장
  // 초기화 해주게 되면 메모리스트라는 빈 맵 자료구조 생성됨
  private final Map<Long, Memo> memoList = new HashMap<>();


  @PostMapping
  public MemoResponseDto creatMemo(@RequestBody MemoRequestDto dto){
    // 식별자가 1씩 증가하도록 만듦
    // Collections.max -> 괄호 안에 들어있는 값중에서 최대값을 뽑아냄
    // memoList.keySet() -> 메모리스트 안에 있는 키값을 모두 꺼내보는것
    Long memoId = memoList.isEmpty() ? 1 : Collections.max(memoList.keySet()) + 1 ;

    // 요청받은 데이터로 Memo 객체 생성
    Memo memo = new Memo(memoId,dto.getTitle(), dto.getContents());

    // Inmemory DB에 Memo 저장
    memoList.put(memoId,memo);

    return new MemoResponseDto(memo);

  }

  @GetMapping("/{id}")
  public MemoResponseDto findMemoById(@PathVariable Long id){
    Memo memo = memoList.get(id);

    return new MemoResponseDto(memo);

  }

  @PutMapping("/{id}")
  public MemoResponseDto updateMemoById(
      @PathVariable Long id,
      @RequestBody MemoRequestDto dto
  ) {

    Memo memo = memoList.get(id);

    memo.update(dto);

    return new MemoResponseDto(memo);
  }

  @DeleteMapping("/{id}")
  public void deletMemo(@PathVariable Long id){
    memoList.remove(id);

  }
}
