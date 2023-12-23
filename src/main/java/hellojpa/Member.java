/*
package hellojpa;

import javax.persistence.*;
import java.util.Date;

@Entity     // JPA가 관리하는 객체라는 뜻 / 데이터베이스 테이블과 맵핑해서 쓴다라고 생각
//@Table(name = "MBR")    // 어떤 DB 테이블과 매핑할지 선택 (기본값 : 엔티티 이름 그대로)

public class MemberBasic {

    @Id                                             // : (@ID만 쓰면) 기본 키 직접 할당
    @GeneratedValue(strategy = GenerationType.IDENTITY) // : 기본 키 자동 생성
    // strategy = GenerationType
    // AUTO : DB 방언에 맞춰서 자동 생성
    // IDENTITY : 기본 키 생성을 DB에 위임 (ex. MySQL의 AUTO_INCREMENT)
    // SEQUENCE : 주로 ORACLE에서 사용
    // TABLE : 키 생성 전용 테이블을 만들어서 DB 시퀀스를 흉내냄
    private Long id;

    //@Column()   // name = "" : DB 테이블에서의 컬럼 이름 (기본값 : 필드 이름 그대로)
                // unique = true : unique 제약조건 (제약 조건 이름이 랜덤 => 잘 안씀 -> @Table에서의 unique 제약 조건을 많이 씀)
                // length = 10 : 길이 설정
                // nullable = false : null 불가
                // insertable / updatable = true/false : 컬럼 수정 시 DB에 반영 할 지 안 할 지
                // columnDefinition = "varchar(100) default 'EMPTY'" : DB 컬럼 정보를 직접 줌 => 사용한 문구가 그대로 DDL 문에 반영
    // => DDL 생성 기능 : JPA 실행 자체에 영향 x, DDl 생성하는데만 영향!
    private String name;

    private Integer age;    // Integer 타입 사용해도 DB에 Integer와 가장 적합한 숫자 타입 선택됨

    @Enumerated(EnumType.STRING)    // 객체에서 ENUM 타입 사용할 때 (MySQL : varchar(255)로 생성됨)
// EnumType.ORDINAL : enum 순서를 DB에 저장 => 사용 X !!!!!!!!!
// EnumType.STRING : enum 이름을 DB에 저장 => 반드시 STRING으로 사용할 것 !
    private RoleType roleType;

    @Temporal(TemporalType.TIMESTAMP)   // 날짜 타입
    // TemporalType.DATE        : 날짜
    // TemporalType.TIME        : 시간
    // TemporalType.TIMESTAMP   : 날짜 + 시간
// java 8 이상 : LocalDate(년월) 혹은 LocalDateTime(년월일) 로 선언하면 @Temporal 필요 X
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @Lob    // CLOB => String 타입에 대해서는 CLOB으로 기본 생성 (MySQL : longtext로 생성됨), BLOB 타입
    private String description;

    @Transient  // DB의 컬럼과 매핑 안함! ( : DB와는 관계없이 메모리에서 작업하고 싶을때)
    private int temp;


    public MemberBasic() {
    }
    // JPA는 내부적으로 리플렉션 등을 쓰기 때문에 동적으로 객체를 생성해야 함 => 기본 생성자가 반드시 필요함!
//    public Member(Long id, String name) {
//        this.id = id;
//        this.name = name;
//    }

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

* */


package hellojpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Member extends BaseEntity{ // BaseEntity 클래스에 저장된 공통 속성을 상속받음
                                        // 상속관계 매핑 x => 속성을 공통으로 쓰고싶을 때 사용 !
    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

//    @Column(name = "TEAM_ID")
//    private Long teamId;        // 외래키 => 직접 적어주는 방법
    // Member와 Team을 레퍼런스로 가져가야 하는데 DB에 맞춰서 모델링 함
    // 외래키와 (객체) 참조는 완전히 다른 패러다임 !

    // 멤버와 팀 => n : 1 (멤버입장 : many, 팀으로 : to one)
//    @ManyToOne(fetch = FetchType.LAZY)  // FetchType.LAZY (지연 로딩 전략) : 멤버와 팀의 쿼리가 분리되서 나감
    //FetchType.LAZY : Team을 프록시타입으로 조회! => Member 클래스만 DB에서 조회 !
    //                 => 지연로딩으로 세팅 : 연관된 것을 프록시로 가져옴!!!!
    @ManyToOne(fetch = FetchType.EAGER) // 즉시 로딩 전략
    // => member와 team에 대한 쿼리가 처음부터 동시에 나감
    // => 프록시 필요 x
    @JoinColumn(name = "TEAM_ID")   // 객체에서의 Team (참조)와 DB에서의 TEAM_ID (외래키) 를 매핑
                                    // 조인하는 컬럼이 무엇인지를 나타냄 -> 외래키
    private Team team;
    // 연관관계의 주인 ! => 외래키가 있는 곳이 연관관계의 주인임
                    //  연관관계의 주인만이 외래키 관리! (등록, 수정)

    /* 일대다 양방향 매핑 => 역방향 (읽기전용)
    @ManyToOne
    @JoinColumn(name = "TEAM_ID", insertable = false, updatable = false)
    private Team team;
     */

    // 일대일 관계
    @OneToOne
    @JoinColumn(name = "LOCKER_ID")
    private Locker locker;

    // 다대다 관계
//    @ManyToMany
//    @JoinTable(name = "MEMBER_PRODUCT")
//    private List<Product> products = new ArrayList<>();

    // 다대다 관계 => 일대다 + 다대일 관계로
    @OneToMany(mappedBy = "member")
    private List<MemberProduct> mwemberProducts = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Team getTeam() {
        return team;
    }

    //public void setTeam(Team team) {    // => 연관관계 편의 메서드 : 값 설정 위해서 각각 메서드 호출 필요 x => 원자적
                                        // 하나만 호출해도 양쪽에 값이 설정되게
    public void changeTeam(Team team){  // 로직이 들어가있으므로 단순 getter, setter가 아님 => 메서드명을 바꿔줌
        this.team = team;
        team.getMembers().add(this);    // 반대편에도 값 설정 추가 !
    }
}
