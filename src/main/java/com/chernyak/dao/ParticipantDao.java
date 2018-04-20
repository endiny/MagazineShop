package com.chernyak.dao;

import com.chernyak.entity.Participant;
import java.util.List;

/**
 * Participant DAO interface
 */
public interface ParticipantDao extends GenericDao<Participant> {
    
     List<Participant> getParticipantByCourseId(int courseId);
}
