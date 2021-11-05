package com.karaoke.manager.entity.support;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class ResponseApi<E> {

  private final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
  private Integer status;
  private String message;
  private E data;

  public ResponseApi(Integer status, String message, E data) {
    this.status = status;
    this.message = message;
    this.data = data;
  }

  public ResponseApi(Integer status, String message) {
    this.status = status;
    this.message = message;
  }

  public ResponseApi(Integer status, E data) {
    this.status = status;
    this.data = data;
    message = "success";
  }

  public ResponseApi(Integer status) {
    this.status = status;
    message = "success";
  }
}
