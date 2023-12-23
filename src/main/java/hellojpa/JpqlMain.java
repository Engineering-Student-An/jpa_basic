package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpqlMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
//            // JPQL : Member는 테이블이 아닌 엔티티를 가리킴!!!
//            List<Member> result = em.createQuery("select m from Member m where m.username like '%kim%'",
//                            Member.class)
//                    .getResultList();
//
//            for (Member member : result) {
//                System.out.println("member = " + member);
//            }

//            //Criteria 사용 준비 => Criteria 사용 X !
//            CriteriaBuilder cb = em.getCriteriaBuilder();
//            CriteriaQuery<Member> query = cb.createQuery(Member.class);
//
//            // 루트 클래스 (조회 시작할 클래스)
//            Root<Member> m = query.from(Member.class);
//
//            // 쿼리 생성
//            CriteriaQuery<Member> cq = query.select(m).where(cb.equal(m.get("username"), "kim"));
//            List<Member> resultList = em.createQuery(cq).getResultList();

//            // QueryDSL 소개 : 실무 권장!
//            JPAFactoryQuery query = new JPAQueryFactory(em);
//            QMember m = QMember.member;
//            List<Member> list =
//                    query.selectFrom(m)
//                            .where(m.age.gt(18))
//                            .orderBy(m.id.desc())
//                            .fetch();

//            // 네이티브 SQL
//            em.createNativeQuery("select MEMBER_ID, city, street, zipcode, USERNAME from MEMBERWITHEM", Member.class)
//                    .getResultList();

            /* flush 동작 경우 : 커밋될 때 / 쿼리 전송될 때 (createQuery 혹은 createNativeQuery 등) */


            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        emf.close();
    }
}
