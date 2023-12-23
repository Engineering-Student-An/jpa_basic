package hellojpa;

import javax.persistence.*;

@Entity
// 부모 클래스에서 조인 전략 선택 가능
//@Inheritance(strategy = InheritanceType.JOINED)   // => 조인 전략
//@DiscriminatorColumn(name = "DISCRIMINATOR")        // DTYPE 사용 (구별자 컬럼)
                                                 // name = " ": (기본값: DTYPE)
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// => 단일 테이블 전략 : 없는 값은 null로 들어감, 성능 가장 좋음
//@DiscriminatorColumn => 단일 테이블 전략에서는 얘 없어도 DTYPE이 필수로 생성됨
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
// => 구현 클래스마다 테이블 전략 : item에 있던 컬럼 모두 하위 테이블로 내려감
// (item이 클래스 : 독단적으로 쓸 수 있다는 뜻 => item 을 추상 클래스로 만들면) item 테이블이 만들어지지 않음!
// 여기서는 @DiscriminatorColumn 이 의미 없음 => 어노테이션 넣어도 사용 안됨
public abstract class Item {

    @Id @GeneratedValue
    public Long id;

    public String name;
    public int price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
