package com.invenia.portalservice.api.config;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Config {

  @Id
  private String id;

  @Column(columnDefinition = "TEXT")
  private String internalBookmark;

  @Column(columnDefinition = "TEXT")
  private String externalBookmark;

  private String theme;
}
