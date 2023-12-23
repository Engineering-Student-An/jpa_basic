package hellojpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

// 영속성 전이 => cascade 예시
@Entity
public class Parent {
    @Id @GeneratedValue
    private Long id;
    private String name;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    // cascade => 해당 엔티티 영속화 할 때 연관 엔티티도 함께 영속화함 ! => em.persist를 여러번 호출 할 필요 x
    // parent 엔티티만 child 엔티티를 관리, 연관 이면 가능 / 다른 엔티티가 child와 연관이 있으면 cascade 사용 x
    // (소유자가 1개일때)
    private List<Child> childList = new ArrayList<>();

    public void addChild(Child child){  // 연관관계 편의 메서드
        childList.add(child);
        child.setParent(this);
    }

    public List<Child> getChildList() {
        return childList;
    }

    public void setChildList(List<Child> childList) {
        this.childList = childList;
    }

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
}
