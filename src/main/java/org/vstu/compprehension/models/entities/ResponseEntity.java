package org.vstu.compprehension.models.entities;

import lombok.ToString;
import org.vstu.compprehension.models.entities.EnumData.SpecValue;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "Response")
public class ResponseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "leftSpecValue")
    @Enumerated(EnumType.ORDINAL)
    private SpecValue specValue;

    @ManyToOne
    @JoinColumn(name = "leftObject_id")
    private AnswerObjectEntity leftAnswerObject;

    @ManyToOne
    @JoinColumn(name = "rightObject_id")
    private AnswerObjectEntity rightAnswerObject;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "interaction_id")
    private InteractionEntity interaction;


}
