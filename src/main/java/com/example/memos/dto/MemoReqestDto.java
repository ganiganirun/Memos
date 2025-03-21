package com.example.memos.dto;

import lombok.Getter;

@Getter
public class MemoRequestDto {
  // 요청 받을 데이터는 title, contents입니다.
  private String title;
  private String contents;


}