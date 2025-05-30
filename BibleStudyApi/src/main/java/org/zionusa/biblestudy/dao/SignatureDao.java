package org.zionusa.biblestudy.dao;

import org.zionusa.base.dao.BaseDao;
import org.zionusa.biblestudy.domain.Signature;

import java.util.List;

public interface SignatureDao extends BaseDao<Signature, Integer> {

    List<Signature> findByTeacherId(Integer teacherId);

    Signature findByTeacherIdAndStudyId(Integer teacherId, Integer studyId);

    List<Signature> findByStudyId(Integer studyId);

    List<Signature> findByGeneralAssemblyGreaterThanOrReadyGreaterThanOrTeachingGreaterThan(Integer generalAssembly, Integer ready, Integer teaching);

    List<Signature> findAllByGeneralAssemblyUpdatedDateBetween(String startDate, String endDate);

    List<Signature> getSignaturesByTeacherIdAndGeneralAssemblyUpdatedDateBetween(
        Integer teacherId, String startDate, String endDate);

    Signature findSignatureByTeacherIdAndStudyId(Integer teacherId, Integer studyId);

    List<Signature> getSignaturesByGeneralAssemblyGraderIdOrReadyGraderIdOrTeachingGraderId(
        Integer generalAssemblyGraderId, Integer readyGraderId, Integer teachingGraderId);

}
