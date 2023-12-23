package hellojpa;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable     // 임베디드 타입 정의
public class Address {
    private String city;
    private String street;
//    @Column(name = "ZIPCODE")   // 테이블에서 매핑할 컬럼 지정 가능!
    private String zipcode;

    public Address() {  // 기본생성자 필수!
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }
    public String getStreet() {
        return street;
    }
    public String getZipcode() {
        return zipcode;
    }

    @Override
    public boolean equals(Object o) {       // 자동으로 생성한 equals (동등성 비교)
                                            // 값 타입의 비교는 항상 equals 사용해야함 !!
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(city, address.city) && Objects.equals(street, address.street) && Objects.equals(zipcode, address.zipcode);
    }

    @Override
    public int hashCode() {         // equals를 구현하면 꼭 hashCode도 같이 구현해줘야함
                                    // 그래야 hash사용하는 자바 컬렉션에서 효율적으로 사용 가능
        return Objects.hash(city, street, zipcode);
    }

    // setter를 모두 지우거나 접근지정자를 private으로 만들어서 Address를 불변객체로 만듦!
//    public void setCity(String city) {
//        this.city = city;
//    }
//    public void setStreet(String street) {
//        this.street = street;
//    }
//    public void setZipcode(String zipcode) {
//        this.zipcode = zipcode;
//    }
}
