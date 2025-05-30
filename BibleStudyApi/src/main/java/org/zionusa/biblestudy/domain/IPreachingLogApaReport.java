package org.zionusa.biblestudy.domain;

public interface IPreachingLogApaReport {
    Integer getActivityPoints();

    void setActivityPoints(Integer value);

    Integer getChurchId();

    void setChurchId(Integer value);

    String getChurchName();

    void setChurchName(String value);

    String getDisplayName();

    void setDisplayName(String value);

    Integer getPreachingPoints();

    void setPreachingPoints(Integer value);

    Integer getNumLogs();

    void setNumLogs(Integer value);

    String getPictureThumbnailUrl();

    void setPictureThumbnailUrl(String value);

    Integer getTeachingPoints();

    void setTeachingPoints(Integer value);

    Integer getTotalPoints();

    void setTotalPoints(Integer value);

    Integer getUserId();

    void setUserId(Integer value);

}
