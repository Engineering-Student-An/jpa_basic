// 07. 고급매핑
package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{

            // 09. 값 타입
            Address address = new Address("city", "s", "z");
            MemberWithEm member1 = new MemberWithEm();
            member1.setName("member1");
//            member1.setHomeAddress(address);
            em.persist(member1);

            // 같은 address 를 member2에 넣는게 아니라 address를 복사한 새 객체를 넣기
            Address copyAddress = new Address(address.getCity(), address.getStreet(), address.getStreet());

            MemberWithEm member2 = new MemberWithEm();
            member2.setName("member2");
//            member2.setHomeAddress(copyAddress);
            em.persist(member2);

            // member1의 주소를 바꿔도 member2의 주소가 함께 바뀜 (값 타입 공유에 의해서)
            // => update 쿼리 2번 전송
            // 공유해서 사용하고 싶으면 값타입 말고 => 엔티티 사용 !
            // address를 복사해서 사용하면 동시에 바뀌지 않음!

            //member1.getHomeAddress().setCity("newCity"); ==> (불변객체) 객체 생성 시점 이후에는 값을 절대 변경할 수 없음 !

            //member1의 address를 바꾸고 싶으면 새로운 address 객체 생성해서 통으로 데이터 변경해야함
            Address newAddress = new Address("NewCity", address.getStreet(), address.getZipcode());
//            member1.setHomeAddress(newAddress);

            // 값타입 컬렉션 저장
            MemberWithEm member = new MemberWithEm();
            member.setName("MEMBER_A");
            member.setHomeAddress(new Address("city", "street", "zipcode"));

            member.getFavoriteFoods().add("치킨");
            member.getFavoriteFoods().add("족발");
            member.getFavoriteFoods().add("피자");

//            member.getAddressHistory().add(new Address("oldCity", "oldStreet", "oldZipcode"));

            em.persist(member); // 값 타입 컬렉션은 따로 persist하지 않음 : 다른 테이블임에도 라이프 사이클이 같이 돌아감
                                // 이유 : 값 타입 컬렉션도 스스로는 라이프 사이클 존재 x, 모든 생명주기가 member에 소속됨
                                //       username, homeAddress 와 같이 값 타입이므로

            // 값타입 컬렉션 조회
            em.flush();
            em.clear();

            MemberWithEm findMember = em.find(MemberWithEm.class, member.getId());  // select 쿼리로 member만 가져옴 => 컬렉션들은 다 지연로딩 !!!!

//            List<Address> addressHistory = findMember.getAddressHistory();  // 이 부분에서 값 타입 컬렉션에 대한 select 쿼리 날라감
//            for (Address address1 : addressHistory) {
//                System.out.println("address1 = " + address1.getCity());
//            }

            // 값타입 수정
//            findMember.getHomeAddress().setCity("newCity"); // 불가!! 값 타입 : immutable(불변) 해야함!!
            Address a = findMember.getHomeAddress();
            findMember.setHomeAddress(new Address("newCity", a.getStreet(), a.getZipcode()));    // 값 타입 : 불변이므로 새로 데이터 통으로!! 입력해야함!!

            // 값타입 컬렉션 수정1
            // Set<String> => String (값타입)이므로 기존 데이터 삭제 후 새로운 데이터 삽입하는 식으로 수정해야 함! UPDATE 불가!!
            findMember.getFavoriteFoods().remove("치킨");
            findMember.getFavoriteFoods().add("한식");

            // 값타입 컬렉션 수정2
            // 컬렉션은 대부분 대상을 찾을 때 equals()를 사용함 => 따라서 equals, hashCode 메서드 제대로 구현 안하면 제대로 작동 x
            findMember.getAddressHistory().remove(new AddressEntity("oldCity", "oldStreet", "oldZipcode"));
            findMember.getAddressHistory().add(new AddressEntity("changeCity", "oldStreet", "oldZipcode"));
            // 쿼리 : MEMBER_ID 관련된 데이터를 통째로 지우고 다시 모두 insert해버림
            // 결론 : 사용하지 말자...

//            Movie movie = new Movie();
//            movie.setDirector("Director A");
//            movie.setActor("Actor B");
//            movie.setName("바람과 함께 사라지다");
//            movie.setPrice(10000);
//            em.persist(movie);      // 조인 전략 : insert into item, insert into movie 2개의 쿼리 나감
                                    // 단일 테이블 전략 : insert into item 1개의 쿼리 나감
                                    // 구현 클래스마다 테이블 전략 : insert into movie 1개의 쿼리 나감

//            Member member = new Member();
//            member.setCreatedBy("An");
//            member.setCreatedDate(LocalDateTime.now());
//            member.setUsername("user1");
//            em.persist(member);
//
//            em.flush();
//            em.clear();

//            Movie findMovie = em.find(Movie.class, movie.getId());  // 조인 전략 : item과 movie를 inner join 해서 select 조회
                                                                    // 단일 테이블 전략 : 조인할 필요없이 select 쿼리 통해서 조회
                                                                    // 구현 클래스마다 테이블 전략 : 심플하게 select 쿼리 조회
            // 구현 클래스마다 테이블 전략 단점 : 부모 클래스로 찾을 때 union all로 다 조회함 (데이터가 존재하는지 모든 테이블을 조회해 봐야하므로)
            // Item findItem = em.find(Item.class, movie.getId());

            // CASCADE와 고아객체
//            Child child1 = new Child();
//            Child child2 = new Child();
//
//            Parent parent = new Parent();
//            parent.addChild(child1);
//            parent.addChild(child2);
//
//            em.persist(parent); // CASCADE 시 child1과 child2를 em.persist하지 않아도 영속화 됨
//
//            em.flush();
//            em.clear();
//
//            Parent findParent = em.find(Parent.class, parent.getId());
//            // orphanRemoval = true : childList 컬렉션에서 제거된 child는 삭제됨! (DELETE 쿼리 발생)
//            findParent.getChildList().remove(0);

            // 08. 프록시와 연관관계 관리
//            Member member = new Member();
//            member.setUsername("Hello");
//            em.persist(member);
//
//            Team team = new Team();
//            team.setName("Team1");
//            em.persist(team);
//            member.changeTeam(team);
//
//            em.flush();
//            em.clear();
//
//            // (지연로딩) Member에 관련된 select 쿼리만 전송 (Team 관련 쿼리 x)
//            // (즉시로딩) Member와 Team 관련 select 쿼리 모두 전송
//            Member m = em.find(Member.class, member.getId());
//            // (지연로딩) 팀 : 프록시 객체
//            // (즉시로딩) 팀 : 프록시 x, Team 클래스
//            System.out.println("m.getTeam().getClass() = " + m.getTeam().getClass());
//            // (지연로딩) 이 시점에 team 관련 DB에 쿼리 전송!! (실제 team을 사용하는 시점에 초기화)
//            // (즉시로딩) 이미 영속성 컨텍스트에 올라와있기 때문에 실제 가지고 있는 값 반환 (쿼리 전송 필요 x)
//            m.getTeam().getName();


            //Member findMember = em.find(Member.class, member.getId());  // 멤버와 팀을 조인해서 팀 관련 데이터도 select 쿼리로 가져옴
//            Member findMember = em.getReference(Member.class, member.getId());  // getReference 호출 시점에는 select 쿼리가 DB에 안날라감
//            System.out.println("findMember.getId() = " + findMember.getId());   // id는 위에서 파라미터로 넣음 (이미 알고있음) => 쿼리 필요 x
//            System.out.println("findMember = " + findMember.getClass());        // findMember : 하이버네이트가 만든 가짜 클래스 (프록시클래스)
//
//            System.out.println("findMember.getUsername() = " + findMember.getUsername());   // DB의 값이 실제로 사용되는 시점에는 쿼리 보내서
//                                                                                            // findMember에 값을 채운후 출력됨
//            // getUsername() 2번 호출 시 target에 값이 있으므로 (이미 프록시가 초기화 됨) 다시 DB에 조회 X
//
//            Member f1 = em.getReference(Member.class, m1.getId());
//            Member f2 = em.find(Member.class, m2.getId());
//
//            System.out.println("m1 == m2 : " + (m1==m2));   // 프록시 객체와 원본은 다른 타입
//            // 프록시 객체와의 타입 체크 시 instanceof 사용! => JPA에서의 타입 체크 시로 범위 확장 (프록시 객체가 사용될 수 있으므로)
//            System.out.println("(m1 instanceof Member) = " + ((m1 instanceof Member) == (m2 instanceof Member)));
//
//            Member reference = em.find(Member.class, m2.getId());
//            System.out.println("f2R.getClass() = " + reference.getClass());   // 위에서 em.find 때문에 이미 영속성 컨텍스트에 올라옴
            // => 프록시 객체 생성이 아닌 영속성 컨텍스트에 존재하는 원본 반환!
            // f1 == reference : true (같은 영속성 컨텍스트에서 찾아오고, PK 동일하면 JPA는 항상 true 반환)
            // => em.getReference로 한 번 프록시로 반환한 놈을 다시 em.find로 반환할때는 원본 엔티티가 아닌 프록시로 반환함! (위의 true 보장 조건 때문에)

            // 프록시 인스턴스의 초기화 여부 확인
//            System.out.println("isLoaded = " + emf.getPersistenceUnitUtil().isLoaded(reference));
//
//            // 프록시 클래스 확인
//            System.out.println("f1.getClass() = " + f1.getClass());
//
//            // 프록시 강제 초기화 (위의 getUsername() 또한 강제 초기화)
//            Hibernate.initialize(f1);   // 강제 초기화

            tx.commit();
        }catch(Exception e){
            tx.rollback();
        }finally{
            em.close();
        }
        emf.close();
    }
}

