package hellojpa;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("A")    // DTYPE의 값 설정 가능 => 기본값 : 엔티티 이름 (Album)
public class Album extends Item{
    private String artist;
}
