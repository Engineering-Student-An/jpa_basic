package hellojpa;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable     // 임베디드 타입 정의
public class Period {
    private LocalDateTime startDate;
    private LocalDateTime endDate;

//    public boolean isWork(){    // 응집성 있게 클래스 내에서 메서드 생성 가능
//    }


    public Period() {   // 기본생성자 필수
    }

    public Period(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
}
