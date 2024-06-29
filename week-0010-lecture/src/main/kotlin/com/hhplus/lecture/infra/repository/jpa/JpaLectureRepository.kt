package com.hhplus.lecture.infra.repository.jpa

import com.hhplus.lecture.domain.entity.Lecture
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query

interface JpaLectureRepository : JpaRepository<Lecture, Long> {
    /**
     * DB 로 Mysql 을 사용한다는 전제하에 베타락을 사용한다.
     * - Mysql 은 MVCC를 제공하므로, 베타락을 사용하더라도 다른 트랜잭션에서 읽기 작업은 허용된다.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select lecture from Lecture lecture where lecture.id = :lectureId")
    fun findByLectureIdWithPessimisticLock(lectureId: Long): Lecture?
}
