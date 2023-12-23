package hellojpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class MemberWithEm{

    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String name;

    //기간 Period
//    @Embedded   // 임베디드 타입 사용
//    private Period workPeriod;
//
//    //주소 Address
//    @Embedded   // 임베디드 타입 사용
//    private Address homeAddress;
//
//    @Embedded
//    @AttributeOverrides({   // 한 엔티티에서 같은 값 타입 사용 할 수 있음
//            @AttributeOverride(name = "city",       // DB 컬럼을 따로 매핑해야함! (컬럼명이 중복되기 때문에)
//            column = @Column(name = "WORK_CITY")),
//            @AttributeOverride(name = "street",
//            column = @Column(name = "WORK_STREET")),
//            @AttributeOverride(name = "zipcode",
//            column = @Column(name = "WORK_ZIPCODE"))
//    })
//    private Address workAddress;

    // 값 타입 컬렉션 예제
    @Embedded
    private Address homeAddress;

    // 기본값 타입을 컬렉션으로 저장
    @ElementCollection                          // 값 타입 컬렉션 (fetch = 기본값 : LAZY) 지연로딩!
    @CollectionTable(name = "FAVORITE_FOOD",    // 컬렉션 테이블 이름 지정
        joinColumns = @JoinColumn(name = "MEMBER_ID") ) // DB에서 매핑할 FK
    @Column(name = "FOOD_NAME")                 // 값 1개이고 임베디드 타입 x 이므로 컬럼명 지정 가능
    private Set<String> favoriteFoods = new HashSet<>();

    // 임베디드 타입을 컬렉션으로 저장
//    @ElementCollection
//    @CollectionTable(name = "ADDRESS",
//        joinColumns = @JoinColumn(name = "MEMBER_ID"))
//    private List<Address> addressHistory = new ArrayList<>(); //값 타입으로 매핑 x => 엔티티로 매핑하기 !!
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "MEMBER_ID") // 일대다 단방향으로 풀어내기!
    private List<AddressEntity> addressHistory = new ArrayList<>();

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

//    public Period getWorkPeriod() {
//        return workPeriod;
//    }
//
//    public void setWorkPeriod(Period workPeriod) {
//        this.workPeriod = workPeriod;
//    }
//
//    public Address getHomeAddress() {
//        return homeAddress;
//    }
//
    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public Set<String> getFavoriteFoods() {
        return favoriteFoods;
    }

    public void setFavoriteFoods(Set<String> favoriteFoods) {
        this.favoriteFoods = favoriteFoods;
    }

//    public List<Address> getAddressHistory() {
//        return addressHistory;
//    }
//
//    public void setAddressHistory(List<Address> addressHistory) {
//        this.addressHistory = addressHistory;
//    }


    public List<AddressEntity> getAddressHistory() {
        return addressHistory;
    }

    public void setAddressHistory(List<AddressEntity> addressHistory) {
        this.addressHistory = addressHistory;
    }
}