/*
package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMainBasic {
    public static void main(String[] args) {

        //EntityManagerFactor : application 구동 시점에 딱 하나만 만들어야 함
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        //EntityManger : 실제 DB에 저장하는 등의 트랜잭션 단위로 생성해야 함
        // 쉽게 말하면 database connection을 하나 얻었다고 생각하면 됨!
        EntityManager em = emf.createEntityManager();

        // JPA에서 데이터를 변경하는 모든 작업은 꼭 트랜잭션 안에서 작업해야됨!!!!!!
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {                   // 정상적일 때 커밋
             // 삽입
            Member member = new Member();
            member.setName("USER1");
            em.persist(member);

            // 트랜잭션을 지원하는 쓰기 지연 : 먼저 1차 캐시에 저장, 쓰기 지연 SQL 저장소에 쿼리 저장
            //  => 커밋하는 시점에 flush -> 실제 DB에 쿼리가 넘어감
            System.out.println("================");
            // 수정
//            Member findMember = em.find(Member.class, 3L);
//            findMember.setName("안녕");
            // em.persist(findMember); 를 해주지 않아도 됨 (java collection 다루는 것과 동일함)
            // update 쿼리가 날라감
            // => jpa 통해서 엔티티를 가져오면 -> jpa가 관리함 -> jpa가 변경이 되었는지 커밋 시점에 체크함
            // -> 바뀌었으면 커밋 직전에 update 쿼리를 날림
            // **** ==> JPA는 값을 변경하면 트랜잭션이 커밋되는 시점에 변경을 반영함! (update 쿼리 날라감)

            // (JPQL) 전체 회원 조회 ==> JPA는 코드를 짤 때 테이블 대상 X!! 엔티티 객체 대상 !!
            // JPQL : 테이블이 아닌 엔티티 객체를 대상으로 쿼리를 짤 수 있는 문법 / SQL을 추상화한 객체 지향 쿼리 언어!
            // member 객체를 다 가져와라.
//            List<Member> result = em.createQuery("select m from Member m", Member.class)
//                    .getResultList();
//            for (Member member : result) {
//                System.out.println("member = " + member.getName());
//            }

            // 삭제
//            em.remove(findMember);

            tx.commit();
        } catch(Exception e){   // 문제가 생기면 롤백
            tx.rollback();
        } finally {             // 작업이 다 끝나면
            // EntityManger가 DB connection을 물고 동작하기 때문에 사용하고 나면 꼭 닫아줘야 함!
            em.close();
        }


        // 전체 application 이 끝나면 EntityManagerFactory도 닫아줌
        emf.close();
    }
}
*/

