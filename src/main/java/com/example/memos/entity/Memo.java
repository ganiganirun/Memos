package com.example.memos.entity;


import com.example.memos.dto.MemoRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Memo {

  // long -> wapper 클래스라 null 포함가능
  private Long id;

  private String title;

  private String contents;

  public void update(MemoRequestDto dto){
    this.title = dto.getTitle();
    this.contents = dto.getContents();
  }

  public void updateTitle(MemoRequestDto dto) {
    this.title = dto.getTitle();
  }

}
