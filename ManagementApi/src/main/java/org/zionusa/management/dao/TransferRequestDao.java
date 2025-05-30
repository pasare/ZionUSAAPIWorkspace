package org.zionusa.management.dao;

import org.zionusa.base.dao.BaseDao;
import org.zionusa.management.domain.TransferRequest;

import java.util.Date;
import java.util.List;

public interface TransferRequestDao extends BaseDao<TransferRequest, Integer> {

    List<TransferRequest> getTransferRequestsByRequestDateBetween(Date startDate, Date endDate);

    List<TransferRequest> getTransferRequestsByUserId(Integer userId);

    List<TransferRequest> getTransferRequestsByNewTeamId(Integer teamId);

    List<TransferRequest> getTransferRequestsByNewGroupId(Integer groupId);

    List<TransferRequest> getTransferRequestsByNewChurchId(Integer churchId);
}
