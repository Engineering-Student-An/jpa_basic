package hellojpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Team extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "team")   // mappedBy = "team" : Member 클래스에서 team (변수명)과 연결되어 있다는 뜻 (team으로 매핑되어있다는 뜻)
    // => Member에 존재하는 team이 연관관계의 주인이라는 뜻 ! : mappedBy가 적힌 곳은 읽기만 가능! => 값을 넣어봐야 아무런 일도 일어나지 않음, 조회만 가능
    private List<Member> members = new ArrayList<>();   // ArrayList로 초기화 해두는 것이 관례 (: add할 때 nullpointerexception 발생 x)

    /* 일대다 단방향 매핑
    @OneToMany
    @JoinColumn(name = "TEAM_ID")
    private List<Member> members = new ArrayList<>();

    // 메인 메서드에서
    team.getMembers().add(member); 로 외래키 update 함 ! => update 쿼리가 추가로 날라감 (그대로 team 테이블이 아니므로)
     */


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

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }
}