/*
em.persist(member);     => DB에 저장한다기 보단, 영속성 컨텍스트를 통해서 이 엔티티를 영속화 한다는 개념
                            : persist 메서드는 DB에 저장하는 것이 아니라 영속성 컨텍스트에 저장한

// 비영속 상태 : 엔티티를 생성한 상태
Member member = new Member();
member.setId(100L);
member.setName("HelloJPA);

// 영속 상태 : DB에 저장되는 것 x => 트랜잭션을 커밋하는 순간에 DB에 쿼리가 날라가서 멤버가 저장됨
em.persist(member);

// 준영속 상태 : 영속성 컨텍스트에서 지움, 영속성 컨텍스트와 아무 관계 없어짐
em.detach(member);

// 삭제 : 실제 DB에서 삭제를 요청하는 상 (영구저장한 DB에서 지운다는 뜻)
em.remove(member);

 */

/*
package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("user1");
            //member.setTeamId(team.getId()); // 외래키 식별자를 직접 다룸 ! => 객체지향스럽지 못함!
//            member.setTeam(team);   // JPA가 자동으로 team에서 pk값을 꺼내서 멤버 insert 시 fk로 사용
            em.persist(member);

            // flush -> clear : 플러시 => 영속성 컨텍스트의 변경사항 DB에 적용 => 클리어 => 영속성 컨텍스트 초기화
            // 따라서 DB에서 값을 가져오는 쿼리를 확인할 수 있음!
            em.flush();
            em.clear();

            Member findMember = em.find(Member.class, member.getId());
//            Team findTeam = em.find(Team.class, findMember.getTeamId());   // 조회에서도 문제 => 연관관계가 없으므로 계속 DB에서 끄집어 내야함 ! => 객체지향스럽지 못함!
            Team findTeam = findMember.getTeam();   // em.find 필요없이 바로 팀을 끄집어 낼 수 있음!

            // 연관관계 수정
//            Team newTeam = em.find(Team.class, 100L);
//            findMember.setTeam(newTeam);        // 팀 변경 => update 쿼리 전송 (외래키가 update 됨)

            Member member1 = em.find(Member.class, member.getId());
            List<Member> members = member1.getTeam().getMembers();  // 반대방향으로도 객체 그래프 탐색 가능!
            for (Member member2 : members) {
                System.out.println("member2 = " + member2.getUsername());
            }
*/
            /*
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);


            Member member = new Member();
            member.setName("userA");
            member.setTeam(team);   // 연관관계의 주인에 값을 저장하는게 맞음!
            // team.getMembers().add(member);   // 객체지향적 => 양쪽에 값 설정해줘야 함!
            em.persist(member);

            // => 양쪽 값 설정해야 하는 이유1
            // Team findTeam = em.find(Team.class, team.getId());
            // List<Member> members = findTeam.getMembers();
            // em.flush(); em.clear(); 가 없다면    (있다면 DB에서 다시 조회하기 때문에 외래키 있으므로 members도 다시 조회하는 JPA 매커니즘 동작)
            // member와 team이 1차 캐시에만 그대로 올라갔기 때문에
            // members를 출력해보면 아무것도 안 들어있음을 알 수 있음
            // 따라서 양쪽에 값 설정해줘야 함!
            // 이유2 : 테스트 케이스 작성 시 JPA 안거치게 작성하는 경우 존재 => 이때 에러 발생 가능
            // ===> 연관관계 편의 메서드 사용!

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
*/

