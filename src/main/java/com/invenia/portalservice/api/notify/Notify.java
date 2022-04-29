package com.invenia.portalservice.api.notify;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notify {

  private String title;
  private String content;
  private String userNo;
}
