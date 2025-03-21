package com.example.memos.controller;

import com.example.memos.dto.MemoRequestDto;
import com.example.memos.dto.MemoResponseDto;
import com.example.memos.entity.Memo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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


//  @PostMapping
//  public MemoResponseDto creatMemo(@RequestBody MemoRequestDto dto){
//    // 식별자가 1씩 증가하도록 만듦
//    // Collections.max -> 괄호 안에 들어있는 값중에서 최대값을 뽑아냄
//    // memoList.keySet() -> 메모리스트 안에 있는 키값을 모두 꺼내보는것
//    Long memoId = memoList.isEmpty() ? 1 : Collections.max(memoList.keySet()) + 1 ;
//
//    // 요청받은 데이터로 Memo 객체 생성
//    Memo memo = new Memo(memoId,dto.getTitle(), dto.getContents());
//
//    // Inmemory DB에 Memo 저장
//    memoList.put(memoId,memo);
//
//    return new MemoResponseDto(memo);
//
//  }

  @PostMapping
  public ResponseEntity<MemoResponseDto> createMemo(@RequestBody MemoRequestDto dto) {

    // MemoId 식별자 계산
    Long memoId = memoList.isEmpty() ? 1 : Collections.max(memoList.keySet()) + 1;

    // 요청받은 데이터로 Memo 객체 생성
    Memo memo = new Memo(memoId, dto.getTitle(), dto.getContents());

    // Inmemory DB에 Memo 저장
    memoList.put(memoId, memo);

    return new ResponseEntity<>(new MemoResponseDto(memo), HttpStatus.CREATED);
  }

  @GetMapping
  public List<MemoResponseDto> findAllMemos() {

    // init List
    List<MemoResponseDto> responseList = new ArrayList<>();

    // HashMap<Memo> -> List<MemoResponseDto>
    // 방법 1
    for (Memo memo : memoList.values()) {
      MemoResponseDto responseDto = new MemoResponseDto(memo);
      responseList.add(responseDto);
    }
    // Map To List
    // 방법 2
    // responseList = memoList.values().stream().map(MemoResponseDto::new).toList();

    return responseList;
  }

//  @GetMapping("/{id}")
//  public MemoResponseDto findMemoById(@PathVariable Long id){
//    Memo memo = memoList.get(id);
//
//    return new MemoResponseDto(memo);
//
//  }

  @GetMapping("/{id}")
  public ResponseEntity<MemoResponseDto> findMemoById(@PathVariable Long id) {

    // 식별자의 Memo가 없다면?
    Memo memo = memoList.get(id);

    // NPE 방지
    if (memo == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(new MemoResponseDto(memo), HttpStatus.OK);
  }

  //  @PutMapping("/{id}")
//  public MemoResponseDto updateMemoById(
//      @PathVariable Long id,
//      @RequestBody MemoRequestDto dto
//  ) {
//
//    Memo memo = memoList.get(id);
//
//    memo.update(dto);
//
//    return new MemoResponseDto(memo);
//  }

  @PutMapping("/{id}")
  public ResponseEntity<MemoResponseDto> updateMemo(
      @PathVariable Long id,
      @RequestBody MemoRequestDto dto
  ) {

    Memo memo = memoList.get(id);

    // NPE 방지
    if (memo == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // 필수값 검증
    if (dto.getTitle() == null || dto.getContents() == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // memo 수정
    memo.update(dto);

    // 응답
    return new ResponseEntity<>(new MemoResponseDto(memo), HttpStatus.OK);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<MemoResponseDto> updateTitle(
      @PathVariable Long id,
      @RequestBody MemoRequestDto dto
  ) {
    Memo memo = memoList.get(id);

    // NPE 방지
    if (memo == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    // 필수값 검증
    if (dto.getTitle() == null || dto.getContents() != null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    memo.updateTitle(dto);

    return new ResponseEntity<>(new MemoResponseDto(memo), HttpStatus.OK);
  }


//  @DeleteMapping("/{id}")
//  public void deletMemo(@PathVariable Long id){
//    memoList.remove(id);
//
//  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteMemo(@PathVariable Long id) {

    // memoList의 Key값에 id를 포함하고 있다면
    if (memoList.containsKey(id)) {
      // key가 id인 value 삭제
      memoList.remove(id);

      return new ResponseEntity<>(HttpStatus.OK);
    }

    // 포함하고 있지 않은 경우
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

}


