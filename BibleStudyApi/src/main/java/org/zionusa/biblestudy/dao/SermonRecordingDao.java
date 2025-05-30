package org.zionusa.biblestudy.dao;

import org.zionusa.base.dao.BaseDao;
import org.zionusa.biblestudy.domain.SermonRecording;

import java.util.List;

public interface SermonRecordingDao extends BaseDao<SermonRecording, Integer> {

    List<SermonRecording> findByParentChurchId(Integer churchId);

    List<SermonRecording> findByPreacherId(Integer preacherId);

    List<SermonRecording> findByChurchId(Integer churchId);
}
