package hellojpa;

public class ValueMain {
    public static void main(String[] args) {

        int a = 10;
        int b = a;  // 기본 타입 (int, double 등) 은 공유 x => 값을 복사

        a = 20;
        System.out.println("a = " + a);
        System.out.println("b = " + b);

        Integer aa = 10;
        Integer bb = aa;    // 래퍼 클래스(Integer 등) 은 레퍼런스 공유

        // aa.setValue(20);  // setValue라는 메서드 있다고 가정 => 메서드에 레퍼런스 넘어가므로 값이 둘 다 바뀜
                             // 그러나 값을 변경하는 방법이 없으므로 side effect 발생 X

        System.out.println("aa = " + aa);
        System.out.println("bb = " + bb);

        Address address1 = new Address("city", "s", "z");
        Address address2 = new Address("city", "s", "z");

        // 값 타입의 비교는 항상 equals 사용해야함 !!
        // 동등성비교 위해서 equals 메서드 오버라이딩 한 경우 true 출력
        System.out.println("address1.equals(address2) = " + address1.equals(address2));

    }
}
