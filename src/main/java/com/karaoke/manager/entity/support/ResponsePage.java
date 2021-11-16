package com.karaoke.manager.entity.support;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponsePage {
  private Object data;
  private Integer currentPage;
  private Integer totalPages;
}
