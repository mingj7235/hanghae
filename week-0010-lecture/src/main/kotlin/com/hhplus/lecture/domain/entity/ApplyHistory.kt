package com.hhplus.lecture.domain.entity

import com.hhplus.lecture.common.type.ApplyStatus
import jakarta.persistence.Column
import jakarta.persistence.ConstraintMode
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.ForeignKey
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class ApplyHistory(
    student: Student,
    lecture: Lecture,
    applyStatus: ApplyStatus,
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @ManyToOne
    @JoinColumn(name = "student_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var student: Student = student
        protected set

    @ManyToOne
    @JoinColumn(name = "lecture_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var lecture: Lecture = lecture
        protected set

    @Column(name = "apply_status")
    @Enumerated(value = EnumType.STRING)
    var applyStatus: ApplyStatus = applyStatus
        protected set
}
